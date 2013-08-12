package je.techtribes.domain;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class TweetTests {

    private ContentSource contentSource;
    private Tweet tweet;
    
    @Before
    public void setUp() throws Exception {
        contentSource = new Person();
        contentSource.setTwitterId("simonbrown");
    }

    @Test
    public void test_GetPermalink() {
        tweet = new Tweet(contentSource, 1234567890, "body", new Date());
        assertEquals("http://twitter.com/simonbrown/status/1234567890", tweet.getPermalink());
    }

}
