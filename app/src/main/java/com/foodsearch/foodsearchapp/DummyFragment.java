package com.foodsearch.foodsearchapp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Thread.sleep;

public class DummyFragment extends Fragment {
    private View view;
    public ArrayList<RestaurantInfo> near;

    private String title;

    private static RecyclerView recyclerView;

    public DummyFragment() {
    }

    public DummyFragment(String title) {
        this.title = title;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment, container, false);

        ArrayList<RestaurantInfo> results = ((ResultActivity) getActivity()).resList;

        setRecyclerView(results);
        return view;

    }

    public class AsyncSort extends AsyncTask<Object, Void, Object[]> {
        @Override
        protected Object[] doInBackground(Object... info) {
            ArrayList<RestaurantInfo> nearest, res, nearestF;
            nearest = (ArrayList<RestaurantInfo>) info[0];
            res = (ArrayList<RestaurantInfo>) info[1];
            nearestF = (ArrayList<RestaurantInfo>) info[2];

            nearest.addAll(res);
            Collections.copy(nearest, res);
            Collections.sort(nearest);
            Object[] toExecute = {nearest, nearestF};

            return toExecute;
        }

        @Override
        protected void onPostExecute(Object... info) {
            ArrayList<RestaurantInfo> nearest, nearestF;
            nearest = (ArrayList<RestaurantInfo>) info[0];
            nearestF = (ArrayList<RestaurantInfo>) info[1];
            for (int i = 0; i < 5; i++) {
                nearestF.add(nearest.get(i));
            }

            getNearest(nearestF);
        }
    }

    public void getNearest(ArrayList<RestaurantInfo> nearF) {
        near = nearF;
    }

    private void setRecyclerView(ArrayList<RestaurantInfo> res) {

        recyclerView = (RecyclerView) view
                .findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView
                .setLayoutManager(new LinearLayoutManager(getActivity()));

        final ArrayList<RestaurantInfo> resS = res;
        ArrayList<RestaurantInfo> best = new ArrayList<>();
        final ArrayList<RestaurantInfo> nearest = new ArrayList<>();
        ArrayList<RestaurantInfo> nearestF = new ArrayList<>();
        RecyclerView_Adapter adapter;

        switch (title) {
            case "BEST":
                for (int i = 0; i < 5; i++) {
                    best.add(res.get(i));
                }
                adapter = new RecyclerView_Adapter(getActivity(), best);
                recyclerView.setAdapter(adapter);
                break;
            case "PRICE":
               /* AsyncSort sorting = new AsyncSort();
                try {
                    sorting.execute(nearest, res, nearestF).get();
                } catch (Exception e) {
                    e.printStackTrace();
                }*/

                nearest.addAll(resS);
                Collections.copy(nearest, resS);
                Collections.sort(nearest);
                for (int i = 0; i < 5; i++) {
                    nearestF.add(nearest.get(i));
                }

                adapter = new RecyclerView_Adapter(getActivity(), nearestF);
                recyclerView.setAdapter(adapter);
                break;
        }

    }
}
