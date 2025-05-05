package codetest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Url {
    private String id;
    private String originalUrl;
    private String shortUrl;
    private LocalDateTime createdAt;
    private int accessCount;

    public Url(String id, String originalUrl, String shortUrl) {
        this.id = id;
        this.originalUrl = originalUrl;
        this.shortUrl = shortUrl;
        this.createdAt = LocalDateTime.now();
        this.accessCount = 0;
    }

    public void incrementAccessCount() {
        this.accessCount++;
    }
}