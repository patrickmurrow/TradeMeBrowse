package com.example.patrickmurrow.trademebrowse.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.patrickmurrow.trademebrowse.R;
import com.example.patrickmurrow.trademebrowse.helpers.UiHelper;
import com.example.patrickmurrow.trademebrowse.model.ListingItem;
import com.squareup.picasso.Picasso;

/**
 * Created by patrickmurrow on 10/11/17.
 */

public class ListingAdapter extends RecyclerView.Adapter<ListingAdapter.ListingHolder> {

    private Context context;
    private ListingItem[] listingItems;
    private LayoutInflater inflater;
    private ItemClickListener itemClickListener;

    public ListingAdapter() {
        //empty
    }

    public ListingAdapter(final Context context, final ListingItem[] data) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.listingItems = data;
    }

    @Override
    public ListingHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View view = inflater.inflate(R.layout.layout_listing_item, parent, false);
        return new ListingHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListingHolder holder, final int position) {
        holder.populateViews(listingItems[position]);
    }

    @Override
    public int getItemCount() {
        if (listingItems != null) {
            return listingItems.length;
        }
        return 0;
    }

    public class ListingHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView titleText;
        private TextView subtitleText;
        private TextView priceText;
        private TextView buyNowText;
        private TextView buyNowDescriptionText;
        private ImageView listingThumbnail;

        ListingHolder(View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.titleText);
            subtitleText = itemView.findViewById(R.id.subtitleText);
            priceText = itemView.findViewById(R.id.priceText);
            buyNowText = itemView.findViewById(R.id.buyNowText);
            listingThumbnail = itemView.findViewById(R.id.listingThumbnail);
            buyNowDescriptionText = itemView.findViewById(R.id.buyNowDescTitle);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(final View view) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(view, getAdapterPosition());
            }
        }

        private void populateViews(final ListingItem listingItem) {
            titleText.setText(listingItem.getTitle());
            subtitleText.setText(listingItem.getSubtitle());
            priceText.setText(UiHelper.getPriceText(listingItem.getPrice()));

            final Double buyNow = listingItem.getBuyNowPrice();
            if (!buyNow.isNaN()) {
                buyNowText.setText(UiHelper.getPriceText(listingItem.getBuyNowPrice()));
            } else {
                buyNowDescriptionText.setVisibility(View.GONE);
            }

            final String thumbnailPath = listingItem.getPictureHref();
            if (!thumbnailPath.isEmpty()) {
                Picasso.with(context).load(listingItem.getPictureHref()).placeholder(R.drawable.no_image_available).into(listingThumbnail);
            } else {
                Picasso.with(context).load(R.drawable.no_image_available).into(listingThumbnail);
            }
        }
    }

    public ListingItem getItem(final int index) {
        return listingItems[index];
    }

    public void setClickListener(final ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(final View view, final int position);
    }
}
