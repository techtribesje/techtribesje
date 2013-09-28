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

    @Test
    public void test_getBodyAsHtml_HasNoEffect_ReturnsTheOriginalBodyWhenThereAreNoUrls() {
        tweet = new Tweet(contentSource, 1234567890, "Here is a string without hyperlinks", new Date());
        assertEquals("Here is a string without hyperlinks", tweet.getBodyAsHtml());
    }

    @Test
    public void test_getBodyAsHtml_CreatesAHtmlHyperlink_WhenTheStringContainsOneUrl() {
        tweet = new Tweet(contentSource, 1234567890, "Here is a link http://www.google.com", new Date());
        assertEquals("Here is a link <a href=\"http://www.google.com\" target=\"_blank\">http://www.google.com</a>", tweet.getBodyAsHtml());
    }

    @Test
    public void test_getBodyAsHtml_CreatesAHtmlHyperlink_WhenTheStringContainsMoreThanOneUrl() {
        tweet = new Tweet(contentSource, 1234567890, "Here is a link http://www.google.com and another http://www.yahoo.com", new Date());
        assertEquals("Here is a link <a href=\"http://www.google.com\" target=\"_blank\">http://www.google.com</a> and another <a href=\"http://www.yahoo.com\" target=\"_blank\">http://www.yahoo.com</a>", tweet.getBodyAsHtml());
    }

}
