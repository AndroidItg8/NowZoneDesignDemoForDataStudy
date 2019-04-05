package itg8.com.nowzonedesigndemo.widget.navigation;

import android.support.annotation.IdRes;

/**
 * Created by Android itg 8 on 9/16/2017.
 */

public interface OnTabSelectListener {
    void onTabSelected(@IdRes int tabId);
    void onTabPostionSelect(int position);
}
