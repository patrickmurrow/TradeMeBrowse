package com.example.patrickmurrow.trademebrowse.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by patrickmurrow on 14/11/17.
 */

public class Listing extends ListingItem {

    private static final String DESCRIPTION_JSON_KEY = "Body";
    private static final String REGION_JSON_KEY = "Region";
    private static final String PHOTOS_JSON_KEY = "Photos";
    private static final String PHOTO_PATHS_JSON_KEY = "Value";
    private static final String MEDIUM_PHOTO_JSON_KEY = "Medium";

    private List<String> photos = new ArrayList<>();
    private String description;
    private String region;

    public Listing(final JSONObject listingJson) throws JSONException {
        super(listingJson);

        description = listingJson.optString(DESCRIPTION_JSON_KEY);
        region = listingJson.optString(REGION_JSON_KEY);

        final JSONArray photosArray = listingJson.optJSONArray(PHOTOS_JSON_KEY);
        for (int i = 0; photosArray != null && i < photosArray.length(); i++) {
            final JSONObject photo = photosArray.getJSONObject(i);
            final JSONObject photoPaths = photo.getJSONObject(PHOTO_PATHS_JSON_KEY);
            final String path = photoPaths.getString(MEDIUM_PHOTO_JSON_KEY);
            photos.add(path);
        }
    }

    public List<String> getPhotos() {
        return photos;
    }

    public String getDescription() {
        return description;
    }

    public String getRegion() {
        return region;
    }
}
