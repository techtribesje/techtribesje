package je.techtribes.web.controller.talks;

import com.structurizr.annotation.UsedBy;
import je.techtribes.component.talk.TalkComponent;
import je.techtribes.domain.ContentSourceStatistics;
import je.techtribes.domain.Talk;
import je.techtribes.util.DateUtils;
import je.techtribes.web.controller.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.*;

@Controller
@UsedBy( person = "Anonymous User", description = "uses" )
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

    @RequestMapping(value = "/talks/videos", method = RequestMethod.GET)
	public String viewTalksWithVideo(ModelMap model) {
        List<Talk> talks = talkComponent.getTalksWithVideo();

        prepareModel(talks, model, "videos");

        return "talks";
	}

    @RequestMapping(value = "/talks/year/{year:[\\d]{4}}", method = RequestMethod.GET)
	public String viewTalksByYear(@PathVariable("year")int year, ModelMap model) {
        List<Talk> talks = talkComponent.getTalksByYear(year);

        prepareModel(talks, model, "" + year);

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

    private void prepareModel(List<Talk> talks, ModelMap model, String activeNav) {
        model.addAttribute("talks", talks);
        model.addAttribute("countries", getCountries(talks));
        model.addAttribute("contentSourceStatistics", new ContentSourceStatistics(talks).getStatistics());
        model.addAttribute("activeNav", activeNav);

        List<String> years = new LinkedList<>();
        int currentYear = DateUtils.getYear();
        for (int year = currentYear; year >= 2012; year--) {
            years.add("" + year);
        }
        model.addAttribute("years", years);

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
