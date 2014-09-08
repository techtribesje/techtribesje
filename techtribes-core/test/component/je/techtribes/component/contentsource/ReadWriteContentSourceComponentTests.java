package je.techtribes.component.contentsource;

import je.techtribes.AbstractComponentTestsBase;
import je.techtribes.domain.*;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class ReadWriteContentSourceComponentTests extends AbstractComponentTestsBase {

    @Before
    public void setUp() {
        getContentSourceComponent().refreshContentSources();
    }

    @Test
    public void testAddAndUpdate() throws Exception {
        assertEquals(2, getContentSourceComponent().getPeople().size());

        Person kirstiebrown = new Person();
        kirstiebrown.setName("Kirstie Brown");
        kirstiebrown.setIsland(Island.Jersey);
        kirstiebrown.setTwitterId("kirstie_brown");
        getContentSourceComponent().add(kirstiebrown);

        assertEquals(3, getContentSourceComponent().getPeople().size());

        kirstiebrown = (Person)getContentSourceComponent().findByShortName("kirstiebrown");
        assertEquals("Kirstie Brown", kirstiebrown.getName());
        assertEquals(Island.Jersey, kirstiebrown.getIsland());
        assertEquals("kirstie_brown", kirstiebrown.getTwitterId());
        assertEquals("", kirstiebrown.getProfile());
        assertNull(kirstiebrown.getUrl());
        assertEquals("https://techtribes.je/static/img/default-profile-image.png", kirstiebrown.getProfileImageUrl().toString());
        assertNull(kirstiebrown.getGitHubId());
        assertEquals(0, kirstiebrown.getTwitterFollowersCount());

        kirstiebrown.setGitHubId("kirstiebrown");
        kirstiebrown.setTwitterFollowersCount(1234);
        kirstiebrown.setUrl(new URL("http://www.kirstiebrown.je"));
        kirstiebrown.setProfileImageUrl(new URL("http://www.kirstiebrown.je/photo.jpg"));
        kirstiebrown.setProfile("Here is some profile text about Kirstie Brown");
        getContentSourceComponent().update(kirstiebrown);

        getContentSourceComponent().refreshContentSources();
        kirstiebrown = (Person)getContentSourceComponent().findByShortName("kirstiebrown");
        assertEquals("Kirstie Brown", kirstiebrown.getName());
        assertEquals(Island.Jersey, kirstiebrown.getIsland());
        assertEquals("Here is some profile text about Kirstie Brown", kirstiebrown.getProfile());
        assertEquals("http://www.kirstiebrown.je", kirstiebrown.getUrl().toString());
        assertEquals("https://www.kirstiebrown.je/photo.jpg", kirstiebrown.getProfileImageUrl().toString());
        assertEquals("kirstie_brown", kirstiebrown.getTwitterId());
        assertEquals("kirstiebrown", kirstiebrown.getGitHubId());
        assertEquals(1234, kirstiebrown.getTwitterFollowersCount());
    }

    @Test
    public void testUpdateTribeMembers() {
        Person chrisclark = (Person)getContentSourceComponent().findByShortName("chrisclark");
        Tribe p247 = (Tribe)getContentSourceComponent().findByShortName("prosperity247");
        assertEquals(0, chrisclark.getNumberOfTribes());
        assertEquals(0, p247.getNumberOfMembers());

        Set<Integer> personIds = new HashSet<>();
        personIds.add(chrisclark.getId());
        getContentSourceComponent().updateTribeMembers(p247, personIds);

        chrisclark = (Person)getContentSourceComponent().findByShortName("chrisclark");
        assertEquals(1, chrisclark.getNumberOfTribes());
        List<Tribe> tribes = new LinkedList<>(chrisclark.getTribes());
        assertEquals("Prosperity 24.7", tribes.get(0).getName());

        p247 = (Tribe)getContentSourceComponent().findByShortName("prosperity247");
        assertEquals(1, p247.getNumberOfMembers());
        List<Person> people = new LinkedList<>(p247.getMembers());
        assertEquals("Chris Clark", people.get(0).getName());
    }

    @Test
    public void testUpdateTribeMembershipForPerson() {
        Person simonbrown = (Person)getContentSourceComponent().findByShortName("simonbrown");
        Tribe techtribesje = (Tribe)getContentSourceComponent().findByShortName("techtribesje");
        assertEquals(0, simonbrown.getNumberOfTribes());
        assertEquals(0, techtribesje.getNumberOfMembers());

        Set<Integer> tribeIds = new HashSet<>();
        tribeIds.add(techtribesje.getId());
        getContentSourceComponent().updateTribeMembershipsForPerson(simonbrown, tribeIds);

        simonbrown = (Person)getContentSourceComponent().findByShortName("simonbrown");
        assertEquals(1, simonbrown.getNumberOfTribes());
        List<Tribe> tribes = new LinkedList<>(simonbrown.getTribes());
        assertEquals("techtribes.je", tribes.get(0).getName());

        techtribesje = (Tribe)getContentSourceComponent().findByShortName("techtribesje");
        assertEquals(1, techtribesje.getNumberOfMembers());
        List<Person> people = new LinkedList<>(techtribesje.getMembers());
        assertEquals("Simon Brown", people.get(0).getName());
    }

}
