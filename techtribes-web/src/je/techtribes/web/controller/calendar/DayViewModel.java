package je.techtribes.web.controller.calendar;

import je.techtribes.domain.Event;
import je.techtribes.domain.NewsFeedEntry;
import je.techtribes.domain.Talk;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DayViewModel {

    private int year;
    private int month;
    private int day;

    private List<Talk> talks = new ArrayList<>();
    private List<Event> events = new ArrayList<>();
    private List<NewsFeedEntry> newsFeedEntries = new ArrayList<>();

    public DayViewModel(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public DayViewModel(Calendar cal) {
        this.year = cal.get(Calendar.YEAR);
        this.month = cal.get(Calendar.MONTH)-1;
        this.day= cal.get(Calendar.DAY_OF_MONTH);
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public void add(Talk talk) {
        talks.add(talk);
    }

    public List<Talk> getTalks() {
        return new ArrayList<>(talks);
    }

    public void add(Event event) {
        events.add(event);
    }

    public List<Event> getEvents() {
        return new ArrayList<>(events);
    }

    public void add(NewsFeedEntry newsFeedEntry) {
        newsFeedEntries.add(newsFeedEntry);
    }

    public List<NewsFeedEntry> getNewsFeedEntries() {
        return new ArrayList<>(newsFeedEntries);
    }

    public String getTitle() {
        return "" + day;
    }

}
