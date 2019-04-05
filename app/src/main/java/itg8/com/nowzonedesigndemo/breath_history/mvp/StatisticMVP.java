package itg8.com.nowzonedesigndemo.breath_history.mvp;

import java.util.Date;

/**
 * Created by swapnilmeshram on 28/09/17.
 */

public interface StatisticMVP {
    public interface StatisticView{
        void onComposedMinutesAvailable(StatisticModuleImp.BreathItem minutes);
        void onStressMinutesAvailable(StatisticModuleImp.BreathItem minutes);
        void onAttentiveMinutesAvailable(StatisticModuleImp.BreathItem minutes);

        void onDateRangeAvail(Date[] dates);

        void onStepsDataAvail(StatisticModuleImp.StepsItem steps);

        void onPostureDataAvail(StatisticModuleImp.PostureItem postureItem);
    }

    public interface StatisticPresenter{
        void onStartGettingDataFromData(Date date);
        void onDestroy();
    }

    public interface StatisticListener{
        void onComposedAvail(StatisticModuleImp.BreathItem minutes);
        void onStressAvail(StatisticModuleImp.BreathItem minutes);
        void onAttentiveAvail(StatisticModuleImp.BreathItem minutes);
        void onStepsAvail(StatisticModuleImp.StepsItem steps);
        void onPostureAvail(StatisticModuleImp.PostureItem minutes);
        void onSleepAvail(String sleepMinutes);

        void onDateRangeAvail(Date[] dates);
    }

    public interface StatisticModule{
        void setListener(StatisticListener listener);
        void onStartGettingData(Date date);
        void onDestrouyed();

        void getDistictDates();
    }
}
