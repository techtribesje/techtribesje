package je.techtribes.web.controller;

import je.techtribes.domain.ContentSourceStatistics;
import je.techtribes.domain.Event;
import je.techtribes.component.event.EventComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Calendar;
import java.util.List;

@Controller
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

    private void prepareModel(ModelMap model, List<Event> events) {
        model.addAttribute("events", events);
        model.addAttribute("contentSourceStatistics", new ContentSourceStatistics(events).getStatistics());
        addCommonAttributes(model);
        setPageTitle(model, "Events");
    }

}
