package je.techtribes.domain;

public class Video {

    private String url;

    public Video(String url) {
        this.url = url;
    }

    public boolean isYouTube() {
        return url != null && url.startsWith("http://youtube.com");
    }

    public String getYouTubeId() {
        if (isYouTube()) {
            return url.substring("http://youtube.com/watch?v=".length());
        } else {
            return null;
        }
    }

    public boolean isUStream() {
        return url != null && url.startsWith("http://www.ustream.tv");
    }

    public String getUStreamId() {
        if (isUStream()) {
            return url.substring("http://www.ustream.tv/recorded/".length());
        } else {
            return null;
        }
    }

}
