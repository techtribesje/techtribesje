package je.techtribes.domain;

import je.techtribes.util.comparator.ContentSourceByNameComparator;

import java.util.*;

public class Tribe extends ContentSource {

    public static String[] MEDIA_TRIBE_KEYWORD_TRIGGERS = new String[] {
            "digital", "technology", "bitcoin", "fintech",
            "computer", "email", "broadband", "fibre",
            "bcs jersey", "digital jersey",
            "egov", "e-gov",
            "coding", "software"
    };

    private final Set<Person> members = new TreeSet<>(new ContentSourceByNameComparator());

    public Tribe(ContentSourceType type) {
        super(type);
    }

    public Tribe(ContentSourceType type, int id) {
        super(type, id);
    }

    public int getNumberOfMembers() {
        return members.size();
    }

    public Collection<Person> getMembers() {
        return new LinkedList<>(members);
    }

    public void add(Person person) {
        synchronized (members) {
            if (!hasMember(person)) {
                members.add(person);
                person.add(this);
            }
        }
    }

    public void remove(Person person) {
        synchronized (members) {
            if (hasMember(person)) {
                members.remove(person);
                person.remove(this);
            }
        }
    }

    public boolean hasMember(Person person) {
        return members.contains(person);
    }

    public void setMembers(Collection<Person> people) {
        synchronized (members) {
            this.members.clear();

            for (Person person : people) {
                add(person);
            }
        }
    }

    @Override
    public boolean isActive() {
        if (getType() == ContentSourceType.Tech) {
            return true;
        } else {
            return super.isActive();
        }
    }
}
