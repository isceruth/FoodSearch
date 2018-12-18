package com.foodsearch.foodsearchapp;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class RestaurantInfo {
    public String restName;
    public String address;
    public int price;
    public String meal;

    public String getMeal() {
        return meal;
    }

    public void setMeal(String meal) {
        this.meal = meal;
    }

    public String getRestName() {
        return restName;
    }

    public void setRestName(String restName) {
        this.restName = restName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}

public class ResultActivity extends AppCompatActivity implements OnMapReadyCallback {

    private RecyclerView mUserListRecyclerView;
    private MapView mMapView;
    public GoogleMap mGoogleMap;
    public LatLng lPoint;

    public ArrayList<RestaurantInfo> resList;
    public JSONObject result;

    private static ViewPager viewPager;
    private static TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        try {
            result = new JSONObject(getIntent().getStringExtra("resultJson"));
            resList = new ArrayList<>();
            JSONArray array = result.getJSONArray("data");
            for ( int i = 0; i < array.length(); i++) {
                RestaurantInfo dto = new RestaurantInfo();
                dto.setMeal((String) array.getJSONObject(i).get("description"));
                dto.setPrice((int) array.getJSONObject(i).get("price"));
                dto.setRestName((String) (array.getJSONObject(i).getJSONObject("restaurant")).get("name"));
                dto.setAddress(array.getJSONObject(i).getJSONObject("restaurant").getJSONObject("location").getString("description").split(";")[0]);
                resList.add(dto);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());//setting current selected item over viewpager
                switch (tab.getPosition()) {
                    case 0:
                        markerOnMap(mGoogleMap);
                        break;
                    case 1:
                        markerOnMap(mGoogleMap);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new DummyFragment("Ближайшие", null), "Nearest");
        adapter.addFrag(new DummyFragment("Лучшие", null), "Best");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mGoogleMap = map;
        LatLng point = new LatLng(50.449480, 30.461201);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(point,11));
        map.setMyLocationEnabled(true);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();//fragment arraylist
        private final List<String> mFragmentTitleList = new ArrayList<>();//title arraylist

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }


        //adding fragments and title method
        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public void markerOnMap(GoogleMap map) {
        map.clear();
        for (int i = 0; i < 3; i++) {
            double lat = Math.random();
            double lon = Math.random();
            LatLng point = new LatLng(50 + lat, 30 + lon);
            lPoint = point;
            map.addMarker(new MarkerOptions().position(point).title("Ресторан"));
        }
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(lPoint, 11));
    }
}
