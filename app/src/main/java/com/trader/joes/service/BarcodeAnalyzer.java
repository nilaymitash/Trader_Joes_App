package com.trader.joes.service;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.Image;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;
import com.trader.joes.fragments.BarcodeBoxView;

import java.util.List;

public class BarcodeAnalyzer implements ImageAnalysis.Analyzer {

    private Context context;
    private BarcodeBoxView barcodeBoxView;
    private float previewViewWidth;
    private float previewViewHeight;
    private float scaleX = 1f;
    private float scaleY = 1f;

    public BarcodeAnalyzer(Context context, BarcodeBoxView barcodeBoxView, float previewViewWidth, float previewViewHeight) {
        this.context = context;
        this.barcodeBoxView = barcodeBoxView;
        this.previewViewWidth = previewViewWidth;
        this.previewViewHeight = previewViewHeight;
    }

    private float translateX(float x) {
        return x * scaleX;
    }

    private float translateY(float y) {
        return y * scaleY;
    }

    private RectF adjustBoundingRect(Rect rect){
        return new RectF(translateX((float)rect.left),
                translateY((float)rect.top),
                translateX((float)rect.right),
                translateY((float)rect.bottom));
    }

    @Override
    @OptIn(markerClass = ExperimentalGetImage.class)
    public void analyze(@NonNull ImageProxy image) {
        Image img = image.getImage();

        if(img != null) {
            //Update scale factors
            scaleX = previewViewWidth / (float)img.getHeight();
            scaleY = previewViewHeight / (float)img.getWidth();

            InputImage inputImage = InputImage.fromMediaImage(img, image.getImageInfo().getRotationDegrees());

            //Process image searching for barcodes
            //TODO: update barcode format
            BarcodeScannerOptions options = new BarcodeScannerOptions.Builder().setBarcodeFormats(Barcode.FORMAT_QR_CODE).build();

            BarcodeScanner scanner = BarcodeScanning.getClient(options);

            scanner.process(inputImage).addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
                @Override
                public void onSuccess(List<Barcode> barcodes) {
                    if (!barcodes.isEmpty()) {
                        for(Barcode barcode : barcodes) {
                            // Update bounding rect
                            Rect boundingBox = barcode.getBoundingBox();
                            barcodeBoxView.setRect(adjustBoundingRect(boundingBox));

                            //TODO: handle barcodes...
                            Toast.makeText(context, barcode.getRawValue(), Toast.LENGTH_SHORT).show();
                            //Add SKU to the card or show error
                        }
                    } else {
                        //Remove bounding rect
                        barcodeBoxView.setRect(new RectF());
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //TODO: handle failure
                }
            });
        }

        image.close();
    }
}
