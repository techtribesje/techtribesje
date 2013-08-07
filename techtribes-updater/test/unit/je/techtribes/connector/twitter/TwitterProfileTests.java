package je.techtribes.connector.twitter;

import org.junit.Test;

import java.net.URL;

import static org.junit.Assert.assertEquals;

public class TwitterProfileTests {

    @Test
    public void test_BasicObjectCreation() throws Exception {
        TwitterProfile profile = new TwitterProfile(
                "simonbrown",
                "My twitter profile",
                new URL("http://twitter.com/simonbrown.jpg"),
                new URL("http://www.simonbrown.je"),
                100);

        assertEquals("simonbrown", profile.getTwitterId());
        assertEquals("My twitter profile", profile.getDescription());
        assertEquals(new URL("http://twitter.com/simonbrown.jpg"), profile.getImageUrl());
        assertEquals(new URL("http://www.simonbrown.je"), profile.getUrl());
        assertEquals(100, profile.getFollowersCount());
    }

}
