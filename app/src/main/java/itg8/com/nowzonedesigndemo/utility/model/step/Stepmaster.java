
package itg8.com.nowzonedesigndemo.utility.model.step;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Stepmaster implements Parcelable
{

    @SerializedName("fromtime")
    @Expose
    private String fromtime;
    @SerializedName("totime")
    @Expose
    private String totime;
    @SerializedName("steps")
    @Expose
    private int steps;
    @SerializedName("calriesburn")
    @Expose
    private int calriesburn;
    @SerializedName("goal")
    @Expose
    private int goal;
    public final static Parcelable.Creator<Stepmaster> CREATOR = new Creator<Stepmaster>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Stepmaster createFromParcel(Parcel in) {
            Stepmaster instance = new Stepmaster();
            instance.fromtime = ((String) in.readValue((String.class.getClassLoader())));
            instance.totime = ((String) in.readValue((String.class.getClassLoader())));
            instance.steps = ((int) in.readValue((int.class.getClassLoader())));
            instance.calriesburn = ((int) in.readValue((int.class.getClassLoader())));
            instance.goal = ((int) in.readValue((int.class.getClassLoader())));
            return instance;
        }

        public Stepmaster[] newArray(int size) {
            return (new Stepmaster[size]);
        }

    }
    ;

    /**
     * 
     * @return
     *     The fromtime
     */
    public String getFromtime() {
        return fromtime;
    }

    /**
     * 
     * @param fromtime
     *     The fromtime
     */
    public void setFromtime(String fromtime) {
        this.fromtime = fromtime;
    }

    /**
     * 
     * @return
     *     The totime
     */
    public String getTotime() {
        return totime;
    }

    /**
     * 
     * @param totime
     *     The totime
     */
    public void setTotime(String totime) {
        this.totime = totime;
    }

    /**
     * 
     * @return
     *     The steps
     */
    public int getSteps() {
        return steps;
    }

    /**
     * 
     * @param steps
     *     The steps
     */
    public void setSteps(int steps) {
        this.steps = steps;
    }

    /**
     * 
     * @return
     *     The calriesburn
     */
    public int getCalriesburn() {
        return calriesburn;
    }

    /**
     * 
     * @param calriesburn
     *     The calriesburn
     */
    public void setCalriesburn(int calriesburn) {
        this.calriesburn = calriesburn;
    }


    public int getGoal() {
        return goal;
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(fromtime);
        dest.writeValue(totime);
        dest.writeValue(steps);
        dest.writeValue(calriesburn);
        dest.writeValue(goal);
    }

    public int describeContents() {
        return  0;
    }

}
