package je.techtribes.component.contentsource;

import je.techtribes.component.log.LoggingComponentFactory;
import je.techtribes.domain.ContentSource;
import je.techtribes.domain.Person;
import je.techtribes.domain.Tribe;
import je.techtribes.util.JdbcDatabaseConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

class JdbcContentSourceDao implements ContentSourceDao {

    private DataSource dataSource;

    JdbcContentSourceDao(JdbcDatabaseConfiguration jdbcDatabaseConfiguration) {
        this.dataSource = jdbcDatabaseConfiguration.getDataSource();
    }

    @Override
    public List<ContentSource> loadContentSources() {
        JdbcTemplate select = new JdbcTemplate(dataSource);
        List<ContentSource> contentSources = select.query("select id, name, twitter_id, github_id, island, type, feed_url1, feed_url2, feed_url3, profile_text, profile_image_url, url, content_aggregated, twitter_followers, search_terms from content_source order by name asc",
                new ContentSourceRowMapper());

        // and we also need to load the tribe->people mappings
        List<TribeAndPersonLink> links = select.query("select tribe_id, person_id from tribe_member",
                new TribeMemberRowMapper());

        Map<Integer, ContentSource> map = new HashMap<>();
        for (ContentSource contentSource : contentSources) {
            map.put(contentSource.getId(), contentSource);
        }

        for (TribeAndPersonLink link : links) {
            ContentSource contentSourceTribe = map.get(link.getTribeId());
            ContentSource contentSourcePerson = map.get(link.getPersonId());
            if (contentSourceTribe != null && contentSourceTribe.isTribe() &&
                    contentSourcePerson != null && contentSourcePerson.isPerson()) {
                Tribe tribe = (Tribe)contentSourceTribe;
                Person person = (Person)contentSourcePerson;
                tribe.add(person);
            } else {
                LoggingComponentFactory.create().warn(this, "Could not add member (tribe=" + link.getTribeId() + ", person=" + link.getPersonId() + ")");
            }
        }

        return contentSources;
    }

    @Override
    public void add(ContentSource contentSource) {
        JdbcTemplate insert = new JdbcTemplate(dataSource);
        insert.update("insert into content_source (name, twitter_id, github_id, island, type, profile_text, profile_image_url, url, feed_url1, feed_url2, feed_url3, content_aggregated, search_terms) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
            contentSource.getName(),
            contentSource.getTwitterId(),
            contentSource.getGitHubId(),
            contentSource.getIsland().toChar(),
            contentSource.getType().toChar(),
            contentSource.getProfile(),
            contentSource.getProfileImageUrl() != null ? contentSource.getProfileImageUrl().toString() : null,
            contentSource.getUrl() != null ? contentSource.getUrl().toString() : null,
            contentSource.getNewsFeeds().size() > 0 ? new LinkedList<>(contentSource.getNewsFeeds()).get(0).getUrl() : null,
            contentSource.getNewsFeeds().size() > 1 ? new LinkedList<>(contentSource.getNewsFeeds()).get(1).getUrl() : null,
            contentSource.getNewsFeeds().size() > 2 ? new LinkedList<>(contentSource.getNewsFeeds()).get(2).getUrl() : null,
            contentSource.isContentAggregated(),
            contentSource.getSearchTerms());
    }

    @Override
    public void update(ContentSource contentSource) {
        JdbcTemplate update = new JdbcTemplate(dataSource);
        update.update("update content_source set profile_text = ?, profile_image_url = ?, url = ?, twitter_followers = ?, github_id = ? where id = ?",
            contentSource.getProfile(),
            contentSource.getProfileImageUrl() != null ? contentSource.getProfileImageUrl().toString() : null,
            contentSource.getUrl() != null ? contentSource.getUrl().toString() : null,
            contentSource.getTwitterFollowersCount(),
            contentSource.getGitHubId(),
            contentSource.getId());
    }

    @Override
    public void updateTribeMembers(ContentSource tribe, List<Integer> personIds) {
        JdbcTemplate template = new JdbcTemplate(dataSource);
        template.update("delete from tribe_member where tribe_id = ?",
                tribe.getId());

        for (Integer personId : personIds) {
            template.update("insert into tribe_member (tribe_id, person_id) values (?, ?)",
                tribe.getId(), personId);
        }
    }

    @Override
    public void updateTribeMembershipsForPerson(ContentSource person, List<Integer> tribeIds) {
        JdbcTemplate template = new JdbcTemplate(dataSource);
        template.update("delete from tribe_member where person_id = ?",
                person.getId());

        for (Integer tribeId : tribeIds) {
            template.update("insert into tribe_member (tribe_id, person_id) values (?, ?)",
                tribeId, person.getId());
        }
    }

}
