package dao;

import config.DatabaseConnection;
import entities.CredencialAcceso;
import exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CredencialAccesoDao implements GenericDao<CredencialAcceso> {

    @Override
    public void crear(CredencialAcceso c) {
        if (c == null) {
            throw new IllegalArgumentException("CredencialAcceso no puede ser nula");
        }
        try (Connection conn = DatabaseConnection.getConnection()) {
            crear(c, conn);
        } catch (SQLException e) {
            throw new DatabaseException("Error creando credencial", e);
        }
    }

    @Override
    public void crear(CredencialAcceso c, Connection conn) {
        String sql = "INSERT INTO CredencialAcceso (hashPassword, salt, ultimoCambio, requiereReset, eliminado) "
                   + "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, c.getHashPassword());
            ps.setString(2, c.getSalt());
            ps.setTimestamp(3, c.getUltimoCambio() != null ? Timestamp.valueOf(c.getUltimoCambio()) : null);
            ps.setBoolean(4, c.isRequiereReset());
            ps.setBoolean(5, c.isEliminado());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) c.setId(rs.getLong(1));
            }

        } catch (SQLException e) {
            throw new DatabaseException("Error creando credencial", e);
        }
    }

    @Override
    public CredencialAcceso leer(long id) {
        String sql = "SELECT * FROM CredencialAcceso WHERE id=? AND eliminado=0";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }

        } catch (SQLException e) {
            throw new DatabaseException("Error leyendo credencial", e);
        }

        return null;
    }

    @Override
    public List<CredencialAcceso> leerTodos() {
        List<CredencialAcceso> lista = new ArrayList<>();
        String sql = "SELECT * FROM CredencialAcceso WHERE eliminado=0";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) lista.add(mapRow(rs));

        } catch (SQLException e) {
            throw new DatabaseException("Error listando credenciales", e);
        }

        return lista;
    }

    @Override
    public void actualizar(CredencialAcceso c) {
        if (c == null || c.getId() == null) {
            throw new IllegalArgumentException("CredencialAcceso e ID no pueden ser nulos");
        }
        try (Connection conn = DatabaseConnection.getConnection()) {
            actualizar(c, conn);
        } catch (SQLException e) {
            throw new DatabaseException("Error actualizando credencial", e);
        }
    }

    @Override
    public void actualizar(CredencialAcceso c, Connection conn) {
        String sql = "UPDATE CredencialAcceso SET hashPassword=?, salt=?, ultimoCambio=?, "
                   + "requiereReset=?, eliminado=? WHERE id=?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getHashPassword());
            ps.setString(2, c.getSalt());
            ps.setTimestamp(3, c.getUltimoCambio() != null ? Timestamp.valueOf(c.getUltimoCambio()) : null);
            ps.setBoolean(4, c.isRequiereReset());
            ps.setBoolean(5, c.isEliminado());
            ps.setLong(6, c.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Error actualizando credencial", e);
        }
    }

    @Override
    public void eliminar(long id) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            eliminar(id, conn);
        } catch (SQLException e) {
            throw new DatabaseException("Error eliminando credencial", e);
        }
    }

    @Override
    public void eliminar(long id, Connection conn) {
        String sql = "UPDATE CredencialAcceso SET eliminado=1 WHERE id=?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error en baja lógica de credencial", e);
        }
    }

    private CredencialAcceso mapRow(ResultSet rs) throws SQLException {
        CredencialAcceso c = new CredencialAcceso();

        c.setId(rs.getLong("id"));
        c.setEliminado(rs.getBoolean("eliminado"));
        c.setHashPassword(rs.getString("hashPassword"));
        c.setSalt(rs.getString("salt"));

        Timestamp ts = rs.getTimestamp("ultimoCambio");
        c.setUltimoCambio(ts != null ? ts.toLocalDateTime() : null);

        c.setRequiereReset(rs.getBoolean("requiereReset"));

        return c;
    }
}
