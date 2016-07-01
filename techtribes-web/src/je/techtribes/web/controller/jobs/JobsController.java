package je.techtribes.web.controller.jobs;

import com.structurizr.annotation.Component;
import com.structurizr.annotation.UsedByPerson;
import je.techtribes.domain.ContentSourceStatistics;
import je.techtribes.component.job.JobComponent;
import je.techtribes.domain.Job;
import je.techtribes.web.controller.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@Component(description = "Allows users to view local job opportunities.")
@UsedByPerson( name = "Anonymous User", description = "uses" )
public class JobsController extends AbstractController {

    private JobComponent jobService;

    @Autowired
    public JobsController(JobComponent jobService) {
        this.jobService = jobService;
    }

    @RequestMapping(value = "/jobs", method = RequestMethod.GET)
	public String viewRecentNews(ModelMap model) {
        List<Job> jobs = jobService.getRecentJobs(36);

        model.addAttribute("jobs", jobs);
        model.addAttribute("contentSourceStatistics", new ContentSourceStatistics(jobs).getStatistics());
        addCommonAttributes(model);
        setPageTitle(model, "Jobs");

        return "jobs";
	}

}
