package com.i2btech.amazontest;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.i2btech.amazontest.helpers.ActivityDataFlowHelper;
import com.i2btech.amazontest.utils.ValidationUtils;

import org.w3c.dom.Text;

import java.io.File;

public class LoggedInActivity extends AppCompatActivity {

    private ImageButton imageButton;
    private TextView textView;
    private TextView filepath;
    private AmazonS3 s3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);

        imageButton = (ImageButton) findViewById(R.id.imgProfImage);
        textView = (TextView) findViewById(R.id.textView);
        filepath = (TextView) findViewById(R.id.txtLoggedFilepath);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePicker();
            }
        });
        s3 = ActivityDataFlowHelper.getS3client();
    }

    private void imagePicker() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        //intent.addCategory(Intent.CATEGORY_OPENABLE);
        //intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);

        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(RESULT_OK == resultCode) {
            Uri uri = data.getData();

            try {
                imageButton.setImageURI(uri);
            } catch (Exception e) {
                Toast.makeText(this, "Error con la imagen. " + e.getCause(), Toast.LENGTH_LONG).show();
            }
            uploadPicture(uri);
        }
    }

    private void uploadPicture(Uri uri) {
        if (uri == null || uri.getPath().isEmpty()) {
            Toast.makeText(this, "Ruta inválida", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            String path = ValidationUtils.getPath(getApplicationContext(), uri, this);

            File file = new File(path);
            filepath.setText(file.getPath() + "- Existe? " + file.exists());
            TransferObserver transferObserver = ActivityDataFlowHelper.getTransferUtility().upload("aws2test", file.getName(), file);

            transferObserver.setTransferListener(new TransferListener() {
                @Override
                public void onStateChanged(int id, TransferState state) {
                    //Toast.makeText(LoggedInActivity.this, "Transferencia en estado " + state.name(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                    int percentage = (int) (bytesCurrent / bytesTotal * 100);
                    textView.setText("Transferencia de " + id + ": " + percentage + "/100");
                }

                @Override
                public void onError(int id, Exception ex) {
                    Toast.makeText(LoggedInActivity.this, ValidationUtils.formatException(ex), Toast.LENGTH_LONG).show();
                }
            });

    } catch (Exception e) {
            Toast.makeText(this, "URI: " + uri.getPath(), Toast.LENGTH_LONG).show();
            Toast.makeText(this, "Fallo: " + ValidationUtils.formatException(e), Toast.LENGTH_LONG).show();
        }
    }
}
