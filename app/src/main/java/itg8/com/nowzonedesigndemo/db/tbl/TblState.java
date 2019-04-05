package itg8.com.nowzonedesigndemo.db.tbl;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


/**
 * Created by itg_Android on 6/7/2017.
 */
@DatabaseTable(tableName = TblState.TABLE_STATE)
public class TblState implements Parcelable {

    public static final String TABLE_STATE = "TABLE_STATE";
    public static final String FIELD_ID="id";
    public static final String FIELD_STATE="state";
    public static final String FIELD_DATE="date";
    private static final String FIELD_COUNT="count";
    private static final String FIELD_TIMESTAMP_START="timestamp_start";
    private static final String FIELD_TIMESTAMP_END="timestamp_end";
    private static final String FIELD_COMMENT="comment";
    private static final String FIELD_LATITUDE="latitude";
    private static final String FIELD_LONGITUDE="longitude";

    @DatabaseField(columnName = FIELD_ID, generatedId = true)
    private long id;

    @DatabaseField(columnName = FIELD_STATE)
    private String state;

    @DatabaseField(columnName = FIELD_DATE)
    private String date;

    @DatabaseField(columnName = FIELD_COUNT)
    private int count;

    @DatabaseField(columnName = FIELD_TIMESTAMP_START)
    private long timestampStart;


    @DatabaseField(columnName = FIELD_TIMESTAMP_END)
    private long timestampEnd;

    @DatabaseField(columnName = FIELD_COMMENT)
    private String comment;

    @DatabaseField(columnName = FIELD_LATITUDE)
    private double latitude;

    @DatabaseField(columnName = FIELD_LONGITUDE)
    private double longitude;


    public TblState() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getTimestampStart() {
        return timestampStart;
    }

    public void setTimestampStart(long timestampStart) {
        this.timestampStart = timestampStart;
    }

    public long getTimestampEnd() {
        return timestampEnd;
    }

    public void setTimestampEnd(long timestampEnd) {
        this.timestampEnd = timestampEnd;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.state);
        dest.writeString(this.date);
        dest.writeInt(this.count);
        dest.writeLong(this.timestampStart);
        dest.writeLong(this.timestampEnd);
        dest.writeString(this.comment);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
    }

    protected TblState(Parcel in) {
        this.id = in.readLong();
        this.state = in.readString();
        this.date = in.readString();
        this.count = in.readInt();
        this.timestampStart = in.readLong();
        this.timestampEnd = in.readLong();
        this.comment = in.readString();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
    }

    public static final Creator<TblState> CREATOR = new Creator<TblState>() {
        @Override
        public TblState createFromParcel(Parcel source) {
            return new TblState(source);
        }

        @Override
        public TblState[] newArray(int size) {
            return new TblState[size];
        }
    };
}
