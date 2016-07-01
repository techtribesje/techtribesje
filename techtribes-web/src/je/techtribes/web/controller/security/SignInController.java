package je.techtribes.web.controller.security;

import com.structurizr.annotation.UsedByPerson;
import je.techtribes.web.controller.AbstractController;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * Allows registered users to sign in using their Twitter ID.
 */
@Controller
@UsedByPerson( name = "Anonymous User", description = "uses" )
public class SignInController extends AbstractController {

	@RequestMapping(value = "/signin", method = RequestMethod.GET)
	public String showHomePage(ModelMap model, HttpServletRequest request) {
        // todo: remove this
        Connection<?> connection = ProviderSignInUtils.getConnection(new ServletWebRequest(request));
        if (connection != null) {
            loggingComponent.error(this, "Connection is null");
        }

        addCommonAttributes(model);
        setPageTitle(model, "Sign in");

		return "signin";
	}

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
   	public String showHomePage(ModelMap model) {
           addCommonAttributes(model);
           setPageTitle(model, "Sign up");

   		return "signup";
   	}

}
