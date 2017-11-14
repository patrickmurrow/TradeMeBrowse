package com.example.patrickmurrow.trademebrowse.helpers;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.example.patrickmurrow.trademebrowse.R;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by patrickmurrow on 13/11/17.
 */

public class UiHelper {

    /**
     * Converts a given density independent pixel value to a float value, so dp can be
     * used dynamically.
     *
     * @param context
     * @param dipValue
     * @return
     */
    public static float getFloatPixelsFromDp(@NotNull Context context, float dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }

    /**
     * Converts a double to a price string with 2 decimal places.
     *
     * @param price
     * @return price display string
     */
    public static String getPriceText(final Double price) {
        final Double roundedPrice = round(price, 2);
        final String roundedPriceString = roundedPrice.toString();
        final Integer decimalCount = roundedPriceString.substring(roundedPriceString.indexOf(".") + 1).length();
        return "$" + roundedPriceString + (decimalCount == 1 ? "0" : "");
    }

    /**
     * Rounds a value to given amount of places.
     *
     * @param value
     * @param places
     * @return
     */
    private static Double round(double value, int places) {
        BigDecimal bigDecimalValue = new BigDecimal(value);
        bigDecimalValue = bigDecimalValue.setScale(places, RoundingMode.HALF_UP);
        return bigDecimalValue.doubleValue();
    }

    /**
     * Purely for aesthetics. Maps root categories' ids to images.
     *
     * @param categoryNumber
     * @return
     */
    public static int getCategoryDrawable(@NotNull String categoryNumber) {
        switch (categoryNumber) {
            case "0001-":
                return R.drawable.motors;
            case "0350-":
                return R.drawable.property;
            case "5000-":
                return R.drawable.jobs;
            case "0187-":
                return R.drawable.antiques;
            case "0339-":
                return R.drawable.art;
            case "0351-":
                return R.drawable.baby;
            case "0193-":
                return R.drawable.books;
            case "5964-":
                return R.drawable.building;
            case "0010-":
                return R.drawable.business;
            case "0153-":
                return R.drawable.clothing;
            case "0002-":
                return R.drawable.computers;
            case "0341-":
                return R.drawable.crafts;
            default:
                return R.drawable.default_icon;

        }
    }
}
