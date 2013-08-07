package je.techtribes.component.search;

import je.techtribes.component.AbstractComponent;
import je.techtribes.component.contentsource.ContentSourceComponent;
import je.techtribes.domain.ContentSource;
import je.techtribes.domain.NewsFeedEntry;
import je.techtribes.domain.Tweet;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.store.NativeFSLockFactory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

class LuceneSearchComponent extends AbstractComponent implements SearchComponent {

    private Analyzer analyzer;
    private Directory index;

    private ContentSourceComponent contentSourceComponent;

    private String luceneIndexPath;

    private static String ID = "id";
    private static String TITLE = "title";
    private static String FULL_CONTENT = "content";
    private static String TRUNCATED_BODY = "truncatedBody";
    private static String DATE = "date";
    private static String CONTENT_SOURCE_ID = "contentSourceId";
    private static String PERMALINK = "permalink";
    private static String TYPE = "type";
    private static String TWITTER_HASHTAGS = "twitter-hashtags";
    private static String TWITTER_USERS = "twitter-users";
    private static String TIMESTAMP = "timestamp";

    private static String TYPE_NEWS_FEED_ENTRY = "n";
    private static String TYPE_TWEET = "t";

    LuceneSearchComponent(ContentSourceComponent contentSourceComponent, String luceneIndexPath) {
        this.contentSourceComponent = contentSourceComponent;
        this.luceneIndexPath = luceneIndexPath;
    }

    public void init() {
        try {
            analyzer = new StandardAnalyzer(Version.LUCENE_35);
            File path = new File(luceneIndexPath);
            createLuceneSearchIndexIfItDoesntExist(path);
            index = new NIOFSDirectory(path, new NativeFSLockFactory());
        } catch (Exception e) {
            SearchException se = new SearchException("Error while initialising search index", e);
            logError(se);
        }
    }

    private void createLuceneSearchIndexIfItDoesntExist(File path) {
        logInfo("Lucene data directory is " + path.getAbsoluteFile());

        if (!path.exists()) {
            logInfo("Creating Lucene data directory at " + path.getAbsolutePath());
            boolean success = path.mkdirs();
            if (!success) {
                logError("Could not create Lucene index directories at " + path.getAbsolutePath());
            }
        }
    }

