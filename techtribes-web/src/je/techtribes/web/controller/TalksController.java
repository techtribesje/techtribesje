package je.techtribes.web.controller;

import je.techtribes.component.talk.TalkComponent;
import je.techtribes.domain.ContentSourceStatistics;
import je.techtribes.domain.Talk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Controller
public class TalksController extends AbstractController {

    private TalkComponent talkComponent;

    @Autowired
    public TalksController(TalkComponent talkComponent) {
        this.talkComponent = talkComponent;
    }

    @RequestMapping(value = "/talks", method = RequestMethod.GET)
	public String viewTalks(ModelMap model) {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        return viewTalksByYear(currentYear, model);
	}

    @RequestMapping(value = "/talks/year/{year:[\\d]{4}}", method = RequestMethod.GET)
	public String viewTalksByYear(@PathVariable("year")int year, ModelMap model) {
        List<Talk> talks = talkComponent.getTalksByYear(year);

        prepareModel(talks, model);
        model.addAttribute("activeNav", "" + year);

        return "talks";
	}

    @RequestMapping(value = "/talks/{id:[\\d]+}", method = RequestMethod.GET)
	public String viewTalks(@PathVariable("id")int id, ModelMap model) {
        Talk talk = talkComponent.getTalk(id);

        model.addAttribute("talk", talk);
        addCommonAttributes(model);
        setPageTitle(model, "Talks", talk.getTitle());

        return "talk";
	}

    private void prepareModel(List<Talk> talks, ModelMap model) {
        model.addAttribute("talks", talks);
        model.addAttribute("countries", getCountries(talks));
        model.addAttribute("contentSourceStatistics", new ContentSourceStatistics(talks).getStatistics());
        setPageTitle(model, "Talks");
        addCommonAttributes(model);
    }

    private Set<String> getCountries(List<Talk> talks) {
        Set<String> countries = new TreeSet<>();

        for (Talk talk : talks) {
            countries.add(talk.getCountry());
        }
        return countries;
    }

}
