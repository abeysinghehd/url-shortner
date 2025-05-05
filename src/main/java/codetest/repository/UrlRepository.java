package codetest.repository;

import codetest.model.Url;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class UrlRepository {

    // In-memory store using ConcurrentHashMap for thread safety
    private final Map<String, Url> urlStore = new ConcurrentHashMap<>();

    public Url save(Url url) {
        urlStore.put(url.getId(), url);
        return url;
    }

    public Optional<Url> findById(String id) {
        return Optional.ofNullable(urlStore.get(id));
    }

    public Optional<Url> findByOriginalUrl(String originalUrl) {
        return urlStore.values().stream()
                .filter(url -> url.getOriginalUrl().equals(originalUrl))
                .findFirst();
    }

    public void incrementAccessCount(String id) {
        urlStore.computeIfPresent(id, (key, url) -> {
            url.incrementAccessCount();
            return url;
        });
    }
}