    private IndexWriter createIndexWriter() throws Exception {
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_35, analyzer);
        return new IndexWriter(index, config);
    }

    public void add(NewsFeedEntry newsFeedEntry) {
        try  {
            IndexWriter indexWriter = createIndexWriter();

            remove(newsFeedEntry, indexWriter);
            insert(newsFeedEntry, indexWriter);

            indexWriter.close();
        } catch (Exception e) {
            SearchException se = new SearchException("Error adding news feed entry", e);
            logError(se);
            throw se;
        }
    }

    public void add(Tweet tweet) {
        try {
            IndexWriter indexWriter = createIndexWriter();

            remove(tweet, indexWriter);
            insert(tweet, indexWriter);

            indexWriter.close();
        } catch (Exception e) {
            SearchException se = new SearchException("Error adding tweet", e);
            logError(se);
            throw se;
        }
    }

    public void deleteTweet(long id) {
        try {
            IndexWriter indexWriter = createIndexWriter();

            Term term = new Term(ID, "" + id);
            indexWriter.deleteDocuments(term);

            indexWriter.close();
        } catch (Exception e) {
            SearchException se = new SearchException("Error removing tweet", e);
            logError(se);
            throw se;
        }
    }

    private void remove(NewsFeedEntry newsFeedEntry, IndexWriter indexWriter) throws Exception {
        Term term = new Term(PERMALINK, newsFeedEntry.getPermalink());
        indexWriter.deleteDocuments(term);
    }

    private void remove(Tweet tweet, IndexWriter indexWriter) throws Exception {
        Term term = new Term(PERMALINK, tweet.getPermalink());
        indexWriter.deleteDocuments(term);
    }

    private void insert(NewsFeedEntry newsFeedEntry, IndexWriter indexWriter) throws IOException {
        indexWriter.addDocument(convertToDocument(newsFeedEntry));
    }

    private Document convertToDocument(NewsFeedEntry newsFeedEntry) {
        DateFormat dateFormat = createDateTimeFormat();
        DateFormat timestampDateFormat = createTimestampFormat();

        Document doc = new Document();
        doc.add(new Field(TITLE, newsFeedEntry.getTitle(), Field.Store.YES, Field.Index.ANALYZED));
        doc.add(new Field(FULL_CONTENT, newsFeedEntry.getTitle() + " " + newsFeedEntry.getBody(), Field.Store.NO, Field.Index.ANALYZED));
        doc.add(new Field(TRUNCATED_BODY, newsFeedEntry.getTruncatedBody(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(DATE, dateFormat.format(newsFeedEntry.getTimestamp()), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(CONTENT_SOURCE_ID, "" + newsFeedEntry.getContentSource().getId(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(PERMALINK, newsFeedEntry.getPermalink(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(TYPE, TYPE_NEWS_FEED_ENTRY, Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(TIMESTAMP, timestampDateFormat.format(newsFeedEntry.getTimestamp()), Field.Store.YES, Field.Index.NOT_ANALYZED));
        return doc;
    }

    private void insert(Tweet tweet, IndexWriter indexWriter) throws IOException {
        indexWriter.addDocument(convertToDocument(tweet));
    }

    private Document convertToDocument(Tweet tweet) {
        DateFormat dateFormat = createDateTimeFormat();
        DateFormat timestampDateFormat = createTimestampFormat();

        Document doc = new Document();
        doc.add(new Field(ID, "" + tweet.getId(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(FULL_CONTENT, tweet.getBody(), Field.Store.NO, Field.Index.ANALYZED));
        doc.add(new Field(TRUNCATED_BODY, tweet.getBody(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(DATE, dateFormat.format(tweet.getTimestamp()), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(CONTENT_SOURCE_ID, "" + tweet.getContentSource().getId(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(PERMALINK, tweet.getPermalink(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(TYPE, TYPE_TWEET, Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(TIMESTAMP, timestampDateFormat.format(tweet.getTimestamp()), Field.Store.YES, Field.Index.NOT_ANALYZED));

        String tokens[] = tweet.getBody().split("\\s");
        StringBuilder twitterHashtags = new StringBuilder();
        StringBuilder twitterUsers = new StringBuilder();
        for (String token : tokens) {
            if (token.startsWith("#")) {
                twitterHashtags.append(token);
                twitterHashtags.append(" ");
            } else if (token.startsWith("@")) {
                twitterUsers.append(token);
                twitterUsers.append(" ");
            }
        }
        doc.add(new Field(TWITTER_HASHTAGS, twitterHashtags.toString(), Field.Store.NO, Field.Index.ANALYZED));
        doc.add(new Field(TWITTER_USERS, twitterUsers.toString(), Field.Store.NO, Field.Index.ANALYZED));
        return doc;
    }

    public List<SearchResult> searchForNewsFeedEntries(String query, int hitsPerPage) {
        return search(query, hitsPerPage, SearchResultType.NewsFeedEntry);
    }

    public List<SearchResult> searchForTweets(String query, int hitsPerPage) {
        return search(query, hitsPerPage, SearchResultType.Tweet);
    }

    public List<SearchResult> searchForAll(String query, int hitsPerPage) {
        return search(query, hitsPerPage, SearchResultType.All);
    }

    private List<SearchResult> search(String queryAsString, int hitsPerPage, SearchResultType type) {
        logDebug("Seaching for " + queryAsString);
        List<SearchResult> contentResults = new LinkedList<>();

        DateFormat dateFormat = createDateTimeFormat();
        Analyzer queryAnalyzer = analyzer;

        try {
            switch (type) {
                case NewsFeedEntry:
                    queryAsString = "(" + queryAsString + ") AND type:n";
                    break;
                case Tweet:
                    if (queryAsString.startsWith("#")) {
                        queryAsString = "twitter-hashtags:" + queryAsString.substring(1) + " AND type:t";
                    } else if (queryAsString.startsWith("@")) {
                        queryAsString = "twitter-users:" + queryAsString.substring(1) + " AND type:t";
                    } else {
                        queryAsString = "(" + queryAsString + ") AND type:t";
                    }
                    break;
                case All:
                    break;
            }

            Query query = new QueryParser(Version.LUCENE_35, FULL_CONTENT, queryAnalyzer).parse(queryAsString);
            IndexReader reader = IndexReader.open(index);
            IndexSearcher searcher = new IndexSearcher(reader);

            ScoreDoc[] hits;
            switch (type) {
                case Tweet:
                case All:
                    Sort sort = new Sort(new SortField[] { new SortField(TIMESTAMP, SortField.STRING, true) });
                    hits = searcher.search(query, hitsPerPage, sort).scoreDocs;
                    break;
                default:
                    hits = searcher.search(query, hitsPerPage).scoreDocs;
                    break;
            }

            for (ScoreDoc hit : hits) {
                try {
                    int docId = hit.doc;
                    Document d = searcher.doc(docId);

                    SearchResult result = new SearchResult(SearchResultType.NewsFeedEntry);
                    String searchResultType = d.get(TYPE);
                    if (searchResultType.equals(TYPE_TWEET)) {
                        result = new SearchResult(SearchResultType.Tweet);
                    }

                    if (result.isNewsFeedEntry()) {
                        result.setTitle(d.get(TITLE));
                    }

                    result.setTruncatedBody(d.get(TRUNCATED_BODY));
                    result.setTimestamp(dateFormat.parse(d.get(DATE)));
                    result.setPermalink(d.get(PERMALINK));

                    ContentSource contentSource = contentSourceComponent.findById(Integer.parseInt(d.get(CONTENT_SOURCE_ID)));
                    if (contentSource == null) {
                        logWarn("Couldn't find content source with ID " + d.get(CONTENT_SOURCE_ID));
                    } else {
                        result.setContentSource(contentSource);
                        contentResults.add(result);
                    }
                } catch (Exception e) {
                    SearchException se = new SearchException("Ignoring search result", e);
                    logWarn(se);
                }
            }

            reader.close();
        } catch (Exception e) {
            SearchException se = new SearchException("Error searching", e);
            logError(se);
        }

        return contentResults;
    }

    @Override
    public void clearSearchIndex() {
        try {
            IndexWriter indexWriter = createIndexWriter();
            indexWriter.deleteAll();
            indexWriter.close();
        } catch (Exception e) {
            SearchException se = new SearchException("Error clearing search index", e);
            logError(se);
        }
    }

    private DateFormat createDateTimeFormat() {
        return new SimpleDateFormat("dd-MMM-yyyy HH:mm");
    }

    public static DateFormat createTimestampFormat() {
        return new SimpleDateFormat("yyyyMMddHHmm");
    }

}