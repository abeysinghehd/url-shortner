package codetest.service;

import codetest.dto.UrlInfoResponseDto;
import codetest.dto.UrlRequestDto;
import codetest.dto.UrlResponseDto;
import codetest.exception.BadRequestException;
import codetest.exception.ResourceNotFoundException;
import codetest.model.Url;
import codetest.repository.UrlRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UrlService {

    private final UrlRepository urlRepository;
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final Base64.Encoder ENCODER = Base64.getUrlEncoder().withoutPadding();

    @Value("${app.base.url:http://short.ly/}")
    private String baseUrl;

    public UrlResponseDto shortenUrl(UrlRequestDto urlRequestDto) {
        String originalUrl = urlRequestDto.getOriginalUrl();

        // Validate URL format
        validateUrl(originalUrl);

        // Check if URL is already shortened
        Optional<Url> existingUrl = urlRepository.findByOriginalUrl(originalUrl);
        if (existingUrl.isPresent()) {
            Url url = existingUrl.get();
            return new UrlResponseDto(url.getOriginalUrl(), url.getShortUrl());
        }

        // Generate a unique short code
        String id = generateUniqueId();
        String shortUrl = baseUrl + id;

        // Save the new URL
        Url url = new Url(id, originalUrl, shortUrl);
        urlRepository.save(url);

        return new UrlResponseDto(url.getOriginalUrl(), url.getShortUrl());
    }

    public String getOriginalUrl(String id) {
        Url url = urlRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Short URL not found"));
        return url.getOriginalUrl();
    }

    public UrlInfoResponseDto getUrlInfo(String id) {
        Url url = urlRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Short URL not found"));

        return new UrlInfoResponseDto(
                url.getOriginalUrl(),
                url.getShortUrl(),
                url.getCreatedAt()
        );
    }

    private String generateUniqueId() {
        byte[] randomBytes = new byte[6];
        RANDOM.nextBytes(randomBytes);
        String id = ENCODER.encodeToString(randomBytes);

        if (urlRepository.findById(id).isPresent()) {
            return generateUniqueId();
        }

        return id;
    }

    private void validateUrl(String url) {
        UrlValidator urlValidator = new UrlValidator(new String[]{"http", "https"});
        if (!urlValidator.isValid(url)) {
            throw new BadRequestException("Invalid URL format");
        }
    }
}