
package itg8.com.nowzonedesigndemo.utility.model.breath;

import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BreathingModel implements Parcelable
{

    @SerializedName("Breathingmaster")
    @Expose
    private List<itg8.com.nowzonedesigndemo.utility.model.breath.Breathingmaster> Breathingmaster = new ArrayList<itg8.com.nowzonedesigndemo.utility.model.breath.Breathingmaster>();
    public final static Parcelable.Creator<BreathingModel> CREATOR = new Creator<BreathingModel>() {


        @SuppressWarnings({
            "unchecked"
        })
        public BreathingModel createFromParcel(Parcel in) {
            BreathingModel instance = new BreathingModel();
            in.readList(instance.Breathingmaster, (itg8.com.nowzonedesigndemo.utility.model.breath.Breathingmaster.class.getClassLoader()));
            return instance;
        }

        public BreathingModel[] newArray(int size) {
            return (new BreathingModel[size]);
        }

    }
    ;

    /**
     * 
     * @return
     *     The Breathingmaster
     */
    public List<itg8.com.nowzonedesigndemo.utility.model.breath.Breathingmaster> getBreathingmaster() {
        return Breathingmaster;
    }

    /**
     * 
     * @param Breathingmaster
     *     The Breathingmaster
     */
    public void setBreathingmaster(List<itg8.com.nowzonedesigndemo.utility.model.breath.Breathingmaster> Breathingmaster) {
        this.Breathingmaster = Breathingmaster;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(Breathingmaster);
    }

    public int describeContents() {
        return  0;
    }

}
