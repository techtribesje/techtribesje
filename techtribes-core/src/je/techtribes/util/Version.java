package je.techtribes.util;

import je.techtribes.component.log.LoggingComponentFactory;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class Version {

    private static final String BUILD_VERSION_KEY = "build.number";
    private static final String BUILD_TIMESTAMP_KEY = "build.timestamp";
    private static final String GIT_COMMIT_KEY = "git.commit";

    private static String version;
    private static Date buildTimestamp;
    private static String gitCommit;

    static {
        try {
            Properties buildProperties = new Properties();
            InputStream in = Version.class.getClassLoader().getResourceAsStream("build.properties");
            DateFormat format = new SimpleDateFormat("dd MMM yyyy HH:mm");
            if (in != null) {
                buildProperties.load(in);
                version = buildProperties.getProperty(BUILD_VERSION_KEY);
                buildTimestamp = format.parse(buildProperties.getProperty(BUILD_TIMESTAMP_KEY));
                gitCommit = buildProperties.getProperty(GIT_COMMIT_KEY);
                in.close();
            }
        } catch (Exception e) {
            LoggingComponentFactory.create().error(new Version(), "Could not load build information", e);
        }
    }

    public static String getBuildNumber() {
        return version;
    }

    public static Date getBuildTimestamp() {
        return buildTimestamp;
    }

    public static String getGitCommit() {
        return gitCommit;
    }

}
