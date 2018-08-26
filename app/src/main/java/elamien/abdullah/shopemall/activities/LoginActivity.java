package elamien.abdullah.shopemall.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import elamien.abdullah.shopemall.R;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    @BindView(R.id.loginImage)
    KenBurnsView loginImage;
    @BindView(R.id.loginEmailEditText)
    EditText loginEmailEditText;
    @BindView(R.id.loginPasswordEditText)
    EditText loginPasswordEditText;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();

    }

    @OnClick(R.id.loginButton)
    public void onLoginButtonClick(View view) {
        if (!isEmail(loginEmailEditText)) {
            loginEmailEditText.setError(getString(R.string.register_email_error_msg));
        } else if (isTextEmpty(loginPasswordEditText)) {
            loginPasswordEditText.setError(getString(R.string.register_password_error_msg));
        } else {
            loginUser();
        }
    }

    private void loginUser() {
        String email = loginEmailEditText.getText().toString().trim();
        String password = loginPasswordEditText.getText().toString();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            launchMainActivity();
                        } else {
                            Log.d(TAG, task.getException().getMessage());
                        }
                    }
                });
    }

    private void launchMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean isTextEmpty(EditText editText) {
        return TextUtils.isEmpty(editText.getText().toString());
    }

    private boolean isEmail(EditText emailET) {
        String email = emailET.getText().toString().trim();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }
}
