package itg8.com.nowzonedesigndemo.common;

import android.app.Application;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.leakcanary.LeakCanary;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import java.util.ArrayList;
import java.util.List;

import itg8.com.nowzonedesigndemo.db.BreathStateImpModel;
import itg8.com.nowzonedesigndemo.db.DBManager;
import itg8.com.nowzonedesigndemo.db.DbHelper;
import itg8.com.nowzonedesigndemo.db.tbl.TblStepCount;
import itg8.com.nowzonedesigndemo.utility.BreathState;
import itg8.com.nowzonedesigndemo.utility.Helper;
import itg8.com.nowzonedesigndemo.utility.model.breath.BreathingModel;
import itg8.com.nowzonedesigndemo.utility.model.breath.Breathingmaster;
import itg8.com.nowzonedesigndemo.utility.model.step.Stepmaster;
import itg8.com.nowzonedesigndemo.utility.model.step.StepsModel;
import timber.log.BuildConfig;
import timber.log.Timber;

import static itg8.com.nowzonedesigndemo.common.CommonMethod.SHARED;
import static timber.log.Timber.DebugTree;

@ReportsCrashes(formUri = "", mailTo = "app.itechgalaxy@gmail.com", mode = ReportingInteractionMode.SILENT)
public class AppApplication extends Application {

    private static final String PROFILE_MODEL = "PROFILE_MODEL";
    private static AppApplication mInstance;
    private ProfileModel profileModel;
    private BreathingModel mBreathModel;
    private StepsModel mStepModel = new StepsModel();

