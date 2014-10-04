package je.techtribes.web.tagext;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

public class CountryFlagTag extends SimpleTagSupport {

    private String name;

    @Override
    public void doTag() throws JspException, IOException {
        getJspContext().getOut().write("/static/img/flags/" + name.replace(" ", "-") + ".png");
    }

    public void setName(String name) {
        this.name = name;
    }

}