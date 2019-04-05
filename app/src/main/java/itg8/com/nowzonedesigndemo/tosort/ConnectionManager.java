package itg8.com.nowzonedesigndemo.tosort;

import java.util.UUID;

/**
 * Created by itg_Android on 2/28/2017.
 */
public interface ConnectionManager {
    void onDistroy();

    void selectedDevice(String address, String name);

    boolean disconnect();

    void isDisconnectedByUser(boolean b);

    void onStartVibrationLow();
}
