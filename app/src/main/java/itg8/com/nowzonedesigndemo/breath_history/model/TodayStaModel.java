package itg8.com.nowzonedesigndemo.breath_history.model;


/**
 * Created by Android itg 8 on 9/22/2017.
 */

public class TodayStaModel {
     private String id;
     private String name;
     private int  drawableImage;

    public TodayStaModel(String id, String name, int drawableImage, int color) {
        this.id = id;
        this.name = name;
        this.drawableImage = drawableImage;
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    private int color;

    public TodayStaModel() {
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDrawbleImage() {
        return drawableImage;
    }

    public void setDrawbleImage(int drawbleImage) {
        this.drawableImage = drawbleImage;
    }
}
