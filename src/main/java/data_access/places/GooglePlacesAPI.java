package data_access.places;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * A simplified data access class for interacting with the Google Places API (v1).
 * Common request and parsing logic is extracted into reusable helper methods.
 */
public class GooglePlacesAPI {

    private static final String BASE_URL = "https://places.googleapis.com/v1";
    private final String apiKey;
    private final HttpClient client;

    public GooglePlacesAPI(String apiKey) {
        this.apiKey = apiKey;
        this.client = HttpClient.newHttpClient();
    }


    // Public Methods

    public List<HashMap<String, Object>> searchText(String query, String fieldMask)
            throws IOException, InterruptedException {
        if (isBlank(fieldMask)) {
            fieldMask = defaultSearchFieldMask();
        }

        JSONObject body = new JSONObject().put("textQuery", query);
        JSONObject json = sendPostRequest(body, fieldMask);
        return parsePlacesList(json.optJSONArray("places"));
    }

    public HashMap<String, Object> getPlaceDetails(String placeId, String fieldMask)
            throws IOException, InterruptedException {
        if (isBlank(fieldMask)) {
            fieldMask = defaultDetailsFieldMask();
        }

        String encodedPlaceId = URLEncoder.encode(placeId, StandardCharsets.UTF_8);
        JSONObject json = sendGetRequest("/places/" + encodedPlaceId, fieldMask);
        return parseSinglePlace(json);
    }


    // Private Helpers

    private JSONObject sendPostRequest(JSONObject body, String fieldMask)
            throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/places:searchText"))
                .header("Content-Type", "application/json")
                .header("X-Goog-Api-Key", apiKey)
                .header("X-Goog-FieldMask", fieldMask)
                .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                .build();
        return sendRequest(request);
    }

    private JSONObject sendGetRequest(String path, String fieldMask)
            throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .header("X-Goog-Api-Key", apiKey)
                .header("X-Goog-FieldMask", fieldMask)
                .GET()
                .build();
        return sendRequest(request);
    }

    private JSONObject sendRequest(HttpRequest request)
            throws IOException, InterruptedException {
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ensureSuccess(response);
        return new JSONObject(response.body());
    }

    private List<HashMap<String, Object>> parsePlacesList(JSONArray places) {
        List<HashMap<String, Object>> results = new ArrayList<>();
        if (places == null) return results;

        for (int i = 0; i < places.length(); i++) { results.add(parseSinglePlace(places.getJSONObject(i))); }
        return results;
    }

    private HashMap<String, Object> parseSinglePlace(JSONObject p) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", p.optString("id", null));
        map.put("name", p.optJSONObject("displayName") != null
                ? p.getJSONObject("displayName").optString("text", null)
                : null);
        map.put("formattedAddress", p.optString("formattedAddress", null));
        map.put("primaryType", p.optString("primaryType", null));
        map.put("rating", p.has("rating") ? p.optDouble("rating") : null);
        map.put("websiteUri", p.optString("websiteUri", null));
        map.put("priceLevel", p.optInt("priceLevel", -1));

        if (p.has("location")) {
            JSONObject location = p.getJSONObject("location");
            double lat = location.optDouble("latitude");
            double lng = location.optDouble("longitude");
            map.put("location", lat + "," + lng);  // store as String
        }
        return map;
    }

    private void ensureSuccess(HttpResponse<?> response) throws IOException {
        int code = response.statusCode();
        if (code < 200 || code >= 300) {
            throw new IOException("Places API error: HTTP " + code + " - " + response.body());
        }
    }

    private String defaultSearchFieldMask() {
        return "places.id,places.displayName,places.formattedAddress,places.primaryType," +
                "places.location,places.rating,places.websiteUri";
    }

    private String defaultDetailsFieldMask() {
        return "id,displayName,formattedAddress,websiteUri,internationalPhoneNumber,rating,location,primaryType";
    }

    private boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }
}
