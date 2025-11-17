package dao;

import java.sql.Connection;
import java.util.List;

public interface GenericDao<T> {

    void crear(T entidad);
    T leer(long id);
    List<T> leerTodos();
    void actualizar(T entidad);
    void eliminar(long id);

    void crear(T entidad, Connection conn);
    void actualizar(T entidad, Connection conn);
    void eliminar(long id, Connection conn);
}
