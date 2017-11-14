package com.example.patrickmurrow.trademebrowse.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by patrickmurrow on 10/11/17.
 */

public class ListingItem {

    private static final String ID_JSON_KEY = "ListingId";
    private static final String TITLE_JSON_KEY = "Title";
    private static final String SUBTITLE_JSON_KEY = "Subtitle";
    private static final String PICTURE_HREF_JSON_KEY = "PictureHref";
    private static final String START_PRICE_JSON_KEY = "StartPrice";
    private static final String BUY_NOW_JSON_KEY = "BuyNowPrice";

    private Integer id;
    private String title;
    private String subtitle;
    private String pictureHref;
    private Double startPrice;
    private Double buyNowPrice;


    public ListingItem(final JSONObject listingJson) throws JSONException {
        id = listingJson.getInt(ID_JSON_KEY);
        title = listingJson.getString(TITLE_JSON_KEY);
        subtitle = listingJson.optString(SUBTITLE_JSON_KEY);
        pictureHref = listingJson.optString(PICTURE_HREF_JSON_KEY);
        startPrice = listingJson.optDouble(START_PRICE_JSON_KEY);
        buyNowPrice = listingJson.optDouble(BUY_NOW_JSON_KEY);
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getPictureHref() {
        return pictureHref;
    }

    public Double getPrice() {
        return startPrice;
    }

    public Double getBuyNowPrice() {
        return buyNowPrice;
    }
}
