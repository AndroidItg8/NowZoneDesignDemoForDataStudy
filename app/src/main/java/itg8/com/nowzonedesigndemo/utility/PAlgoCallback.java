package itg8.com.nowzonedesigndemo.utility;


import android.util.SparseArray;

import java.util.HashMap;
import java.util.List;

interface PAlgoCallback {
    void onCountResultAvailable(int count, long model, SparseArray<List<Integer>> allDiff);

    void onCountFail();

}
