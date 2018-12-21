package com.foodsearch.foodsearchapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Creates and populates tabs in application with restaurant data.
 */
public class DummyFragment extends Fragment {
    private View view;
    public ArrayList<RestaurantInfo> near;

    private String title;

    private static RecyclerView recyclerView;

    /**
     * Default constructor
     */
    public DummyFragment() {
    }

    /**
     * Constructor for creating a tab with specified title
     * @param title - Title for tab on Result tab
     */
    public DummyFragment(String title) {
        this.title = title;
    }

    /**
     * Called to have the fragment instantiate its user interface view
     * @param inflater - template to create a view with certain paremeters
     * @param container - container to which created view will belong
     * @param savedInstanceState - saving view information during view creation
     * @return - return created view
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment, container, false);

        ArrayList<RestaurantInfo> results = ((ResultActivity) getActivity()).resList;

        setRecyclerView(results);
        return view;

    }

    /**
     * Class that does sorting operation of RestaurantInfo object in separate from UI thread
     */
    public class AsyncSort extends AsyncTask<Object, Void, Object[]> {

        /**
         * Method that automatically executed first after execute() call of a class instance.
         * Sorts Object ArrayList;
         * @param info - all the required info for sorting Object array
         * @return - sorted array
         */
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

        /**
         * Method that automatically executed after doInBackground() finished.
         * Adds first five Objects to another ArrayList that will be rendered on result screen.
         * @param info - two ArrayLists passed from doInBackground() method
         */
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

    /**
     * Assigns to class member final sorted ArrayList
     * @param nearF - sorted ArrayList
     */
    public void getNearest(ArrayList<RestaurantInfo> nearF) {
        near = nearF;
    }

    /**
     * Setting recycler view, creating two tabs with sorted ArrayLists
     * @param res - ArrayList of restaurants
     */
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
        int limit = 5;

        switch (title) {
            case "BEST":
                if (res.size() < 5) {
                    limit = res.size();
                }
                for (int i = 0; i < limit; i++) {
                    best.add(res.get(i));
                }
                adapter = new RecyclerView_Adapter(getActivity(), best);
                recyclerView.setAdapter(adapter);
                break;
            case "PRICE":
                limit = 5;
               /* AsyncSort sorting = new AsyncSort();
                try {
                    sorting.execute(nearest, res, nearestF).get();
                } catch (Exception e) {
                    e.printStackTrace();
                }*/

                nearest.addAll(resS);
                Collections.copy(nearest, resS);
                Collections.sort(nearest);
                if (nearest.size() < 5) {
                    limit = nearest.size();
                }
                for (int i = 0; i < limit; i++) {
                    nearestF.add(nearest.get(i));
                }

                adapter = new RecyclerView_Adapter(getActivity(), nearestF);
                recyclerView.setAdapter(adapter);
                break;
        }

    }
}
