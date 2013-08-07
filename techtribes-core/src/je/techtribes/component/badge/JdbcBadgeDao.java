package je.techtribes.component.badge;

import je.techtribes.domain.ContentSource;
import je.techtribes.domain.badge.AwardedBadge;
import je.techtribes.util.JdbcDatabaseConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.LinkedList;
import java.util.List;

class JdbcBadgeDao implements BadgeDao {

    private DataSource dataSource;

    JdbcBadgeDao(JdbcDatabaseConfiguration jdbcDatabaseConfiguration) {
        this.dataSource = jdbcDatabaseConfiguration.getDataSource();
    }

    @Override
    public List<AwardedBadge> getBadges() {
        JdbcTemplate select = new JdbcTemplate(dataSource);
        return select.query("select badge_id, content_source_id, date from badge order by date desc",
                new AwardedBadgeRowMapper());
    }

    @Override
    public List<AwardedBadge> getBadges(ContentSource contentSource) {
        if (contentSource != null) {
            JdbcTemplate select = new JdbcTemplate(dataSource);
            return select.query("select badge_id, content_source_id, date from badge where content_source_id = ? order by date desc",
                    new Object[] { contentSource.getId() },
                    new AwardedBadgeRowMapper());
        } else {
            return new LinkedList<>();
        }
    }

    @Override
    public void add(AwardedBadge badge) {
        if (badge != null) {
            JdbcTemplate insert = new JdbcTemplate(dataSource);
            insert.update("insert into badge (badge_id, content_source_id, date) values (?, ?, ?)",
                badge.getBadge().getId(), badge.getContentSourceId(), badge.getDate());
        }
    }

    @Override
    public List<AwardedBadge> getRecentAwardedBadges(int pageSize) {
        JdbcTemplate select = new JdbcTemplate(dataSource);
        return select.query("select badge_id, content_source_id, date from badge order by date desc limit ?",
                new Object[] { pageSize },
                new AwardedBadgeRowMapper());
    }

}
