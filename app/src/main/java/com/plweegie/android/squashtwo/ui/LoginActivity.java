package com.plweegie.android.squashtwo.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsetsController;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.plweegie.android.squashtwo.R;
import com.plweegie.android.squashtwo.auth.GithubOauth;
import com.plweegie.android.squashtwo.auth.ResultCode;
import com.plweegie.android.squashtwo.databinding.ActivityLoginBinding;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private Button mLoginBtn;
    private Activity mActivity;
    private ActivityLoginBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getWindow().getInsetsController().setSystemBarsAppearance(
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            );
        } else {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorToolbar));

        setSupportActionBar(mBinding.mainToolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
            ViewCompat.setOnApplyWindowInsetsListener(mBinding.mainToolbar, (v, insets) -> {
                Insets viewInsets = insets.getInsets(WindowInsetsCompat.Type.statusBars());

                ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
                mlp.topMargin = viewInsets.top;
                v.setLayoutParams(mlp);

                return WindowInsetsCompat.CONSUMED;
            });
        }

        mActivity = this;

        String clientId = getString(R.string.client_id);
        String clientSecret = getString(R.string.client_secret);

        mLoginBtn = findViewById(R.id.login_btn);
        mLoginBtn.setOnClickListener(view -> GithubOauth.Builder()
                .withClientId(clientId)
                .withClientSecret(clientSecret)
                .withContext(mActivity)
                .withScopeList(new ArrayList<>(List.of("public_repo")))
                .packageName("com.plweegie.android.squashtwo")
                .nextActivity("com.plweegie.android.squashtwo.ui.GithubPagerActivity")
                .debug(true)
                .execute());
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
