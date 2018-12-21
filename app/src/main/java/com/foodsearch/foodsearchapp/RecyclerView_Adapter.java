package com.foodsearch.foodsearchapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Adapter for RecyclerView
 */
public class RecyclerView_Adapter extends
        RecyclerView.Adapter<ViewHolder> {
    private ArrayList<RestaurantInfo> arrayList;
    private Context context;

    /**
     * Constructor for RecyclerView
     * @param context - Activity where views are created
     * @param arrayList - Objects for which views should be created
     */
    public RecyclerView_Adapter(Context context,
                                ArrayList<RestaurantInfo> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    /**
     * Gets number of views that should be created
     * @return - int - number of items
     */
    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);

    }

    /**
     * Handles logic after view creation
     * @param holder - layout that controls view
     * @param position - number of item in ArrayList
     */
    @Override
    public void onBindViewHolder(ViewHolder holder,
                                 final int position) {

        final ViewHolder mainHolder = (ViewHolder) holder;
        mainHolder.title.setText(arrayList.get(position).getRestName());
        mainHolder.price.setText(arrayList.get(position).getPrice() + " грн");
        mainHolder.meal.setText(arrayList.get(position).getMeal());
        mainHolder.description.setText(arrayList.get(position).getAddress());

        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LatLng point = ((ResultActivity) getActivity()).getLocationFromAddress(mainHolder.description);
                ((ResultActivity) getActivity()).mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 11));
            }
        });*/
    }

    /**
     * Creates new view in current layout
     */
    @Override
    public ViewHolder onCreateViewHolder(
            ViewGroup viewGroup, int viewType) {

        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(
                R.layout.item_row, viewGroup, false);
        ViewHolder mainHolder = new ViewHolder(mainGroup) {
            @Override
            public String toString() {
                return super.toString();
            }
        };


        return mainHolder;

    }


}
