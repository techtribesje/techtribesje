package je.techtribes.component.talk;

import je.techtribes.AbstractComponentTestsBase;
import je.techtribes.domain.ContentSource;
import je.techtribes.domain.Talk;
import je.techtribes.domain.TalkType;
import je.techtribes.util.DateUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

public class TalkComponentTests extends AbstractComponentTestsBase {

    private int currentYear;
    private SimpleDateFormat dateFormat;

    @Before
    public void setUp() {
        currentYear = Calendar.getInstance().get(Calendar.YEAR);

        JdbcTemplate template = getJdbcTemplate();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone(DateUtils.UTC_TIME_ZONE));

        for (int i = 1; i <= 12; i++) {
            template.update("insert into talk (name, description, type, event_name, city, country, content_source_id, url, talk_date, slides_url, video_url) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    "Talk " + i,
                    "Here is a description for talk " + i,
                    "w",
                    "Event name for talk " + i,
                    "City " + i,
                    "Country " + i,
                    getContentSourceComponent().findByShortName("simonbrown").getId(),
                    "http://event.com/talk" + i,
                    dateFormat.format(DateUtils.getDate(currentYear - 1, i, 1)),
                    null,
                    null);
        }
        for (int i = 1; i <= 12; i++) {
            template.update("insert into talk (name, description, type, event_name, city, country, content_source_id, url, talk_date, slides_url, video_url) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    "Talk " + (12+i),
                    "Here is a description for talk " + (12+i),
                    "p",
                    "Event name for talk " + (12+i),
                    i <= 6 ? "St Helier" : "St Peter Port",
                    i <= 6 ? "Jersey" : "Guernsey",
                    getContentSourceComponent().findByShortName("chrisclark").getId(),
                    "http://event.com/talk" + (12+i),
                    dateFormat.format(DateUtils.getDate(currentYear + 1, i, 1)),
                    null,
                    null);
        }

