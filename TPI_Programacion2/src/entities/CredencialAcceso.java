package entities;

import java.time.LocalDateTime;

public class CredencialAcceso {

    private Long id;
    private boolean eliminado;
    private String hashPassword;
    private String salt;
    private LocalDateTime ultimoCambio;
    private boolean requiereReset;

    public CredencialAcceso() {}

    public CredencialAcceso(Long id, boolean eliminado, String hashPassword,
                            String salt, LocalDateTime ultimoCambio,
                            boolean requiereReset) {
        this.id = id;
        this.eliminado = eliminado;
        this.hashPassword = hashPassword;
        this.salt = salt;
        this.ultimoCambio = ultimoCambio;
        this.requiereReset = requiereReset;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public boolean isEliminado() { return eliminado; }
    public void setEliminado(boolean eliminado) { this.eliminado = eliminado; }

    public String getHashPassword() { return hashPassword; }
    public void setHashPassword(String hashPassword) {
        if (hashPassword == null || hashPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("HashPassword no puede ser nulo o vacío");
        }
        this.hashPassword = hashPassword;
    }

    public String getSalt() { return salt; }
    public void setSalt(String salt) {
        if (salt == null || salt.trim().isEmpty()) {
            throw new IllegalArgumentException("Salt no puede ser nulo o vacío");
        }
        this.salt = salt;
    }

    public LocalDateTime getUltimoCambio() { return ultimoCambio; }
    public void setUltimoCambio(LocalDateTime ultimoCambio) { this.ultimoCambio = ultimoCambio; }

    public boolean isRequiereReset() { return requiereReset; }
    public void setRequiereReset(boolean requiereReset) { this.requiereReset = requiereReset; }

    @Override
    public String toString() {
        return "CredencialAcceso{id=" + id +
                ", hashPassword='" + hashPassword + '\'' +
                ", requiereReset=" + requiereReset +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CredencialAcceso that = (CredencialAcceso) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
