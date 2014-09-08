package je.techtribes.component.contentsource;

import je.techtribes.component.log.LoggingComponentFactory;
import je.techtribes.domain.ContentSource;
import je.techtribes.domain.ContentSourceFactory;
import je.techtribes.domain.ContentSourceType;
import je.techtribes.domain.Island;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;

class ContentSourceResultExtractor implements ResultSetExtractor<ContentSource> {

    @Override
    public ContentSource extractData(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        char typeAsCharacter = rs.getString("type").charAt(0);
        ContentSourceType type = ContentSourceType.lookupByChar(typeAsCharacter);
        ContentSource contentSource = ContentSourceFactory.create(type, id);

        contentSource.setName(rs.getString("name"));
        contentSource.setTwitterId(rs.getString("twitter_id"));
        contentSource.setGitHubId(rs.getString("github_id"));
        contentSource.addNewsFeed(rs.getString("feed_url1"));
        contentSource.addNewsFeed(rs.getString("feed_url2"));
        contentSource.addNewsFeed(rs.getString("feed_url3"));
        contentSource.setIsland(Island.lookupByChar(rs.getString("island")));

        contentSource.setProfile(rs.getString("profile_text"));

        String profileImageUrlAsString = rs.getString("profile_image_url");
        if (profileImageUrlAsString != null && !profileImageUrlAsString.isEmpty()) {
            try {
                contentSource.setProfileImageUrl(new URL(profileImageUrlAsString.replace("http:", "https:")));
            } catch (MalformedURLException e) {
                LoggingComponentFactory.create().warn(this, "Couldn't get profile image URL for " + contentSource.getName(), e);
            }
        }

        String urlAsString = rs.getString("url");
        if (urlAsString != null && !urlAsString.isEmpty()) {
            try {
                contentSource.setUrl(new URL(urlAsString));
            } catch (MalformedURLException e) {
                LoggingComponentFactory.create().warn(this, "Couldn't get profile image URL for " + contentSource.getName(), e);
            }
        }

        contentSource.setContentAggregated(rs.getBoolean("content_aggregated"));
        contentSource.setTwitterFollowersCount(rs.getInt("twitter_followers"));
        contentSource.setSearchTerms(rs.getString("search_terms"));

        return contentSource;
    }

}
