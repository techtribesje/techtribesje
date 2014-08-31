package je.techtribes.web.controller.calendar;

public class MonthLinkViewModel {

    private String title;
    private String href;

    public MonthLinkViewModel(String title, String href) {
        this.title = title;
        this.href = href;
    }

    public String getTitle() {
        return title;
    }

    public String getHref() {
        return href;
    }

}
