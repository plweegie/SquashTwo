package com.plweegie.android.squashtwo.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.plweegie.android.squashtwo.R;
import com.plweegie.android.squashtwo.auth.GithubOauth;
import com.plweegie.android.squashtwo.auth.ResultCode;

import java.util.ArrayList;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    private Button mLoginBtn;
    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mActivity = this;

        String clientId = getString(R.string.client_id);
        String clientSecret = getString(R.string.client_secret);

        mLoginBtn = findViewById(R.id.login_btn);
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GithubOauth.Builder()
                        .withClientId(clientId)
                        .withClientSecret(clientSecret)
                        .withContext(mActivity)
                        .withScopeList(new ArrayList(Arrays.asList("public_repo")))
                        .packageName("com.plweegie.android.squashtwo")
                        .nextActivity("com.plweegie.android.squashtwo.ui.GithubPagerActivity")
                        .debug(true)
                        .execute();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == ResultCode.SUCCESS) {
            if (requestCode == GithubOauth.REQUEST_CODE) {
                finish();
            }
        }
    }
}
