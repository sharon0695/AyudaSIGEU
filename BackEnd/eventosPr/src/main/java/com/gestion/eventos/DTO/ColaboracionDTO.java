package com.gestion.eventos.DTO;

public class ColaboracionDTO {
    private String nitOrganizacion;
    private String certificadoParticipacion;
    private String representanteAlterno;

    // Constructores
    public ColaboracionDTO() {}

    public ColaboracionDTO(String nitOrganizacion, String certificadoParticipacion, String representanteAlterno) {
        this.nitOrganizacion = nitOrganizacion;
        this.certificadoParticipacion = certificadoParticipacion;
        this.representanteAlterno = representanteAlterno;
    }

    // Getters y Setters
    public String getNitOrganizacion() {
        return nitOrganizacion;
    }

    public void setNitOrganizacion(String nitOrganizacion) {
        this.nitOrganizacion = nitOrganizacion;
    }

    public String getCertificadoParticipacion() {
        return certificadoParticipacion;
    }

    public void setCertificadoParticipacion(String certificadoParticipacion) {
        this.certificadoParticipacion = certificadoParticipacion;
    }

    public String getRepresentanteAlterno() {
        return representanteAlterno;
    }

    public void setRepresentanteAlterno(String representanteAlterno) {
        this.representanteAlterno = representanteAlterno;
    }
}
