package com.c.yzxing;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.qrcode.Constant;
import com.example.qrcode.ScannerActivity;
import com.google.zxing.BarcodeFormat;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private final int REQUEST_PERMISION_CODE_CAMARE = 0;
    private final int RESULT_REQUEST_CODE = 1;
    private static final String TAG = "MainActivity";

    private HashMap<String, Set> mHashMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button;
        button = findViewById(R.id.zxing_button);
        button.setOnClickListener(mScannerListener);

        Set<BarcodeFormat> codeFormats = EnumSet.of(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_128,
                BarcodeFormat.CODE_93);
        mHashMap.put(ScannerActivity.BARCODE_FORMAT, codeFormats);
    }

    private View.OnClickListener mScannerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                goScanner();
            }else{
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISION_CODE_CAMARE);
            }
        }
    };

    private void goScanner(){
        Intent intent = new Intent(this, ScannerActivity.class);
        intent.putExtra(Constant.EXTRA_SCANNER_FRAME_WIDTH, 400);
        intent.putExtra(Constant.EXTRA_SCANNER_FRAME_HEIGHT, 400);
        intent.putExtra(Constant.EXTRA_SCANNER_FRAME_TOP_PADDING, 100);
        intent.putExtra(Constant.EXTRA_IS_ENABLE_SCAN_FROM_PIC, true);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.EXTRA_SCAN_CODE_TYPE, mHashMap);
        intent.putExtras(bundle);
        startActivityForResult(intent, RESULT_REQUEST_CODE);
    }

/*    @Override
    public void onRequestPermissionResult(int requestCode,
                                          @NonNull String permissions[], @NonNull int[] grantResults){
        switch(requestCode){
            case REQUEST_PERMISION_CODE_CAMARE:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    goScanner();
                }
                return;
            }
        }
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RESULT_REQUEST_CODE:
                    if (data == null) {return;}
                    String type = data.getStringExtra(Constant.EXTRA_RESULT_CODE_TYPE);
                    String content = data.getStringExtra(Constant.EXTRA_RESULT_CONTENT);
                    Toast.makeText(MainActivity.this,"codeType:" + type
                            + "-----content:" + content,Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
