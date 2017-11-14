package com.example.patrickmurrow.trademebrowse.model;

import com.example.patrickmurrow.trademebrowse.helpers.UiHelper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by patrickmurrow on 9/11/17.
 */

public class Category {

    private static final String NAME_JSON_KEY = "Name";
    private static final String NUMBER_JSON_KEY = "Number";
    private static final String PATH_JSON_KEY = "Path";
    private static final String COUNT_JSON_KEY = "Count";
    public static final String SUBCATEGORIES_JSON_KEY = "Subcategories";

    private String name;
    private String number;
    private String path;
    private Integer count;
    private boolean hasSubcategories;

    public Category(final JSONObject categoryJson) throws JSONException {
        name = categoryJson.getString(NAME_JSON_KEY);
        number = categoryJson.getString(NUMBER_JSON_KEY);
        path = categoryJson.getString(PATH_JSON_KEY);
        count = categoryJson.optInt(COUNT_JSON_KEY);
        hasSubcategories = !categoryJson.isNull(SUBCATEGORIES_JSON_KEY);
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public boolean hasSubcategories() {
        return hasSubcategories;
    }

    public int getDrawable() {
        return UiHelper.getCategoryDrawable(number);
    }
}