    public static synchronized AppApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
//        ACRA.init(this);
        mInstance.initPreference();
        mBreathModel = new BreathingModel();
        Prefs.putString(CommonMethod.STATE, null);
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }

        LeakCanary.install(this);
        if (BuildConfig.DEBUG) {
            Timber.plant(new DebugTree());
        } else {
            Timber.plant(new CrashReportingTree());
        }
    }

    private void initPreference() {
        new Prefs.Builder()
                .setContext(this)
                .setMode(MODE_PRIVATE)
                .setPrefsName(SHARED)
                .setUseDefaultSharedPreference(false)
                .build();
    }

    public ProfileModel getProfileModel() {
        String mProfileModel = Prefs.getString(PROFILE_MODEL, null);
        if (profileModel == null && mProfileModel != null) {
            profileModel = new Gson().fromJson(mProfileModel, new TypeToken<ProfileModel>() {
            }.getType());
        }
        return profileModel;
    }

    public void setProfileModel(ProfileModel model) {
        if (model != null)
            Prefs.putString(PROFILE_MODEL, new Gson().toJson(model));
    }

    public BreathingModel getBreathModel() {
        return mBreathModel;
    }


    public void resetBreathing() {
        mBreathModel = new BreathingModel();
    }

    public BreathingModel addBreathing(Breathingmaster detail) {
        List<Breathingmaster> details = getBreathingModelFromPref();
        if (details == null) {
            details = new ArrayList<>();
        }
        details.add(detail);
        mBreathModel.setBreathingmaster(details);
        Prefs.putString(CommonMethod.STORAGE_BREATHS, new Gson().toJson(mBreathModel));
        return mBreathModel;
    }


    public void removeBreathing(Breathingmaster detail) {
        List<Breathingmaster> details = getBreathingModelFromPref();
        if (details == null) {
            details = new ArrayList<>();
        }
//        if (details.contains(detail))
        Prefs.remove(CommonMethod.STORAGE_BREATHS);
        details.clear();
    }


    public BreathStateImpModel getBreathStateModel() {
        BreathStateImpModel impModel = new BreathStateImpModel(this);
        return impModel;
    }

    public void addSteps(Stepmaster count) {
        List<Stepmaster> list = getStepsModelFromPref();
        if (list == null || list.size() <= 0) {
            list = new ArrayList<>();
        }
        list.add(count);
        mStepModel.setStepmaster(list);
        Prefs.putString(CommonMethod.STORAGE_STEPS, new Gson().toJson(mStepModel));
    }


    public StepsModel getStepsToSendOnServer() {
        getStepsModelFromPref();
        return mStepModel;
    }


    public void updateSteps(int step, int calBurn, long endTimestamp) {
        List<Stepmaster> list = mStepModel.getStepmaster();
        if (list == null || list.size() <= 0) {
            throw new IllegalStateException("Cannot update null stepsmaster");
        }
        Stepmaster m = list.get(list.size() - 1);
        m.setTotime(Helper.convertTimestampToTime(endTimestamp));
        mStepModel.setStepmaster(list);
    }

    public void resetSteps() {
        mStepModel = new StepsModel();
        Prefs.remove(CommonMethod.STORAGE_STEPS);
    }

    public void removeLastStep() {
        if (mStepModel.getStepmaster() != null && mStepModel.getStepmaster().size() > 0) {
            mStepModel.getStepmaster().remove(mStepModel.getStepmaster().size() - 1);
        }
    }

    public void storeStepsAndBreath() {
        Prefs.putString(CommonMethod.STORAGE_STEPS, new Gson().toJson(mStepModel));
        Prefs.putString(CommonMethod.STORAGE_BREATHS, new Gson().toJson(mBreathModel));
    }


    public void restoreStepsAndBreath() {
        if (Prefs.contains(CommonMethod.STORAGE_STEPS))
            mStepModel = new Gson().fromJson(Prefs.getString(CommonMethod.STORAGE_STEPS, ""), StepsModel.class);
        if (Prefs.contains(CommonMethod.STORAGE_BREATHS))
            mBreathModel = new Gson().fromJson(Prefs.getString(CommonMethod.STORAGE_BREATHS, ""), BreathingModel.class);
    }

    public List<Breathingmaster> getBreathingModelFromPref() {
        if (Prefs.getString(CommonMethod.STORAGE_BREATHS, null) != null) {
            mBreathModel = new Gson().fromJson(Prefs.getString(CommonMethod.STORAGE_BREATHS, null), BreathingModel.class);
        }
        return mBreathModel.getBreathingmaster();
    }

    public List<Stepmaster> getStepsModelFromPref() {
        if (Prefs.getString(CommonMethod.STORAGE_STEPS, null) != null) {
            mStepModel = new Gson().fromJson(Prefs.getString(CommonMethod.STORAGE_STEPS, null), StepsModel.class);
        }
        return mStepModel.getStepmaster();
    }

    public void changeLastBreathingState() {
        if (mBreathModel.getBreathingmaster() != null && mBreathModel.getBreathingmaster().size() > 0)
            mBreathModel.getBreathingmaster().get(mBreathModel.getBreathingmaster().size() - 1).setStateofmindmasterId(Helper.getIdByState(BreathState.UNKNOWN));
    }

    public double getAverageStepCountTimeDiff() {
        double mAverage = Prefs.getDouble(CommonMethod.AVG_STEP_COUNT_TIME_DIFF, 0);
        long mCount = Prefs.getLong(CommonMethod.COUNT_FOR_AVG, 0);
        if (mAverage == 0 || mCount == 0)
            return 0;
        if (mCount < 60)
            return 0;
        return mAverage;
    }

    public void addAverageStepsDiff(int diff) {
        double mAverage = Prefs.getDouble(CommonMethod.AVG_STEP_COUNT_TIME_DIFF, 0);
        long mCount = Prefs.getLong(CommonMethod.COUNT_FOR_AVG, 0) + 1;
        mAverage = (mAverage + diff) / (mCount);
        Prefs.putDouble(CommonMethod.AVG_STEP_COUNT_TIME_DIFF, mAverage);
        Prefs.putLong(CommonMethod.COUNT_FOR_AVG, mCount);
    }

    /**
     * A tree which logs important information for crash reporting.
     */
    private static class CrashReportingTree extends Timber.Tree {
        @Override
        protected void log(int priority, String tag, String message, Throwable t) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return;
            }

            FakeCrashLibrary.log(priority, tag, message);

            if (t != null) {
                if (priority == Log.ERROR) {
                    FakeCrashLibrary.logError(t);
                } else if (priority == Log.WARN) {
                    FakeCrashLibrary.logWarning(t);
                }
            }
        }
    }


}
