package je.techtribes.component.talk;

import com.structurizr.annotation.Component;
import je.techtribes.domain.ContentSource;
import je.techtribes.domain.Talk;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Component(description = "Provides access to information about talks by local speakers.")
public interface TalkComponent {

    List<Talk> getRecentTalks();

    List<Talk> getUpcomingTalks(int pageSize);

    List<Talk> getTalksWithVideo();

    List<Talk> getTalksByYear(int year);

    List<Talk> getTalksByMonth(int year, int month);

    List<Talk> getTalks(ContentSource contentSource);

    List<Talk> getTalks(Collection<ContentSource> contentSources);

    Talk getTalk(int id);

    long getNumberOfLocalTalks(int id, Date start, Date end);

    long getNumberOfInternationalTalks(int id, Date start, Date end);

}
