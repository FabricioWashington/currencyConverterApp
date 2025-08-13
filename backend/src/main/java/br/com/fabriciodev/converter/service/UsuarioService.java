package br.com.fabriciodev.converter.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

import br.com.fabriciodev.converter.components.AuthContext;
import br.com.fabriciodev.converter.config.Constants;
import br.com.fabriciodev.converter.config.GoogleOAuthProperties;
import br.com.fabriciodev.converter.dto.GoogleLoginRequest;
import br.com.fabriciodev.converter.dto.UsuarioDTO;
import br.com.fabriciodev.converter.dto.UsuarioPerfilDTO;
import br.com.fabriciodev.converter.dto.filtro.FiltroDTO;
import br.com.fabriciodev.converter.model.Usuario;
import br.com.fabriciodev.converter.model.VwAcessos;
import br.com.fabriciodev.converter.repository.UsuarioRepository;
import br.com.fabriciodev.converter.repository.VwAcessosRepository;
import br.com.fabriciodev.util.PasswordValidator;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository userRepository;
    private final VwAcessosRepository vwAcessosRepository;
    private final TokenService tokenService;
    private final UsuarioPerfilService usuarioPerfilService;
    private final ModelMapper modelMapper;
    private final AuthContext authContext;
    private final PasswordEncoder passwordEncoder;

    public List<UsuarioDTO> findAll() {
        return userRepository.findByInAtivo(Constants.ATIVO_BOOL).stream()
                .map(e -> modelMapper.map(e, UsuarioDTO.class))
                .collect(Collectors.toList());
    }

    public List<UsuarioDTO> search(FiltroDTO filtro, Map<String, Object> dataFiltro) {
        return userRepository.findAll().stream()
                .filter(e -> e.getInAtivo() == Constants.ATIVO_BOOL)
                .filter(e -> {
                    boolean matches = true;

                    if (filtro.getSearch() != null && !filtro.getSearch().isEmpty()) {
                        matches = e.getNoUsuario().toLowerCase()
                                .contains(filtro.getSearch().toLowerCase());
                    }

                    if (dataFiltro != null && dataFiltro.containsKey("ativo")) {
                        boolean ativoFiltro = Boolean.parseBoolean(dataFiltro.get("ativo").toString());
                        matches = matches
                                && (e.getInAtivo() == (ativoFiltro ? Constants.ATIVO_BOOL : Constants.INATIVO_BOOL));
                    }

                    return matches;
                })
                .skip((long) filtro.getPage() * filtro.getPageSize())
                .limit(filtro.getPageSize())
                .map(usuario -> modelMapper.map(usuario, UsuarioDTO.class))
                .collect(Collectors.toList());
    }

    public List<UsuarioDTO> findActive() {
        return userRepository.findByInAtivo(Constants.ATIVO_BOOL).stream()
                .map(e -> modelMapper.map(e, UsuarioDTO.class))
                .collect(Collectors.toList());
    }

    public UsuarioDTO get(Integer id) {
        Usuario entity = userRepository.findById(id)
                .filter(e -> e.getInAtivo() == Constants.ATIVO_BOOL)
                .orElseThrow(() -> new RuntimeException("Usuario não encontrado"));
        return modelMapper.map(entity, UsuarioDTO.class);
    }

    public UsuarioDTO create(UsuarioDTO dto) {
        if (dto.getDsEmail() == null || dto.getDsEmail().isBlank()) {
            throw new RuntimeException(Constants.EMAIL_REQUIRED);
        }

        if (userRepository.existsByDsEmail(dto.getDsEmail())) {
            throw new RuntimeException(Constants.EMAIL_ALREADY_EXISTS);
        }

        if (dto.getDsSenha() == null || dto.getDsSenha().isBlank()) {
            throw new RuntimeException(Constants.PASSWORD_REQUIRED);
        }

        if (!dto.getDsSenha().equals(dto.getDsSenhaConfirmacao())) {
            throw new RuntimeException(Constants.PASSWORD_CONFIRMATION_MISMATCH);
        }

        PasswordValidator.validar(dto.getDsSenha());

        Usuario entity = modelMapper.map(dto, Usuario.class);

        entity.setDsSenha(passwordEncoder.encode(dto.getDsSenha()));
        entity.setDtCriacao(LocalDateTime.now());
        entity.setInAtivo(Constants.ATIVO_BOOL);

        if (entity.getIdUsuarioCriacao() == null) {
            entity.setIdUsuarioCriacao(Constants.ID_USUARIO_SISTEMA);
        }

        if (entity.getDsCelular() != null) {
            entity.setDsCelular(entity.getDsCelular().replaceAll("\\D", ""));
        }
        entity = userRepository.save(entity);

        UsuarioPerfilDTO perfilDto = new UsuarioPerfilDTO();
        perfilDto.setIdUsuario(entity.getIdUsuario());
        perfilDto.setIdPerfil(Constants.ID_PERFIL_ADMIN);
        perfilDto.setDtUltimoAcesso(LocalDateTime.now());
        perfilDto.setInAtivo(true);

        usuarioPerfilService.create(perfilDto);

        Map<String, String> authRequest = new HashMap<>();
        authRequest.put("email", entity.getDsEmail());
        authRequest.put("senha", dto.getDsSenha());

        Map<String, Object> authResult = this.authenticateUser(authRequest);
        String token = (String) authResult.get("token");

        return UsuarioDTO.fromModelToken(entity, token);
    }

    public UsuarioDTO update(Integer id, UsuarioDTO dto) {
        Usuario usuario = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(Constants.USER_NOT_FOUND));

        boolean trocarSenha = dto.getDsSenhaAntiga() != null && dto.getDsSenhaNova() != null;
        String novaSenhaHash = null;

        if (trocarSenha) {
            if (!passwordEncoder.matches(dto.getDsSenhaAntiga(), usuario.getDsSenha())) {
                throw new RuntimeException(Constants.PASSWORD_INCORRECT);
            }

            if (passwordEncoder.matches(dto.getDsSenhaNova(), usuario.getDsSenha())) {
                throw new RuntimeException(Constants.PASSWORD_SAME);
            }

            if (!dto.getDsSenhaNova().equals(dto.getDsSenhaConfirmacao())) {
                System.out.println("senha nova: " + dto.getDsSenhaNova());
                System.out.println("senha confirmacao: " + dto.getDsSenhaConfirmacao());
                throw new RuntimeException(Constants.PASSWORD_CONFIRMATION_MISMATCH);
            }

            PasswordValidator.validar(dto.getDsSenhaNova());

            novaSenhaHash = passwordEncoder.encode(dto.getDsSenhaNova());
        }

        dto.setDsSenha(null);
        dto.setDsSenhaAntiga(null);
        dto.setDsSenhaNova(null);

        modelMapper.map(dto, usuario);

        if (novaSenhaHash != null) {
            usuario.setDsSenha(novaSenhaHash);
        }

        usuario.setDtAlteracao(LocalDateTime.now());
        usuario.setIdUsuarioAlteracao(authContext.getIdUsuario());

        return modelMapper.map(userRepository.save(usuario), UsuarioDTO.class);
    }

    public Map<String, Object> authenticateUser(Map<String, String> request) {
        String email = request.get("email");
        String senha = request.get("senha");

        if (email == null || senha == null) {
            throw new IllegalArgumentException("Email e senha são obrigatórios.");
        }

        Usuario usuario = userRepository.findByDsEmail(email).orElse(null);

        if (usuario == null || !usuario.getInAtivo()) {
            throw new RuntimeException("Usuário não encontrado ou inativo.");
        }

        if (!usuario.getIdCadastroTipo().equals(Constants.ID_TIPO_CADASTRO_CURRENCY)) {
            throw new RuntimeException("Este e-mail está vinculado a outro tipo de login.");
        }

        if (!tokenService.getPasswordEncoder().matches(senha, usuario.getDsSenha())) {
            throw new RuntimeException("Senha incorreta.");
        }

        VwAcessos vwAcesso = vwAcessosRepository.findFirstByIdUsuario(usuario.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado na view vw_acessos."));

        String token = tokenService.gerarToken(vwAcesso);

        return Map.of(
                "token", token,
                "user", UsuarioDTO.fromModel(usuario));
    }

    public Map<String, Object> autenticarComGoogle(GoogleLoginRequest request) {
        GoogleIdToken.Payload payload = GoogleAuthService.verify(request.getIdToken());

        if (payload == null
                || !payload.getAudience().equals(GoogleOAuthProperties.CLIENT_ID)
                || !payload.getAuthorizedParty().equals(GoogleOAuthProperties.CLIENT_ID)
                || !payload.getEmail().equals(request.getEmail())
                || !Boolean.TRUE.equals(payload.getEmailVerified())
                || !payload.getSubject().equals(request.getId())) {
            throw new IllegalArgumentException("invalid_token");
        }

        String email = payload.getEmail();
        String name = (String) payload.get("name");
        String pictureUrl = (String) payload.get("picture");

        Usuario usuario = userRepository.findByDsEmail(email).orElse(null);

        if (usuario != null && !usuario.getIdCadastroTipo().equals(Constants.ID_TIPO_CADASTRO_GOOGLE)) {
            throw new RuntimeException("Este e-mail já está cadastrado com outro tipo de login.");
        }

        if (usuario != null && pictureUrl != null && !pictureUrl.equals(usuario.getDsFotoExterna())) {
            usuario.setDsFotoExterna(pictureUrl);
            usuario.setDtAlteracao(LocalDateTime.now());
            userRepository.save(usuario);
        }

        if (usuario == null) {
            usuario = new Usuario();
            usuario.setNoUsuario(name);
            usuario.setDsEmail(email);
            usuario.setIdCadastroTipo(Constants.ID_TIPO_CADASTRO_GOOGLE);
            usuario.setDsSenha("autenticacaocomgoogle");
            usuario.setDtCriacao(LocalDateTime.now());
            usuario.setInAtivo(true);
            usuario.setIdUsuarioCriacao(Constants.ID_USUARIO_SISTEMA);
            usuario.setDsFotoExterna(pictureUrl);
            usuario = userRepository.save(usuario);
        }

        VwAcessos vwAcesso = vwAcessosRepository.findFirstByIdUsuario(usuario.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado na view vw_acessos."));

        String token = tokenService.gerarToken(vwAcesso);

        return Map.of(
                "token", token,
                "user", UsuarioDTO.fromModel(usuario));
    }

    public void delete(Integer id) {
        Usuario usuario = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(Constants.USER_NOT_FOUND));
        usuario.setInAtivo(Constants.INATIVO_BOOL);
        usuario.setDtAlteracao(LocalDateTime.now());
        userRepository.save(usuario);
    }

    ////// REVISAR metodos abaixo, pois não estão sendo utilizados no momento //////

    public UsuarioDTO preCreate(UsuarioDTO dto) {
        Usuario usuario = dto.toModel();

        boolean senhaValida = true;
        if (!usuario.getIdCadastroTipo().equals(Constants.ID_TIPO_CADASTRO_GOOGLE)) {
            senhaValida = senhaValida(usuario);
        }

        if (usuario.getIdCadastroTipo().equals(Constants.ID_TIPO_CADASTRO_GOOGLE)
                || usuario.getIdCadastroTipo().equals(Constants.ID_TIPO_CADASTRO_FACEBOOK)) {
            usuario.setInEmailValido(true);
        }

        if (usuario.getIdCadastroTipo() == null) {
            usuario.setIdCadastroTipo(Constants.ID_TIPO_CADASTRO_CURRENCY);
        }

        usuario.setDsSenha(usuario.getDsSenha() != null ? md5(usuario.getDsSenha()) : null);
        usuario.setDtCriacao(LocalDateTime.now());
        usuario.setIdUsuarioCriacao(
                usuario.getIdUsuarioCriacao() != null ? usuario.getIdUsuarioCriacao() : Constants.ID_USUARIO_SISTEMA);
        usuario.setDsCelular(usuario.getDsCelular().replaceAll("\\D", ""));

        if (senhaValida) {
            return UsuarioDTO.fromModel(userRepository.save(usuario));
        }
        return null;
    }

    public UsuarioDTO preUpdate(UsuarioDTO dto) {
        Usuario usuario = dto.toModel();
        return UsuarioDTO.fromModel(userRepository.save(usuario));
    }

    public boolean trocarSenha(Integer idUsuario, String senhaAnterior, String novaSenha, String confirmarSenha) {
        Usuario usuario = userRepository.findById(idUsuario).orElseThrow();

        if (!usuario.getDsSenha().equals(md5(senhaAnterior))) {
            throw new RuntimeException(Constants.PASSWORD_INCORRECT);
        }

        if (usuario.getDsSenha().equals(md5(novaSenha))) {
            throw new RuntimeException(Constants.PASSWORD_SAME);
        }

        if (!novaSenha.equals(confirmarSenha)) {
            throw new RuntimeException(Constants.PASSWORD_MISMATCH);
        }

        usuario.setDsSenha(md5(novaSenha));
        userRepository.save(usuario);
        return true;
    }

    public boolean restaurarSenha(Integer idUsuario, String novaSenha) {
        Usuario usuario = userRepository.findById(idUsuario).orElseThrow();

        if (usuario.getDsSenha().equals(md5(novaSenha))) {
            throw new RuntimeException(Constants.PASSWORD_SAME);
        }

        usuario.setDsSenha(novaSenha);
        if (!senhaValida(usuario)) {
            return false;
        }

        usuario.setDsSenha(md5(novaSenha));
        userRepository.save(usuario);
        return true;
    }

    private boolean senhaValida(Usuario usuario) {
        String senha = usuario.getDsSenha();

        if (usuario.getIdCadastroTipo().equals(Constants.ID_TIPO_CADASTRO_GOOGLE)
                || usuario.getIdCadastroTipo().equals(Constants.ID_TIPO_CADASTRO_FACEBOOK)) {
            return true;
        }

        if (senha == null || senha.contains(" ") || senha.trim().length() < 8) {
            throw new RuntimeException(Constants.PASSWORD_INVALID_LENGTH);
        }

        if (!Pattern.compile("[!@#\\$%\\^&*()_+\\-=\\[\\]{}|,;:'\"<>\\/?`\\.]").matcher(senha).find()) {
            throw new RuntimeException(Constants.PASSWORD_SPECIAL_CHAR);
        }

        if (!Pattern.compile("[a-z]").matcher(senha).find()
                || !Pattern.compile("[A-Z]").matcher(senha).find()) {
            throw new RuntimeException(Constants.PASSWORD_UPPERCASE_LOWERCASE);
        }

        if (!Pattern.compile("[0-9]").matcher(senha).find()) {
            throw new RuntimeException(Constants.PASSWORD_NUMBER);
        }

        return true;
    }

    public boolean preDelete(Integer id) {
        userRepository.deleteById(id);
        return true;
    }

    public UsuarioDTO preFind(Integer id) {
        Optional<Usuario> usuario = userRepository.findById(id);
        return usuario.map(UsuarioDTO::fromModel).orElse(null);
    }

    private String md5(String input) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : array) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    // public UsuarioDTO create(UsuarioDTO dto) {
    // if (dto.getDsEmail() == null || dto.getDsEmail().isBlank()) {
    // throw new RuntimeException(Constants.EMAIL_REQUIRED);
    // }

    // if (repository.existsByDsEmail(dto.getDsEmail())) {
    // throw new RuntimeException(Constants.EMAIL_ALREADY_EXISTS);
    // }

    // // Define o tipo de cadastro como padrão, se não informado
    // if (dto.getIdCadastroTipo() == null) {
    // dto.setIdCadastroTipo(Constants.ID_TIPO_CADASTRO_converter);
    // }

    // boolean senhaValida = true;

    // // Valida senha somente se não for Google ou Facebook
    // if (!dto.getIdCadastroTipo().equals(Constants.ID_TIPO_CADASTRO_GOOGLE)
    // && !dto.getIdCadastroTipo().equals(Constants.ID_TIPO_CADASTRO_FACEBOOK)) {

    // if (dto.getDsSenha() == null || dto.getDsSenha().isBlank()) {
    // throw new RuntimeException(Constants.PASSWORD_REQUIRED);
    // }

    // if (!dto.getDsSenha().equals(dto.getDsSenhaConfirmacao())) {
    // throw new RuntimeException(Constants.PASSWORD_CONFIRMATION_MISMATCH);
    // }

    // PasswordValidator.validar(dto.getDsSenha());
    // }

    // Usuario usuario = modelMapper.map(dto, Usuario.class);

    // // Define senha somente se for necessário
    // if (dto.getDsSenha() != null) {
    // usuario.setDsSenha(md5(dto.getDsSenha()));
    // }

    // // E-mail validado automaticamente para Google/Facebook
    // if (dto.getIdCadastroTipo().equals(Constants.ID_TIPO_CADASTRO_GOOGLE)
    // || dto.getIdCadastroTipo().equals(Constants.ID_TIPO_CADASTRO_FACEBOOK)) {
    // usuario.setInEmailValido(true);
    // }

    // usuario.setDtCriacao(LocalDateTime.now());
    // usuario.setInAtivo(Constants.ATIVO_BOOL);

    // if (usuario.getIdUsuarioCriacao() == null) {
    // usuario.setIdUsuarioCriacao(Constants.ID_USUARIO_SISTEMA);
    // }

    // if (usuario.getDsCelular() != null) {
    // usuario.setDsCelular(usuario.getDsCelular().replaceAll("\\D", ""));
    // }

    // return UsuarioDTO.fromModel(repository.save(usuario));
    // }

}
