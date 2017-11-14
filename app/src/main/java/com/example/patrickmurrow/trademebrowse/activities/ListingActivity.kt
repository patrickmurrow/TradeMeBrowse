package com.example.patrickmurrow.trademebrowse.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.android.volley.Response
import com.daimajia.slider.library.SliderLayout
import com.daimajia.slider.library.SliderTypes.BaseSliderView
import com.daimajia.slider.library.SliderTypes.DefaultSliderView
import com.example.patrickmurrow.trademebrowse.R
import com.example.patrickmurrow.trademebrowse.helpers.UiHelper
import com.example.patrickmurrow.trademebrowse.helpers.NetworkHelper
import com.example.patrickmurrow.trademebrowse.model.Listing
import kotlinx.android.synthetic.main.activity_listing.*
import org.json.JSONObject

/**
 * Created by patrickmurrow on 14/11/17.
 */
class ListingActivity : AppCompatActivity() {

    companion object {
        val LISTING_ID_EXTRA = "listingId"
    }

    private val errorListener = Response.ErrorListener { error -> Log.d(packageName, "ERROR: " + error.toString()) }

    private var listingId = 0
    private lateinit var listing: Listing

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listing)
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24px)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        listingId = intent.getIntExtra(LISTING_ID_EXTRA, 0)
        if (savedInstanceState != null) {
            listingId = savedInstanceState.getInt(LISTING_ID_EXTRA)
        }

        getListingDetails()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getListingDetails() {
        val responseListener = Response.Listener<JSONObject> { response ->
            listing = Listing(response)

            setupViews()
        }

        NetworkHelper.requestListing(listingId, responseListener, errorListener)
    }

    private fun setupViews() {

        setupImageSlider()

        titleText.text = listing.title
        if (listing.subtitle.isEmpty()) {
            subtitleText.visibility = View.GONE
        } else {
            subtitleText.text = listing.subtitle
        }
        descriptionText.text = listing.description

        regionText.text = "Location: " + listing.region
        priceText.text = UiHelper.getPriceText(listing.price)
    }

    private fun setupImageSlider() {
        if (listing.photos.isEmpty()) {
            val sliderView = DefaultSliderView(this)
            sliderView.image(R.drawable.no_image_available).scaleType = BaseSliderView.ScaleType.CenterInside
            imageSlider.addSlider(sliderView)
        } else {
            for (photoPath in listing.photos) {
                val sliderView = DefaultSliderView(this)
                sliderView.image(photoPath).scaleType = BaseSliderView.ScaleType.CenterInside
                imageSlider.addSlider(sliderView)
            }
        }
        imageSlider.setPresetTransformer(SliderLayout.Transformer.Default)
        imageSlider.setCustomIndicator(pagerIndicator)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putInt(LISTING_ID_EXTRA, listingId)
        super.onSaveInstanceState(outState)
    }
}