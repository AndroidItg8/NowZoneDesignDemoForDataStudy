package itg8.com.nowzonedesigndemo.common;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

import static com.j256.ormlite.field.DataType.DATE_LONG;

/**
 * Created by itg_Android on 3/2/2017.
 */

@DatabaseTable(tableName = DataModelPressure.TABLE_NAME)
public class DataModelPressure implements Parcelable {
    public static final String TABLE_NAME = "DataModelPressure";

    public static final String FIELD_SERIAL_NO = "serial_no";

    public static final String FIELD_PRESSURE = "pressure";

    public static final String FIELD_PRESSURE_COUNT = "pressure_count";

    public static final String FIELD_ACC_X = "acc_x";
    public static final String FIELD_ACC_Y = "acc_y";
    public static final String FIELD_ACC_Z = "acc_z";

    public static final String FIELD_GYR_X = "gyr_x";
    public static final String FIELD_GYR_Y = "gyr_y";
    public static final String FIELD_GYR_Z = "gyr_z";

    public static final String FIELD_MAG_X = "mag_x";
    public static final String FIELD_MAG_Y = "mag_y";
    public static final String FIELD_MAG_Z = "mag_z";

    public static final String FIELD_TEMP = "temp";
    public static final String FIELD_BATTERY = "battery";
    public static final String FIELD_TIMESTAMP = "timestamp";
    public static final String FIELD_LOAD_CELL_ONE = "load_cell_one";
    public static final String FIELD_LOAD_CELL_TWO = "load_cell_two";
    public static final String FIELD_MIC = "mic";
    public static final String FIELD_CHARGING = "charging";
    public static final String FIELD_OPTICAL = "optical";
    public static final String FIELD_IS_SENT = "is_sent";

    public static final String FIELD_DATETIME = "datetime";
    public static final String FIELD_DATE = "date";
    public static final String FIELD_TIME = "time";
    public static final String FIELD_UNUSED = "unused";
    public static final String FIELD_USERID = "user_id";
    public static final String FIELD_TIMESTAMP_DATE = "timestamp_date";

    @DatabaseField(columnName = FIELD_SERIAL_NO, generatedId = true)
    @SerializedName("sr_no")
    @Expose
    private long serialNo;

    @DatabaseField(columnName = FIELD_PRESSURE)
    @SerializedName("pressure")
    @Expose
    private double pressure;

    @DatabaseField(columnName = FIELD_ACC_X)
    @SerializedName("acc_x")
    @Expose
    private float x;

    @DatabaseField(columnName = FIELD_ACC_Y)
    @SerializedName("acc_y")
    @Expose
    private float y;

    @DatabaseField(columnName = FIELD_ACC_Z)
    @SerializedName("acc_z")
    @Expose
    private float z;

    @DatabaseField(columnName = FIELD_GYR_X)
    @SerializedName("gyr_x")
    @Expose
    private double gX;

    @DatabaseField(columnName = FIELD_GYR_Y)
    @SerializedName("gyr_y")
    @Expose
    private double gY;

    @DatabaseField(columnName = FIELD_GYR_Z)
    @SerializedName("gyr_z")
    @Expose
    private double gZ;

    @DatabaseField(columnName = FIELD_MAG_X)
    @SerializedName("mag_x")
    @Expose
    private double mX;

    @DatabaseField(columnName = FIELD_MAG_Y)
    @SerializedName("mag_y")
    @Expose
    private double mY;

    @DatabaseField(columnName = FIELD_MAG_Z)
    @SerializedName("mag_z")
    @Expose
    private double mZ;

    @DatabaseField(columnName = FIELD_TEMP)
    @SerializedName("tempreture")
    @Expose
    private float temprature;

    @DatabaseField(columnName = FIELD_BATTERY)
    @SerializedName("battery")
    @Expose
    private int battery;

    @DatabaseField(columnName = FIELD_TIMESTAMP)
    @Expose
    private long timestamp;
    @DatabaseField(columnName = FIELD_TIMESTAMP_DATE,dataType = DATE_LONG)
    @Expose
    private Date timestampDate;

    @DatabaseField(columnName = FIELD_PRESSURE_COUNT)
    @SerializedName("pressure_processed")
    @Expose
    private double pressureProcessed;

    @DatabaseField(columnName = FIELD_LOAD_CELL_ONE)
    @SerializedName("load_cell_1")
    @Expose
    private long loadCell1;

    @DatabaseField(columnName = FIELD_LOAD_CELL_TWO)
    @SerializedName("load_cell_2")
    @Expose
    private long loadCell2;

    @DatabaseField(columnName = FIELD_MIC)
    @SerializedName("mic")
    @Expose
    private long mic;

    @DatabaseField(columnName = FIELD_CHARGING)
    @SerializedName("chargingbit")
    @Expose
    private int charging;

    @DatabaseField(columnName = FIELD_OPTICAL)
    @SerializedName("optical")
    @Expose
    private long optical;

    @DatabaseField(columnName = FIELD_IS_SENT)
    private boolean isSent;

    @DatabaseField(columnName = FIELD_DATETIME)
    @SerializedName("datetime")
    @Expose
    private String datetime;

    @DatabaseField(columnName = FIELD_DATE)
    @SerializedName("date")
    @Expose
    private String date;

    @DatabaseField(columnName = FIELD_TIME)
    @SerializedName("time")
    @Expose
    private String time;


    @DatabaseField(columnName = FIELD_UNUSED)
    @SerializedName("unused")
    @Expose
    private String unused;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @DatabaseField(columnName = FIELD_USERID)
    @SerializedName("user_id")
    @Expose
    private String userId;


    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUnused() {
        return unused;
    }

    public void setUnused(String unused) {
        this.unused = unused;
    }




    public boolean isSent() {
        return isSent;
    }

