package org.grp5.getacar.service;

import org.grp5.getacar.service.util.FeedMessage;

/**
 * RSSFeed Service.
 */
public interface RSSFeed {
    void addFeedMessage(FeedMessage feedMessage) throws Exception;
}
