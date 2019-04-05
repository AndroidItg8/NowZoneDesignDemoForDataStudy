package itg8.com.nowzonedesigndemo.db.tbl;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;


/**
 * Created by swapnilmeshram on 04/10/17.
 */

@DatabaseTable(tableName = TblPostureSedentary.TABLE_NAME)
public class TblPostureSedentary implements Parcelable {
    public static final String TABLE_NAME = "tblPostureSedentary";

    public static final String FIELD_NAME_ID="id";
    public static final String FIELD_DATE="date";
    private static final String FIELD_START_TIMESTAMP="starttime";
    private static final String FIELD_END_TIMESTAMP="endtime";


    @DatabaseField(columnName = FIELD_NAME_ID, generatedId = true)
    private long id;

    @DatabaseField(columnName = FIELD_DATE, dataType = DataType.DATE_STRING, format = "yyyy-MM-dd")
    private Date date;

    @DatabaseField(columnName = FIELD_START_TIMESTAMP)
    private long startTimestamp;

    @DatabaseField(columnName = FIELD_END_TIMESTAMP)
    private long endTimestamp;


    public TblPostureSedentary() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(long startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public long getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(long endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeLong(this.date != null ? this.date.getTime() : -1);
        dest.writeLong(this.startTimestamp);
        dest.writeLong(this.endTimestamp);
    }

    protected TblPostureSedentary(Parcel in) {
        this.id = in.readLong();
        long tmpDate = in.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
        this.startTimestamp = in.readLong();
        this.endTimestamp = in.readLong();
    }

    public static final Creator<TblPostureSedentary> CREATOR = new Creator<TblPostureSedentary>() {
        @Override
        public TblPostureSedentary createFromParcel(Parcel source) {
            return new TblPostureSedentary(source);
        }

        @Override
        public TblPostureSedentary[] newArray(int size) {
            return new TblPostureSedentary[size];
        }
    };
}
