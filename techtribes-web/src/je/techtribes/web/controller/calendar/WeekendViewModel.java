package je.techtribes.web.controller.calendar;

import je.techtribes.domain.Event;
import je.techtribes.domain.NewsFeedEntry;
import je.techtribes.domain.Talk;

public class WeekendViewModel extends DayViewModel {

    private DayViewModel dayViewModelForSaturday;
    private DayViewModel dayViewModelForSunday;

    public WeekendViewModel(DayViewModel dayViewModelForSaturday, DayViewModel dayViewModelForSunday) {
        super(dayViewModelForSaturday.getYear(), dayViewModelForSaturday.getMonth(), dayViewModelForSaturday.getDay());
        this.dayViewModelForSaturday = dayViewModelForSaturday;
        this.dayViewModelForSunday = dayViewModelForSunday;

        for (Talk talk : dayViewModelForSaturday.getTalks()) {
            add(talk);
        }
        for (Talk talk : dayViewModelForSunday.getTalks()) {
            add(talk);
        }

        for (Event event : dayViewModelForSaturday.getEvents()) {
            add(event);
        }
        for (Event event : dayViewModelForSunday.getEvents()) {
            add(event);
        }

        for (NewsFeedEntry newsFeedEntry : dayViewModelForSaturday.getNewsFeedEntries()) {
            add(newsFeedEntry);
        }
        for (NewsFeedEntry newsFeedEntry : dayViewModelForSunday.getNewsFeedEntries()) {
            add(newsFeedEntry);
        }
    }

    @Override
    public boolean isBlank() {
        return dayViewModelForSaturday.isBlank() && dayViewModelForSunday.isBlank();
    }

    @Override
    public boolean isPadding() {
        return dayViewModelForSaturday.isPadding() && dayViewModelForSunday.isPadding();
    }

    @Override
    public String getTitle() {
        return dayViewModelForSaturday.getTitle() + "/" + dayViewModelForSunday.getTitle();
    }

}
