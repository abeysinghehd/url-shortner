package codetest.controller;

import codetest.dto.UrlInfoResponseDto;
import codetest.dto.UrlRequestDto;
import codetest.dto.UrlResponseDto;
import codetest.service.UrlService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UrlController {

    private final UrlService urlService;

    @PostMapping("/shorten")
    public ResponseEntity<UrlResponseDto> shortenUrl(@Valid @RequestBody UrlRequestDto urlRequestDto) {
        UrlResponseDto urlResponseDto = urlService.shortenUrl(urlRequestDto);
        return new ResponseEntity<>(urlResponseDto, HttpStatus.CREATED);
    }

    @GetMapping("/info/{id}")
    public ResponseEntity<UrlInfoResponseDto> getUrlInfo(@PathVariable String id) {
        UrlInfoResponseDto urlInfo = urlService.getUrlInfo(id);
        return ResponseEntity.ok(urlInfo);
    }
}
