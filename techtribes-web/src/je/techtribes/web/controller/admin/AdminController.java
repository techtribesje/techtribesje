package je.techtribes.web.controller.admin;

import je.techtribes.domain.*;
import je.techtribes.web.controller.AbstractController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Controller
public class AdminController extends AbstractController {

    @RequestMapping(value="/admin/status", method = RequestMethod.GET)
	public String viewStatus(ModelMap model) {
        model.addAttribute("contentSources", contentSourceComponent.getPeopleAndTribes());
        addCommonAttributes(model);

        return "admin-status";
	}

    @RequestMapping(value="/admin/tribes/{name:^[a-z-0-9]*$}", method = RequestMethod.GET)
    public String viewTribe(@PathVariable("name")String shortName, ModelMap model) {
        ContentSource contentSource = contentSourceComponent.findByShortName(shortName);
        if (contentSource.isPerson()) {
            loggingComponent.error(this, shortName + " is not a tribe");
            return "404";
        }
        Tribe tribe = (Tribe)contentSource;

        List<ContentSource> members = new LinkedList<>();
        List<ContentSource> nonmembers = new LinkedList<>();

        for (ContentSource person : contentSourceComponent.getContentSources(ContentSourceType.Person)) {
            if (tribe.hasMember((Person) person)) {
                members.add(person);
            } else {
                nonmembers.add(person);
            }
        }

        model.addAttribute("tribe", tribe);
        model.addAttribute("members", members);
        model.addAttribute("nonmembers", nonmembers);
        addCommonAttributes(model);

        return "admin-tribe";
    }

    @RequestMapping(value="/admin/tribes/{name:^[a-z-0-9]*$}", method = RequestMethod.POST)
    public String updateTribe(@PathVariable("name")String shortName, ModelMap model, HttpServletRequest request) {
        ContentSource contentSource = contentSourceComponent.findByShortName(shortName);
        if (contentSource.isPerson()) {
            loggingComponent.error(this, shortName + " is not a tribe");
            return "404";
        }
        Tribe tribe = (Tribe)contentSource;

        Set<Integer> personIds = new HashSet<>();
        String[] parameters = request.getParameterValues("person");
        if (parameters != null) {
            for (String param : parameters) {
                try {
                    personIds.add(Integer.parseInt(param));
                } catch (NumberFormatException e) {
                    // do nothing
                }
            }
        }

        tribe.setProfile(request.getParameter("profile"));
        contentSourceComponent.update(tribe);

        contentSourceComponent.updateTribeMembers(tribe, personIds);

        return viewTribe(shortName, model);
    }

    @RequestMapping(value="/admin/add", method = RequestMethod.GET)
    public String addContentSource(ModelMap model) {
        ContentSource contentSource = new Person();

        model.addAttribute("contentSource", contentSource);
        addCommonAttributes(model);

        return "admin-add";
    }

    @RequestMapping(value="/admin/add", method = RequestMethod.POST)
    public String updateTribe(ModelMap model, HttpServletRequest request) {
        char typeAsCharacter = request.getParameter("type").charAt(0);
        ContentSourceType type = ContentSourceType.lookupByChar(typeAsCharacter);
        ContentSource contentSource = ContentSourceFactory.create(type);

        contentSource.setName(request.getParameter("name"));
        contentSource.setProfile(request.getParameter("profile"));
        contentSource.setTwitterId(request.getParameter("twitterId"));

        char island = request.getParameter("island").charAt(0);
        switch (island) {
            case 'j':
                contentSource.setIsland(Island.Jersey);
                break;
            case 'g':
                contentSource.setIsland(Island.Guernsey);
                break;
            default:
                contentSource.setIsland(Island.None);
                break;
        }

        String url = request.getParameter("url");
        try {
            if (url != null && !url.isEmpty()) {
                contentSource.setUrl(new URL(url));
            }
        } catch (MalformedURLException e) {
            loggingComponent.warn(this, "Could not parse URL of " + url, e);
        }

        String profileImageUrl = request.getParameter("profileImageUrl");
        try {
            if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                contentSource.setProfileImageUrl(new URL(profileImageUrl));
            }
        } catch (MalformedURLException e) {
            loggingComponent.warn(this, "Could not parse URL of " + profileImageUrl, e);
        }

        String feedUrl1 = request.getParameter("feedUrl1");
        if (feedUrl1 != null && !feedUrl1.isEmpty()) {
            contentSource.addNewsFeed(feedUrl1);
        }

        String feedUrl2 = request.getParameter("feedUrl2");
        if (feedUrl2 != null && !feedUrl2.isEmpty()) {
            contentSource.addNewsFeed(feedUrl2);
        }

        String feedUrl3 = request.getParameter("feedUrl3");
        if (feedUrl3 != null && !feedUrl3.isEmpty()) {
            contentSource.addNewsFeed(feedUrl3);
        }

        String contentAggregated = request.getParameter("contentAggregated");
        contentSource.setContentAggregated(contentAggregated != null && contentAggregated.equalsIgnoreCase("true"));

        contentSourceComponent.add(contentSource);

        if (contentSource.isTribe()) {
            return "redirect:/tribes/" + contentSource.getShortName();
        } else {
            return "redirect:/people/" + contentSource.getShortName();
        }
    }

}
