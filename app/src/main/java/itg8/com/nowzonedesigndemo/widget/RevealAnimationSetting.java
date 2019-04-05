package itg8.com.nowzonedesigndemo.widget;

import android.os.Parcelable;

/**
 * Created by Android itg 8 on 9/22/2017.
 */

public abstract class RevealAnimationSetting implements Parcelable {
    public abstract int getCenterX();
    public abstract int getCenterY();
    public abstract int getWidth();
    public abstract int getHeight();

//    public static RevealAnimationSetting with(int centerX, int centerY, int width, int height) {
//        return new AutoValue_RevealAnimationSetting(centerX, centerY, width, height);
//    }
}