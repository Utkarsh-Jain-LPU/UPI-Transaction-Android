package com.utkarsh.upi_;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payUsingUpi();
            }
        });
    }

    void payUsingUpi() {

        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", "7007582341@ybl")
                .appendQueryParameter("pn", "Developer Leo")
                .appendQueryParameter("tn", "Message")
                .appendQueryParameter("am", "1")
                .appendQueryParameter("cu", "INR")
                .build();

        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);
        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");
        if(null != chooser.resolveActivity(getPackageManager())) {
            startActivityForResult(chooser,131);
        } else {
            Toast.makeText(MainActivity.this,"No UPI Application found...",Toast.LENGTH_SHORT).show();
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 131) {
            if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                if (data != null) {
                    String text = data.getStringExtra("response");
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add(text);
                    upiPaymentDataOperation(dataList);
                }
                else {
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
            }
            else {
                ArrayList<String> dataList = new ArrayList<>();
                dataList.add("nothing");
                upiPaymentDataOperation(dataList);
            }
        }
    }

    private void upiPaymentDataOperation(ArrayList<String> data) {
        String str = data.get(0);
        String paymentCancel = "";
        if (str == null)
            str = "discard";
        String status = "";
        String[] response = str.split("&");
        for (String s : response) {
            String[] equalStr = s.split("=");
            if (equalStr.length >= 2) {
                if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                    status = equalStr[1].toLowerCase();
                }
            } else {
                paymentCancel = "Payment Cancelled by User...";
            }
        }
        if (status.equals("success")) {
            Toast.makeText(MainActivity.this, "Transaction Successful...", Toast.LENGTH_SHORT).show();
        } else if ("Payment Cancelled by User...".equals(paymentCancel)) {
            Toast.makeText(MainActivity.this, "Payment Cancelled by User...", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "Transaction Failed...", Toast.LENGTH_SHORT).show();

        }
    }
}
