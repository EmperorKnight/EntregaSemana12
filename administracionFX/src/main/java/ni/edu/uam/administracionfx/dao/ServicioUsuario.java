package ni.edu.uam.administracionfx.dao;

import ni.edu.uam.administracionfx.Services.ServicioGeneric;
import ni.edu.uam.administracionfx.model.Usuario;

import java.util.List;

public class ServicioUsuario extends ServicioGeneric {

    public ServicioUsuario(IDao dao) {
        super(dao);
    }

    public List<Usuario> getAll(){
        return dao.getAll("Usuario.findAll", Usuario.class);
    }

    public void save(Usuario usuario){
        dao.insert(usuario);
    }

    public void update(Usuario usuario){
        dao.update(usuario);
    }

    public void delete(Usuario usuario){
        dao.remove(usuario);
    }
}
