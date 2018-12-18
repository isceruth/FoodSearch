package com.foodsearch.foodsearchapp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public abstract class ViewHolder extends RecyclerView.ViewHolder {

    public TextView title;
    public TextView description;
    public TextView meal;
    public TextView price;

    public ViewHolder(View view) {
        super(view);

        this.title = (TextView) view.findViewById(R.id.cardTitle);
        this.description = (TextView) view.findViewById(R.id.cardDescription);
        this.meal = (TextView) view.findViewById(R.id.cardMeal);
        this.price = (TextView) view.findViewById(R.id.cardPrice);
    }


}
