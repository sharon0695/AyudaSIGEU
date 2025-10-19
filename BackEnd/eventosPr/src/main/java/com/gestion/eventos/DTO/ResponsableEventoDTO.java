package com.gestion.eventos.DTO;

import com.gestion.eventos.Model.ResponsableEventoModel;

public class ResponsableEventoDTO {
    private Integer idUsuario;
    private String documentoAval;
    private ResponsableEventoModel.tipo_aval tipoAval;

    public ResponsableEventoDTO() {}

    public ResponsableEventoDTO(Integer idUsuario, String documentoAval, ResponsableEventoModel.tipo_aval tipoAval) {
        this.idUsuario = idUsuario;
        this.documentoAval = documentoAval;
        this.tipoAval = tipoAval;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getDocumentoAval() {
        return documentoAval;
    }

    public void setDocumentoAval(String documentoAval) {
        this.documentoAval = documentoAval;
    }

    public ResponsableEventoModel.tipo_aval getTipoAval() {
        return tipoAval;
    }

    public void setTipoAval(ResponsableEventoModel.tipo_aval tipoAval) {
        this.tipoAval = tipoAval;
    }
}
