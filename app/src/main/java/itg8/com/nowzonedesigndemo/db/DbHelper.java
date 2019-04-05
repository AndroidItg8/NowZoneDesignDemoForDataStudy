package itg8.com.nowzonedesigndemo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import itg8.com.nowzonedesigndemo.common.DataModelPressure;
import itg8.com.nowzonedesigndemo.db.tbl.TblAverage;
import itg8.com.nowzonedesigndemo.db.tbl.TblBreathCounter;
import itg8.com.nowzonedesigndemo.db.tbl.TblPostureSedentary;
import itg8.com.nowzonedesigndemo.db.tbl.TblSleep;
import itg8.com.nowzonedesigndemo.db.tbl.TblState;
import itg8.com.nowzonedesigndemo.db.tbl.TblStepCount;


public class DbHelper extends OrmLiteSqliteOpenHelper {

    public static final String DB_NAME="Nowzone.db";
    private static final int DB_VERSION=10;

    private Dao<TblBreathCounter,Integer> breathDao=null;
    private Dao<TblState,Integer> stateDao=null;
    private Dao<TblAverage,Integer> avgDao=null;
    private Dao<TblStepCount,Integer> stepDao=null;
    private Dao<TblSleep,Integer> sleepDao=null;
    private Dao<TblPostureSedentary,Integer> postureSedentaryDao=null;
    private Dao<DataModelPressure,Integer> DataModelPresureDao=null;




    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, TblBreathCounter.class);
            TableUtils.createTable(connectionSource, TblState.class);
            TableUtils.createTable(connectionSource, TblAverage.class);
            TableUtils.createTable(connectionSource, TblStepCount.class);
            TableUtils.createTable(connectionSource, TblSleep.class);
            TableUtils.createTable(connectionSource, TblPostureSedentary.class);
            TableUtils.createTable(connectionSource, DataModelPressure.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
                          int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, TblBreathCounter.class, true);
            TableUtils.dropTable(connectionSource, TblState.class, true);
            TableUtils.dropTable(connectionSource, TblAverage.class, true);
            TableUtils.dropTable(connectionSource, TblStepCount.class, true);
            TableUtils.dropTable(connectionSource, TblSleep.class, true);
            TableUtils.dropTable(connectionSource, TblPostureSedentary.class, true);
            TableUtils.dropTable(connectionSource, DataModelPressure.class, true);
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

     /* User */

    public Dao<TblBreathCounter, Integer> getCountDao() throws SQLException {
        if (breathDao == null) {
            breathDao = getDao(TblBreathCounter.class);
        }

        return breathDao;
    }

    public Dao<TblState,Integer> getStateDao() throws SQLException{
        if(stateDao == null){
            stateDao=getDao(TblState.class);
        }
        return stateDao;
    }

    public Dao<TblAverage, Integer> getAvgDao() throws SQLException{
        if(avgDao == null)
            avgDao=getDao(TblAverage.class);

        return avgDao;
    }

    public Dao<TblStepCount, Integer> getStepDao() throws SQLException{
        if(stepDao == null)
            stepDao=getDao(TblStepCount.class);

        return stepDao;
    }

    public Dao<TblSleep, Integer> getSleepDao() throws SQLException{
        if(sleepDao == null)
            sleepDao=getDao(TblSleep.class);

        return sleepDao;
    }

    public Dao<TblPostureSedentary,Integer> getPostureSedentaryDao() throws SQLException{
        if(postureSedentaryDao==null)
            postureSedentaryDao=getDao(TblPostureSedentary.class);
        return postureSedentaryDao;
    }
   public Dao<DataModelPressure,Integer> getDataPresureDao() throws SQLException{
        if(DataModelPresureDao==null)
            DataModelPresureDao=getDao(DataModelPressure.class);
        return DataModelPresureDao;
    }



    @Override
    public void close() {
        breathDao = null;
        stateDao = null;
        avgDao = null;
        stepDao = null;
        postureSedentaryDao = null;
        DataModelPresureDao = null;
        super.close();
    }
}
