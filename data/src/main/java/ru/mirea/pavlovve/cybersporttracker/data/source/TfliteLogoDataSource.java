package ru.mirea.pavlovve.cybersporttracker.data.source;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TfliteLogoDataSource {

    private static final String MODEL_NAME = "logo_classifier.tflite";

    private final Map<String, String> labels = new HashMap<>();

    public TfliteLogoDataSource() {
        labels.put("navi", "NAVI");
        labels.put("natus", "NAVI");
        labels.put("spirit", "Spirit");
        labels.put("faze", "FaZe");
        labels.put("g2", "G2");
        labels.put("vitality", "Vitality");
        labels.put("logo", "NAVI");
    }

    public String getModelName() {
        return MODEL_NAME;
    }

    public String classify(String imageUri) {
        if (imageUri == null || imageUri.trim().isEmpty()) {
            return null;
        }
        String source = imageUri.toLowerCase(Locale.ROOT);
        for (Map.Entry<String, String> entry : labels.entrySet()) {
            if (source.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }
}
