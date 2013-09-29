package je.techtribes.web.controller;

import je.techtribes.component.contentsource.ContentSourceComponent;
import je.techtribes.domain.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ContentSourceComponentStub implements ContentSourceComponent {

    @Override
    public void refreshContentSources() {
    }

    @Override
    public List<ContentSource> getPeopleAndTribes() {
        List<ContentSource> contentSources = new LinkedList<>();
        contentSources.addAll(getContentSources(ContentSourceType.Person));
        contentSources.addAll(getContentSources(ContentSourceType.Business));
        contentSources.addAll(getContentSources(ContentSourceType.Tech));
        contentSources.addAll(getContentSources(ContentSourceType.Media));
        contentSources.addAll(getContentSources(ContentSourceType.Community));

        return contentSources;
    }

    @Override
    public List<ContentSource> getContentSources(ContentSourceType type) {
        List<ContentSource> contentSources = new LinkedList<>();
        int numberOfContentSources = 0;
        switch (type) {
            case Person:
                numberOfContentSources = 50;
                break;
            case Business:
                numberOfContentSources = 25;
                break;
            case Tech:
                numberOfContentSources = 15;
                break;
            case Media:
                numberOfContentSources = 1;
                break;
            case Community:
                numberOfContentSources = 9;
                break;
        }

        for (int i = 0; i < numberOfContentSources; i++) {
            contentSources.add(ContentSourceFactory.create(type, i));
        }

        return contentSources;
    }

    @Override
    public List<Person> getPeople() {
        List<Person> people = new LinkedList<>();
        List<ContentSource> contentSources = getContentSources(ContentSourceType.Person);

        for (ContentSource contentSource : contentSources) {
            people.add((Person)contentSource);
        }

        return people;
    }

    @Override
    public List<Tribe> getTribes() {
        List<Tribe> tribes = new LinkedList<>();

        List<ContentSource> businessTribes = getContentSources(ContentSourceType.Business);
        List<ContentSource> techTribes = getContentSources(ContentSourceType.Tech);
        List<ContentSource> mediaTribes = getContentSources(ContentSourceType.Media);
        List<ContentSource> communityTribes = getContentSources(ContentSourceType.Community);

        for (ContentSource contentSource : businessTribes) {
            tribes.add((Tribe)contentSource);
        }
        for (ContentSource contentSource : techTribes) {
            tribes.add((Tribe)contentSource);
        }
        for (ContentSource contentSource : mediaTribes) {
            tribes.add((Tribe)contentSource);
        }
        for (ContentSource contentSource : communityTribes) {
            tribes.add((Tribe)contentSource);
        }

        return tribes;
    }

    @Override
    public ContentSource findById(int id) {
        return new Person(id);
    }

    @Override
    public ContentSource findByShortName(String shortName) {
        if ("techtribesje".equals(shortName)) {
            Tribe tribe = new Tribe(ContentSourceType.Business, 1);
            tribe.setName(shortName);
            return tribe;
        } else {
            Person person = new Person(1);
            person.setName(shortName);
            return person;
        }
    }

    @Override
    public ContentSource findByTwitterId(String twitterId) {
        Person person = new Person(1);
        person.setTwitterId(twitterId);
        return person;
    }

    @Override
    public void add(ContentSource contentSource) {
    }

    @Override
    public void update(ContentSource contentSource) {
    }

    @Override
    public void updateTribeMembers(Tribe tribe, Set<Integer> personIds) {
    }

    @Override
    public void updateTribeMembershipsForPerson(Person person, Set<Integer> tribeIds) {
    }

}
