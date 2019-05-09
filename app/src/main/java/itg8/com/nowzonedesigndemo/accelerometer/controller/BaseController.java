package itg8.com.nowzonedesigndemo.accelerometer.controller;

public class BaseController {


    private boolean xVisible;
    private boolean yVisible;
    private boolean zVisible;

    public void setVisibilityOfGraph(boolean xVisible, boolean yVisible, boolean zVisible) {

        this.xVisible = xVisible;
        this.yVisible = yVisible;
        this.zVisible = zVisible;
    }

    public boolean isxVisible() {
        return xVisible;
    }

    public boolean isyVisible() {
        return yVisible;
    }

    public boolean iszVisible() {
        return zVisible;
    }
}
