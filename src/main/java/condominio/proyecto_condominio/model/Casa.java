package condominio.proyecto_condominio.model;

public class Casa {

    private int idCasa;

    private int numeroCasa;

    private String estado;

    private String propietario;

    private String telefono;

    private String correo;

    public Casa() {
    }

    public Casa(
            int idCasa,
            int numeroCasa,
            String estado
    ) {
        this.idCasa = idCasa;
        this.numeroCasa = numeroCasa;
        this.estado = estado;
    }

    public int getIdCasa() {
        return idCasa;
    }

    public void setIdCasa(int idCasa) {
        this.idCasa = idCasa;
    }

    public int getNumeroCasa() {
        return numeroCasa;
    }

    public void setNumeroCasa(int numeroCasa) {
        this.numeroCasa = numeroCasa;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getPropietario() {
        return propietario;
    }

    public void setPropietario(String propietario) {
        this.propietario = propietario;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}