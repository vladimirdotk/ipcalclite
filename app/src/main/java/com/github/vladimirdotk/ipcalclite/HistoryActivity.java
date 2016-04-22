package com.github.vladimirdotk.ipcalclite;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Set;

public class HistoryActivity extends AppCompatActivity {

    private Button calculatorBtn;

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        sp = getSharedPreferences(IpCalcActivity.APP_PREFERENCES, MODE_PRIVATE);
        Set<String> data = sp.getStringSet(IpCalcActivity.IP_DATA_KEY, new HashSet<String>());

        if (!data.isEmpty()) {

            LinearLayout historyLayout = (LinearLayout) findViewById(R.id.historyData);

            for (String ipData: data) {
                String[] ipDataParts = ipData.split("---");
                TextView historyElement = new TextView(this);
                ipData =
                        "IP: " + ipDataParts[0] + "\n" +
                        "Mask: " + ipDataParts[1] + "\n" +
                        "Network IP: " + ipDataParts[2] + "\n" +
                        "Total IPs: " + ipDataParts[3] + "\n";

                ScrollView.LayoutParams lp = (ScrollView.LayoutParams) historyLayout.getLayoutParams();
                lp.setMargins(0,25,0,0);
                historyElement.setLayoutParams(lp);
                historyElement.setText(ipData);
                historyLayout.addView(historyElement);
            }
        }

        calculatorBtn = (Button) findViewById(R.id.calculatorBtn);
        calculatorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HistoryActivity.this, IpCalcActivity.class);
                startActivity(intent);
            }
        });
    }
}
