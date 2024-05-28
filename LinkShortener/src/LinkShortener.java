import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class LinkShortener {
    private Map<String, String> urlMap;  // Maps short URLs to long URLs
    private Map<String, String> reverseUrlMap;  // Maps long URLs to short URLs
    private Map<String, Integer> accessCountMap;  // Tracks access counts for short URLs
    private static final String BASE_URL = "http://short.url/";
    private static final String CHAR_SET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int SHORT_URL_LENGTH = 6;
    private static final String STORAGE_FILE = "urlMappings.txt";

    public LinkShortener() {
        urlMap = new HashMap<>();
        reverseUrlMap = new HashMap<>();
        accessCountMap = new HashMap<>();
        loadMappings();
    }

    // Shortens the given long URL, with optional custom alias
    public String shortenURL(String longURL, String customAlias) {
        if (!isValidURL(longURL)) {
            return "Error: Invalid URL.";
        }

        if (customAlias != null && !customAlias.isEmpty()) {
            if (urlMap.containsKey(customAlias)) {
                return "Error: Custom alias already in use.";
            }
            urlMap.put(customAlias, longURL);
            reverseUrlMap.put(longURL, customAlias);
            accessCountMap.put(customAlias, 0);
            saveMappings();
            return BASE_URL + customAlias;
        }

        if (reverseUrlMap.containsKey(longURL)) {
            return BASE_URL + reverseUrlMap.get(longURL);
        }

        String shortURL = generateShortURL();
        while (urlMap.containsKey(shortURL)) {
            shortURL = generateShortURL();
        }

        urlMap.put(shortURL, longURL);
        reverseUrlMap.put(longURL, shortURL);
        accessCountMap.put(shortURL, 0);
        saveMappings();

        return BASE_URL + shortURL;
    }

    // Expands the given short URL to its original long URL
    public String expandURL(String shortURL) {
        String key = shortURL.replace(BASE_URL, "");
        if (urlMap.containsKey(key)) {
            accessCountMap.put(key, accessCountMap.get(key) + 1);
            saveMappings();
            return urlMap.get(key);
        }
        return "Error: Short URL does not exist.";
    }

    // Returns the access count for the given short URL
    public int getAccessCount(String shortURL) {
        String key = shortURL.replace(BASE_URL, "");
        return accessCountMap.getOrDefault(key, 0);
    }

    // Generates a random short URL
    private String generateShortURL() {
        StringBuilder shortURL = new StringBuilder();
        for (int i = 0; i < SHORT_URL_LENGTH; i++) {
            int index = (int) (Math.random() * CHAR_SET.length());
            shortURL.append(CHAR_SET.charAt(index));
        }
        return shortURL.toString();
    }

    // Saves the mappings to a file for persistence
    private void saveMappings() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(STORAGE_FILE))) {
            for (String key : urlMap.keySet()) {
                writer.println(key + "," + urlMap.get(key) + "," + accessCountMap.get(key));
            }
        } catch (IOException e) {
            System.out.println("Error saving mappings: " + e.getMessage());
        }
    }

    // Loads the mappings from a file
    private void loadMappings() {
        File file = new File(STORAGE_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(STORAGE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String shortURL = parts[0];
                    String longURL = parts[1];
                    int accessCount = Integer.parseInt(parts[2]);
                    urlMap.put(shortURL, longURL);
                    reverseUrlMap.put(longURL, shortURL);
                    accessCountMap.put(shortURL, accessCount);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading mappings: " + e.getMessage());
        }
    }

    // Validates the given URL
    private boolean isValidURL(String url) {
        return url.startsWith("http://") || url.startsWith("https://");
    }
}
