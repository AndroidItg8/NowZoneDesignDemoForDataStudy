package itg8.com.nowzonedesigndemo.accelerometer;

public class ColorController {
    public static final String COLOR_X = "#1976D2";
    public static final String COLOR_Y = "#388E3C";
    public static final String COLOR_Z = "#FF5722";
    public static final String COLOR_DEFAULT = "#b3a13c";
    private String colors = null;

    public ColorController() {
    }

//    public String setColorToGraph(int temp, int whereFrom) {
//        colors = colorForAccMagnoGyno(temp);
//
//
//        return colors;
//    }





    public String colorForAccMagnoGyno(int temp) {
        if (temp == 0)
            colors = COLOR_X;
        else if (temp == 1)
            colors = COLOR_Y;
        else if (temp == 2)
            colors = COLOR_Z;
        else
            colors= COLOR_DEFAULT;
        return colors;


    }
}
