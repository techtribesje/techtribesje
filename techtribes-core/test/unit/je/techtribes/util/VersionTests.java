package je.techtribes.util;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class VersionTests {

    @Test
    public void testVersionInformationIsLoadable() {
        assertNotNull(Version.getBuildNumber());
        assertNotNull(Version.getBuildTimestamp());
        assertNotNull(Version.getGitCommit());
   }

}