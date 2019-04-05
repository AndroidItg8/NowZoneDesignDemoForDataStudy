package itg8.com.nowzonedesigndemo.home;

/**
 * Created by Android itg 8 on 9/18/2017.
 */

public class HeaderList {
     private  int icon;
     private String title;

    public HeaderList(int icon, String title) {
        this.icon = icon;
        this.title = title;
    }

    public HeaderList() {
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
