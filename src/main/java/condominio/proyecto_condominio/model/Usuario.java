package condominio.proyecto_condominio.model;

public class Usuario {

    private int id;
    private String usuario; // 🔥 antes era "nombre"
    private String correo;
    private String contrasena;
    private boolean primerIngreso;
    private String rol;
    private int intentosFallidos;
    private boolean bloqueado;

    public Usuario(int id, String usuario, String correo, String contrasena,
                   boolean primerIngreso, String rol,
                   int intentosFallidos, boolean bloqueado) {

        this.id = id;
        this.usuario = usuario;
        this.correo = correo;
        this.contrasena = contrasena;
        this.primerIngreso = primerIngreso;
        this.rol = rol;
        this.intentosFallidos = intentosFallidos;
        this.bloqueado = bloqueado;
    }

    public int getId() { return id; }
    public String getUsuario() { return usuario; } // 🔥 FIX
    public String getCorreo() { return correo; }
    public String getContrasena() { return contrasena; }
    public boolean isPrimerIngreso() { return primerIngreso; }
    public String getRol() { return rol; }
    public int getIntentosFallidos() { return intentosFallidos; }
    public boolean isBloqueado() { return bloqueado; }

    public void setIntentosFallidos(int intentosFallidos) {
        this.intentosFallidos = intentosFallidos;
    }

    public void setBloqueado(boolean bloqueado) {
        this.bloqueado = bloqueado;
    }
}