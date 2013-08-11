package je.techtribes.component.newsfeedconnector;

import com.sun.syndication.feed.WireFeed;
import com.sun.syndication.feed.atom.Content;
import com.sun.syndication.feed.atom.Entry;
import com.sun.syndication.feed.atom.Link;
import com.sun.syndication.feed.rss.Channel;
import com.sun.syndication.feed.rss.Item;
import com.sun.syndication.io.WireFeedInput;
import com.sun.syndication.io.XmlReader;
import je.techtribes.component.AbstractComponent;
import je.techtribes.domain.NewsFeed;
import je.techtribes.domain.NewsFeedEntry;
import je.techtribes.util.comparator.ContentItemComparator;

import java.net.HttpURLConnection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

class RomeNewsFeedConnector extends AbstractComponent implements NewsFeedConnector {

    private static int CONNECTION_TIMEOUT = 1000 * 30;
    private static int READ_TIMEOUT = 1000 * 30;

    public List<NewsFeedEntry> loadNewsFeedEntries(NewsFeed feed) throws NewsFeedConnectorException {
        logDebug("Refreshing feed from " + feed.getUrl());
        HttpURLConnection conn = null;

        try {
            conn = (HttpURLConnection)feed.getUrl().openConnection();
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setReadTimeout(READ_TIMEOUT);
            conn.addRequestProperty("User-Agent", "techtribes.je");

            WireFeedInput input = new WireFeedInput(false);
            WireFeed wf = input.build(new XmlReader(conn));

            List<NewsFeedEntry> entries = new LinkedList<>();

            if (wf.getFeedType() != null && wf.getFeedType().startsWith("rss")) {
                Channel rssFeed = (Channel) wf;
                feed.setTitle(rssFeed.getTitle());

                for (Item item : (List<Item>)rssFeed.getItems()) {
                    String body = "";
                    if (item.getContent() != null && item.getContent().getValue() != null) {
                        body = item.getContent().getValue();
                    } else if (item.getDescription() != null && item.getDescription().getValue() != null) {
                        body = item.getDescription().getValue();
                    }
                    NewsFeedEntry fe = new NewsFeedEntry(
                            item.getLink(),
                            item.getTitle(),
                            body,
                            item.getPubDate(),
                            feed.getContentSource()
                    );
                    add(entries, fe);
                }
            } else if (wf.getFeedType() != null && wf.getFeedType().startsWith("atom")) {
                com.sun.syndication.feed.atom.Feed atomFeed = (com.sun.syndication.feed.atom.Feed) wf;
                feed.setTitle(atomFeed.getTitle());

                for (Entry entry : (List<Entry>) atomFeed.getEntries()) {
                    String href = null;
                    for (Link link : (List<Link>) entry.getAlternateLinks()) {
                        if ("text/html".equals(link.getType())) {
                            href = link.getHref();
                            continue;
                        }
                    }

                    if (href == null && entry.getAlternateLinks().size() > 0) {
                        // take the first one! :-/
                        href = ((List<Link>)entry.getAlternateLinks()).get(0).getHref();
                    }

                    String body = null;
                    for (Content content : (List<Content>) entry.getContents()) {
                        if ("html".equals(content.getType())) {
                            body = content.getValue();
                        }
                    }
                    if (body == null) {
                        Content content = entry.getSummary();
                        if ("html".equals(content.getType())) {
                            body = content.getValue();
                        }
                    }

                    NewsFeedEntry fe = new NewsFeedEntry(
                            href,
                            entry.getTitle(),
                            body,
                            entry.getPublished() != null ? entry.getPublished() : entry.getUpdated(),
                            feed.getContentSource()
                    );
                    add(entries, fe);
                }
            }

            Collections.sort(entries, new ContentItemComparator());

            logDebug("Refreshed feed from " + feed.getUrl());

            return entries;
        } catch (Exception e) {
            NewsFeedConnectorException nfce = new NewsFeedConnectorException("Could not update news feed from " + feed.getUrl(), e);
            logWarn(nfce);
            throw nfce;
        } finally {
            try {
                if (conn != null) {
                    conn.disconnect();
                }
            } catch (Exception e) {
                NewsFeedConnectorException nfce = new NewsFeedConnectorException("Could not disconnect from " + feed.getUrl(), e);
                logWarn(nfce);
            }
        }
    }

    private void add(List<NewsFeedEntry> entries, NewsFeedEntry fe) {
        // this tries to ensure that only Channel Island news is aggregated from DQ Mag
        if (fe.getContentSource().getShortName().equals("digitalquadrantmagazine")) {
            String body = fe.getBody().toLowerCase();
            if (body.contains("jersey") || body.contains("guernsey") || body.contains("channel island")) {
                entries.add(fe);
            }
        } else {
            entries.add(fe);
        }
    }

}
