package itg8.com.nowzonedesigndemo.common;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by itg_Android on 3/2/2017.
 */

public class DataModelAcc implements Parcelable {
    private static StringBuilder builder;
    private double pressure;
    private double x;
    private double y;
    private double z;
    private int temprature;
    private int battery;
    private long timestamp;

    public DataModelAcc(int pressure, int x, int y, int z, int temprature, int battery, long timestamp) {
        this.pressure = pressure;
        this.x = x;
        this.y = y;
        this.z = z;
        this.temprature = temprature;
        this.battery = battery;
        this.timestamp = timestamp;
    }





    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public int getTemprature() {
        return temprature;
    }

    public void setTemprature(int temprature) {
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










    public DataModelAcc() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.pressure);
        dest.writeDouble(this.x);
        dest.writeDouble(this.y);
        dest.writeDouble(this.z);
        dest.writeInt(this.temprature);
        dest.writeInt(this.battery);
        dest.writeLong(this.timestamp);
    }

    protected DataModelAcc(Parcel in) {
        this.pressure = in.readDouble();
        this.x = in.readDouble();
        this.y = in.readDouble();
        this.z = in.readDouble();
        this.temprature = in.readInt();
        this.battery = in.readInt();
        this.timestamp = in.readLong();
    }

    public static final Creator<DataModelAcc> CREATOR = new Creator<DataModelAcc>() {
        @Override
        public DataModelAcc createFromParcel(Parcel source) {
            return new DataModelAcc(source);
        }

        @Override
        public DataModelAcc[] newArray(int size) {
            return new DataModelAcc[size];
        }
    };
}
