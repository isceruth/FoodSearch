package com.foodsearch.foodsearchapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class DummyFragment extends Fragment {
    private View view;

    private String title;
    private String description;
    private String meal;
    private int price;

    private static RecyclerView recyclerView;

    public DummyFragment() {
    }

    public DummyFragment(String title, String description) {
        this.title = title;
        this.description = description;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment, container, false);

        ArrayList<RestaurantInfo> results = ((ResultActivity) getActivity()).resList;

        setRecyclerView(results);
        return view;

    }

    private void setRecyclerView(ArrayList<RestaurantInfo> res) {

        recyclerView = (RecyclerView) view
                .findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView
                .setLayoutManager(new LinearLayoutManager(getActivity()));

        RecyclerView_Adapter adapter = new RecyclerView_Adapter(getActivity(), res);
        recyclerView.setAdapter(adapter);

    }
}
