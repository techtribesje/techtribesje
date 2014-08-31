package je.techtribes.web.controller.calendar;

import com.structurizr.annotation.Component;
import com.structurizr.annotation.UsedBy;
import je.techtribes.component.event.EventComponent;
import je.techtribes.component.newsfeedentry.NewsFeedEntryComponent;
import je.techtribes.component.talk.TalkComponent;
import je.techtribes.domain.Event;
import je.techtribes.domain.NewsFeedEntry;
import je.techtribes.domain.Talk;
import je.techtribes.util.DateUtils;
import je.techtribes.web.controller.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Controller
@Component(description = "Allows users to view a summary of site content by month.")
@UsedBy( person = "Anonymous User", description = "uses" )
public class ViewByMonthController extends AbstractController {

    private EventComponent eventComponent;
    private TalkComponent talkComponent;
    private NewsFeedEntryComponent newsFeedEntryComponent;

    @Autowired
    public ViewByMonthController(EventComponent eventComponent, TalkComponent talkComponent, NewsFeedEntryComponent newsFeedEntryComponent) {
        this.eventComponent = eventComponent;
        this.talkComponent = talkComponent;
        this.newsFeedEntryComponent = newsFeedEntryComponent;
    }

    @RequestMapping(value = "/{year:\\d\\d\\d\\d}/{month:\\d\\d}", method = RequestMethod.GET)
	public String viewRecentNews(ModelMap model, @PathVariable("year")int year, @PathVariable("month")int month) {

        boolean invalid = false;
        if (month < 1) {
            month = 1;
            invalid = true;
        }  else if (month > 12) {
            month = 12;
            invalid = true;
        }

        if (invalid) {
            MonthLinkViewModel monthLinkViewModel = createMonthLinkViewModel(DateUtils.getCalendar(year, month, 1));
            return "redirect:" + monthLinkViewModel.getHref();
        }

        List<DayViewModel> days = new ArrayList<>();
        int lastDayOfMonth = DateUtils.getLastDayOfMonth(year, month);
        for (int i = 1; i <= lastDayOfMonth; i++) {
            days.add(new DayViewModel(year, month, i));
        }

        List<Talk> talks = talkComponent.getTalksByMonth(year, month);
        for (Talk talk : talks) {
            int day = DateUtils.getDay(talk.getDate());
            days.get(day-1).add(talk);
        }

        List<Event> events = eventComponent.getEventsByMonth(year, month);
        for (Event event : events) {
            int day = DateUtils.getDay(event.getStartDate());
            days.get(day-1).add(event);
        }

        List<NewsFeedEntry> newsFeedEntries = newsFeedEntryComponent.getNewsFeedEntries(
                DateUtils.getStartOfMonth(year, month).getTime(),
                DateUtils.getEndOfMonth(year, month).getTime());
        for (NewsFeedEntry newsFeedEntry : newsFeedEntries) {
            int day = DateUtils.getDay(newsFeedEntry.getTimestamp());
            days.get(day-1).add(newsFeedEntry);
        }

        Calendar cal = DateUtils.getCalendar(year, month, 1);
        while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            cal.add(Calendar.DATE, -1);
            days.add(0, new DayViewModel(cal));
        }

        cal = DateUtils.getCalendar(year, month, DateUtils.getLastDayOfMonth(year, month));
        while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            cal.add(Calendar.DATE, 1);
            days.add(new DayViewModel(cal));
        }

        List<DayViewModel> viewModel = new ArrayList<>();
        for (int week = 0; week < (days.size() / 7); week++) {
            for (int day = 0; day <= 4; day++) {
                DayViewModel dayViewModel = days.get((week * 7) + day);
                viewModel.add(dayViewModel);
            }

            DayViewModel dayViewModelForSaturday = days.get((week * 7) + 5);
            DayViewModel dayViewModelForSunday = days.get((week * 7) + 6);

            WeekendViewModel weekendViewModel = new WeekendViewModel(dayViewModelForSaturday, dayViewModelForSunday);
            viewModel.add(weekendViewModel);
        }

        String title = DateUtils.format("MMMM yyyy", DateUtils.getStartOfMonth(year, month));

        Calendar previousMonth = DateUtils.getCalendar(year, month, 1);
        previousMonth.add(Calendar.MONTH, -1);
        Calendar nextMonth = DateUtils.getCalendar(year, month, 1);
        nextMonth.add(Calendar.MONTH, 1);

        model.addAttribute("viewModel", viewModel);
        model.addAttribute("month", month);
        model.addAttribute("title", title);

        model.addAttribute("previousMonthLink", createMonthLinkViewModel(previousMonth));
        model.addAttribute("nextMonthLink", createMonthLinkViewModel(nextMonth));

        addCommonAttributes(model);
        setPageTitle(model, title);

        return "month";
	}

    private MonthLinkViewModel createMonthLinkViewModel(Calendar cal) {
        NumberFormat yearFormat = new DecimalFormat("0000");
        NumberFormat monthFormat = new DecimalFormat("00");

        return new MonthLinkViewModel(
                DateUtils.format("MMMM yyyy", cal),
                "/" + yearFormat.format(cal.get(Calendar.YEAR)) + "/" + monthFormat.format(cal.get(Calendar.MONTH)+1));
    }

}
