package itg8.com.nowzonedesigndemo.db;

import android.content.Context;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import itg8.com.nowzonedesigndemo.custom_widget.Crud;
import itg8.com.nowzonedesigndemo.db.tbl.TblPostureSedentary;
import itg8.com.nowzonedesigndemo.db.tbl.TblState;
import itg8.com.nowzonedesigndemo.db.tbl.TblStepCount;

/**
 * Created by swapnilmeshram on 28/09/17.
 */

public class BreathStateImpModel implements Crud {
    private final DbHelper helper;


    public BreathStateImpModel(Context context) {
        DBManager.init(context);
        helper=DBManager.getInstance().getHelper();
    }


    @Override
    public int create(Object item) {
        int index=-1;
        TblState state=(TblState)item;
            try {
                index=helper.getStateDao().create(state);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        return index;
    }

    @Override
    public int update(Object item) {
        int index=-1;
        TblState state=(TblState)item;
        try {
            index=helper.getStateDao().update(state);
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
        TblState state=null;
        try {
            state=helper.getStateDao().queryForId((int) id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return state;
    }

    @Override
    public List<?> findAll() {
        List<TblState> items=null;
        try{
            items=helper.getStateDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public List<?> findBreathBy(String key, String value){
        List<TblState> items=null;
        try{
            items=helper.getStateDao().queryForEq(key,value);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public List<?> findStepsBy(String key,Date value){
        List<TblStepCount> items=null;
        try{
            items=helper.getStepDao().queryForEq(key,value);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }





    public List<TblState> getDistict(String key){
        List<TblState> item=null;
        try{
            item=helper.getStateDao().queryBuilder().distinct().selectColumns(key).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return item;
    }


    public List<TblPostureSedentary> findPostureBy(String fieldDate, Date date) {
        List<TblPostureSedentary> item=null;
        try{
            item=helper.getPostureSedentaryDao().queryForEq(fieldDate,date);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return item;
    }

    public List<TblPostureSedentary> findAllPosture() {
        try {
            return helper.getPostureSedentaryDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
