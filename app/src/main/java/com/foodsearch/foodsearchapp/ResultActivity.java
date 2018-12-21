package com.foodsearch.foodsearchapp;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Stores data about restaurant and defines a method to compare them by price of a meal
 */
class RestaurantInfo implements Comparable {
    public String restName;
    public String address;
    public String price;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    /*@Override
    public int compareTo(Object o) {
        List<Address> addressStart;
        List<Address> addressFinish;
        float[] resS = new float[3];
        float[] resF = new float[3];
        double latS, latF, lonS, lonF;
        Geocoder coder = new Geocoder(ResultActivity.getAppContext());
        try {
            addressStart = coder.getFromLocationName(getAddress(), 5);
            addressFinish = coder.getFromLocationName(((RestaurantInfo) o).getAddress(), 5);

            if (addressStart == null && addressFinish == null) {
                return 0;
            }

            if (addressStart == null) {
                return -1;
            }

            if (addressFinish == null) {
                return 1;
            }

            Log.i("TAG", "SIZE IS " + String.valueOf(addressStart.size()));

            Address locationStart = addressStart.get(0);
            latS = locationStart.getLatitude();
            lonS = locationStart.getLongitude();
            Location.distanceBetween(MainActivity.latitude, MainActivity.longitude, latS, lonS, resS);

            Address locationFinish = addressFinish.get(0);
            latF = locationFinish.getLatitude();
            lonF = locationFinish.getLongitude();
            Location.distanceBetween(MainActivity.latitude, MainActivity.longitude, latF, lonF, resF);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resS[0] < resF[0] ? -1 : 1;
    }*/

    /**
     * Compares two restaurant by price of meals
     * @param o - Object to compare with
     */
    @Override
    public int compareTo(Object o) {
        return (Float.valueOf(getPrice()) < Float.valueOf(((RestaurantInfo) o).getPrice())) ? -1 : 1;
    }
}

/**
 * Defines Activity that shows results of user query
 */
public class ResultActivity extends AppCompatActivity implements OnMapReadyCallback {

    private RecyclerView mUserListRecyclerView;
    private MapView mMapView;
    public GoogleMap mGoogleMap;
    public LatLng lPoint;
    public static Context mContext;

    public ArrayList<RestaurantInfo> resList;
    public JSONObject result;
    Geocoder coder;

    private static ViewPager viewPager;
    private static TabLayout tabLayout;
    public ArrayList<Object[]> markers = new ArrayList<>();

    /**
     * Automatically called after intent start in previous activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        ResultActivity.mContext = getApplicationContext();
        coder = new Geocoder(ResultActivity.getAppContext());

        try {
            result = new JSONObject(getIntent().getStringExtra("resultJson"));
            resList = new ArrayList<>();
            JSONArray array = result.getJSONArray("data");
            for (int i = 0; i < array.length(); i++) {
                Object[] point = new Object[2];
                RestaurantInfo dto = new RestaurantInfo();
                dto.setMeal((String) array.getJSONObject(i).get("description"));
                dto.setPrice(String.valueOf(array.getJSONObject(i).get("price")));
                dto.setRestName((String) (array.getJSONObject(i).getJSONObject("restaurant")).get("name"));
                dto.setAddress(array.getJSONObject(i).getJSONObject("restaurant").getJSONObject("location").getString("description").split(";")[0]);
                if ((dto.getAddress()).equals("null")) {
                    Log.i("TAG", "Skipped");
                } else {
                    resList.add(dto);
                    point[0] = (LatLng) getLocationFromAddress(dto.getAddress());
                    point[1] = (String) (array.getJSONObject(i).getJSONObject("restaurant")).get("name");
                    markers.add(point);
                }
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
                viewPager.setCurrentItem(tab.getPosition());
                switch (tab.getPosition()) {
                    case 0:
                        break;
                    case 1:
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

    /**
     * Gets Activity context
     */
    public static Context getAppContext() {
        return ResultActivity.mContext;
    }

    /**
     * Get coordinates from address passed in a string
     * @param strAddress - String address
     * @return LatLng Object (two coordinates)
     */
    public LatLng getLocationFromAddress(String strAddress) {

        List<Address> address;
        LatLng p1 = null;

        try {
            Log.i("TAG", strAddress);
            address = coder.getFromLocationName(strAddress, 5);

            if (address == null) {
                return null;
            }

            if (address.size() == 0) {
                p1 = new LatLng(0,0);
                return p1;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    /**
     * Sets up TabLayout with two tabs
     */
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new DummyFragment("PRICE"), "Cheapest");
        adapter.addFrag(new DummyFragment("BEST"), "Best");
        viewPager.setAdapter(adapter);
    }

    /**
     * Creates Google Map v2 fragment in top half screen and puts markers of restaurants
     */
    @Override
    public void onMapReady(GoogleMap map) {
        mGoogleMap = map;
        for (int i = 0; i < markers.size(); i++) {
            LatLng pt = (LatLng) markers.get(i)[0];
            String name = (String) markers.get(i)[1];
            if (pt.latitude != 0){
                map.addMarker(new MarkerOptions().position(pt).title(name));
            }
        }
        map.setMyLocationEnabled(true);
    }

    /**
     * Handles tabs creation
     */
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

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

        /**
         * Adds a tab with specified title
         */
        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        /**
         * Gets tab's title
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
