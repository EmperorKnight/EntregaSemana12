package ni.edu.uam.administracionfx.dao;

import java.util.List;

public interface IDao {

    <T> List<T> getAll(String namedQuery, Class<T> clazz);
    <T> void insert(T entity);
    <T> void update(T entity);
    <T> void remove(T entity);
    <T> T findById(Class<T> clazz,String id);

}
