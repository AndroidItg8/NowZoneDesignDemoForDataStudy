package itg8.com.nowzonedesigndemo.db;

import android.content.Context;

import java.sql.SQLException;

import java.util.List;

import itg8.com.nowzonedesigndemo.common.DataModelPressure;
import itg8.com.nowzonedesigndemo.custom_widget.Crud;


public class PresureAccelerometerSateImp implements Crud {

    private final DbHelper helper;


    public PresureAccelerometerSateImp(Context context) {
        DBManager.init(context);
        helper=DBManager.getInstance().getHelper();
    }
    @Override
    public int create(Object item) {
        int index=-1;
        DataModelPressure state=(DataModelPressure)item;
        try {
            index=helper.getDataPresureDao().create(state);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public int update(Object item) {
        int index=-1;
        DataModelPressure state=(DataModelPressure)item;
        try {
            index= helper.getDataPresureDao().update(state);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public int delete(Object item) {
        return 0;
    }

    @Override
    public Object findById(long id) {
        DataModelPressure state=null;
        try {
            state=helper.getDataPresureDao().queryForId((int) id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return state;
    }

    @Override
    public List<?> findAll() {
        List<DataModelPressure> items=null;
        try{
            items=helper.getDataPresureDao().queryBuilder().where().eq(DataModelPressure.FIELD_IS_SENT,0).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }


    public void deleteBetweenIDS(Long[] longs) {

        try {
            helper.getDataPresureDao().deleteBuilder().where().between(DataModelPressure.FIELD_SERIAL_NO,longs[0],longs[1]).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
