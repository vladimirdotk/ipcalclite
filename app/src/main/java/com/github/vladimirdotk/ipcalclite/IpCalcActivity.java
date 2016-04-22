package com.github.vladimirdotk.ipcalclite;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;

public class IpCalcActivity extends AppCompatActivity {

    private TextView ipField;
    private String ipAdress;

    private TextView maskField;
    private String networkMask;

    private TextView networkIpField;
    private String networkIp;

    private TextView totalIpsField;
    private String totalIps;

    private Button calculateBtn;
    private Button historyBtn;

    private SharedPreferences sp;

    public static final String APP_PREFERENCES = "ipCalcLiteData";
    public static final String IP_DATA_KEY = "ipData";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip_calc);
        initObjects();
        setDefaultValues();
        bindButtons();
    }

    private void initObjects() {
        ipField = (TextView) findViewById(R.id.ip_address);
        maskField = (TextView) findViewById(R.id.ip_mask);
        networkIpField = (TextView) findViewById(R.id.networkIpValue);
        totalIpsField = (TextView) findViewById(R.id.totalIpsValue);
        calculateBtn = (Button) findViewById(R.id.calculateBtn);
        historyBtn = (Button) findViewById(R.id.historyBtn);
    }

    private void setDefaultValues() {
        networkIpField.setText(R.string.default_value);
        totalIpsField.setText(R.string.default_value);
    }

    private void bindButtons() {
        calculateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ipAdress = ipField.getText().toString();
                networkMask = maskField.getText().toString();

                if (!IpUtils.validateIp(ipAdress)) {
                    showErrorToast("Please specify correct ip!");
                    setDefaultValues();
                    return;
                }

                if (!IpUtils.validateMask(networkMask)) {
                    showErrorToast("Please specify correct mask!");
                    setDefaultValues();
                    return;
                }

                networkIp = IpUtils.calculateNetworkIp(ipAdress, networkMask);
                networkIpField.setText(networkIp);

                totalIps = IpUtils.calculateTotalIps(networkMask);
                totalIpsField.setText(totalIps);

                addDataToSp(ipAdress, networkMask, networkIp, totalIps);
            }
        });

        historyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IpCalcActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });

    }

    private void showErrorToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    public Set<String> getDataFromSp() {
        return sp.getStringSet(IP_DATA_KEY, new HashSet<String>());
    }

    private void addDataToSp(String ipAdress, String networkMask, String networkIp, String totalIps) {
        sp = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        Set<String> ipData = getDataFromSp();
        String data = ipAdress + "---" + networkMask + "---" + networkIp + "---" + totalIps;

        if (!ipData.contains(data)) {
            ipData.add(data);
        }

        e.putStringSet(IP_DATA_KEY, ipData);
        e.apply();
    }
}
