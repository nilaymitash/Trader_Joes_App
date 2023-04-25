package com.trader.joes.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.trader.joes.R;
import com.trader.joes.adapter.StoreLocationAdapter;
import com.trader.joes.model.Store;
import com.trader.joes.model.StoreLocationResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

public class FindStoreFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private Location currentLocation;
    private LocationManager locationManager;
    private TextInputEditText mZipcodeInput;
    private Button mSearchBtn;
    private StoreLocationAdapter storeLocationAdapter;
    private RecyclerView locationRecyclerView;
    private List<Store> locationList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find_store, container, false);

        //initialize UI components
        mZipcodeInput = view.findViewById(R.id.zipcode_input);
        mSearchBtn = view.findViewById(R.id.search_stores_btn);
        storeLocationAdapter = new StoreLocationAdapter(locationList);
        locationRecyclerView = view.findViewById(R.id.location_list);
        locationRecyclerView.setAdapter(storeLocationAdapter);

        // Initialize map fragment
        SupportMapFragment mapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //initialize Location Manager
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        getCoordinates();
        //String zipcode = getZipCode(coordinates);

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //move this to on click of a button: https://code.tutsplus.com/tutorials/android-from-scratch-using-rest-apis--cms-27117
                String userInputZipcode = String.valueOf(mZipcodeInput.getText());
                new FindStoreTask(userInputZipcode).execute();
            }
        });



        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Philadelphia and move the camera
        LatLng philly = new LatLng(39.952583, -75.165222);
        mMap.addMarker(new MarkerOptions()
                .position(philly)
                .title("Marker in Philadelphia"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(philly));
    }

    private boolean isLocationPermissionGranted() {
        if(getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            getActivity().requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
            return false;
        }

        return true;
    }

    @SuppressLint("MissingPermission")
    private void getCoordinates() {
        Coordinates coordinates = new Coordinates();
        boolean hasGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        LocationListener gpsLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                coordinates.setLongitude(location.getLongitude());
                coordinates.setLatitude(location.getLatitude());
                String zipcode = getZipCode(coordinates);
                if(mZipcodeInput.getText() == null || mZipcodeInput.getText().toString().equals("")) {
                    mZipcodeInput.setText(zipcode);
                    //download list only the first time:
                    if(locationList.isEmpty()) {
                        new FindStoreTask(zipcode).execute();
                    }
                }
            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {

            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }
        };

        if(isLocationPermissionGranted() && hasGPS) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0F, gpsLocationListener);
        }
    }


    private String getZipCode(Coordinates coordinates) {
        if(getActivity() != null) {
            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(coordinates.getLatitude(), coordinates.getLongitude(), 1);
                return addresses.get(0).getPostalCode();
            } catch (IOException e) {
                e.printStackTrace();
                return "19333"; //return a default zip code if there is an exception
            }
        }

        return "19333"; //94043
    }

    private class FindStoreTask extends AsyncTask<Void, Void, String> {
        String userEnteredZipcode = "19333";

        public FindStoreTask(String userEnteredZipcode) {
            this.userEnteredZipcode = userEnteredZipcode;
        }

        public String getPayload() {
            return "{\n" +
                    "    \"request\": {\n" +
                    "        \"appkey\": \"8BC3433A-60FC-11E3-991D-B2EE0C70A832\",\n" +
                    "        \"formdata\": {\n" +
                    "            \"geoip\": false,\n" +
                    "            \"dataview\": \"store_default\",\n" +
                    "            \"limit\": 4,\n" +
                    "            \"geolocs\": {\n" +
                    "                \"geoloc\": [\n" +
                    "                    {\n" +
                    "                        \"addressline\": \"" + this.userEnteredZipcode + "\",\n" +
                    "                        \"country\": \"US\",\n" +
                    "                        \"latitude\": \"\",\n" +
                    "                        \"longitude\": \"\"\n" +
                    "                    }\n" +
                    "                ]\n" +
                    "            },\n" +
                    "            \"searchradius\": \"10\",\n" +
                    "            \"where\": {\n" +
                    "                \"warehouse\": {\n" +
                    "                    \"distinctfrom\": \"1\"\n" +
                    "                }\n" +
                    "            },\n" +
                    "            \"false\": \"0\"\n" +
                    "        }\n" +
                    "    }\n" +
                    "}";
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //TODO: show progress bar
        }

        @Override
        protected String doInBackground(Void... voids) {
            String result = "";
            HttpsURLConnection myConnection = null;
            try {
                URL findStoreEndpoint = new URL("https://alphaapi.brandify.com/rest/locatorsearch");
                myConnection = (HttpsURLConnection) findStoreEndpoint.openConnection();
                myConnection.setRequestMethod("POST");
                myConnection.setDoOutput(true);
                myConnection.getOutputStream().write(getPayload().getBytes());

                InputStream in = myConnection.getInputStream();
                InputStreamReader isw = new InputStreamReader(in);
                int data = isw.read();
                while (data != -1) {
                    result += (char) data;
                    data = isw.read();
                }

                // return the data to onPostExecute method
                return result;

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (myConnection != null) {
                    myConnection.disconnect();
                }
            }

            return result;
        }

        @Override
        protected void onPostExecute(String str) {
            super.onPostExecute(str);
            //hide progress bar
            try {
                JSONObject jsonObject = new JSONObject(str);
                Gson gson= new Gson();
                StoreLocationResponse storeResponseObj = gson.fromJson(jsonObject.get("response").toString(), StoreLocationResponse.class);
                List<Store> list =  storeResponseObj.getCollection();
                //update recycler view locations
                storeLocationAdapter.updateStoreList(list);
                locationList = list;
                //Plot coordinates on the map
                plotMarkersOnMap(list);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void plotMarkersOnMap(List<Store> list) {
        LatLng firstStore = new LatLng(list.get(0).getLatitude(), list.get(0).getLongitude());
        for (Store store : list) {
            LatLng latLng = new LatLng(store.getLatitude(), store.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title(store.getName()));
        }
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(firstStore, 10));
    }

    private class Coordinates {
        private double latitude;
        private double longitude;

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }
    }
}