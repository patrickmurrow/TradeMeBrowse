package com.example.patrickmurrow.trademebrowse.helpers;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.patrickmurrow.trademebrowse.Application;

/**
 * Helper class for network calls.
 *
 * Created by patrickmurrow on 9/11/17.
 */

public class NetworkHelper {
    private static RequestQueue MAIN_QUEUE = getQueue();

    private static final String CATEGORY_PATH = "https://api.tmsandbox.co.nz/v1/Categories/%s.json";
    private static final String LISTING_QUERY_PATH = "https://api.tmsandbox.co.nz/v1/Search/General.json?category=%s&search_string=%s";
    private static final String LISTING_PATH = "https://api.tmsandbox.co.nz/v1/Listings/%s.json";

    private static RequestQueue getQueue() {
        final Context context = Application.getContext();
        return Volley.newRequestQueue(context);
    }

    private static void request(final String url, final Response.Listener responseListener, final Response.ErrorListener errorListener) {
        final AuthJsonObjectRequest request = new AuthJsonObjectRequest(url, null, responseListener, errorListener);
        MAIN_QUEUE.add(request);
    }

    public static void requestCategories(final String categoryNumber, final Response.Listener responseListener, final Response.ErrorListener errorListener) {
        final String url = String.format(CATEGORY_PATH, categoryNumber);
        request(url, responseListener, errorListener);
    }

    public static void requestListingsBySearchWithCategory(final String categoryNumber, final String searchQuery, final Response.Listener responseListener, final Response.ErrorListener errorListener) {
        final String url = String.format(LISTING_QUERY_PATH, categoryNumber, searchQuery);
        request(url, responseListener, errorListener);
    }

    public static void requestListing(final Integer listingId, final Response.Listener responseListener, final Response.ErrorListener errorListener) {
        final String url = String.format(LISTING_PATH, listingId);
        request(url, responseListener, errorListener);
    }
}
