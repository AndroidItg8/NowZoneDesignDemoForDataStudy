package itg8.com.nowzonedesigndemo.db.tbl;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


import java.util.Date;

import static itg8.com.nowzonedesigndemo.db.tbl.TblStepCount.FIELD_CAL_BURN;
import static itg8.com.nowzonedesigndemo.db.tbl.TblStepCount.FIELD_STEPS;

@DatabaseTable(tableName = TblStepCount.TABLE_NAME)
public class TblStepCount implements Parcelable {
    public static final String TABLE_NAME = "TABLE_STEP";
    private static final String  FILED_ID="id";
    public static final String FIELD_DATE="date";
    static final String FIELD_STEPS="steps";
    static final String FIELD_CAL_BURN="calburn";
    private static final String FIELD_GOAL="goals";
    private static final String START_TIMESTAMP="starttimestamp";
    private static final String END_TIMESTAMP="endtimestamp";

    @DatabaseField(columnName = FILED_ID,generatedId = true)
    private long id;

    @DatabaseField(columnName = FIELD_DATE, dataType = DataType.DATE_STRING, format = "yyyy-MM-dd")
    private Date date;

    @DatabaseField(columnName = FIELD_STEPS)
    private int steps;

    @DatabaseField(columnName = FIELD_CAL_BURN)
    private double calBurn;

    @DatabaseField(columnName = FIELD_GOAL)
    private int goal;

    @DatabaseField(columnName = START_TIMESTAMP)
    private long startTimestamp;

    @DatabaseField(columnName = END_TIMESTAMP)
    private long endTimestamp;

    public TblStepCount() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public double getCalBurn() {
        return calBurn;
    }

    public void setCalBurn(double calBurn) {
        this.calBurn = calBurn;
    }


    public int getGoal() {
        return goal;
    }

    public void setGoal(int goal) {
        this.goal = goal;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeLong(this.date != null ? this.date.getTime() : -1);
        dest.writeInt(this.steps);
        dest.writeDouble(this.calBurn);
        dest.writeInt(this.goal);
        dest.writeLong(this.startTimestamp);
        dest.writeLong(this.endTimestamp);
    }

    protected TblStepCount(Parcel in) {
        this.id = in.readLong();
        long tmpDate = in.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
        this.steps = in.readInt();
        this.calBurn = in.readDouble();
        this.goal = in.readInt();
        this.startTimestamp = in.readLong();
        this.endTimestamp = in.readLong();
    }

    public static final Creator<TblStepCount> CREATOR = new Creator<TblStepCount>() {
        @Override
        public TblStepCount createFromParcel(Parcel source) {
            return new TblStepCount(source);
        }

        @Override
        public TblStepCount[] newArray(int size) {
            return new TblStepCount[size];
        }
    };
}
