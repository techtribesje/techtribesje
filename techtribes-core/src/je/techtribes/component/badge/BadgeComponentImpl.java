package je.techtribes.component.badge;

import com.structurizr.annotation.UsesContainer;
import je.techtribes.util.AbstractComponent;
import je.techtribes.component.contentsource.ContentSourceComponent;
import je.techtribes.domain.ContentSource;
import je.techtribes.domain.badge.AwardedBadge;
import je.techtribes.util.JdbcDatabaseConfiguration;
import je.techtribes.util.PageSize;

import java.util.List;

@UsesContainer(name="Relational Database", description = "Reads from and writes to")
class BadgeComponentImpl extends AbstractComponent implements BadgeComponent {

    private JdbcBadgeDao badgeDao;
    private ContentSourceComponent contentSourceComponent;

    BadgeComponentImpl(JdbcDatabaseConfiguration jdbcDatabaseConfiguration, ContentSourceComponent contentSourceComponent) {
        this.badgeDao = new JdbcBadgeDao(jdbcDatabaseConfiguration);
        this.contentSourceComponent = contentSourceComponent;
    }

    @Override
    public List<AwardedBadge> getAwardedBadges() {
        try {
            List<AwardedBadge> awardedBadges = badgeDao.getBadges();
            enrich(awardedBadges);

            return awardedBadges;
        } catch (Exception e) {
            BadgeException be = new BadgeException("Error while loading badges", e);
            logError(be);
            throw be;
        }
    }

    @Override
    public List<AwardedBadge> getAwardedBadges(ContentSource contentSource) {
        try {
            List<AwardedBadge> awardedBadges = badgeDao.getBadges(contentSource);
            enrich(awardedBadges);

            return awardedBadges;
        } catch (Exception e) {
            BadgeException be = new BadgeException("Error while loading badges for content source with ID " + contentSource.getId(), e);
            logError(be);
            throw be;
        }
    }

    @Override
    public void add(AwardedBadge badge) {
        try {
            badgeDao.add(badge);
        } catch (Exception e) {
            BadgeException be = new BadgeException("Error while loading badges", e);
            logError(be);
            throw be;
        }
    }

    @Override
    public List<AwardedBadge> getRecentAwardedBadges(int pageSize) {
        try {
            PageSize.validatePageSize(pageSize);
            List<AwardedBadge> awardedBadges = badgeDao.getRecentAwardedBadges(pageSize);
            enrich(awardedBadges);

            return awardedBadges;
        } catch (Exception e) {
            BadgeException be = new BadgeException("Error getting recent badge winners", e);
            logError(be);
            throw be;
        }
    }

    private void enrich(List<AwardedBadge> awardedBadges) {
        for (AwardedBadge awardedBadge : awardedBadges) {
            ContentSource contentSource = contentSourceComponent.findById(awardedBadge.getContentSourceId());
            awardedBadge.setContentSource(contentSource);
        }
    }

}
