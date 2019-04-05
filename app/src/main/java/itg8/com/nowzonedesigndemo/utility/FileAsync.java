package itg8.com.nowzonedesigndemo.utility;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import itg8.com.nowzonedesigndemo.common.DataModelPressure;
import timber.log.Timber;


public class FileAsync extends AsyncTask<DataModelPressure[], Void, Boolean> {

    public static final Object[] DATA_LOCK = new Object[0];
    private static final int ACTUAL_NUMBER_OF_COUNT_IN_FILE = 30;
    private static final String TAG = ServiceOnCheck.class.getSimpleName();
    private static final int BUFFER = 1024;
    private static String COMPLETE_FILE_PATH;

    private String[] fileList;
    private String content;

    public FileAsync(String path) {
        COMPLETE_FILE_PATH = path;
        File file = new File(path);
        if (file.exists()) {
            fileList = file.list();
        } else {
            fileList = new String[0];
        }
    }

    /**
     * Append String to end of File.
     *
     * @param appendContents
     * @param file
     * @return
     */
    public static boolean appendStringToFile(final String appendContents, final File file) {
        boolean result = false;
        try {
            synchronized (DATA_LOCK) {
                if (file != null && file.canWrite()) {
                    boolean b = file.createNewFile(); // ok if returns false, overwrite
                    Writer out = new BufferedWriter(new FileWriter(file, true), 1024);
                    out.write(appendContents);
                    out.close();
                    result = true;
                }
            }
        } catch (IOException e) {
            Timber.d(e.getMessage());
            //   Log.e(Constants.LOG_TAG, "Error appending string data to file " + e.getMessage(), e);
        }
        return result;
    }

    @Override
    protected final Boolean doInBackground(DataModelPressure[]... lists) {
        DataModelPressure[] dataModels =lists[0].clone();
        storeToFile(dataModels);
        return true;
    }

    /**
     * We will store complete data to file as per conditions
     *
     * @param models
     */
    private synchronized void storeToFile(DataModelPressure[] models) {
        /**
         * We are first storing json in file. But now we will create simple text file
         * TODO uncomment after SAAS
         */
//        String content = new Gson().toJson(models);

         content = createDataStructureFromModel(models);
        String[] allFileListByDt = getFileList();
        if (allFileListByDt.length > 0) {
            createFile(content, checkIfNewFileOrOldFileReplace(allFileListByDt[allFileListByDt.length-1],models[0].getTimestamp()),"0");
        } else {
            createFile(content, getIntFromTSMP(models[0].getTimestamp()) + ".txt", "0");
        }

    }

    private String createDataStructureFromModel(DataModelPressure[] models) {
        StringBuilder sb=new StringBuilder();
        for (DataModelPressure model :
                models) {
            sb.append(Helper.convertTimestampToTime(model.getTimestamp())).append(" ").append(model.getPressure()).append(" ").append(model.getX()).append(" ").append(model.getY()).append(" ").append(model.getZ()).append(" ").append(model.getBattery()).append(" ").append(model.getTemprature());
            sb.append("\n");
        }
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }

    //Timestamp is in full format. we dont want that much big . so eliminate millisecond
    private String getIntFromTSMP(long timestamp) {
        int count = 0;
        int next30=0;
        try {
            count = (int) (timestamp / 1000);
            next30=(int)count+1800;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG,"TempTP: Final file: "+String.valueOf(count)+"_"+String.valueOf(next30));
        return String.valueOf(count)+"_"+String.valueOf(next30);
    }

    /**
     * this method creates a file which is provided by
     *
     * @param content  actual json content
     * @param fileName actual file name
     */
    private void createFile(String content, String fileName) {
        if (content != null && fileName != null)
            writeInFile(content, fileName);
    }

    /**
     * this method creates a file which is provided by
     *
     * @param content  actual json content
     * @param fileName actual file name
     * @param count    actual count of json available in file
     */
    private void createFile(String content, String fileName, String count) {
//        String newFileName = count + "_" + fileName;
        writeInFile(content,  fileName);
    }

