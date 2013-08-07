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
            // this is definitely a minimum viable implementation! :-)
            return url.substring("http://youtube.com/watch?v=".length());
        } else {
            return null;
        }
    }

}
