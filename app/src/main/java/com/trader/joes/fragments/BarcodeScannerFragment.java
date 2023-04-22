package com.trader.joes.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.trader.joes.R;

public class BarcodeScannerFragment extends Fragment {

    private SurfaceView surfaceView;
    private TextView barcodeText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_barcode_scanner, container, false);

        //initialize UI components
        surfaceView = view.findViewById(R.id.surfaceView);
        barcodeText = view.findViewById(R.id.barcode_value);

        return view;
    }
}