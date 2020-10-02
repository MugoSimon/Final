package lastie_wangechian_Final.com.Intro;

public class ScreenItem {

    String Title, Description;
    int ScreenImg;

    //generated constructors
    public ScreenItem(String title, String description, int screenImg) {
        Title = title;
        Description = description;
        ScreenImg = screenImg;
    }

    //generated getter Title
    public String getTitle() {
        return Title;
    }

    //generated setter Tittle
    public void setTitle(String title) {
        Title = title;
    }

    //generated getter Description
    public String getDescription() {
        return Description;
    }

    //generated setter Description
    public void setDescription(String description) {
        Description = description;
    }

    //generated getter Screenimg
    public int getScreenImg() {
        return ScreenImg;
    }

    //generated settter ScreenImg
    public void setScreenImg(int screenImg) {
        ScreenImg = screenImg;
    }
}