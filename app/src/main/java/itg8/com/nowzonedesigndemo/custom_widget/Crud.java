package itg8.com.nowzonedesigndemo.custom_widget;

import java.util.Date;
import java.util.List;

/**
 * Created by swapnilmeshram on 28/09/17.
 */

public interface Crud {
    public int create(Object item);
    public int update(Object item);
    public int delete(Object item);
    public Object findById(long id);
    public List<?> findAll();


}
