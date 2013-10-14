package je.techtribes.component.talk;

import je.techtribes.component.log.LoggingComponentFactory;
import je.techtribes.domain.Talk;
import je.techtribes.domain.TalkType;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;

class TalkResultExtractor implements ResultSetExtractor<Talk> {

    @Override
    public Talk extractData(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        Talk talk = new Talk(id);
        talk.setTitle(rs.getString("name"));
        talk.setDescription(rs.getString("description"));
        talk.setType(TalkType.lookupByChar(rs.getString("type").charAt(0)));
        talk.setEventName(rs.getString("event_name"));
        talk.setCity(rs.getString("city"));
        talk.setCountry(rs.getString("country"));
        talk.setContentSourceId(rs.getInt("content_source_id"));
        talk.setDate(rs.getDate("talk_date"));

        try {
            talk.setUrl(new URL(rs.getString("url")));
        } catch (MalformedURLException e) {
            LoggingComponentFactory.create().warn(this, "Couldn't set URL for " + talk.getTitle(), e);
        }

        if (rs.getString("slides_url") != null) {
            try {
                talk.setSlidesUrl(new URL(rs.getString("slides_url")));
            } catch (MalformedURLException e) {
                LoggingComponentFactory.create().warn(this, "Couldn't set slides URL for " + talk.getTitle(), e);
            }
        }

        if (rs.getString("video_url") != null) {
            try {
                talk.setVideoUrl(new URL(rs.getString("video_url")));
            } catch (MalformedURLException e) {
                LoggingComponentFactory.create().warn(this, "Couldn't set video URL for " + talk.getTitle(), e);
            }
        }

        return talk;
    }

}
