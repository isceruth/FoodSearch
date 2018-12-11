package com.foodsearch.foodsearchapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

public class RecyclerView_Adapter extends
        RecyclerView.Adapter<ViewHolder> {
    private ArrayList<String> arrayList;
    private Context context;


    public RecyclerView_Adapter(Context context,
                                ArrayList<String> arrayList) {
        this.context = context;
        this.arrayList = arrayList;

    }


    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder,
                                 int position) {


        final ViewHolder mainHolder = (ViewHolder) holder;
        //Setting text over textview
        mainHolder.title.setText(arrayList.get(position));

    }

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
