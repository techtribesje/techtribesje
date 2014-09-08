package je.techtribes.component.contentsource;

import je.techtribes.AbstractComponentTestsBase;
import je.techtribes.domain.*;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ReadOnlyContentSourceComponentTests extends AbstractComponentTestsBase {

    @Test
    public void testPersonDetailsAreCorrectlyLoadedFromTheDatabase() {
        Person person = (Person)getContentSourceComponent().findByShortName("simonbrown");
        assertEquals(ContentSourceType.Person, person.getType());
        assertEquals("Simon Brown", person.getName());
        assertEquals(Island.Jersey, person.getIsland());
        assertEquals("Here is some profile text about Simon Brown", person.getProfile());
        assertEquals("http://www.simonbrown.je", person.getUrl().toString());
        assertEquals("simonbrown", person.getTwitterId());
        assertNull(person.getGitHubId());
        assertNull(person.getSearchTerms());
        assertEquals("https://techtribes.je/static/img/default-profile-image.png", person.getProfileImageUrl().toString());
        assertEquals(0, person.getTwitterFollowersCount());
        assertEquals(0, person.getNumberOfTribes());
    }

    @Test
    public void testBusinessTribeDetailsAreCorrectlyLoadedFromTheDatabase() {
        Tribe tribe = (Tribe)getContentSourceComponent().findByShortName("prosperity247");
        assertEquals(ContentSourceType.Business, tribe.getType());
        assertEquals("Prosperity 24.7", tribe.getName());
        assertEquals(Island.Jersey, tribe.getIsland());
        assertEquals("Here is some profile text about P247", tribe.getProfile());
        assertEquals("http://www.prosperity247.com", tribe.getUrl().toString());
        assertEquals("p247", tribe.getTwitterId());
        assertEquals(null, tribe.getGitHubId());
        assertEquals(0, tribe.getNumberOfMembers());
    }

    @Test
    public void testCommunityTribeDetailsAreCorrectlyLoadedFromTheDatabase() {
        Tribe tribe = (Tribe)getContentSourceComponent().findByShortName("techtribesje");
        assertEquals(ContentSourceType.Community, tribe.getType());
        assertEquals("techtribes.je", tribe.getName());
        assertEquals(Island.Jersey, tribe.getIsland());
        assertEquals("http://www.techtribes.je", tribe.getUrl().toString());
        assertEquals("techtribesje", tribe.getTwitterId());
        assertEquals(null, tribe.getGitHubId());
    }

    @Test
    public void testTechTribeDetailsAreCorrectlyLoadedFromTheDatabase() {
        Tribe tribe = (Tribe)getContentSourceComponent().findByShortName("coding");
        assertEquals(ContentSourceType.Tech, tribe.getType());
        assertEquals("Coding", tribe.getName());
        assertEquals(Island.None, tribe.getIsland());
        assertNull(tribe.getUrl());
        assertNull(tribe.getTwitterId());
        assertEquals("coding programming java .net ruby php python perl", tribe.getSearchTerms());
        assertEquals(null, tribe.getGitHubId());
    }

    @Test
    public void testFindById_ReturnsContentSource_WhenTheIdIsValid() {
        ContentSource contentSource = getContentSourceComponent().findById(1);
        assertEquals("Simon Brown", contentSource.getName());
    }

    @Test
    public void testFindById_ReturnsNull_WhenTheIdIsNotValid() {
        ContentSource contentSource = getContentSourceComponent().findById(999);
        assertNull(contentSource);
    }

    @Test
    public void testFindByShortName_ReturnsContentSource_WhenTheShortNameIsValid() {
        ContentSource contentSource = getContentSourceComponent().findByShortName("simonbrown");
        assertEquals("Simon Brown", contentSource.getName());
    }

    @Test
    public void testFindByShortName_ReturnsContentSource_WhenTheShortNameIsValidIgnoringCase() {
        ContentSource contentSource = getContentSourceComponent().findByShortName("SIMONBROWN");
        assertEquals("Simon Brown", contentSource.getName());
    }

    @Test
    public void testFindByShortName_ReturnsNull_WhenTheShortNameIsNotValid() {
        ContentSource contentSource = getContentSourceComponent().findByShortName("xxxxxxxxxx");
        assertNull(contentSource);
    }

    @Test
    public void testFindByShortName_ReturnsNull_WhenTheShortNameIsNull() {
        ContentSource contentSource = getContentSourceComponent().findByShortName(null);
        assertNull(contentSource);
    }

    @Test
    public void testFindByTwitterId_ReturnsContentSource_WhenTheTwitterIdIsValid() {
        ContentSource contentSource = getContentSourceComponent().findByTwitterId("p247");
        assertEquals("Prosperity 24.7", contentSource.getName());
    }

    @Test
    public void testFindByTwitterId_ReturnsContentSource_WhenTheTwitterIdIsValidIgnoringCase() {
        ContentSource contentSource = getContentSourceComponent().findByTwitterId("P247");
        assertEquals("Prosperity 24.7", contentSource.getName());
    }

    @Test
    public void testFindByTwitterId_ReturnsNull_WhenTheTwitterIdIsNotValid() {
        ContentSource contentSource = getContentSourceComponent().findByTwitterId("yyyyyyyyyy");
        assertNull(contentSource);
    }

    @Test
    public void testFindByTwitterId_ReturnsNull_WhenTheTwitterIdIsNull() {
        ContentSource contentSource = getContentSourceComponent().findByTwitterId(null);
        assertNull(contentSource);
    }

    @Test
    public void testGetPeople() {
        List<Person> people = getContentSourceComponent().getPeople();
        assertEquals(2, people.size());
        assertEquals("Chris Clark", people.get(0).getName());
        assertEquals("Simon Brown", people.get(1).getName());
    }

    @Test
    public void testGetPeopleAndTribes() {
        List<ContentSource> peopleAndTribes = getContentSourceComponent().getPeopleAndTribes();
        assertEquals(6, peopleAndTribes.size());
        assertEquals("Chris Clark", peopleAndTribes.get(0).getName());
        assertEquals("Coding", peopleAndTribes.get(1).getName());
        assertEquals("DQ Magazine", peopleAndTribes.get(2).getName());
        assertEquals("Prosperity 24.7", peopleAndTribes.get(3).getName());
        assertEquals("Simon Brown", peopleAndTribes.get(4).getName());
        assertEquals("techtribes.je", peopleAndTribes.get(5).getName());
    }

    @Test
    public void testGetTribes() {
        List<Tribe> tribes = getContentSourceComponent().getTribes();
        assertEquals(4, tribes.size());
        assertEquals("Coding", tribes.get(0).getName());
        assertEquals("DQ Magazine", tribes.get(1).getName());
        assertEquals("Prosperity 24.7", tribes.get(2).getName());
        assertEquals("techtribes.je", tribes.get(3).getName());
    }

    @Test
    public void testGetContentSourceByType_ReturnsPeople_WhenPersonIsSpecified() {
        List<ContentSource> peopleAndTribes = getContentSourceComponent().getContentSources(ContentSourceType.Person);
        assertEquals(2, peopleAndTribes.size());
        assertEquals("Chris Clark", peopleAndTribes.get(0).getName());
        assertEquals("Simon Brown", peopleAndTribes.get(1).getName());
    }

    @Test
    public void testGetContentSourceByType_ReturnsTribes_WhenBusinessTribeIsSpecified() {
        List<ContentSource> peopleAndTribes = getContentSourceComponent().getContentSources(ContentSourceType.Business);
        assertEquals(1, peopleAndTribes.size());
        assertEquals("Prosperity 24.7", peopleAndTribes.get(0).getName());
    }

    @Test
    public void testGetContentSourceByType_ReturnsTribes_WhenCommunityTribeIsSpecified() {
        List<ContentSource> peopleAndTribes = getContentSourceComponent().getContentSources(ContentSourceType.Community);
        assertEquals(1, peopleAndTribes.size());
        assertEquals("techtribes.je", peopleAndTribes.get(0).getName());
    }

    @Test
    public void testGetContentSourceByType_ReturnsTribes_WhenMediaTribeIsSpecified() {
        List<ContentSource> peopleAndTribes = getContentSourceComponent().getContentSources(ContentSourceType.Media);
        assertEquals(1, peopleAndTribes.size());
        assertEquals("DQ Magazine", peopleAndTribes.get(0).getName());
    }

    @Test
    public void testGetContentSourceByType_ReturnsTribes_WhenTechTribeIsSpecified() {
        List<ContentSource> peopleAndTribes = getContentSourceComponent().getContentSources(ContentSourceType.Tech);
        assertEquals(1, peopleAndTribes.size());
        assertEquals("Coding", peopleAndTribes.get(0).getName());
    }

}
