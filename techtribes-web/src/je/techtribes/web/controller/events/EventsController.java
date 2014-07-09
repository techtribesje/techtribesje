package je.techtribes.web.controller.events;

import com.structurizr.annotation.UsedBy;
import je.techtribes.component.event.EventException;
import je.techtribes.domain.ContentSourceStatistics;
import je.techtribes.domain.Event;
import je.techtribes.component.event.EventComponent;
import je.techtribes.util.ICalendarFormatter;
import je.techtribes.web.controller.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.InternalResourceView;

import java.util.Calendar;
import java.util.List;

@Controller
@UsedBy( person = "Anonymous User", description = "uses" )
public class EventsController extends AbstractController {

    private EventComponent eventService;

    @Autowired
    public EventsController(EventComponent eventService) {
        this.eventService = eventService;
    }

    @RequestMapping(value = "/events", method = RequestMethod.GET)
	public String viewEvents(ModelMap model) {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        return viewEventsByYear(currentYear, model);
	}

    @RequestMapping(value = "/events/year/{year:[\\d]{4}}", method = RequestMethod.GET)
	public String viewEventsByYear(@PathVariable("year")int year, ModelMap model) {
        List<Event> events = eventService.getEventsByYear(year);

        prepareModel(model, events);
        model.addAttribute("activeNav", "" + year);

        return "events";
	}

    @RequestMapping(value = "/events/{id:[\\d]+}", method = RequestMethod.GET)
	public String viewEvent(@PathVariable("id")int id, ModelMap model) {
        Event event = null;
        try {
            event = eventService.getEvent(id);
        } catch (EventException ee) {
            loggingComponent.error(this, "Error finding event with ID " + id, ee);
        }

        if (event != null) {
            model.addAttribute("event", event);
            addCommonAttributes(model);
            setPageTitle(model, "Event", event.getTitle());

            return "event";
        } else {
            return "404";
        }
	}

    @RequestMapping(value = "/events/{id:[\\d]+}/icalendar", method = RequestMethod.GET)
	public View downloadICalendarFileForEventWithId(@PathVariable("id")int id, ModelMap model) {
        Event event = null;
        try {
            event = eventService.getEvent(id);
        } catch (EventException ee) {
            loggingComponent.error(this, "Error serving up iCalendar for event with ID " + id, ee);
        }

        if (event != null) {
            ICalendarFormatter formatter = new ICalendarFormatter();
            model.addAttribute("iCalendar", formatter.format(event));

            return new InternalResourceView("/WEB-INF/views-other/icalendar.jsp");
        } else {
            return new InternalResourceView("/WEB-INF/views/404.jsp");
        }
	}

    private void prepareModel(ModelMap model, List<Event> events) {
        model.addAttribute("events", events);
        model.addAttribute("contentSourceStatistics", new ContentSourceStatistics(events).getStatistics());
        addCommonAttributes(model);
        setPageTitle(model, "Events");
    }

}
