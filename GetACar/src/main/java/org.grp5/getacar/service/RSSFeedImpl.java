package org.grp5.getacar.service;

import com.google.inject.Inject;
import org.grp5.getacar.service.util.Feed;
import org.grp5.getacar.service.util.FeedMessage;
import org.grp5.getacar.service.util.RSSFeedParser;
import org.grp5.getacar.service.util.RSSFeedWriter;
import org.grp5.getacar.web.guice.annotation.AbsoluteRSSFeedPath;
import org.joda.time.DateTime;

import java.io.File;
import java.util.List;

/**
 * Implementation of the {@link org.grp5.getacar.service.RSSFeed} service
 */
public class RSSFeedImpl implements RSSFeed {

    private final String absoluteRSSFeedPath;

    @Inject
    public RSSFeedImpl(@AbsoluteRSSFeedPath String absoluteRSSFeedPath) {
        this.absoluteRSSFeedPath = absoluteRSSFeedPath;
    }

    @Override
    public synchronized void addFeedMessage(FeedMessage feedMessage) throws Exception {
        final File rssXMLFile = new File(absoluteRSSFeedPath);
        final Feed rssFeed;
        if (!rssXMLFile.exists()) {
            rssFeed = new Feed("The Get A Car RSS Feed", "http://localhost:8080/getacar",
                    "We inform you about new vehicles here", "English", "Get A Car Inc.",
                    new DateTime().toString());
        } else {
            rssFeed = new RSSFeedParser(absoluteRSSFeedPath).readFeed();
        }
        rssXMLFile.delete();
        final List<FeedMessage> feedMessages = rssFeed.getMessages();
        feedMessages.add(feedMessage);
        if (feedMessages.size() > 10) {
            feedMessages.remove(0);
        }
        final RSSFeedWriter rssFeedWriter = new RSSFeedWriter(rssFeed, absoluteRSSFeedPath);
        rssFeedWriter.write();
    }
}
