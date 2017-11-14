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
import com.squareup.picasso.Picasso;

/**
 * Created by patrickmurrow on 9/11/17.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder> {

    private Context context;
    private Category[] categories;
    private LayoutInflater inflater;
    private ItemClickListener itemClickListener;

    public CategoryAdapter(final Context context, final Category[] data) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.categories = data;
    }

    @Override
    public CategoryHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View view = inflater.inflate(R.layout.layout_category_item, parent, false);
        return new CategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(final CategoryHolder holder, final int position) {
        holder.populateViews(categories[position]);
    }

    @Override
    public int getItemCount() {
        return categories.length;
    }

    public class CategoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView categoryTitle;
        private ImageView categoryImage;

        CategoryHolder(View itemView) {
            super(itemView);
            categoryTitle = itemView.findViewById(R.id.categoryNameText);
            categoryImage = itemView.findViewById(R.id.categoryImage);
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
            Picasso.with(context).load(category.getDrawable()).into(categoryImage);
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
