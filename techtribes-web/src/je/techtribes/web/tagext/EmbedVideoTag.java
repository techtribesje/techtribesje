package je.techtribes.web.tagext;

import je.techtribes.domain.Video;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

public class EmbedVideoTag extends SimpleTagSupport {

    private String url;

    @Override
    public void doTag() throws JspException, IOException {
        Video video = new Video(url);
        if (video.isYouTube()) {
            getJspContext().getOut().write("<iframe width=\"560\" height=\"315\" src=\"http://www.youtube.com/embed/" + video.getYouTubeId() + "\" frameborder=\"0\" allowfullscreen></iframe>");
        }
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