    private void writeInFile(String content, String newFileName) {
//        File completeFileStructure = new File(newFileName);
//        File completeFileStructure = new File(COMPLETE_FILE_PATH,Helper.getCurrentDate()+".txt");
        File completeFileStructure = new File(COMPLETE_FILE_PATH,newFileName);
        //Log.d(TAG,"FileName:"+completeFileStructure);
        try {
            FileWriter fWriter;
            fWriter = new FileWriter(completeFileStructure, true);
            if(completeFileStructure.exists()) {
                fWriter.append(content).append("\n");
            }else {
                fWriter.write(content);
            }
        fWriter.flush();
        fWriter.close();
                } catch (IOException e) {
            e.printStackTrace();
        }
        /**
         * Got file having data from http://www.java2s.com/Code/Android/File/Writeandappendstringtofile.htm
         */
//        boolean b=appendStringToFile(content,completeFileStructure);
//            Timber.d("FILE APPEND: "+b);
//        try {
        /**
         * this is my old method. Its creating same file override data
         */
//            FileWriter fWriter = new FileWriter(completeFileStructure,true);
//            fWriter.append(content).append("\n");
//            fWriter.flush();
//            fWriter.close();

//            FileOutputStream foutStream=context.openFileOutput(completeFileStructure.getName(), Context.MODE_APPEND);
        /**
         * New information from internet, @FileOutputStream true means append
         */
//            FileOutputStream fileOutputStream=new FileOutputStream(completeFileStructure,true);
//            OutputStreamWriter writer=new OutputStreamWriter(fileOutputStream);
//            writer.write(content);
//            writer.flush();
//            writer.close();

//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    private void writeContentToFile(String s, String models) {
//        File newFileName = checkIfNewFileOrOldFileReplace(models);
//        if (newFileName != null) {
//            createFile(s, newFileName.getAbsolutePath());
//        }
    }

    private String checkIfNewFileOrOldFileReplace(String models, long timestamp) {
        if (models.indexOf(".") > 0)
            models = models.substring(0, models.lastIndexOf("."));
        String[] getCountAndData = models.split("_");

        long count1;
        long count2;
        long tempTP;
        if (getCountAndData.length > 1) {
            String countString = getCountAndData[0];
            count1 = Long.parseLong(countString);
            count2=Long.parseLong(getCountAndData[1]);

            tempTP=(int)(timestamp/1000);
            Log.d(TAG,"TempTP:"+tempTP+" ");
            if(count1<tempTP && count2>=tempTP){
                return models+".txt";
            }else {
//                zip(COMPLETE_FILE_PATH+"/"+models+".txt",COMPLETE_FILE_PATH+"/"+models+".zip");
                return getIntFromTSMP(timestamp) + ".txt";
            }

//            if (count < ACTUAL_NUMBER_OF_COUNT_IN_FILE) {
//                count += 1;
//                newFile = new File(COMPLETE_FILE_PATH, String.valueOf(count) + "_" + getCountAndData[1]);
//                boolean b = oldFile.renameTo(newFile);
//                if (b)
//                    return newFile;
//                else
//                    return oldFile;
            } else {
            Log.i(TAG,"Error please debug "+models);
                return "Err.txt";
            }
//        }
//        return newFile;
    }

    private String[] getFileList() {

        return fileList;
    }


    public void zip(String _files, String zipFileName) {
        try {

            BufferedInputStream origin = null;
            FileOutputStream dest = new FileOutputStream(zipFileName);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
                    dest));
            byte data[] = new byte[BUFFER];

                Log.v("Compress", "Adding: " + _files);
                FileInputStream fi = new FileInputStream(_files);
                origin = new BufferedInputStream(fi, BUFFER);

                ZipEntry entry = new ZipEntry(_files.substring(_files.lastIndexOf("/") + 1));
                out.putNextEntry(entry);
                int count;

                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();

            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
    }
}
