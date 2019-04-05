
package itg8.com.nowzonedesigndemo.utility.model.step;

import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StepsModel implements Parcelable
{

    @SerializedName("Stepmaster")
    @Expose
    private List<itg8.com.nowzonedesigndemo.utility.model.step.Stepmaster> Stepmaster = new ArrayList<itg8.com.nowzonedesigndemo.utility.model.step.Stepmaster>();
    public final static Parcelable.Creator<StepsModel> CREATOR = new Creator<StepsModel>() {


        @SuppressWarnings({
            "unchecked"
        })
        public StepsModel createFromParcel(Parcel in) {
            StepsModel instance = new StepsModel();
            in.readList(instance.Stepmaster, (itg8.com.nowzonedesigndemo.utility.model.step.Stepmaster.class.getClassLoader()));
            return instance;
        }

        public StepsModel[] newArray(int size) {
            return (new StepsModel[size]);
        }

    }
    ;

    /**
     * 
     * @return
     *     The Stepmaster
     */
    public List<itg8.com.nowzonedesigndemo.utility.model.step.Stepmaster> getStepmaster() {
        return Stepmaster;
    }

    /**
     * 
     * @param Stepmaster
     *     The Stepmaster
     */
    public void setStepmaster(List<itg8.com.nowzonedesigndemo.utility.model.step.Stepmaster> Stepmaster) {
        this.Stepmaster = Stepmaster;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(Stepmaster);
    }

    public int describeContents() {
        return  0;
    }

}
