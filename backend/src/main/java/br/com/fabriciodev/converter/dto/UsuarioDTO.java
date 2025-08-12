package br.com.fabriciodev.converter.dto;

import br.com.fabriciodev.converter.model.Usuario;
import lombok.Data;

@Data
public class UsuarioDTO {
    private Integer idUsuario;
    private String noUsuario;
    private String dsEmail;
    private String dsSenha;
    private String dsFoto;
    private String dsCelular;
    private String dsSenhaAntiga;
    private String dsSenhaNova;
    private String dsSenhaConfirmacao;
    private boolean inExcluido;
    private String token;

    public Usuario toModel() {
        Usuario usuario = new Usuario();
        usuario.setNoUsuario(this.noUsuario);
        usuario.setDsEmail(this.dsEmail);
        usuario.setDsSenha(this.dsSenha);
        return usuario;
    }

    public static UsuarioDTO fromModelToken(Usuario usuario, String token) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setNoUsuario(usuario.getNoUsuario());
        dto.setDsEmail(usuario.getDsEmail());
        dto.setDsSenha(null);
        dto.setToken(token);
        return dto;
    }

    public static UsuarioDTO fromModel(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setNoUsuario(usuario.getNoUsuario());
        dto.setDsEmail(usuario.getDsEmail());
        dto.setDsSenha(null);
        return dto;
    }
}
