package codetest.controller;

import codetest.dto.UrlInfoResponseDto;
import codetest.dto.UrlRequestDto;
import codetest.dto.UrlResponseDto;
import codetest.exception.BadRequestException;
import codetest.exception.ResourceNotFoundException;
import codetest.service.UrlService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UrlController.class)
public class UrlControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UrlService urlService;

    private final String originalUrl = "https://www.originenergy.com.au/electricity-gas/plans.html";
    private final String shortUrl = "http://short.ly/a1B2c3";
    private final String shortCode = "a1B2c3";

    @Test
    void testShortenUrl_Success() throws Exception {
        // Arrange
        UrlRequestDto requestDto = new UrlRequestDto(originalUrl);
        UrlResponseDto responseDto = new UrlResponseDto(originalUrl, shortUrl);

        when(urlService.shortenUrl(any(UrlRequestDto.class))).thenReturn(responseDto);

        // Act & Assert
        mockMvc.perform(post("/api/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.originalUrl").value(originalUrl))
                .andExpect(jsonPath("$.shortUrl").value(shortUrl));
    }

    @Test
    void testShortenUrl_InvalidUrl() throws Exception {
        // Arrange
        UrlRequestDto requestDto = new UrlRequestDto("invalidUrl");

        when(urlService.shortenUrl(any(UrlRequestDto.class))).thenThrow(new BadRequestException("Invalid URL format"));

        // Act & Assert
        mockMvc.perform(post("/api/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testShortenUrl_EmptyUrl() throws Exception {
        // Arrange
        UrlRequestDto requestDto = new UrlRequestDto("");

        // Act & Assert
        mockMvc.perform(post("/api/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetUrlInfo_Success() throws Exception {
        // Arrange
        LocalDateTime createdAt = LocalDateTime.now();
        UrlInfoResponseDto responseDto = new UrlInfoResponseDto(originalUrl, shortUrl, createdAt);

        when(urlService.getUrlInfo(shortCode)).thenReturn(responseDto);

        // Act & Assert
        mockMvc.perform(get("/api/info/{id}", shortCode))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.originalUrl").value(originalUrl))
                .andExpect(jsonPath("$.shortUrl").value(shortUrl));
    }

    @Test
    void testGetUrlInfo_NotFound() throws Exception {
        // Arrange
        when(urlService.getUrlInfo(shortCode)).thenThrow(new ResourceNotFoundException("Short URL not found"));

        // Act & Assert
        mockMvc.perform(get("/api/info/{id}", shortCode))
                .andExpect(status().isNotFound());
    }
}