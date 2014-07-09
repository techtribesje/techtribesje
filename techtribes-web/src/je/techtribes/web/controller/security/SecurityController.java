package je.techtribes.web.controller.security;

import je.techtribes.web.controller.AbstractController;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class SecurityController extends AbstractController {

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

    @RequestMapping(value = "/signout", method = RequestMethod.GET)
   	public String showPage(ModelMap model, HttpServletRequest request) {
           SecurityContextHolder.clearContext();
           HttpSession session = request.getSession(false);
           if (session != null) {
               session.invalidate();
           }

   		return "redirect:/";
   	}

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
   	public String showHomePage(ModelMap model) {
           addCommonAttributes(model);
           setPageTitle(model, "Sign up");

   		return "signup";
   	}

}
