package service;

import config.DatabaseConnection;
import dao.UsuarioDao;
import dao.CredencialAccesoDao;
import entities.Usuario;
import entities.CredencialAcceso;
import exceptions.DatabaseException;
import exceptions.ValidationException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UsuarioService implements GenericService<Usuario> {

    private final UsuarioDao usuarioDao = new UsuarioDao();
    private final CredencialAccesoDao credDao = new CredencialAccesoDao();

    @Override
    public void insertar(Usuario usuario) {
        // 1) Validaciones de negocio básicas
        validarUsuario(usuario);

        CredencialAcceso cred = usuario.getCredencialAcceso();
        if (cred == null) {
            throw new ValidationException("El usuario debe tener una credencial asociada");
        }

        // 2) Transacción: alta de CredencialAcceso + Usuario
        try (Connection conn = DatabaseConnection.getConnection()) {
            try {
                conn.setAutoCommit(false);

                // Primero se crea la credencial
                credDao.crear(cred, conn);

                // Asociamos la credencial creada al usuario
                usuario.setCredencialAcceso(cred);

                // Luego se crea el usuario usando la MISMA conexión
                usuarioDao.crear(usuario, conn);

                conn.commit();
            } catch (Exception e) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    // Si falla el rollback, lo encapsulamos igual
                    throw new DatabaseException("Error al hacer rollback en alta de usuario", ex);
                }
                throw new DatabaseException("Error en la transacción de alta de usuario", e);
            } finally {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    // No re-lanzamos, pero en un proyecto real se loguearía
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("No se pudo abrir la conexión a la base para alta de usuario", e);
        }
    }
    
    public Usuario buscarPorUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new ValidationException("El username no puede estar vacío");
        }
        return usuarioDao.buscarPorUsername(username);
    }
    
    public Usuario buscarPorEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new ValidationException("El email no puede estar vacío");
        }
        return usuarioDao.buscarPorEmail(email);
    }

    @Override
    public Usuario getById(long id) {
        return usuarioDao.leer(id);
    }

    @Override
    public List<Usuario> getAll() {
        return usuarioDao.leerTodos();
    }

    @Override
    public void actualizar(Usuario usuario) {
        // Podés hacer la actualización simple o transaccional.
        // Acá la hago transaccional por si se actualiza también la credencial.

        validarUsuario(usuario);

        CredencialAcceso cred = usuario.getCredencialAcceso();
        if (cred == null) {
            throw new ValidationException("El usuario debe tener una credencial asociada para actualizar");
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            try {
                conn.setAutoCommit(false);

                // Actualizamos primero la credencial asociada
                credDao.actualizar(cred, conn);

                // Luego actualizamos el usuario
                usuarioDao.actualizar(usuario, conn);

                conn.commit();
            } catch (Exception e) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    throw new DatabaseException("Error al hacer rollback en actualización de usuario", ex);
                }
                throw new DatabaseException("Error en la transacción de actualización de usuario", e);
            } finally {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    // en un proyecto real se loguearía
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("No se pudo abrir la conexión a la base para actualizar usuario", e);
        }
    }

    @Override
    public void eliminar(long id) {
        // Transacción: baja lógica de Usuario + CredencialAcceso
        try (Connection conn = DatabaseConnection.getConnection()) {
            try {
                conn.setAutoCommit(false);

                // Esto internamente:
                //  - Busca el usuario con la MISMA conn
                //  - Marca eliminada la credencial asociada
                //  - Marca eliminado el usuario
                usuarioDao.eliminar(id, conn);

                conn.commit();
            } catch (Exception e) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    throw new DatabaseException("Error al hacer rollback en baja lógica de usuario", ex);
                }
                throw new DatabaseException("Error en la transacción de baja lógica de usuario", e);
            } finally {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    // se ignora/loguea
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("No se pudo abrir la conexión a la base para eliminar usuario", e);
        }
    }

    // =======================
    // Validaciones de negocio
    // =======================

    private void validarUsuario(Usuario u) {
        if (u == null) {
            throw new ValidationException("El usuario no puede ser null");
        }

        if (u.getUsername() == null || u.getUsername().isBlank()) {
            throw new ValidationException("El username es obligatorio");
        }

        if (u.getEmail() == null || u.getEmail().isBlank()) {
            throw new ValidationException("El email es obligatorio");
        }

        // Estos límites deberían coincidir con los del esquema MySQL
        if (u.getUsername().length() > 30) {
            throw new ValidationException("El username no puede superar los 30 caracteres");
        }

        if (u.getEmail().length() > 120) {
            throw new ValidationException("El email no puede superar los 120 caracteres");
        }
    }
}
