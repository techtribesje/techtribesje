package je.techtribes.web.tagext;

import je.techtribes.domain.badge.Badge;
import je.techtribes.domain.badge.Badges;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

public class BadgeTag extends SimpleTagSupport {

    private int id;
    private int size = 96;

    private String cssClasses;

    @Override
    public void doTag() throws JspException, IOException {
        Badge badge = Badges.find(id);
        if (badge != null) {
            StringBuilder buf = new StringBuilder();

            buf.append("<img src=\"/static/img/badges/").append(id).append(".png\" alt=\"").append(badge.getName()).append("\" title=\"").append(badge.getName()).append("\" border=\"0\" width=\"").append(size).append("px\"");
            if (cssClasses != null && !cssClasses.isEmpty()) {
                buf.append(" class=\"").append(cssClasses).append("\"");
            }
            buf.append("/>");

            getJspContext().getOut().write(buf.toString());
        }
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setCssClasses(String cssClasses) {
        this.cssClasses = cssClasses;
    }

}
