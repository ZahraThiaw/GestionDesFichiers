package sn.zahra.thiaw.gestiondesfichiers.Configs;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.ArrayList;

@Configuration
@ConfigurationProperties(prefix = "file.upload")
public class FileStorageConfig {
    private List<String> allowedContentTypes = new ArrayList<>();
    private long maxFileSize = 10_000_000; // Default 10MB

    public List<String> getAllowedContentTypes() {
        return allowedContentTypes;
    }

    public void setAllowedContentTypes(List<String> allowedContentTypes) {
        this.allowedContentTypes = allowedContentTypes;
    }

    public long getMaxFileSize() {
        return maxFileSize;
    }

    public void setMaxFileSize(long maxFileSize) {
        this.maxFileSize = maxFileSize;
    }
}