package je.techtribes.component.newsfeedconnector;

import com.structurizr.annotation.SoftwareSystemDependency;
import je.techtribes.domain.ContentSourceType;
import je.techtribes.domain.NewsFeed;
import je.techtribes.domain.NewsFeedEntry;
import je.techtribes.domain.Tribe;
import je.techtribes.util.AbstractComponent;
import je.techtribes.util.comparator.ContentItemComparator;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@SoftwareSystemDependency(target = "Blogs", description = "Gets blog/news posts from")
class NewsFeedConnectorImpl extends AbstractComponent implements NewsFeedConnector {

    private NewsFeedInterface newsFeedInterface;

    NewsFeedConnectorImpl(NewsFeedInterface newsFeedInterface) {
        this.newsFeedInterface = newsFeedInterface;
    }

    public List<NewsFeedEntry> loadNewsFeedEntries(NewsFeed feed) throws NewsFeedConnectorException {
        try {
            List<NewsFeedEntry> newsFeedEntries = newsFeedInterface.loadNewsFeedEntries(feed);

            List<NewsFeedEntry> filteredNewsFeedEntries = new LinkedList<>();
            for (NewsFeedEntry newsFeedEntry : newsFeedEntries) {
                add(filteredNewsFeedEntries, newsFeedEntry);
            }
            Collections.sort(newsFeedEntries, new ContentItemComparator());

            return filteredNewsFeedEntries;
        } catch (Exception e) {
            NewsFeedConnectorException nfce = new NewsFeedConnectorException("Could not update news feed from " + feed.getUrl(), e);
            logWarn(nfce);
            throw nfce;
        }
    }

    private void add(List<NewsFeedEntry> entries, NewsFeedEntry fe) {
        if (fe.getContentSource().getType() == ContentSourceType.Media) {
            if (fe.getContentSource().getShortName().equals("digitalquadrantmagazine")) {
                // this tries to ensure that only Channel Island news is aggregated from DQ Mag
                String body = fe.getBody().toLowerCase();
                if (body.contains("jersey") || body.contains("guernsey") || body.contains("channel island")) {
                    entries.add(fe);
                }
            } else {
                // this tries to ensure that only digital/IT/technology news is aggregated
                String content = fe.getTitle().toLowerCase() + " " + fe.getBody().toLowerCase();
                for (String keyword : Tribe.MEDIA_TRIBE_KEYWORD_TRIGGERS) {
                    if (content.contains(keyword) && !entries.contains(fe)) {
                        entries.add(fe);
                    }
                }
            }
        } else {
            entries.add(fe);
        }
    }

}
