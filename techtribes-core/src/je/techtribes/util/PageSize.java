package je.techtribes.util;

public class PageSize {

    public static final int RECENT_NEWS_FEED_ENTRIES = 12;
    public static final int RECENT_NEWS = 12;
    public static final int RECENT_TWEETS = 28;
    public static final int SEARCH_RESULTS = 99;
    public static final int SEARCH_RESULTS_CONTENT = 12;
    public static final int RECENT_TALKS = 3;
    public static final int RECENT_JOBS = 6;
    public static final int RECENT_EVENTS = 3;

    public static int calculateNumberOfPages(long items, int pageSize) {
        return (int)Math.ceil(items / (double)pageSize);
    }

    public static int validatePage(int page, int maxPage) {
        if (page < 1) {
            return 1;
        }

        if (page > maxPage) {
            return maxPage;
        }

        return page;
    }

    public static int validatePage(int page) {
        if (page < 1) {
            return 1;
        } else {
            return page;
        }
    }

    public static int validatePageSize(int pageSize) {
        if (pageSize < 1) {
            return 1;
        } else {
            return pageSize;
        }
    }

}
