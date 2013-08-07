package je.techtribes.component.talk;

import je.techtribes.domain.ContentSource;
import je.techtribes.domain.Talk;

import java.util.Collection;
import java.util.Date;
import java.util.List;

interface TalkDao {

    List<Talk> getTalks(Date end);

    List<Talk> getTalksByYear(int year);

    List<Talk> getTalks(ContentSource contentSource);

    List<Talk> getTalks(Collection<ContentSource> contentSources);

    Talk getTalk(int id);

    long getNumberOfLocalTalks(int id, Date start, Date end);

    long getNumberOfInternationalTalks(int id, Date start, Date end);

}
