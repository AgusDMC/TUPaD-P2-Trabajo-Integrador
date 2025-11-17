package entities;

import java.time.LocalDateTime;

public class Usuario {

    private Long id;
    private boolean eliminado;
    private String username;
    private String email;
    private boolean activo;
    private LocalDateTime fechaRegistro;

    private CredencialAcceso credencialAcceso;

    public Usuario() {}

    public Usuario(Long id, boolean eliminado, String username, String email,
                   boolean activo, LocalDateTime fechaRegistro,
                   CredencialAcceso credencialAcceso) {

        this.id = id;
        this.eliminado = eliminado;
        this.username = username;
        this.email = email;
        this.activo = activo;
        this.fechaRegistro = fechaRegistro;
        this.credencialAcceso = credencialAcceso;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public boolean isEliminado() { return eliminado; }
    public void setEliminado(boolean eliminado) { this.eliminado = eliminado; }

    public String getUsername() { return username; }
    public void setUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username no puede ser nulo o vacío");
        }
        this.username = username;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email no puede ser nulo o vacío");
        }
        this.email = email;
    }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public CredencialAcceso getCredencialAcceso() { return credencialAcceso; }
    public void setCredencialAcceso(CredencialAcceso credencialAcceso) {
        if (credencialAcceso == null) {
            throw new IllegalArgumentException("CredencialAcceso no puede ser nula");
        }
        this.credencialAcceso = credencialAcceso;
    }

    @Override
    public String toString() {
        return "Usuario{id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", activo=" + activo +
                ", credencialAcceso=" + (credencialAcceso != null ? credencialAcceso.getId() : null) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return id != null && id.equals(usuario.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
