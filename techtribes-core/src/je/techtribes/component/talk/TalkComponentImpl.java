package je.techtribes.component.talk;

import je.techtribes.component.AbstractComponent;
import je.techtribes.component.contentsource.ContentSourceComponent;
import je.techtribes.domain.ContentSource;
import je.techtribes.util.ContentItemFilter;
import je.techtribes.util.ContentSourceCollectionFormatter;
import je.techtribes.util.DateUtils;
import je.techtribes.util.JdbcDatabaseConfiguration;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

class TalkComponentImpl extends AbstractComponent implements TalkComponent {

    private TalkDao talkDao;
    private ContentSourceComponent contentSourceComponent;

    TalkComponentImpl(JdbcDatabaseConfiguration jdbcDatabaseConfiguration, ContentSourceComponent contentSourceComponent) {
        this.talkDao = new JdbcTalkDao(jdbcDatabaseConfiguration);
        this.contentSourceComponent = contentSourceComponent;
    }

    @Override
    public List<Talk> getRecentTalks() {
        try {
            List<Talk> talks = talkDao.getTalks(DateUtils.getEndOfToday());
            filterAndEnrich(talks);

            return talks;
        } catch (Exception e) {
            TalkException te = new TalkException("Error getting recent talks", e);
            logError(te);
            throw te;
        }
    }

    @Override
    public List<Talk> getTalksByYear(int year) {
        try {
            List<Talk> talks = talkDao.getTalksByYear(year);
            filterAndEnrich(talks);

            return talks;
        } catch (Exception e) {
            TalkException te = new TalkException("Error getting talks for year " + year, e);
            logError(te);
            throw te;
        }
    }

    @Override
    public List<Talk> getTalks(ContentSource contentSource) {
        try {
            List<Talk> talks = talkDao.getTalks(contentSource);
            filterAndEnrich(talks);

            return talks;
        } catch (Exception e) {
            TalkException te = new TalkException("Error getting talk for content source with ID " + contentSource.getId(), e);
            logError(te);
            throw te;
        }
    }

    @Override
    public List<Talk> getTalks(Collection<ContentSource> contentSources) {
        try {
            if (contentSources != null && contentSources.isEmpty()) {
                return new LinkedList<>();
            }

            List<Talk> talks = talkDao.getTalks(contentSources);
            filterAndEnrich(talks);

            return talks;
        } catch (Exception e) {
            TalkException te = new TalkException("Error getting talks for content sources with IDs " + ContentSourceCollectionFormatter.format(contentSources), e);
            logError(te);
            throw te;
        }
    }

    @Override
    public Talk getTalk(int id) {
        try {
            List<Talk> talks = new LinkedList<>();
            Talk talk = talkDao.getTalk(id);
            talks.add(talk);
            filterAndEnrich(talks);

            return talk;
        } catch (Exception e) {
            TalkException te = new TalkException("Error getting talk with ID " + id, e);
            logError(te);
            throw te;
        }
    }

    @Override
    public long getNumberOfLocalTalks(int id, Date start, Date end) {
        try {
            return talkDao.getNumberOfLocalTalks(id, start, end);
        } catch (Exception e) {
            TalkException te = new TalkException("Error getting number of local talks for person with " + id + " between " + start + " and " + end, e);
            logError(te);
            throw te;
        }
    }

    @Override
    public long getNumberOfInternationalTalks(int id, Date start, Date end) {
        try {
            return talkDao.getNumberOfInternationalTalks(id, start, end);
        } catch (Exception e) {
            TalkException te = new TalkException("Error getting number of international talks for person with " + id + " between " + start + " and " + end, e);
            logError(te);
            throw te;
        }
    }

    private void filterAndEnrich(List<Talk> talks) {
        ContentItemFilter<Talk> filter = new ContentItemFilter<>();
        filter.filter(talks, contentSourceComponent, true);
    }

}
