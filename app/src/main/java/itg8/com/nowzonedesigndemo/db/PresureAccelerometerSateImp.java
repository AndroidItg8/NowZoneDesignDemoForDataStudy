package itg8.com.nowzonedesigndemo.db;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.stmt.UpdateBuilder;

import java.sql.SQLException;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import itg8.com.nowzonedesigndemo.common.DataModelPressure;
import itg8.com.nowzonedesigndemo.custom_widget.Crud;


public class PresureAccelerometerSateImp implements Crud {

    private final DbHelper helper;


    public PresureAccelerometerSateImp(Context context) {
        DBManager.init(context);
        helper = DBManager.getInstance().getHelper();
    }

    @Override
    public int create(Object item) {
        int index = -1;
        DataModelPressure state = (DataModelPressure) item;
        try {
            index = helper.getDataPresureDao().create(state);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public int update(Object item) {
        int index = -1;
        DataModelPressure state = (DataModelPressure) item;
        try {
            index = helper.getDataPresureDao().update(state);
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
        DataModelPressure state = null;
        try {
            state = helper.getDataPresureDao().queryForId((int) id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return state;
    }



    @Override
    public List<?> findAll() {
        List<DataModelPressure> items = null;
        try {
            items = helper.getDataPresureDao().queryBuilder().orderBy(DataModelPressure.FIELD_TIMESTAMP_DATE,false).limit(3600L).where().eq(DataModelPressure.FIELD_IS_SENT, false).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    private static final String TAG = "PresureAccelerometerSat";
    public List<DataModelPressure> getBetween(Date start, Date end){
        Log.d(TAG, "getBetween: startDate->"+start.toString()+" endDate->"+end.toString());
        List<DataModelPressure> items = null;
        try {
            items = helper.getDataPresureDao().queryBuilder().where().between(DataModelPressure.FIELD_TIMESTAMP_DATE, start,end).query();
//            items = helper.getDataPresureDao().queryBuilder().where().le(DataModelPressure.FIELD_TIMESTAMP_DATE,start).and().le(DataModelPressure.FIELD_TIMESTAMP_DATE,end).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }


    public void deleteBetweenIDS(Date[] longs) {
        if (longs !=null && longs.length > 1) {
//            if(longs[0]>longs[1]){
//                long temp=longs[0];
//                longs[0]=longs[1];
//                longs[1]=temp;
//            }
            try {
                UpdateBuilder<DataModelPressure, Integer> builder = helper.getDataPresureDao().updateBuilder();
                builder.updateColumnValue(DataModelPressure.FIELD_IS_SENT, true);
                builder.where().ge(DataModelPressure.FIELD_TIMESTAMP_DATE, longs[0]).and().le(DataModelPressure.FIELD_TIMESTAMP_DATE, longs[1]);

                builder.update();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
