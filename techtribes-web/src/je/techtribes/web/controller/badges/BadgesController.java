package je.techtribes.web.controller.badges;

import com.structurizr.annotation.UsedBy;
import je.techtribes.component.badge.BadgeComponent;
import je.techtribes.domain.badge.AwardedBadge;
import je.techtribes.domain.badge.Badge;
import je.techtribes.domain.badge.Badges;
import je.techtribes.web.controller.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Controller
@UsedBy( person = "Anonymous User", description = "uses" )
public class BadgesController extends AbstractController {

    private BadgeComponent badgeComponent;

    @Autowired
    public BadgesController(BadgeComponent badgeComponent) {
        this.badgeComponent = badgeComponent;
    }

    @RequestMapping(value = "/badges", method = RequestMethod.GET)
	public String viewBadges(ModelMap model) {
        List<Badge> badges = Badges.getBadges();
        List<AwardedBadge> awardedBadges = badgeComponent.getAwardedBadges();

        Map<Badge, List<AwardedBadge>> map = new HashMap<>();
        for (AwardedBadge awardedBadge : awardedBadges) {
            List<AwardedBadge> list = new LinkedList<>();
            if (map.containsKey(awardedBadge.getBadge())) {
                list = map.get(awardedBadge.getBadge());
            }
            list.add(awardedBadge);
            map.put(awardedBadge.getBadge(), list);
        }

        model.addAttribute("badges", badges);
        model.addAttribute("awardedBadges", map);
        addCommonAttributes(model);
        setPageTitle(model, "Badges");

        return "badges";
	}

}
