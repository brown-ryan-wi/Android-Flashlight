package com.clearcreekcode.tapflashlight;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Camera;
import android.os.PowerManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class FlashlightActivity extends ActionBarActivity {

    private Camera camera;
    private PowerManager.WakeLock wakeLock;
    private boolean isSupported_FEATURE_CAMERA_FLASH;
    private RelativeLayout rootLayout;
    private TextView txtInstruction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.clearcreekcode.tapflashlight.R.layout.activity_flashlight);

        rootLayout = (RelativeLayout) findViewById(com.clearcreekcode.tapflashlight.R.id.rootLayout);
        rootLayout.setOnClickListener(btnFlashlightClicked);

        txtInstruction = (TextView) findViewById(com.clearcreekcode.tapflashlight.R.id.txtInstruction);

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, getString(com.clearcreekcode.tapflashlight.R.string.do_not_dim_screen));

        isSupported_FEATURE_CAMERA_FLASH = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    View.OnClickListener btnFlashlightClicked = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            if( isFlashLightOn() ) {
                turnFlashLightOff();
            } else {
                turnFlashLightOn();
            }
        }
    };

    private boolean isFlashLightOn()
    {
        Camera.Parameters cameraParam = camera.getParameters();
        return cameraParam.getFlashMode().equals(cameraParam.FLASH_MODE_TORCH);
    }

    private void turnFlashLightOn()
    {
        if( isSupported_FEATURE_CAMERA_FLASH ) {
            Camera.Parameters cameraParam = camera.getParameters();
            List<String> flashModes = cameraParam.getSupportedFlashModes();
            if( flashModes != null && flashModes.contains(cameraParam.FLASH_MODE_TORCH) ) {
                cameraParam.setFlashMode(cameraParam.FLASH_MODE_TORCH);
                camera.setParameters(cameraParam);

                rootLayout.setBackgroundColor(Color.BLACK);
                txtInstruction.setText(com.clearcreekcode.tapflashlight.R.string.tap_to_turn_off);
            } else {
                Toast.makeText(this, com.clearcreekcode.tapflashlight.R.string.camera_flash_not_supported,Toast.LENGTH_LONG);
                rootLayout.setBackgroundColor(Color.WHITE);
            }
        } else {
            Toast.makeText(this, com.clearcreekcode.tapflashlight.R.string.camera_flash_not_supported,Toast.LENGTH_LONG);
            rootLayout.setBackgroundColor(Color.WHITE);
        }
        wakeLock.acquire();
    }

    private void turnFlashLightOff()
    {
        if( isSupported_FEATURE_CAMERA_FLASH ) {
            Camera.Parameters cameraParam = camera.getParameters();
            List<String> flashModes = cameraParam.getSupportedFlashModes();
            if( flashModes != null && flashModes.contains(cameraParam.FLASH_MODE_OFF) ) {
                cameraParam.setFlashMode(cameraParam.FLASH_MODE_OFF);
                camera.setParameters(cameraParam);
                txtInstruction.setText(com.clearcreekcode.tapflashlight.R.string.tap_to_turn_on);
            } else {
                rootLayout.setBackgroundColor(Color.BLACK);
            }
        } else {
            rootLayout.setBackgroundColor(Color.BLACK);
        }
        wakeLock.release();
    }

    @Override
    public void onStart() {
        super.onStart();
        camera = Camera.open();
        camera.startPreview();
        turnFlashLightOn();
    }

    @Override
    public void onStop() {
        super.onStop();

        if( isFlashLightOn() ) {
            turnFlashLightOff();
        }

        camera.stopPreview();
        camera.release();
        camera = null;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(com.clearcreekcode.tapflashlight.R.menu.flashlight, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        /*int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }
}
