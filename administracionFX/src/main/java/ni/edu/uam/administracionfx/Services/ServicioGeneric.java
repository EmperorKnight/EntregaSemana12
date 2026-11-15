package ni.edu.uam.administracionfx.Services;

import ni.edu.uam.administracionfx.dao.IDao;
import ni.edu.uam.administracionfx.dao.ImplDao;

public class ServicioGeneric {
    protected IDao dao;
    public ServicioGeneric(IDao dao){
        this.dao = new ImplDao();
    }
}
