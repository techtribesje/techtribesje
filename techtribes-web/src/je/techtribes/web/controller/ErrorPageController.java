package je.techtribes.web.controller;

import com.structurizr.annotation.UsedByPerson;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * Handles all error scenarios (e.g. page not found, internal server error, etc).
 */
@Controller
@UsedByPerson( name = "Anonymous User", description = "uses" )
public class ErrorPageController extends AbstractController {

    @RequestMapping(value= "/400", method = RequestMethod.GET)
    public String show400Page(ModelMap model) {
        addCommonAttributes(model);
        return "404";

    }

    @RequestMapping(value= "/403", method = RequestMethod.GET)
    public String show403Page(ModelMap model) {
        addCommonAttributes(model);
        setPageTitle(model, "403");

        return "403";
    }

    @RequestMapping(value= "/404", method = RequestMethod.GET)
    public String show404Page(ModelMap model, HttpServletRequest request) {
        loggingComponent.warn(this, "[404] " + request.getAttribute("javax.servlet.forward.request_uri"));
        addCommonAttributes(model);
        setPageTitle(model, "404");

        return "404";
    }

    @RequestMapping(value= "/500", method = RequestMethod.GET)
    public String showErrorPage(ModelMap model) {
        addCommonAttributes(model);
        setPageTitle(model, "500");

        return "500";
    }

}