    public void setSent(boolean sent) {
        isSent = sent;
    }

    public double getgX() {
        return gX;
    }

    public void setgX(double gX) {
        this.gX = gX;
    }

    public double getgY() {
        return gY;
    }

    public void setgY(double gY) {
        this.gY = gY;
    }

    public double getgZ() {
        return gZ;
    }

    public void setgZ(double gZ) {
        this.gZ = gZ;
    }

    public double getmX() {
        return mX;
    }

    public void setmX(double mX) {
        this.mX = mX;
    }

    public double getmY() {
        return mY;
    }

    public void setmY(double mY) {
        this.mY = mY;
    }

    public double getmZ() {
        return mZ;
    }

    public void setmZ(double mZ) {
        this.mZ = mZ;
    }

    public double getPressureProcessed() {
        return pressureProcessed;
    }

    public void setPressureProcessed(double pressureProcessed) {
        this.pressureProcessed = pressureProcessed;
    }

    public long getLoadCell1() {
        return loadCell1;
    }

    public void setLoadCell1(long loadCell1) {
        this.loadCell1 = loadCell1;
    }

    public long getLoadCell2() {
        return loadCell2;
    }

    public void setLoadCell2(long loadCell2) {
        this.loadCell2 = loadCell2;
    }

    public long getMic() {
        return mic;
    }

    public void setMic(long mic) {
        this.mic = mic;
    }

    public int getCharging() {
        return charging;
    }

    public void setCharging(int charging) {
        this.charging = charging;
    }

    public long getOptical() {
        return optical;
    }

    public void setOptical(long optical) {
        this.optical = optical;
    }

    public long getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(long serialNo) {
        this.serialNo = serialNo;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public float getTemprature() {
        return temprature;
    }

    public void setTemprature(float temprature) {
        this.temprature = temprature;
    }

    public int getBattery() {
        return battery;
    }

    public void setBattery(int battery) {
        this.battery = battery;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }


    public DataModelPressure() {
    }


    public Date getTimestampDate() {
        return timestampDate;
    }

    public void setTimestampDate(Date timestampDate) {
        this.timestampDate = timestampDate;
    }


    @Override
    public String toString() {
        return new StringBuilder().append("serial_no:").append(serialNo).append(" ").append("pressure: ").append(pressure).append("pressure_count :").append(pressureProcessed).append("acc_x :").append(x).append(" ").append("acc_y :").append(y).append(" ").append("acc_z :").append(z).append(" ").append("gyr_x :").append(gX).append(" ").append("gyr_y :").append(gY).append(" ").append("gyr_z :").append(gZ).append(" ").append("mag_x :").append(mX).append(" ").append("mag_y :").append(mY).append(" ").append("mag_z :").append(mZ).append(" ").append("temp :").append(temprature).append(" ").append("battery :").append(battery).append(" ").append("timestamp :").append(timestamp).append(" ").append("load_cell_one :").append(loadCell1).append(" ").append("load_cell_two :").append(loadCell2).append(" ").append("mic :").append(mic).append(" ").append("charging :").append(charging).append(" ").append("optical :").append(optical).append(" ").append("is_sent :").append(isSent).toString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.serialNo);
        dest.writeDouble(this.pressure);
        dest.writeFloat(this.x);
        dest.writeFloat(this.y);
        dest.writeFloat(this.z);
        dest.writeDouble(this.gX);
        dest.writeDouble(this.gY);
        dest.writeDouble(this.gZ);
        dest.writeDouble(this.mX);
        dest.writeDouble(this.mY);
        dest.writeDouble(this.mZ);
        dest.writeFloat(this.temprature);
        dest.writeInt(this.battery);
        dest.writeLong(this.timestamp);
        dest.writeLong(this.timestampDate != null ? this.timestampDate.getTime() : -1);
        dest.writeDouble(this.pressureProcessed);
        dest.writeLong(this.loadCell1);
        dest.writeLong(this.loadCell2);
        dest.writeLong(this.mic);
        dest.writeInt(this.charging);
        dest.writeLong(this.optical);
        dest.writeByte(this.isSent ? (byte) 1 : (byte) 0);
        dest.writeString(this.datetime);
        dest.writeString(this.date);
        dest.writeString(this.time);
        dest.writeString(this.unused);
        dest.writeString(this.userId);
    }

    protected DataModelPressure(Parcel in) {
        this.serialNo = in.readLong();
        this.pressure = in.readDouble();
        this.x = in.readFloat();
        this.y = in.readFloat();
        this.z = in.readFloat();
        this.gX = in.readDouble();
        this.gY = in.readDouble();
        this.gZ = in.readDouble();
        this.mX = in.readDouble();
        this.mY = in.readDouble();
        this.mZ = in.readDouble();
        this.temprature = in.readFloat();
        this.battery = in.readInt();
        this.timestamp = in.readLong();
        long tmpTimestampDate = in.readLong();
        this.timestampDate = tmpTimestampDate == -1 ? null : new Date(tmpTimestampDate);
        this.pressureProcessed = in.readDouble();
        this.loadCell1 = in.readLong();
        this.loadCell2 = in.readLong();
        this.mic = in.readLong();
        this.charging = in.readInt();
        this.optical = in.readLong();
        this.isSent = in.readByte() != 0;
        this.datetime = in.readString();
        this.date = in.readString();
        this.time = in.readString();
        this.unused = in.readString();
        this.userId = in.readString();
    }

    public static final Creator<DataModelPressure> CREATOR = new Creator<DataModelPressure>() {
        @Override
        public DataModelPressure createFromParcel(Parcel source) {
            return new DataModelPressure(source);
        }

        @Override
        public DataModelPressure[] newArray(int size) {
            return new DataModelPressure[size];
        }
    };
}
