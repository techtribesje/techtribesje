package je.techtribes.web.controller;

import je.techtribes.domain.ContentSource;
import je.techtribes.domain.Person;
import je.techtribes.domain.Tribe;
import je.techtribes.util.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Controller
public class UserProfileController extends AbstractController {

    @RequestMapping(value="/user/profile", method = RequestMethod.GET)
    public String viewProfile(ModelMap model) {
        ContentSource contentSource = lookupContentSourceBySignedInTwitterId();

        if (contentSource == null) {
            return "redirect:/";
        }

        if (contentSource.isTribe()) {
            Tribe tribe = (Tribe)contentSource;
            List<ContentSource> members = new LinkedList<>();
            List<ContentSource> nonmembers = new LinkedList<>();

            for (ContentSource person : contentSourceComponent.getPeople()) {
                if (tribe.getMembers().contains(person)) {
                    members.add(person);
                } else {
                    nonmembers.add(person);
                }
            }

            model.addAttribute("tribe", tribe);
            model.addAttribute("members", members);
            model.addAttribute("nonmembers", nonmembers);
            addCommonAttributes(model);
            setPageTitle(model, "Profile");

            return "profile-tribe";
        } else {
            Person person = (Person)contentSource;

            List<ContentSource> memberOfSocial = new LinkedList<>();
            List<ContentSource> memberOfTech = new LinkedList<>();
            List<ContentSource> memberOfBusiness = new LinkedList<>();
            List<ContentSource> notMemberOfSocial = new LinkedList<>();
            List<ContentSource> notMemberOfTech = new LinkedList<>();
            List<ContentSource> notMemberOfBusiness = new LinkedList<>();

            for (Tribe tribe : contentSourceComponent.getTribes()) {
                if (tribe.getMembers().contains(person)) {
                    switch (tribe.getType()) {
                        case Community:
                            memberOfSocial.add(tribe);
                            break;
                        case Tech:
                            memberOfTech.add(tribe);
                            break;
                        case Business:
                            memberOfBusiness.add(tribe);
                            break;
                    }
                } else {
                    switch (tribe.getType()) {
                        case Community:
                            notMemberOfSocial.add(tribe);
                            break;
                        case Tech:
                            notMemberOfTech.add(tribe);
                            break;
                        case Business:
                            notMemberOfBusiness.add(tribe);
                            break;
                    }
                }
            }

            model.addAttribute("person", person);
            model.addAttribute("memberOfSocial", memberOfSocial);
            model.addAttribute("memberOfTech", memberOfTech);
            model.addAttribute("memberOfBusiness", memberOfBusiness);
            model.addAttribute("notMemberOfSocial", notMemberOfSocial);
            model.addAttribute("notMemberOfTech", notMemberOfTech);
            model.addAttribute("notMemberOfBusiness", notMemberOfBusiness);
            addCommonAttributes(model);
            setPageTitle(model, "Profile");

            return "profile-person";
        }
    }

    @RequestMapping(value="/user/profile", method = RequestMethod.POST)
    public String updateProfile(ModelMap model, HttpServletRequest request) {
        ContentSource contentSource = lookupContentSourceBySignedInTwitterId();

        if (contentSource == null) {
            return "redirect:/";
        }

        contentSource.setGitHubId(StringUtils.filterHtml(request.getParameter("gitHubId")));
        contentSourceComponent.update(contentSource);

        if (contentSource.isTribe()) {
            Tribe tribe = (Tribe)contentSource;
            List<Integer> personIds = new LinkedList<>();
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

            Set<Integer> validatedPersonIds = new HashSet<>();
            for (Integer personId : personIds) {
                ContentSource cs = contentSourceComponent.findById(personId);
                if (cs.isPerson()) {
                    validatedPersonIds.add(personId);
                }
            }

            contentSourceComponent.updateTribeMembers(tribe, validatedPersonIds);
        }

        return "redirect:/user/profile";
    }

}
