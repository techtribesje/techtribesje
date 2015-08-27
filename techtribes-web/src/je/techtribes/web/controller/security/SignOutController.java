package je.techtribes.web.controller.security;

import com.structurizr.annotation.UsedBy;
import je.techtribes.web.controller.AbstractController;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Allows the user to sign out.
 */
@Controller
@UsedBy( person = "Aggregated User", description = "uses" )
public class SignOutController extends AbstractController {

    @RequestMapping(value = "/signout", method = RequestMethod.GET)
   	public String showPage(ModelMap model, HttpServletRequest request) {
           SecurityContextHolder.clearContext();
           HttpSession session = request.getSession(false);
           if (session != null) {
               session.invalidate();
           }

   		return "redirect:/";
   	}

}
