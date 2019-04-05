package itg8.com.nowzonedesigndemo.breath_history.mvp;

import java.util.Date;

import itg8.com.nowzonedesigndemo.common.BaseWeakPresenter;

/**
 * Created by swapnilmeshram on 28/09/17.
 */

public class StatisticPresenterImp extends BaseWeakPresenter implements StatisticMVP.StatisticPresenter, StatisticMVP.StatisticListener {

    StatisticMVP.StatisticModule module;
    public StatisticPresenterImp(StatisticMVP.StatisticView o) {
        super(o);
        module=new StatisticModuleImp();
        module.setListener(this);
        module.getDistictDates();
    }

    @Override
    public void onStartGettingDataFromData(Date date) {
        module.onStartGettingData(date);
    }

    @Override
    public void onDestroy() {

    }

    private StatisticMVP.StatisticView getStatView(){
        return (StatisticMVP.StatisticView) getView();
    }

    @Override
    public void onComposedAvail(StatisticModuleImp.BreathItem minutes) {
        if(hasView())
            getStatView().onComposedMinutesAvailable(minutes);
    }

    @Override
    public void onStressAvail(StatisticModuleImp.BreathItem minutes) {
        if(hasView())
            getStatView().onStressMinutesAvailable(minutes);
    }

    @Override
    public void onAttentiveAvail(StatisticModuleImp.BreathItem minutes) {
        if(hasView())
            getStatView().onAttentiveMinutesAvailable(minutes);
    }

    @Override
    public void onStepsAvail(StatisticModuleImp.StepsItem steps) {
        if(hasView()){
            getStatView().onStepsDataAvail(steps);
        }
    }

    @Override
    public void onPostureAvail(StatisticModuleImp.PostureItem postureItem) {
        if(hasView()){
            getStatView().onPostureDataAvail(postureItem);
        }
    }

    @Override
    public void onSleepAvail(String sleepMinutes) {

    }

    @Override
    public void onDateRangeAvail(Date[] dates) {
        if(hasView())
            getStatView().onDateRangeAvail(dates);
    }
}
