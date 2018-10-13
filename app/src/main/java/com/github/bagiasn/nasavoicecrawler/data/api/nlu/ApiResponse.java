package com.github.bagiasn.nasavoicecrawler.data.api.nlu;

import com.squareup.moshi.Json;

import java.util.List;

/**
 * This class represents the API response to {@link ApiRequest}.
 */
public class ApiResponse {
    private String error;
    private Image image;
    private Speak speak;
    private Graph graph;

    public String getError() {
        return error;
    }

    public String getImageUrl() {
        return image.getUrl();
    }

    public String getImageDescription() {
        return image.getDescription();
    }

    public String getTextToSpeak() {
        return speak.getText();
    }

    public String getTextLanguage() {
        return speak.getLanguage();
    }

    public String getGraphTitle() {
        return graph.getTitle();
    }

    public List<Double> getGraphValues() {
        return graph.getValues();
    }

    /**
     * Nested class representing image information.
     */
    private static class Image {
        @Json(name = "title")
        private String description;
        private String url;

        private String getUrl() {
            return url;
        }

        private String getDescription() {
            return description;
        }
    }

    /**
     * Nested class representing tts information.
     */
    private static class Speak {
        @Json(name = "title")
        private String text;
        private String language;

        private String getText() {
            return text;
        }

        private String getLanguage() {
            return language;
        }
    }

    /**
     * Nested class representing graph information.
     */
    private static class Graph {
        private String title;
        private List<Double> values;

        private String getTitle() {
            return title;
        }

        private List<Double> getValues() {
            return values;
        }
    }
}
