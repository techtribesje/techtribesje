package je.techtribes.domain;

import org.junit.Test;

import static org.junit.Assert.*;

public class VideoTests {

    private Video video;

    @Test
    public void testIsYouTube_ReturnsFalse_WhenUrlIsNull() {
        video = new Video(null);
        assertFalse(video.isYouTube());
    }

    @Test
    public void test_IsYouTube_ReturnsFalse_WhenVideoIsntAYouTubeVideo() {
        video = new Video("http://vimeo.com/123456789");
        assertFalse(video.isYouTube());
    }

    @Test
    public void test_IsYouTube_ReturnsTrue_WhenVideoIsAYouTubeVideo() {
        video = new Video("http://youtube.com/watch?v=DUAWAfHnx9Y");
        assertTrue(video.isYouTube());
    }

    @Test
    public void test_GetYouTubeId_ReturnsTheYouTubeId_WhenVideoIsAYouTubeVideo() {
        video = new Video("http://youtube.com/watch?v=DUAWAfHnx9Y");
        assertEquals("DUAWAfHnx9Y", video.getYouTubeId());
    }

    @Test
    public void test_GetYouTubeId_ReturnsNull_WhenVideoIsNotAYouTubeVideo() {
        video = new Video("http://vimeo.com/123456789");
        assertNull(video.getYouTubeId());
    }

}
