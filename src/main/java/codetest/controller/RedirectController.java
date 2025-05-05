package codetest.controller;

import codetest.service.UrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequiredArgsConstructor
public class RedirectController {

    private final UrlService urlService;

    @GetMapping("/{id}")
    public RedirectView redirectToOriginalUrl(@PathVariable String id) {
        String originalUrl = urlService.getOriginalUrl(id);
        return new RedirectView(originalUrl);
    }
}