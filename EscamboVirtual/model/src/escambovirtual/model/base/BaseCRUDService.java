package escambovirtual.model.base;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Joao
 */
public interface BaseCRUDService<E extends BaseEntity> {
    //fica aqui os serviços do CRUD
    
    public void create(E entity) throws Exception;
    
    public E readById(Long id) throws Exception;
    
    public List<E> readByCriteria(Map<Long,Object> criteria) throws Exception;
    
    public void update(E entity) throws Exception;
    
    public void delete(Long id) throws Exception;
}
