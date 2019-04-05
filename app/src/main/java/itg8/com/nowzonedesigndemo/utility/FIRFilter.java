package itg8.com.nowzonedesigndemo.utility;

/**
 * Created by swapnilmeshram on 26/10/17.
 */

public class FIRFilter {
    private int length;
    private double[] delayLine;
    private double[] impulseResponse;
    private int count=0;

    FIRFilter(double[] coef){
        length=coef.length;
        impulseResponse=coef;
        delayLine=new double[length];
    }

    public double getOutputSample(double inputSample){
        delayLine[count] =inputSample;
        double result=0.0;
        int index=count;
        for(int i=0;i<length; i++){
            result+=impulseResponse[i] * delayLine[index--];
            if(index<0) index=length;
        }
        if(++count>=length) count=0;
        return result;
    }
}
