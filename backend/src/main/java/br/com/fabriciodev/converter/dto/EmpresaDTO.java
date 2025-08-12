package br.com.fabriciodev.converter.dto;

import java.time.LocalDateTime;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaDTO {

    private Integer idEmpresa;
    private String coEmpresaSituacao;
    private String nuCnpjCpf;
    private String noRazaoSocial;
    private String noFantasia;
    private String dsCaminhoLogo;
    private LocalDateTime dtCriacao;
    private LocalDateTime dtAlteracao;
    private Integer idUsuarioCriacao;
    private Integer idUsuarioAlteracao;
    private String coEmpresaTipo;
    private Date dtExcluir;
    private String dsEmailCobranca;
    private String nuTelCobranca;
    private String dsHorarioFuncionamento;
    private String nuInscricaoEstadual;

}
