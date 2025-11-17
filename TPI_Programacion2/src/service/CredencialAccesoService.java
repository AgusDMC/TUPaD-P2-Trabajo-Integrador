package service;

import dao.CredencialAccesoDao;
import entities.CredencialAcceso;
import java.util.List;

public class CredencialAccesoService implements GenericService<CredencialAcceso> {

    private final CredencialAccesoDao dao = new CredencialAccesoDao();

    @Override
    public void insertar(CredencialAcceso entidad) {
        dao.crear(entidad);
    }

    @Override
    public CredencialAcceso getById(long id) {
        return dao.leer(id);
    }

    @Override
    public List<CredencialAcceso> getAll() {
        return dao.leerTodos();
    }

    @Override
    public void actualizar(CredencialAcceso entidad) {
        dao.actualizar(entidad);
    }

    @Override
    public void eliminar(long id) {
        dao.eliminar(id);
    }
}
