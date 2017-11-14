package com.example.patrickmurrow.trademebrowse.fragments

import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.android.volley.Response
import com.example.patrickmurrow.trademebrowse.R
import com.example.patrickmurrow.trademebrowse.activities.ListingActivity
import com.example.patrickmurrow.trademebrowse.adapters.ListingAdapter
import com.example.patrickmurrow.trademebrowse.helpers.NetworkHelper
import com.example.patrickmurrow.trademebrowse.model.ListingItem
import kotlinx.android.synthetic.main.fragment_listings.*
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by patrickmurrow on 10/11/17.
 */
class ListingsFragment : Fragment() {

    private val LISTINGS_JSON_KEY = "List"

    private val errorListener = Response.ErrorListener { error -> Log.d(activity.packageName, "ERROR: " + error.toString()) }

    private var listingAdapter = ListingAdapter()
    private val listings = ArrayList<ListingItem>()
    private var categoryNumber = "0"
    private var searchQuery = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_listings, container, false)
    }

    fun updateListings(categoryNumber: String, searchQuery: String) {
        showProgress()
        listings.clear()

        this.categoryNumber = categoryNumber
        this.searchQuery = searchQuery
        getListings()
    }

    fun showProgress() {
        listingsProgress.visibility = View.VISIBLE
        listingsGrid.visibility = View.GONE
    }

    fun hideProgress() {
        listingsGrid.visibility = View.VISIBLE
        listingsProgress.visibility = View.GONE
    }

    private fun getListings() {
        val responseListener = Response.Listener<JSONObject> { response ->
            try {
                val listingsJson = response.optJSONArray(LISTINGS_JSON_KEY)

                if (!response.isNull(LISTINGS_JSON_KEY) && listingsJson.length() > 0) {
                    (0 until listingsJson.length()).mapTo(listings) {
                        ListingItem(listingsJson.getJSONObject(it))
                    }
                }

                updateViews()
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

        NetworkHelper.requestListingsBySearchWithCategory(searchQuery, categoryNumber, responseListener, errorListener)
    }

    private fun updateViews() {
        hideProgress()

        val slideUp = AnimationUtils.loadAnimation(activity, R.anim.slide_up_in_anim)
        listingsGrid.startAnimation(slideUp)

        listingAdapter = ListingAdapter(activity, listings.toTypedArray())
        listingsGrid.layoutManager = LinearLayoutManager(activity)

        val itemClickListener = ListingAdapter.ItemClickListener { _, position ->
            listingClick(listingAdapter, position)
        }

        listingAdapter.setClickListener(itemClickListener)
        listingsGrid.adapter = listingAdapter

        if (listings.isEmpty()) {
            emptyView.visibility = View.VISIBLE
            listingsGrid.visibility = View.GONE
        } else {
            emptyView.visibility = View.GONE
            listingsGrid.visibility = View.VISIBLE
        }
    }

    private fun listingClick(listingAdapter: ListingAdapter, position: Int) {
        val listing = listingAdapter.getItem(position)
        val intent = Intent(activity, ListingActivity::class.java)
        intent.putExtra(ListingActivity.LISTING_ID_EXTRA, listing.id)
        startActivity(intent)
    }
}