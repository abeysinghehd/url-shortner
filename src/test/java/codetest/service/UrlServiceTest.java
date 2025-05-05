package codetest.service;

import codetest.dto.UrlInfoResponseDto;
import codetest.dto.UrlRequestDto;
import codetest.dto.UrlResponseDto;
import codetest.exception.BadRequestException;
import codetest.exception.ResourceNotFoundException;
import codetest.model.Url;
import codetest.repository.UrlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UrlServiceTest {

    @Mock
    private UrlRepository urlRepository;

    @InjectMocks
    private UrlService urlService;

    private final String baseUrl = "http://short.ly/";
    private final String originalUrl = "https://www.originenergy.com.au/electricity-gas/plans.html";
    private final String shortCode = "a1B2c3";
    private final String shortUrl = baseUrl + shortCode;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(urlService, "baseUrl", baseUrl);
    }

    @Test
    void testShortenUrl_Success() {
        // Arrange
        UrlRequestDto urlRequestDto = new UrlRequestDto(originalUrl);
        Url url = new Url(shortCode, originalUrl, shortUrl);

        when(urlRepository.findByOriginalUrl(originalUrl)).thenReturn(Optional.empty());
        when(urlRepository.save(any(Url.class))).thenReturn(url);

        // Mock the random ID generation
        doReturn(Optional.empty()).when(urlRepository).findById(any());

        // Act
        UrlResponseDto result = urlService.shortenUrl(urlRequestDto);

        // Assert
        assertNotNull(result);
        assertEquals(originalUrl, result.getOriginalUrl());
        assertTrue(result.getShortUrl().startsWith(baseUrl));
        verify(urlRepository).save(any(Url.class));
    }

    @Test
    void testShortenUrl_AlreadyExists() {
        // Arrange
        UrlRequestDto urlRequestDto = new UrlRequestDto(originalUrl);
        Url existingUrl = new Url(shortCode, originalUrl, shortUrl);

        when(urlRepository.findByOriginalUrl(originalUrl)).thenReturn(Optional.of(existingUrl));

        // Act
        UrlResponseDto result = urlService.shortenUrl(urlRequestDto);

        // Assert
        assertNotNull(result);
        assertEquals(originalUrl, result.getOriginalUrl());
        assertEquals(shortUrl, result.getShortUrl());
        verify(urlRepository, never()).save(any(Url.class));
    }

    @Test
    void testShortenUrl_InvalidUrl() {
        // Arrange
        UrlRequestDto urlRequestDto = new UrlRequestDto("invalidUrl");

        // Act & Assert
        assertThrows(BadRequestException.class, () -> urlService.shortenUrl(urlRequestDto));
        verify(urlRepository, never()).save(any(Url.class));
    }

    @Test
    void testGetOriginalUrl_Success() {
        // Arrange
        Url url = new Url(shortCode, originalUrl, shortUrl);

        when(urlRepository.findById(shortCode)).thenReturn(Optional.of(url));

        // Act
        String result = urlService.getOriginalUrl(shortCode);

        // Assert
        assertEquals(originalUrl, result);
    }

    @Test
    void testGetOriginalUrl_NotFound() {
        // Arrange
        when(urlRepository.findById(shortCode)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> urlService.getOriginalUrl(shortCode));
    }

    @Test
    void testGetUrlInfo_Success() {
        // Arrange
        LocalDateTime createdAt = LocalDateTime.now();
        Url url = new Url(shortCode, originalUrl, shortUrl);
        url.setCreatedAt(createdAt);
        url.setAccessCount(5);

        when(urlRepository.findById(shortCode)).thenReturn(Optional.of(url));

        // Act
        UrlInfoResponseDto result = urlService.getUrlInfo(shortCode);

        // Assert
        assertNotNull(result);
        assertEquals(originalUrl, result.getOriginalUrl());
        assertEquals(shortUrl, result.getShortUrl());
        assertEquals(createdAt, result.getCreatedAt());
    }

    @Test
    void testGetUrlInfo_NotFound() {
        // Arrange
        when(urlRepository.findById(shortCode)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> urlService.getUrlInfo(shortCode));
    }
}