package com.trader.joes.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.trader.joes.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class FindStoreFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find_store, container, false);

        //initialize UI components
        // Initialize map fragment
        /*SupportMapFragment mapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);*/

        //move this to on click of a button: https://code.tutsplus.com/tutorials/android-from-scratch-using-rest-apis--cms-27117
        new FindStoreTask().execute();

        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private class FindStoreTask extends AsyncTask<Void, Void, String> {

        String payload = "{\n" +
                "    \"request\": {\n" +
                "        \"appkey\": \"8BC3433A-60FC-11E3-991D-B2EE0C70A832\",\n" +
                "        \"formdata\": {\n" +
                "            \"geoip\": false,\n" +
                "            \"dataview\": \"store_default\",\n" +
                "            \"limit\": 4,\n" +
                "            \"geolocs\": {\n" +
                "                \"geoloc\": [\n" +
                "                    {\n" +
                "                        \"addressline\": \"19333\",\n" +
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
                myConnection.getOutputStream().write(payload.getBytes());

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
            //show results
            try {
                JSONObject jsonObject = new JSONObject(str);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}