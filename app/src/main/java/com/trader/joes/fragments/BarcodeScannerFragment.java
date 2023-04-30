package com.trader.joes.fragments;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.common.util.concurrent.ListenableFuture;
import com.trader.joes.R;
import com.trader.joes.service.BarcodeAnalyzer;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This fragment represents the barcode scanner
 * References:
 * https://developers.google.com/ml-kit/vision/barcode-scanning/android#java
 * Kotlin QR code example: https://medium.com/codex/scan-barcodes-in-android-using-the-ml-kit-30b2a03ccd50
 */
public class BarcodeScannerFragment extends Fragment {

    private ExecutorService cameraExecutor;
    private BarcodeBoxView barcodeBoxView;
    private PreviewView previewView;

    private ActivityResultLauncher<String> requestCameraPermissionLauncher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_barcode_scanner, container, false);

        //initialize UI components
        previewView = view.findViewById(R.id.preview_view);

        cameraExecutor = Executors.newSingleThreadExecutor();
        barcodeBoxView = new BarcodeBoxView(getContext());

        getActivity().addContentView(barcodeBoxView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        requestCameraPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if(result) {
                    //Camera permission granted
                    startCamera();
                } else {
                    //permission denied - show a dialog box
                    new MaterialAlertDialogBuilder(getActivity()).setTitle("Permission required")
                            .setMessage("Barcode scanner needs to access the camera to scan barcodes. Go to settings to give camera permission.")
                            .setPositiveButton(R.string.ok, null).setCancelable(true)
                            .create().show()
                    ;
                }
            }
        });
        requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cameraExecutor.shutdown();
        barcodeBoxView.setRect(new RectF());
    }

    @Override
    public void onPause() {
        super.onPause();
        cameraExecutor.shutdown();
        barcodeBoxView.setRect(new RectF());
    }

    private void startCamera() {
        //instantiate a camera provider promise
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(getActivity());

        //add a listener to the promise. Will execute run() when promise is resolved
        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                    //preview
                    Preview preview = new Preview.Builder().build();
                    preview.setSurfaceProvider(previewView.getSurfaceProvider());

                    //Image analyzer
                    ImageAnalysis imageAnalyzer = new ImageAnalysis.Builder().setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build();
                    imageAnalyzer.setAnalyzer(cameraExecutor, new BarcodeAnalyzer(getActivity(), barcodeBoxView, (float)previewView.getWidth(), (float)previewView.getHeight()));

                    // Select back camera as a default
                    CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;

                    //Unbind use cases before rebinding
                    cameraProvider.unbindAll();

                    //Bind use cases to camera
                    cameraProvider.bindToLifecycle(getActivity(), cameraSelector, preview, imageAnalyzer);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, ContextCompat.getMainExecutor(getActivity()));
    }
}