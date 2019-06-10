package itg8.com.nowzonedesigndemo.sleep.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import itg8.com.nowzonedesigndemo.common.CommonMethod;

import static itg8.com.nowzonedesigndemo.db.tbl.TblSleep.FIELD_ID;

@DatabaseTable(tableName = SleepActivityModel.TABLE_NAME_SLEEP_RESULT)
public class SleepActivityModel {
    public static final String TABLE_NAME_SLEEP_RESULT = "TBL_SLEEP_RESULT";

    private static final String FIELD_START_TIME="start_time";
    private static final String FIELD_END_TIME="end_time";
    public static final String FIELD_SLEEP_ID="sleep_id";

    public static final int MOVEMENT=1;
    public static final int NO_MOVEMENT=2;

    @DatabaseField(columnName = FIELD_ID, generatedId = true)
    private long id;

    @DatabaseField(columnName = FIELD_SLEEP_ID)
    private long tblSleepID;

    @DatabaseField(columnName = FIELD_START_TIME)
    private long startTime;

    @DatabaseField(columnName = FIELD_END_TIME)
    private long endTime;

    private int type;


    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTblSleepID() {
        return tblSleepID;
    }

    public void setTblSleepID(long tblSleepID) {
        this.tblSleepID = tblSleepID;
    }

    public String getStartTimeLabel() {
        return CommonMethod.getTimeFromTMPAmPm(getStartTime());
    }


    public String getEndTimeLabel() {
        return CommonMethod.getTimeFromTMPAmPm(getEndTime());
    }
}
