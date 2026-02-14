package fr.corpo.salle.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "app.documents")
public class DocumentStorageProperties {

    /**
     * Dossier externe qui contient les PDFs (prod-ready)
     */
    private String storageDir;

    /**
     * Allowlist de hosts autorisés pour proxifier des URLs (anti-SSRF)
     */
    private List<String> allowedUrlHosts = List.of();

    public String getStorageDir() { return storageDir; }
    public void setStorageDir(String storageDir) { this.storageDir = storageDir; }

    public List<String> getAllowedUrlHosts() { return allowedUrlHosts; }
    public void setAllowedUrlHosts(List<String> allowedUrlHosts) { this.allowedUrlHosts = allowedUrlHosts; }
}
