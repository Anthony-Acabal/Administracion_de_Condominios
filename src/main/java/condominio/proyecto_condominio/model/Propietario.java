package condominio.proyecto_condominio.model;

public class Propietario {
    
    private int idPropietario; 
    private String primerNombre;
    private String segundoNombre;
    private String tercerNombre;
    private String primerApellido;
    private String segundoApellido;
    private int idCasa; // Manejado como entero
    private String telefono; 
    private String correoElectronico;
    private String estado;

    public Propietario() {
        this.estado = "Activo"; // Valor por defecto
    }

    public int getIdPropietario() { return idPropietario; }
    public void setIdPropietario(int idPropietario) { this.idPropietario = idPropietario; }

    public String getPrimerNombre() { return primerNombre; }
    public void setPrimerNombre(String primerNombre) { this.primerNombre = primerNombre; }

    public String getSegundoNombre() { return segundoNombre; }
    public void setSegundoNombre(String segundoNombre) { this.segundoNombre = segundoNombre; }

    public String getTercerNombre() { return tercerNombre; }
    public void setTercerNombre(String tercerNombre) { this.tercerNombre = tercerNombre; }

    public String getPrimerApellido() { return primerApellido; }
    public void setPrimerApellido(String primerApellido) { this.primerApellido = primerApellido; }

    public String getSegundoApellido() { return segundoApellido; }
    public void setSegundoApellido(String segundoApellido) { this.segundoApellido = segundoApellido; }

    public int getIdCasa() { return idCasa; }
    public void setIdCasa(int idCasa) { this.idCasa = idCasa; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getCorreoElectronico() { return correoElectronico; }
    public void setCorreoElectronico(String correoElectronico) { this.correoElectronico = correoElectronico; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}