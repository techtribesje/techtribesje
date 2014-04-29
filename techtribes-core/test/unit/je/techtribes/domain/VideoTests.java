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

    @Test
    public void testIsUStream_ReturnsFalse_WhenUrlIsNull() {
        video = new Video(null);
        assertFalse(video.isUStream());
    }

    @Test
    public void test_IsUStream_ReturnsFalse_WhenVideoIsntAUStreamVideo() {
        video = new Video("http://vimeo.com/123456789");
        assertFalse(video.isUStream());
    }

    @Test
    public void test_IsUStream_ReturnsTrue_WhenVideoIsAUStreamVideo() {
        video = new Video("http://www.ustream.tv/recorded/46743935");
        assertTrue(video.isUStream());
    }

    @Test
    public void test_GetUStreamId_ReturnsTheUStreamId_WhenVideoIsAUStreamVideo() {
        video = new Video("http://www.ustream.tv/recorded/46743935");
        assertEquals("46743935", video.getUStreamId());
    }

    @Test
    public void test_GetUStreamId_ReturnsNull_WhenVideoIsNotAUStreamVideo() {
        video = new Video("http://vimeo.com/123456789");
        assertNull(video.getYouTubeId());
    }

}
