package codetest.controller;

import codetest.exception.ResourceNotFoundException;
import codetest.service.UrlService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RedirectController.class)
public class RedirectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UrlService urlService;

    private final String originalUrl = "https://www.originenergy.com.au/electricity-gas/plans.html";
    private final String shortCode = "a1B2c3";

    @Test
    void testRedirectToOriginalUrl_Success() throws Exception {
        // Arrange
        when(urlService.getOriginalUrl(shortCode)).thenReturn(originalUrl);

        // Act & Assert
        mockMvc.perform(get("/{id}", shortCode))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(originalUrl));
    }

    @Test
    void testRedirectToOriginalUrl_NotFound() throws Exception {
        // Arrange
        when(urlService.getOriginalUrl(shortCode)).thenThrow(new ResourceNotFoundException("Short URL not found"));

        // Act & Assert
        mockMvc.perform(get("/{id}", shortCode))
                .andExpect(status().isNotFound());
    }
}