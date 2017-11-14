package com.example.patrickmurrow.trademebrowse.activities

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.HorizontalScrollView
import com.android.volley.Response
import com.example.patrickmurrow.trademebrowse.R
import com.example.patrickmurrow.trademebrowse.adapters.CategoryAdapter
import com.example.patrickmurrow.trademebrowse.adapters.SubcategoryAdapter
import com.example.patrickmurrow.trademebrowse.fragments.ListingsFragment
import com.example.patrickmurrow.trademebrowse.helpers.UiHelper
import com.example.patrickmurrow.trademebrowse.helpers.NetworkHelper
import com.example.patrickmurrow.trademebrowse.model.Category
import kotlinx.android.synthetic.main.activity_categories.*
import kotlinx.android.synthetic.main.layout_search_selection.*
import org.json.JSONException
import org.json.JSONObject


/**
 * Created by patrickmurrow on 9/11/17.
 */
class CategoryActivity : AppCompatActivity() {

    private val selectedCategories = ArrayList<Category>()
    private val categories = ArrayList<Category>()
    private val subcategories = ArrayList<Category>()

    private val errorListener = Response.ErrorListener { error -> Log.d(this.packageName, "ERROR: " + error.toString()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories)

        val responseListener = Response.Listener<JSONObject> { response ->
            try {
                val categoriesJson = response.getJSONArray(Category.SUBCATEGORIES_JSON_KEY)

                (0 until categoriesJson.length()).mapTo(categories) {
                    Category(categoriesJson.getJSONObject(it))
                }

                setupRootCategories()
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

        // Request root categories
        NetworkHelper.requestCategories("0", responseListener, errorListener)

        setupListeners()
    }

    private fun setupListeners() {
        val onQueryTextListener = object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                hideRootCategories()
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        }

        searchView.setOnQueryTextListener(onQueryTextListener)

        removeLastButton.setOnClickListener {
            removeClick()
        }

        collapseLayout.setOnClickListener {
            toggleSubcategoryView()
        }
    }

    /**
     * On root category click, hides root categories, shows the selected category's
     * subcategories. Shows listings of the selected root category.
     */
    private fun categoryClick(categoryAdapter: CategoryAdapter, position: Int) {
        val category = categoryAdapter.getItem(position)
        searchView.setQuery("", false)
        searchView.isIconified = true

        selectedCategories.add(category)

        val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up_anim)
        slideUp.setAnimationListener(object : Animation.AnimationListener {

            override fun onAnimationStart(animation: Animation) {
                updateSearchHierarchyView()
            }

            override fun onAnimationRepeat(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                hideRootCategories()
            }
        })
        categoriesGrid.startAnimation(slideUp)
    }

    /**
     * On subcategory click, shows the selected subcategory's subcategories, if any. Shows
     * listings of the selected subcategory.
     */
    private fun subcategoryClick(subcategoryAdapter: SubcategoryAdapter, position: Int) {
        val category = subcategoryAdapter.getItem(position)
        selectedCategories.add(category)

        updateSubCategories()

        if (category.hasSubcategories()) {
            val slideIn = AnimationUtils.loadAnimation(this, R.anim.slide_in_anim)
            subcategoriesList.startAnimation(slideIn)
        }
    }

