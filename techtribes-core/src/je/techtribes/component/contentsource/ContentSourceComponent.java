package je.techtribes.component.contentsource;

import com.structurizr.element.Component;
import je.techtribes.domain.ContentSource;
import je.techtribes.domain.ContentSourceType;
import je.techtribes.domain.Person;
import je.techtribes.domain.Tribe;

import java.util.List;
import java.util.Set;

@Component(responsibility = "Provides access to information about people and tribes.")
public interface ContentSourceComponent {

    void refreshContentSources();

    List<ContentSource> getPeopleAndTribes();

    List<ContentSource> getContentSources(ContentSourceType type);

    List<Person> getPeople();

    List<Tribe> getTribes();

    ContentSource findById(int id);

    ContentSource findByShortName(String shortName);

    ContentSource findByTwitterId(String twitterId);

    void add(ContentSource contentSource);

    void update(ContentSource contentSource);

    void updateTribeMembers(Tribe tribe, Set<Integer> personIds);

    void updateTribeMembershipsForPerson(Person person, Set<Integer> tribeIds);

}
