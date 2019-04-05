package itg8.com.nowzonedesigndemo.db.tbl;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = TblSleep.TABLE_NAME_SLEEP)
public class TblSleep implements Parcelable {
    static final String TABLE_NAME_SLEEP = "tblsleep";

    public static final String FIELD_ID="id";
    public static final String FIELD_DATE="date";
    private static final String FIELD_TIME_START="time_start";
    private static final String FIELD_TIME_END="time_end";
    private static final String FIELD_MIN="minutes";
    private static final String FIELD_STATE="sleep_state";
    private static final String FIELD_TMPSTMP="timestamp";

    @DatabaseField(columnName = FIELD_ID, generatedId = true)
    private long id;

    @DatabaseField(columnName = FIELD_DATE)
    private String date;

    @DatabaseField(columnName = FIELD_TIME_START)
    private long timeStart;

    @DatabaseField(columnName = FIELD_TIME_END)
    private long timeEnd;

    @DatabaseField(columnName = FIELD_MIN)
    private String minutes;

    @DatabaseField(columnName = FIELD_STATE)
    private String sleepState;

    @DatabaseField(columnName = FIELD_TMPSTMP)
    private long timestamp;


    public TblSleep() {
    }


    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(long timeStart) {
        this.timeStart = timeStart;
    }

    public long getTimeEnd() {
        return timeEnd;
    }



    public void setTimeEnd(long timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getMinutes() {
        return minutes;
    }

    public void setMinutes(String minutes) {
        this.minutes = minutes;
    }

    public String getSleepState() {
        return sleepState;
    }

    public void setSleepState(String sleepState) {
        this.sleepState = sleepState;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.date);
        dest.writeLong(this.timeStart);
        dest.writeLong(this.timeEnd);
        dest.writeString(this.minutes);
        dest.writeString(this.sleepState);
        dest.writeLong(this.timestamp);
    }

    protected TblSleep(Parcel in) {
        this.id = in.readLong();
        this.date = in.readString();
        this.timeStart = in.readLong();
        this.timeEnd = in.readLong();
        this.minutes = in.readString();
        this.sleepState = in.readString();
        this.timestamp = in.readLong();
    }

    public static final Creator<TblSleep> CREATOR = new Creator<TblSleep>() {
        @Override
        public TblSleep createFromParcel(Parcel source) {
            return new TblSleep(source);
        }

        @Override
        public TblSleep[] newArray(int size) {
            return new TblSleep[size];
        }
    };
}