    /**
     * Toggles the visibility of the subcategories selection view.
     */
    private fun toggleSubcategoryView() {
        if (subcategoriesList.visibility == View.VISIBLE) {
            subcategoriesList.visibility = View.GONE
            collapseExpandArrow.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_keyboard_arrow_down_white_24px))
        } else {
            subcategoriesList.visibility = View.VISIBLE
            collapseExpandArrow.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_keyboard_arrow_up_white_24px))
        }
    }

    /**
     * Removes the latest category in the search hierarchy. Sends the user back to root
     * category selection when all search criteria is removed.
     */
    private fun removeClick() {
        if (selectedCategories.isEmpty()) {
            searchView.setQuery("", false)
            searchView.isIconified = true
        } else {
            selectedCategories.removeAt(selectedCategories.size - 1)
        }

        if (selectedCategories.isEmpty() && searchView.query.isEmpty()) {
            subcategories.clear()
            hideSearchHierarchyView()
        } else {
            updateSubCategories()
        }
    }

    /**
     * Hides the ability to see and select subcategories, shows root categories.
     */
    private fun hideSearchHierarchyView() {
        val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up_anim)

        slideUp.setAnimationListener(object : Animation.AnimationListener {

            override fun onAnimationStart(animation: Animation) {
                searchHierarchyLayout.elevation = -2f
                collapseLayout.elevation = -2f
                subcategoriesList.visibility = View.GONE
                listingsLayout.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                searchHierarchyLayout.elevation =  UiHelper.getFloatPixelsFromDp(this@CategoryActivity, 2f)
                collapseLayout.elevation = UiHelper.getFloatPixelsFromDp(this@CategoryActivity, 2f)
                searchHierarchyLayout.visibility = View.GONE
                collapseLayout.visibility = View.GONE
                rootCategoriesLayout.visibility = View.VISIBLE
                collapseExpandArrow.setImageDrawable(ContextCompat.getDrawable(this@CategoryActivity, R.drawable.ic_keyboard_arrow_up_white_24px))
                getListingsFragment().showProgress()
            }
        })

        searchHierarchyLayout.startAnimation(slideUp)
        collapseLayout.startAnimation(slideUp)

        val slideIn = AnimationUtils.loadAnimation(this@CategoryActivity, R.anim.slide_up_in_anim)
        categoriesGrid.startAnimation(slideIn)
    }

    private fun hideRootCategories() {
        rootCategoriesLayout.visibility = View.GONE
        listingsLayout.visibility = View.VISIBLE
        updateSubCategories()

        val slideDown = AnimationUtils.loadAnimation(this@CategoryActivity, R.anim.slide_down_anim)
        subcategoriesList.startAnimation(slideDown)
        collapseLayout.startAnimation(slideDown)
    }

    private fun setupRootCategories() {
        categoryProgress.visibility = View.GONE
        rootCategoriesLayout.visibility = View.VISIBLE

        val categoryAdapter = CategoryAdapter(this, categories.toTypedArray())
        val numberOfColumns = 3
        categoriesGrid.layoutManager = GridLayoutManager(this, numberOfColumns)

        val itemClickListener = CategoryAdapter.ItemClickListener { _, position ->
            categoryClick(categoryAdapter, position)
        }

        categoryAdapter.setClickListener(itemClickListener)
        categoriesGrid.adapter = categoryAdapter
    }

    private fun updateSubCategories() {
        subcategories.clear()

        updateSearchHierarchyView()

        var latestCategoryNumber = "0"
        if (!selectedCategories.isEmpty()) {
            latestCategoryNumber  = selectedCategories[selectedCategories.size - 1].number
        }

        val responseListener = Response.Listener<JSONObject> { response ->
            try {
                val categoriesJson = response.optJSONArray(Category.SUBCATEGORIES_JSON_KEY)

                if (!response.isNull(Category.SUBCATEGORIES_JSON_KEY) && categoriesJson.length() > 0) {
                    (0 until categoriesJson.length()).mapTo(subcategories) {
                        Category(categoriesJson.getJSONObject(it))
                    }
                }

                updateSubCategoriesView()
                getListingsFragment().updateListings(searchView.query.toString(), latestCategoryNumber)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

        NetworkHelper.requestCategories(latestCategoryNumber, responseListener, errorListener)
    }

    private fun updateSubCategoriesView() {
        if (subcategories.isEmpty()) {
            subcategoriesList.visibility = View.GONE
            collapseLayout.visibility = View.GONE
            return
        } else {
            subcategoriesList.visibility = View.VISIBLE
            collapseLayout.visibility = View.VISIBLE
        }

        val subcategoryAdapter = SubcategoryAdapter(this, subcategories.toTypedArray())
        subcategoriesList.layoutManager = LinearLayoutManager(this)

        val itemClickListener = SubcategoryAdapter.ItemClickListener { _, position ->
            subcategoryClick(subcategoryAdapter, position)
        }

        subcategoryAdapter.setClickListener(itemClickListener)
        subcategoriesList.adapter = subcategoryAdapter
        updateSearchHierarchyView()
    }

    /**
     * Updates the view of the user's current search hierarchy.
     */
    private fun updateSearchHierarchyView() {
        var hierarchyText = ""
        for (category in selectedCategories) {
            if (!hierarchyText.isEmpty()) {
                hierarchyText += "  >  "
            }
            hierarchyText += category.name
        }

        if (!searchView.query.isEmpty()) {
            if (!hierarchyText.isEmpty()) {
                hierarchyText += "  >  "
            }
            hierarchyText += "Showing results for \"" + searchView.query + "\""
        }

        rootCategory.text = hierarchyText

        searchHierarchyScrollView.postDelayed({
            searchHierarchyScrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT)
        }, 100L)

        if (selectedCategories.size > 1 || !searchView.query.isEmpty() && !selectedCategories.isEmpty()) {
            removeLastButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_backspace_white_24px))
        } else {
            removeLastButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_clear_white_24px))
        }

        searchHierarchyLayout.visibility = View.VISIBLE
    }

    private fun getListingsFragment(): ListingsFragment {
        return fragmentManager.findFragmentById(R.id.listingsFragment) as ListingsFragment
    }

    override fun onBackPressed() {
        if (selectedCategories.isEmpty()) {
            super.onBackPressed()
        } else {
            removeClick()
        }
    }
}