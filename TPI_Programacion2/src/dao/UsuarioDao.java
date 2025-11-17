package dao;

import config.DatabaseConnection;
import entities.Usuario;
import entities.CredencialAcceso;
import exceptions.EntityNotFoundException;
import exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDao implements GenericDao<Usuario> {

    private final CredencialAccesoDao credDao = new CredencialAccesoDao();

    @Override
    public void crear(Usuario u) {
        if (u == null || u.getCredencialAcceso() == null) {
            throw new IllegalArgumentException("Usuario y su CredencialAcceso no pueden ser nulos");
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            crear(u, conn);
        } catch (SQLException e) {
            throw new DatabaseException("Error creando usuario", e);
        }
    }


    @Override
    public void crear(Usuario u, Connection conn) {
        // Crear primero la credencial (B)
        credDao.crear(u.getCredencialAcceso(), conn);

        String sql = "INSERT INTO Usuario (username, email, activo, fechaRegistro, CredencialAcceso_id, eliminado) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, u.getUsername());
            ps.setString(2, u.getEmail());
            ps.setBoolean(3, u.isActivo());
            ps.setTimestamp(4, u.getFechaRegistro() != null ? Timestamp.valueOf(u.getFechaRegistro()) : null);
            ps.setLong(5, u.getCredencialAcceso().getId());
            ps.setBoolean(6, u.isEliminado());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) u.setId(rs.getLong(1));
            }

        } catch (SQLException e) {
            throw new DatabaseException("Error creando usuario", e);
        }
    }


    @Override
    public Usuario leer(long id) {
        String sql = "SELECT * FROM Usuario WHERE id=? AND eliminado=0";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }

        } catch (SQLException e) {
            throw new DatabaseException("Error leyendo usuario", e);
        }

        return null;
    }


    @Override
    public List<Usuario> leerTodos() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM Usuario WHERE eliminado=0";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) lista.add(mapRow(rs));

        } catch (SQLException e) {
            throw new DatabaseException("Error listando usuarios", e);
        }

        return lista;
    }


    @Override
    public void actualizar(Usuario u) {
        if (u == null || u.getId() == null || u.getCredencialAcceso() == null) {
            throw new IllegalArgumentException("Usuario, su ID y CredencialAcceso no pueden ser nulos");
        }
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                actualizar(u, conn);
                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw new DatabaseException("Error actualizando usuario", e);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error en transacción de actualización", e);
        }
    }

    @Override
    public void actualizar(Usuario u, Connection conn) {
        // Actualizar primero B
        credDao.actualizar(u.getCredencialAcceso(), conn);

        String sql = "UPDATE Usuario SET username=?, email=?, activo=?, fechaRegistro=?, eliminado=? WHERE id=?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, u.getUsername());
            ps.setString(2, u.getEmail());
            ps.setBoolean(3, u.isActivo());
            ps.setTimestamp(4, u.getFechaRegistro() != null ? Timestamp.valueOf(u.getFechaRegistro()) : null);
            ps.setBoolean(5, u.isEliminado());
            ps.setLong(6, u.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Error actualizando usuario", e);
        }
    }


    @Override
    public void eliminar(long id) {
        // Versión simple sin manejo explícito de transacciones.
        try (Connection conn = DatabaseConnection.getConnection()) {
            eliminar(id, conn);
        } catch (SQLException e) {
            throw new DatabaseException("Error eliminando usuario", e);
        }
    }


    @Override
    public void eliminar(long id, Connection conn) {
        Usuario u = leerPorId(id, conn);
        if (u == null) {
            throw new EntityNotFoundException("Usuario con id " + id + " no encontrado");
        }

        credDao.eliminar(u.getCredencialAcceso().getId(), conn);

        String sql = "UPDATE Usuario SET eliminado=1 WHERE id=?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error eliminando usuario", e);
        }
    }
    
    public Usuario buscarPorUsername(String username) {
        String sql = "SELECT * FROM Usuario WHERE username = ? AND eliminado = 0";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error buscando usuario por username", e);
        }
        return null;
    }
    
    public Usuario buscarPorEmail(String email) {
        String sql = "SELECT * FROM Usuario WHERE email = ? AND eliminado = 0";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }

        } catch (SQLException e) {
            throw new DatabaseException("Error buscando usuario por email", e);
        }

        return null;
    }

    
    // Helper interno para leer un usuario usando una conexión existente (para transacciones).
    private Usuario leerPorId(long id, Connection conn) {
        String sql = "SELECT * FROM Usuario WHERE id=? AND eliminado=0";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error leyendo usuario dentro de la transacción", e);
        }

        return null;
    }

    private Usuario mapRow(ResultSet rs) throws SQLException {

        Usuario u = new Usuario();

        u.setId(rs.getLong("id"));
        u.setEliminado(rs.getBoolean("eliminado"));
        u.setUsername(rs.getString("username"));
        u.setEmail(rs.getString("email"));
        u.setActivo(rs.getBoolean("activo"));

        Timestamp ts = rs.getTimestamp("fechaRegistro");
        u.setFechaRegistro(ts != null ? ts.toLocalDateTime() : null);

        long credId = rs.getLong("CredencialAcceso_id");
        CredencialAcceso cred = credDao.leer(credId);
        
        if (cred == null) {
            throw new EntityNotFoundException("CredencialAcceso con id " + credId + " no encontrada");
        }
        u.setCredencialAcceso(cred);

        return u;
    }
}
