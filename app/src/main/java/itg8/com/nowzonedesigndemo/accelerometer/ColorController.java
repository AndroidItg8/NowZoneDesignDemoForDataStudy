package itg8.com.nowzonedesigndemo.accelerometer;

public class ColorController {
    private String colors = null;

    public ColorController() {
    }

    public String setColorToGraph(int temp, int whereFrom) {

        if (whereFrom == 1 || whereFrom == 2 || whereFrom == 3) {
            colors = colorForAccMagnoGyno(temp);
        }
        else if (whereFrom == 4) {
            colors = colorForLoadCell(temp);
        }
        else if (whereFrom == 5 || whereFrom == 6 || whereFrom == 7) {
            colors = colorForSignal(temp);
        }

        return colors;
    }


    public String colorForLoadCell(int temp) {
        if (temp == 0)
            colors = "#FF5252";
        else if (temp == 1)
            colors = "500702";


        return colors;
    }

    public String colorForSignal(int temp) {
        if (temp == 0)
            colors = "#D32F2F";


        return colors;
    }

    public String colorForAccMagnoGyno(int temp) {
        if (temp == 0)
            colors = "#1976D2";
        else if (temp == 1)
            colors = "#388E3C";
        else if (temp == 2)
            colors = "#FF5722";
        return colors;


    }
}
