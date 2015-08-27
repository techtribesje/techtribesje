package je.techtribes.web.controller.creations;

import com.structurizr.annotation.UsedBy;
import je.techtribes.component.creation.CreationComponent;
import je.techtribes.domain.Creation;
import je.techtribes.web.controller.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Shows creations (websites, apps, etc) by local people/businesses.
 */
@Controller
@UsedBy( person = "Anonymous User", description = "uses" )
public class CreationsController extends AbstractController {

    private CreationComponent creationComponent;

    @Autowired
    public CreationsController(CreationComponent creationComponent) {
        this.creationComponent = creationComponent;
    }

    @RequestMapping(value = "/creations", method = RequestMethod.GET)
	public String viewBooks(ModelMap model) {
        List<Creation> creations = creationComponent.getCreations();

        model.addAttribute("creations", creations);
        addCommonAttributes(model);
        setPageTitle(model, "Creations");

        return "creations";
	}

}
