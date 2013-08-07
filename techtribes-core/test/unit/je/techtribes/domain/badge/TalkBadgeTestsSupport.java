package je.techtribes.domain.badge;

import je.techtribes.domain.ContentSource;
import je.techtribes.domain.Talk;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TalkBadgeTestsSupport extends BadgeTestsSupport {

    protected Talk createTalk(int id, ContentSource contentSource, Date date, String country) {
        Talk talk = new Talk(id);
        talk.setContentSource(contentSource);
        talk.setDate(date);
        talk.setCountry(country);

        return talk;
    }

}
