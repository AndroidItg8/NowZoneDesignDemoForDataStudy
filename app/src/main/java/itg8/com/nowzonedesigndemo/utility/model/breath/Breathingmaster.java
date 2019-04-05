
package itg8.com.nowzonedesigndemo.utility.model.breath;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Breathingmaster implements Parcelable
{

    @SerializedName("time_of")
    @Expose
    private String timeOf;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("count")
    @Expose
    private int count;
    @SerializedName("stateofmindmaster_id")
    @Expose
    private int stateofmindmasterId;
    public final static Parcelable.Creator<Breathingmaster> CREATOR = new Creator<Breathingmaster>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Breathingmaster createFromParcel(Parcel in) {
            Breathingmaster instance = new Breathingmaster();
            instance.timeOf = ((String) in.readValue((String.class.getClassLoader())));
            instance.created = ((String) in.readValue((String.class.getClassLoader())));
            instance.count = ((int) in.readValue((int.class.getClassLoader())));
            instance.stateofmindmasterId = ((int) in.readValue((int.class.getClassLoader())));
            return instance;
        }

        public Breathingmaster[] newArray(int size) {
            return (new Breathingmaster[size]);
        }

    }
    ;

    /**
     * 
     * @return
     *     The timeOf
     */
    public String getTimeOf() {
        return timeOf;
    }

    /**
     * 
     * @param timeOf
     *     The time_of
     */
    public void setTimeOf(String timeOf) {
        this.timeOf = timeOf;
    }

    /**
     * 
     * @return
     *     The count
     */
    public int getCount() {
        return count;
    }

    /**
     * 
     * @param count
     *     The count
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * 
     * @return
     *     The stateofmindmasterId
     */
    public int getStateofmindmasterId() {
        return stateofmindmasterId;
    }

    /**
     * 
     * @param stateofmindmasterId
     *     The stateofmindmaster_id
     */
    public void setStateofmindmasterId(int stateofmindmasterId) {
        this.stateofmindmasterId = stateofmindmasterId;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(timeOf);
        dest.writeValue(created);
        dest.writeValue(count);
        dest.writeValue(stateofmindmasterId);
    }

    public int describeContents() {
        return  0;
    }

}
