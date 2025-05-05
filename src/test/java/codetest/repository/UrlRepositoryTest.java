package codetest.repository;

import codetest.model.Url;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UrlRepositoryTest {

    private UrlRepository urlRepository;
    private final String id = "abc123";
    private final String originalUrl = "https://www.example.com";
    private final String shortUrl = "http://short.ly/" + id;

    @BeforeEach
    void setUp() {
        urlRepository = new UrlRepository();
    }

    @Test
    void testSave_Success() {
        // Arrange
        Url url = new Url(id, originalUrl, shortUrl);

        // Act
        Url savedUrl = urlRepository.save(url);

        // Assert
        assertEquals(url, savedUrl);
        Optional<Url> retrievedUrl = urlRepository.findById(id);
        assertTrue(retrievedUrl.isPresent());
        assertEquals(url, retrievedUrl.get());
    }

    @Test
    void testFindById_WhenExists() {
        // Arrange
        Url url = new Url(id, originalUrl, shortUrl);
        urlRepository.save(url);

        // Act
        Optional<Url> result = urlRepository.findById(id);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(url, result.get());
    }

    @Test
    void testFindById_WhenNotExists() {
        // Act
        Optional<Url> result = urlRepository.findById("nonExistentId");

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void testFindByOriginalUrl_WhenExists() {
        // Arrange
        Url url = new Url(id, originalUrl, shortUrl);
        urlRepository.save(url);

        // Act
        Optional<Url> result = urlRepository.findByOriginalUrl(originalUrl);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(url, result.get());
    }

    @Test
    void testFindByOriginalUrl_WhenNotExists() {
        // Act
        Optional<Url> result = urlRepository.findByOriginalUrl("https://nonexistent.com");

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void testMultipleUrlsWithDifferentIds() {
        // Arrange
        Url url1 = new Url("id1", "https://example1.com", "http://short.ly/id1");
        Url url2 = new Url("id2", "https://example2.com", "http://short.ly/id2");

        // Act
        urlRepository.save(url1);
        urlRepository.save(url2);

        // Assert
        Optional<Url> retrieved1 = urlRepository.findById("id1");
        Optional<Url> retrieved2 = urlRepository.findById("id2");

        assertTrue(retrieved1.isPresent());
        assertTrue(retrieved2.isPresent());
        assertEquals(url1, retrieved1.get());
        assertEquals(url2, retrieved2.get());
    }

    @Test
    void testUpdateExistingUrl() {
        // Arrange
        Url url = new Url(id, originalUrl, shortUrl);
        urlRepository.save(url);

        // Act - modify and save again
        url.setAccessCount(10);
        LocalDateTime newTime = LocalDateTime.now().plusDays(1);
        url.setCreatedAt(newTime);
        urlRepository.save(url);

        // Assert
        Optional<Url> retrievedUrl = urlRepository.findById(id);
        assertTrue(retrievedUrl.isPresent());
        assertEquals(10, retrievedUrl.get().getAccessCount());
        assertEquals(newTime, retrievedUrl.get().getCreatedAt());
    }
}