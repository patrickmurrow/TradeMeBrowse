package com.example.patrickmurrow.trademebrowse.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.patrickmurrow.trademebrowse.R;
import com.example.patrickmurrow.trademebrowse.model.Category;

/**
 * Created by patrickmurrow on 9/11/17.
 */

public class SubcategoryAdapter extends RecyclerView.Adapter<SubcategoryAdapter.SubcategoryHolder> {

    private Category[] categories;
    private LayoutInflater inflater;
    private ItemClickListener itemClickListener;

    public SubcategoryAdapter(final Context context, final Category[] data) {
        this.inflater = LayoutInflater.from(context);
        this.categories = data;
    }

    @Override
    public SubcategoryHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View view = inflater.inflate(R.layout.layout_subcategory_item, parent, false);
        return new SubcategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(final SubcategoryHolder holder, final int position) {
        holder.populateViews(categories[position]);
    }

    @Override
    public int getItemCount() {
        return categories.length;
    }

    public class SubcategoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView categoryTitle;
        private ImageView hasSubcategoriesImage;

        SubcategoryHolder(View itemView) {
            super(itemView);
            categoryTitle = itemView.findViewById(R.id.categoryNameText);
            hasSubcategoriesImage = itemView.findViewById(R.id.hasSubcategoriesImage);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(final View view) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(view, getAdapterPosition());
            }
        }

        private void populateViews(final Category category) {
            categoryTitle.setText(category.getName());

            if (!category.hasSubcategories()) {
                hasSubcategoriesImage.setVisibility(View.GONE);
            }
        }
    }

    public Category getItem(final int index) {
        return categories[index];
    }

    public void setClickListener(final ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(final View view, final int position);
    }
}
