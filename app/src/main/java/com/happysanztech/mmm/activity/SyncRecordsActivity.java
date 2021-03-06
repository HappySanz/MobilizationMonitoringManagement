package com.happysanztech.mmm.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.happysanztech.mmm.R;
import com.happysanztech.mmm.bean.database.SQLiteHelper;
import com.happysanztech.mmm.helper.AlertDialogHelper;
import com.happysanztech.mmm.interfaces.DialogClickListener;
import com.happysanztech.mmm.syncadapter.SyncLocationRecords;
import com.happysanztech.mmm.syncadapter.SyncProspectRecords;
import com.happysanztech.mmm.utils.CommonUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class SyncRecordsActivity extends AppCompatActivity implements View.OnClickListener, DialogClickListener {

    private static final String TAG = SyncRecordsActivity.class.getName();
    private LinearLayout layLocationRecords, layProstectRecords;
    SQLiteHelper db;

    private TextView txtShowRecordCount, txtShowProspectCount;

    private SyncLocationRecords syncLocationRecords;
    private SyncProspectRecords syncProspectRecords;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_records);
        db = new SQLiteHelper(getApplicationContext());
        syncLocationRecords = new SyncLocationRecords(this);
        syncProspectRecords = new SyncProspectRecords(this);

        layLocationRecords = findViewById(R.id.btnSyncStoredRecords);
        layLocationRecords.setOnClickListener(this);

        layProstectRecords = findViewById(R.id.btnSyncStoredDetails);
        layProstectRecords.setOnClickListener(this);

        txtShowRecordCount = findViewById(R.id.text_data_count);
        txtShowProspectCount = findViewById(R.id.text_prostect_data_count);

        int count = Integer.parseInt(db.isRecordSynced());
        txtShowRecordCount.setText("" + count);

        int countProspect = Integer.parseInt(db.isProspectRecordSynced());
        txtShowProspectCount.setText("" + countProspect);

        ImageView bckbtn = (ImageView) findViewById(R.id.back_tic_his);
        bckbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onClick(View v) {
        if (CommonUtils.isNetworkAvailable(this)) {
            if (v == layLocationRecords) {
                int checkSync = Integer.parseInt(db.isRecordSynced());
                if (checkSync != 0) {
                    try {

                        syncLocationRecords.SyncToServer();

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                } else {
                    AlertDialogHelper.showSimpleAlertDialog(this, "Nothing to sync");
                }

            } else if (v == layProstectRecords) {
                int checkSync = Integer.parseInt(db.isProspectRecordSynced());
                if (checkSync != 0) {
                    try {

                        syncProspectRecords.SyncToServer();

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                } else {
                    AlertDialogHelper.showSimpleAlertDialog(this, "Nothing to sync");
                }
            }

        } else {
            AlertDialogHelper.showSimpleAlertDialog(this, "No Network connection");
        }

    }

    @Override
    public void onAlertPositiveClicked(int tag) {

    }

    @Override
    public void onAlertNegativeClicked(int tag) {

    }
}
