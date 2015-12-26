package com.clearcreekcode.flashlight;

import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

public class FlashlightActivity extends ActionBarActivity {

    //
    private Camera camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashlight);

        Button btnFlashlight;
        btnFlashlight = (Button) findViewById(R.id.btnFlashlight);
        btnFlashlight.setOnClickListener(btnFlashlightClicked);
    }

    View.OnClickListener btnFlashlightClicked = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            if( isFlashLightOn() ) {
                turnFlashLightOff(view);
            } else {
                turnFlashLightOn(view);
            }
            Toast.makeText(view.getContext(),"Flashlight on", Toast.LENGTH_LONG).show();
        }
    };

    private void turnFlashLightOff(View view)
    {
        if( view.getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH) ) {
            Camera.Parameters cameraParam = camera.getParameters();
            List<String> flashModes = cameraParam.getSupportedFlashModes();
            if( flashModes != null && flashModes.contains(cameraParam.FLASH_MODE_OFF) ) {
                cameraParam.setFlashMode(cameraParam.FLASH_MODE_OFF);
                camera.setParameters(cameraParam);
            } else {
                // TODO: FLASH_MODE_OFF not supported
            }
        } else {
            // TODO: Flash not supported
        }
    }

    private boolean isFlashLightOn()
    {
        Camera.Parameters cameraParam = camera.getParameters();
        return cameraParam.getFlashMode().equals(cameraParam.FLASH_MODE_TORCH);
    }

    private void turnFlashLightOn(View view)
    {
        if( view.getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH) ) {
            Camera.Parameters cameraParam = camera.getParameters();
            List<String> flashModes = cameraParam.getSupportedFlashModes();
            if( flashModes != null && flashModes.contains(cameraParam.FLASH_MODE_TORCH) ) {
                cameraParam.setFlashMode(cameraParam.FLASH_MODE_TORCH);
                camera.setParameters(cameraParam);
            } else {
                // TODO: FLASH_MODE_TORCH not supported
            }
        } else {
            // TODO: Flash not supported
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        camera = Camera.open();
        camera.startPreview();
    }

    @Override
    public void onStop() {
        super.onStop();
        camera.stopPreview();
        camera.release();
        camera = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.flashlight, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