        // and disable content aggregation to check talks by this content source remain included in queries
        getJdbcTemplate().update("update content_source set content_aggregated = 0 where id = ?", getContentSourceComponent().findByShortName("chrisclark").getId());
        getContentSourceComponent().refreshContentSources();
    }

    @Test
    public void test_getTalk_ReturnsTheTalk_WhenAValidIdIsSpecified() {
        Talk talk = getTalkComponent().getTalk(1);
        assertEquals("Talk 1", talk.getTitle());
        assertEquals("Here is a description for talk 1", talk.getDescription());
        assertEquals(TalkType.Workshop, talk.getType());
        assertEquals("Event name for talk 1", talk.getEventName());
        assertEquals("City 1", talk.getCity());
        assertEquals("Country 1", talk.getCountry());
        assertEquals("Simon Brown", talk.getContentSource().getName());
        assertEquals("http://event.com/talk1", talk.getUrl().toString());
        assertEquals(currentYear-1 + "-01-01", dateFormat.format(talk.getDate()));
    }

    @Test
    public void test_getNumberOfLocalTalks_ReturnsZero_WhenTheContentSourceHasDoneNone() {
        assertEquals(0, getTalkComponent().getNumberOfLocalTalks(
                getContentSourceComponent().findByShortName("simonbrown").getId(),
                DateUtils.getStartOfYear(currentYear - 1),
                DateUtils.getEndOfYear(currentYear + 1)));
    }

    @Test
    public void test_getNumberOfLocalTalks_ReturnsZero_WhenTheContentSourceHasDoneNoneDuringTheSpecifiedDates() {
        assertEquals(0, getTalkComponent().getNumberOfLocalTalks(
                getContentSourceComponent().findByShortName("chrisclark").getId(),
                DateUtils.getStartOfYear(currentYear - 1),
                DateUtils.getEndOfYear(currentYear - 1)));
    }

    @Test
    public void test_getNumberOfLocalTalks_ReturnsNonZero_WhenTheContentSourceHasDoneSomeTalksDuringTheSpecifiedDates() {
        assertEquals(12, getTalkComponent().getNumberOfLocalTalks(
                getContentSourceComponent().findByShortName("chrisclark").getId(),
                DateUtils.getStartOfYear(currentYear + 1),
                DateUtils.getEndOfYear(currentYear + 1)));
    }

    @Test
    public void test_getNumberOfInternationalTalks_ReturnsZero_WhenTheContentSourceHasDoneNone() {
        assertEquals(0, getTalkComponent().getNumberOfInternationalTalks(
                getContentSourceComponent().findByShortName("chrisclark").getId(),
                DateUtils.getStartOfYear(currentYear - 1),
                DateUtils.getEndOfYear(currentYear + 1)));
    }

    @Test
    public void test_getNumberOfInternationalTalks_ReturnsZero_WhenTheContentSourceHasDoneNoneDuringTheSpecifiedDates() {
        assertEquals(0, getTalkComponent().getNumberOfInternationalTalks(
                getContentSourceComponent().findByShortName("simonbrown").getId(),
                DateUtils.getStartOfYear(currentYear + 1),
                DateUtils.getEndOfYear(currentYear + 1)));
    }

    @Test
    public void test_getNumberOfInternationalTalks_ReturnsNonZero_WhenTheContentSourceHasDoneSomeTalksDuringTheSpecifiedDates() {
        assertEquals(12, getTalkComponent().getNumberOfInternationalTalks(
                getContentSourceComponent().findByShortName("simonbrown").getId(),
                DateUtils.getStartOfYear(currentYear - 1),
                DateUtils.getEndOfYear(currentYear - 1)));
    }

    @Test
    public void test_getRecentTalks_ReturnsAllTalksFromTodayAndInThePast() {
        getJdbcTemplate().update("insert into talk (name, description, type, event_name, city, country, content_source_id, url, talk_date, slides_url, video_url) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                "Talk for today",
                "Here is a description for talk for today",
                "w",
                "Event name for talk for today",
                "City",
                "Country",
                getContentSourceComponent().findByShortName("simonbrown").getId(),
                "http://event.com/today",
                dateFormat.format(DateUtils.getToday()),
                null,
                null);

        List<Talk> talks = getTalkComponent().getRecentTalks();
        assertEquals(13, talks.size()); // 12 from last year plus the one from today

        assertEquals("Talk for today", talks.get(0).getTitle());
        for (int i = 1; i <= 12; i++) {
            assertEquals("Talk " + (13-i), talks.get(i).getTitle());
        }
    }

    @Test
    public void test_getTalksByYear_ReturnsEmptyList_WhenThereAreNoTalks() {
        List<Talk> talks = getTalkComponent().getTalksByYear(currentYear);
        assertEquals(0, talks.size());
    }

    @Test
    public void test_getTalksByYear_ReturnsSomeTalks_WhenThereAreSome() {
        List<Talk> talks = getTalkComponent().getTalksByYear(currentYear-1);
        assertEquals(12, talks.size());
        for (int i = 0; i < 11; i++) {
            assertEquals("Talk " + (12-i), talks.get(i).getTitle());
        }
    }

    @Test
    public void test_getTalksByContentSource_ReturnsEmptyList_WhenThereAreNone() {
        List<Talk> talks = getTalkComponent().getTalks(getContentSourceComponent().findByShortName("techtribesje"));
        assertEquals(0, talks.size());
    }

    @Test
    public void test_getTalksByContentSource_ReturnsSomeTalks_WhenThereAreSome() {
        List<Talk> talks = getTalkComponent().getTalks(getContentSourceComponent().findByShortName("simonbrown"));
        assertEquals(12, talks.size());
        for (int i = 0; i < 11; i++) {
            assertEquals("Talk " + (12-i), talks.get(i).getTitle());
        }
    }

    @Test
    public void test_getTalksByContentSources_ReturnsEmptyList_WhenGivenAnEmptyList() {
        List<ContentSource> contentSources = new LinkedList<>();
        List<Talk> talks = getTalkComponent().getTalks(contentSources);
        assertEquals(0, talks.size());
    }

    @Test
    public void test_getTalksByContentSources_ReturnsEmptyList_WhenThereAreNone() {
        List<ContentSource> contentSources = new LinkedList<>();
        contentSources.add(getContentSourceComponent().findByShortName("techtribesje"));
        List<Talk> talks = getTalkComponent().getTalks(contentSources);
        assertEquals(0, talks.size());
    }

    @Test
    public void test_getTalksByContentSources_ReturnsSomeTalks_WhenThereAreSome() {
        List<ContentSource> contentSources = new LinkedList<>();
        contentSources.add(getContentSourceComponent().findByShortName("simonbrown"));
        contentSources.add(getContentSourceComponent().findByShortName("chrisclark"));
        List<Talk> talks = getTalkComponent().getTalks(contentSources);
        assertEquals(24, talks.size());
        for (int i = 0; i < 23; i++) {
            assertEquals("Talk " + (24-i), talks.get(i).getTitle());
        }
    }

    @After
    public void tearDown() {
        JdbcTemplate template = getJdbcTemplate();
        template.execute("truncate table talk");
    }

}
