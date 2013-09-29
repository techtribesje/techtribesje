package je.techtribes.component.contentsource;

import je.techtribes.domain.ContentSource;

import java.util.List;
import java.util.Set;

interface ContentSourceDao {

    public List<ContentSource> loadContentSources();

    public void add(ContentSource contentSource);

    public void update(ContentSource contentSource);

    public void updateTribeMembers(ContentSource tribe, Set<Integer> personIds);

    public void updateTribeMembershipsForPerson(ContentSource person, Set<Integer> tribeIds);

}
