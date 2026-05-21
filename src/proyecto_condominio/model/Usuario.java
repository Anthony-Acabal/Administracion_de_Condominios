package proyecto_condominio.model;

public class Usuario {
    private String nombre;
    private String correo;
    private String contrasena;
    private boolean primerIngreso;
    private String rol;
    private int intentosFallidos;
    private boolean bloqueado;

    public Usuario(String nombre, String correo, String contrasena, boolean primerIngreso, String rol, int intentosFallidos, boolean bloqueado) {
        this.nombre = nombre;
        this.correo = correo;
        this.contrasena = contrasena;
     this.primerIngreso = primerIngreso;
        this.rol = rol;
        this.intentosFallidos = intentosFallidos;
        this.bloqueado = bloqueado;
    }

    // Getters básicos
    public String getNombre() { return nombre; }
    public String getCorreo() { return correo; }
    public String getContrasena() { return contrasena; }
    public boolean isPrimerIngreso() { return primerIngreso; }
    public String getRol() { return rol; }
    public int getIntentosFallidos() { return intentosFallidos; }
    public boolean isBloqueado() { return bloqueado; }
}

