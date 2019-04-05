package itg8.com.nowzonedesigndemo.home;

/**
 * Created by Android itg 8 on 9/16/2017.
 */

class NavDrawerItem {

    private String title;
    private int icon;
    public HeaderList headerList;

    public HeaderList getHeaderList() {
        return headerList;
    }

    public void setHeaderList(HeaderList headerList) {
        this.headerList = headerList;
    }

    public NavDrawerItem(String title, int icon) {
        this.title = title;
        this.icon = icon;
    }

    public NavDrawerItem() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
