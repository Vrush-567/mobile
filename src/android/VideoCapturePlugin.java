package org.apache.cordova.plugin;

import android.app.Activity;
import android.content.Intent;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Toast;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class VideoCapturePlugin extends CordovaPlugin {

    private static final int CAPTURE_VIDEO_REQUEST_CODE = 100;

    private CallbackContext callbackContext;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("captureVideo")) {
            this.callbackContext = callbackContext;
            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);
            cordova.startActivityForResult(this, intent, CAPTURE_VIDEO_REQUEST_CODE);
            return true;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_VIDEO_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            try {
                InputStream inputStream = cordova.getActivity().getContentResolver().openInputStream(data.getData());
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                }
                byte[] videoBytes = byteArrayOutputStream.toByteArray();
                String encodedVideo = Base64.encodeToString(videoBytes, Base64.DEFAULT);
                callbackContext.success(encodedVideo);
            } catch (Exception e) {
                callbackContext.error("Failed to encode video: " + e.getMessage());
            }
        } else {
            callbackContext.error("Video capture failed or cancelled");
        }
    }
}
