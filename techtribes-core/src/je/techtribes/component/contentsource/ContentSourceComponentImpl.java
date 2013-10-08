package je.techtribes.component.contentsource;

import je.techtribes.component.AbstractComponent;
import je.techtribes.domain.ContentSource;
import je.techtribes.domain.ContentSourceType;
import je.techtribes.domain.Person;
import je.techtribes.domain.Tribe;
import je.techtribes.util.JdbcDatabaseConfiguration;

import java.util.*;

class ContentSourceComponentImpl extends AbstractComponent implements ContentSourceComponent {

    private JdbcContentSourceDao contentSourceDao;
    private UserConnectionDao userConnectionDao;

    private List<ContentSource> peopleAndTribes;
    private Map<Integer, ContentSource> peopleMapById;
    private Map<String, ContentSource> peopleMapByShortName;
    private Map<String, ContentSource> peopleMapByTwitterId;

    ContentSourceComponentImpl(JdbcDatabaseConfiguration jdbcDatabaseConfiguration) {
        this.contentSourceDao = new JdbcContentSourceDao(jdbcDatabaseConfiguration);
        this.userConnectionDao = new JdbcUserConnectionDao(jdbcDatabaseConfiguration);

        peopleAndTribes = new LinkedList<>();
        peopleMapById = new HashMap<>();
        peopleMapByShortName = new HashMap<>();
        peopleMapByTwitterId = new HashMap<>();
    }

    public void init() {
        try {
        refreshContentSources();
        } catch (Exception e) {
            logError(new ContentSourceException("Could not initialise component", e));
        }
    }

    @Override
    public synchronized void refreshContentSources() {
        List<ContentSource> allContentSources = new LinkedList<>();

        try {
            allContentSources = contentSourceDao.loadContentSources();
        } catch (Exception e) {
            ContentSourceException cse = new ContentSourceException("Error loading content sources", e);
            logError(cse);
        }

        cacheContentSources(allContentSources);
        loadUserConnections();
    }

    @Override
    public List<ContentSource> getPeopleAndTribes() {
        return new LinkedList<>(peopleAndTribes);
    }

    @Override
    public List<Person> getPeople() {
        List<Person> filteredList = new LinkedList<>();
        for (ContentSource contentSource : peopleAndTribes) {
            if (contentSource.getType() == ContentSourceType.Person) {
                filteredList.add((Person)contentSource);
            }
        }

        return filteredList;
    }

    @Override
    public List<ContentSource> getContentSources(ContentSourceType type) {
        List<ContentSource> filteredList = new LinkedList<>();
        for (ContentSource contentSource : peopleAndTribes) {
            if (contentSource.getType() == type) {
                filteredList.add(contentSource);
            }
        }

        return filteredList;
    }

    @Override
    public List<Tribe> getTribes() {
        List<Tribe> tribes = new LinkedList<>();

        for (ContentSource contentSource : peopleAndTribes) {
            if (contentSource.isTribe()) {
                tribes.add((Tribe)contentSource);
            }
        }

        return tribes;
    }

    private void cacheContentSources(List<ContentSource> allContentSources) {
        List<ContentSource> peopleAndTribes = new LinkedList<>();
        Map<Integer, ContentSource> peopleAndTribesById = new HashMap<>();
        Map<String, ContentSource> peopleAndTribesByShortName = new HashMap<>();
        Map<String, ContentSource> peopleAndTribesByTwitterId = new HashMap<>();

        for (ContentSource contentSource : allContentSources) {
            if (contentSource.isContentAggregated()) {
                peopleAndTribes.add(contentSource);
            }

            peopleAndTribesById.put(contentSource.getId(), contentSource);
            peopleAndTribesByShortName.put(contentSource.getShortName(), contentSource);
            if (contentSource.hasTwitterId()) {
                peopleAndTribesByTwitterId.put(contentSource.getTwitterId().toLowerCase(), contentSource);
            }
        }

        this.peopleAndTribes = peopleAndTribes;
        this.peopleMapById = peopleAndTribesById;
        this.peopleMapByShortName = peopleAndTribesByShortName;
        this.peopleMapByTwitterId = peopleAndTribesByTwitterId;
    }

    @Override
    public ContentSource findById(int id) {
        if (peopleMapById.containsKey(id)) {
            return peopleMapById.get(id);
        } else {
            return null;
        }
    }

    @Override
    public ContentSource findByShortName(String shortName) {
        if (shortName == null) {
            return null;
        }

        if (peopleMapByShortName.containsKey(shortName.toLowerCase())) {
            return peopleMapByShortName.get(shortName.toLowerCase());
        } else {
            return null;
        }
    }

    @Override
    public ContentSource findByTwitterId(String twitterId) {
        if (twitterId == null) {
            return null;
        }

        if (peopleMapByTwitterId.containsKey(twitterId.toLowerCase())) {
            return peopleMapByTwitterId.get(twitterId.toLowerCase());
        } else {
            return null;
        }
    }

    @Override
    public void add(ContentSource contentSource) {
        try {
            contentSourceDao.add(contentSource);
            refreshContentSources();
        } catch (Exception e) {
            ContentSourceException cse = new ContentSourceException("Error adding content source", e);
            logError(cse);
            throw cse;
        }
    }

    @Override
    public void update(ContentSource contentSource) {
        try {
            contentSourceDao.update(contentSource);
        } catch (Exception e) {
            ContentSourceException cse = new ContentSourceException("Error updating content source", e);
            logError(cse);
            throw cse;
        }
    }

    @Override
    public void updateTribeMembers(Tribe tribe, Set<Integer> personIds) {
        try {
            contentSourceDao.updateTribeMembers(tribe, personIds);
            refreshContentSources();
        } catch (Exception e) {
            ContentSourceException cse = new ContentSourceException("Error while updating tribe members for tribe " + tribe.getName(), e);
            logError(cse);
            throw cse;
        }
    }

    @Override
    public void updateTribeMembershipsForPerson(Person person, Set<Integer> tribeIds) {
        try {
            contentSourceDao.updateTribeMembershipsForPerson(person, tribeIds);
            refreshContentSources();
        } catch (Exception e) {
            ContentSourceException cse = new ContentSourceException("Error while updating tribe memberships for person " + person.getName(), e);
            logError(cse);
            throw cse;
        }
    }

    private void loadUserConnections() {
        try {
            List<String> twitterIds = userConnectionDao.getTwitterIds();
            for (String twitterId : twitterIds) {
                ContentSource contentSource = findByTwitterId(twitterId);
                if (contentSource != null) {
                    contentSource.setSignedInBefore(true);
                }
            }
        } catch (Exception e) {
            ContentSourceException cse = new ContentSourceException("Error looking up list of Twitter IDs", e);
            logError(cse);
            throw(cse);
        }
    }

}
