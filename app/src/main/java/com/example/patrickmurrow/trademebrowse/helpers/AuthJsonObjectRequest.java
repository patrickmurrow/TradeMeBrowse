package com.example.patrickmurrow.trademebrowse.helpers;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by patrickmurrow on 10/11/17.
 */

public class AuthJsonObjectRequest extends JsonObjectRequest {

    private static final String OAUTH_TOKEN = "2F528F0A07E1932275413A19D41CA932";
    private static final String OAUTH_SECRET = "42A32713AFD77FA8FE8159C353E3FCDE";
    private static final String CONSUMER_KEY = "A1AC63F0332A131A78FAC304D007E7D1";
    private static final String CONSUMER_SECRET = "EC7F18B17A062962C6930A8AE88B16C7";

    public AuthJsonObjectRequest(final String url, final JSONObject jsonRequest, final Response.Listener<JSONObject> listener, final Response.ErrorListener errorListener) {
        super(url, jsonRequest, listener, errorListener);
    }

    /**
     * Uses hardcoded oauth keys and secrets.
     *
     * @return request headers
     * @throws AuthFailureError
     */
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        final Map<String, String> headers = new HashMap<>();
        final String uid = UUID.randomUUID().toString().substring(0, 6);

        headers.put("Authorization", "OAuth " + "oauth_consumer_key=" + CONSUMER_KEY +
                ", oauth_token=" + OAUTH_TOKEN +
                ", oauth_version=1.0" +
                ", oauth_timestamp=" + System.currentTimeMillis() +
                ", oauth_nonce=" + uid +
                ", oauth_signature_method=PLAINTEXT" +
                ", oauth_signature=" + CONSUMER_SECRET + "%26" + OAUTH_SECRET);
        return headers;
    }
}
