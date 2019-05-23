package itg8.com.nowzonedesigndemo.utility;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;

import itg8.com.nowzonedesigndemo.R;
import itg8.com.nowzonedesigndemo.common.AppApplication;
import itg8.com.nowzonedesigndemo.common.CommonMethod;

public class ExportToCsv {

    public static File exportEmailInCSV() throws IOException {


        File folder = new File(Environment.getExternalStorageDirectory()
                + "/Nowzone");

        boolean var = false;
        if (!folder.exists())
            var = folder.mkdir();

        System.out.println("" + var);


        final File file = new File(folder.toString(), (CommonMethod.getDateTimeForFileFromTMP(new Date().getTime())) + ".csv");
//        String data = "datetime,pressure,acc_x,acc_y,acc_z,gyr_x,gyr_y,gyr_z,mag_x,mag_y,mag_z,temperature,battery,pressure_processed,load_cell1,load_cell2,mic,charging,optical\n";
//        data = data + s;
        file.createNewFile();
        return file;
    }


    public static String getTitle() {
        return "datetime,pressure,acc_x,acc_y,acc_z,gyr_x,gyr_y,gyr_z,mag_x,mag_y,mag_z,temperature,battery,pressure_processed,load_cell1,load_cell2,mic,charging,optical,unused\n";
    }


    private static FileWriter fWriter;
    public static boolean writeToFile(String data,File file) {
        try {
                fWriter = new FileWriter(file, true);
                fWriter.append(data);
                fWriter.flush();
                fWriter.close();
            return true;
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
        return false;
    }

    public static void stopWriter(){
           fWriter=null;
    }

}
