package it.jaschke.alexandria;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Toast;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class SimpleScannerActivity extends ActionBarActivity implements ZBarScannerView.ResultHandler {
    private ZBarScannerView mScannerView;

    public static Integer SCAN = 1;
    public static String ISBN = "ISBN";

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZBarScannerView(this);
        setContentView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        Result rawResult2 = rawResult;
        Toast.makeText(this, "Contents = " + rawResult.getContents() +
                ", Format = " + rawResult.getBarcodeFormat().getName(), Toast.LENGTH_SHORT).show();
        mScannerView.startCamera();
        String format = rawResult.getBarcodeFormat().getName();
        if ( format.equals("ISBN10") || format.equals("ISBN13") ) {
            Intent output = new Intent();
            output.putExtra(ISBN, rawResult.getContents());
            setResult(RESULT_OK, output);
            finish();
        } else {
            int duration = Toast.LENGTH_SHORT;
            String msg = (String) getResources().getText(R.string.unknown_format);
            Toast.makeText(this, msg, duration).show();
        }
    }
}
