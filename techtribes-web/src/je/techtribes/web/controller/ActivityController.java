package je.techtribes.web.controller;

import je.techtribes.component.activity.ActivityComponent;
import je.techtribes.domain.Activity;
import je.techtribes.component.badge.BadgeComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class ActivityController extends AbstractController {

    private ActivityComponent activityComponent;
    private BadgeComponent badgeComponent;

    @Autowired
    public ActivityController(ActivityComponent activityComponent, BadgeComponent badgeComponent) {
        this.activityComponent = activityComponent;
        this.badgeComponent = badgeComponent;
    }

    @RequestMapping(value = "/activity", method = RequestMethod.GET)
	public String viewRecentNews(ModelMap model) {
        model.addAttribute("activityListForPeople", activityComponent.getActivityListForPeople());
        model.addAttribute("topScoreForPeople", activityComponent.getActivityListForPeople().get(0).getScore());

        model.addAttribute("activityListForBusinessTribes", activityComponent.getActivityListForBusinessTribes());
        model.addAttribute("topScoreForBusinessTribes", calculateTopRawScore(activityComponent.getActivityListForBusinessTribes()));

        model.addAttribute("activityListForCommunityTribes", activityComponent.getActivityListForCommunityTribes());
        model.addAttribute("topScoreForCommunityTribes", calculateTopRawScore(activityComponent.getActivityListForCommunityTribes()));

        model.addAttribute("recentAwardedBadges", badgeComponent.getRecentAwardedBadges(10));

        addCommonAttributes(model);
        setPageTitle(model, "Activity");

		return "activity";
	}

    private long calculateTopRawScore(List<Activity> activityList) {
        long topScore = 0;
        for (Activity activity : activityList) {
            long rawScore = activity.getScore();
            if (rawScore > topScore) {
                topScore = rawScore;
            }
        }

        return topScore;
    }

}
