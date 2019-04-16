package itg8.com.nowzonedesigndemo.home.mvp;

import android.content.Context;

public interface BreathFragmentModel {
    void checkBLEConnected(Context context);

    void onInitStateTime();

    void initDB(Context context);

    void onDestroy();

    void dataStarted(boolean b);
}
