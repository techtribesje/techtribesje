package je.techtribes.web.tagext;

import je.techtribes.domain.Video;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

public class EmbedVideoTag extends SimpleTagSupport {

    private String url;
    private int width = 560;
    private int height = 315;

    @Override
    public void doTag() throws JspException, IOException {
        Video video = new Video(url);
        if (video.isYouTube()) {
            getJspContext().getOut().write("<iframe width=\"" + width + "\" height=\"" + height + "\" src=\"http://www.youtube.com/embed/" + video.getYouTubeId() + "\" frameborder=\"0\" allowfullscreen></iframe>");
        } else if (video.isUStream()) {
            getJspContext().getOut().write("<iframe width=\"" + width + "\" height=\"" + height + "\" src=\"http://www.ustream.tv/embed/recorded/" + video.getUStreamId() + "?v=3&amp;wmode=direct\" scrolling=\"no\" frameborder=\"0\"></iframe>");
        }
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
