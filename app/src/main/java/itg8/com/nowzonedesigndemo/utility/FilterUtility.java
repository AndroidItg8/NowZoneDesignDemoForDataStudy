package itg8.com.nowzonedesigndemo.utility;

import java.util.ArrayList;
import java.util.List;

import itg8.com.nowzonedesigndemo.db.tbl.TblState;
import itg8.com.nowzonedesigndemo.db.tbl.TblStepCount;

/**
 * Created by Android itg 8 on 9/20/2017.
 */

public class FilterUtility {

    private FilterBuilder filterBuilder;

    public FilterUtility(FilterBuilder filterBuilder) {

        this.filterBuilder = filterBuilder;
    }

    public  List<TblState>  getFilteredList()
    {
        return filterBuilder.tblStateList;
    }

    public List<TblStepCount> getFilterStepList()
    {
       return filterBuilder.tblStepCountsList;
    }

    public static class FilterBuilder{
        private List<TblState> tblStateList;
        private List<TblStepCount> tblStepCountsList;

        public FilterBuilder createBuilder(List<TblState> tblStateList)
        {

            this.tblStateList = new ArrayList<>();
            this.tblStateList.addAll(tblStateList);


            return this;
        }
//         public  FilterBuilder createStepBuilder(List<TblStepCount> listActivityState)
//         {
//             this.tblStepCountsList = new ArrayList<>();
//             this.tblStepCountsList.addAll(listActivityState);
//              return  this;
//         }

        public FilterBuilder setFilter(BreathState state)
        {
            List<TblState> tempTblStates = new ArrayList<>();
            for (TblState tbleState: tblStateList) {
                 if(tbleState.getState().equalsIgnoreCase(state.name()))
                 {
                     tempTblStates.add(tbleState);
                 }
            }
            tblStateList.clear();
            tblStateList.addAll(tempTblStates);

            return this;


        }

//         public FilterBuilder setStepFilter(ActivityState typeStep)
//         {
//             List<TblStepCount> tempTblStepCount = new ArrayList<>();
//
//             for (TblStepCount tbleStepCount: tblStepCountsList) {
//                 if(tbleStepCount.getState().equalsIgnoreCase(typeStep.name()))
//                 {
//                     tempTblStepCount.add(tbleStepCount);
//                 }
//             }
//             tblStepCountsList.clear();
//             tblStepCountsList.addAll(tempTblStepCount);
//
//             return this;
//         }
        public FilterUtility build()
        {
             return new FilterUtility(this);
        }
    }
}
