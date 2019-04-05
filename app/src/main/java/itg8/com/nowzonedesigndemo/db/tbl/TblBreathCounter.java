package itg8.com.nowzonedesigndemo.db.tbl;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;
import java.util.UUID;

/**
 * Created by itg_Android on 4/17/2017.
 */

@DatabaseTable(tableName = TblBreathCounter.TABLE_NAME_BREATH_COUNTER)
public class TblBreathCounter implements Parcelable {
    public static final String TABLE_NAME_BREATH_COUNTER="breath_counter";

    public static final String FIELD_NAME_ID="id";
    public static final String FIELD_NAME_COUNT="count";
    public static final String FIELD_NAME_TIMESTAMP="timestamp";
    public static final String FIELD_DATE="date";

    @DatabaseField(columnName = FIELD_NAME_ID, generatedId = true)
    private long id;

    @DatabaseField(columnName = FIELD_NAME_COUNT)
    private int count;

    @DatabaseField(columnName = FIELD_NAME_TIMESTAMP)
    private long timestamp;


    @DatabaseField(columnName = FIELD_DATE, dataType = DataType.DATE_STRING, format = "yyyy-MM-dd")
    private Date date;


    public TblBreathCounter() {
        // Don't forget the empty constructor, needed by ORMLite.
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeInt(this.count);
        dest.writeLong(this.timestamp);
        dest.writeLong(this.date != null ? this.date.getTime() : -1);
    }

    protected TblBreathCounter(Parcel in) {
        this.id = in.readLong();
        this.count = in.readInt();
        this.timestamp = in.readLong();
        long tmpDate = in.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
    }

    public static final Creator<TblBreathCounter> CREATOR = new Creator<TblBreathCounter>() {
        @Override
        public TblBreathCounter createFromParcel(Parcel source) {
            return new TblBreathCounter(source);
        }

        @Override
        public TblBreathCounter[] newArray(int size) {
            return new TblBreathCounter[size];
        }
    };
}
