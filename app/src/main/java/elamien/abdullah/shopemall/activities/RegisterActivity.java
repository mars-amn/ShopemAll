package elamien.abdullah.shopemall.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import elamien.abdullah.shopemall.GlideApp;
import elamien.abdullah.shopemall.R;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = RegisterActivity.class.getSimpleName();
    @BindView(R.id.loginMemberLabelTextView)
    TextView existedMembershipLabel;
    @BindView(R.id.registerLayoutParent)
    ConstraintLayout registerLayoutParent;
    @BindView(R.id.registerImage)
    KenBurnsView registerImage;
    @BindView(R.id.registerEmailEditText)
    EditText registerEmailEditText;
    @BindView(R.id.registerPasswordEditText)
    EditText registerPasswordEditText;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreenWindow();
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        loadRegisterImage();
        setupExistedMembershipLabel();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            launchMainActivity();
        } else {
            Toast.makeText(this, "User is null", Toast.LENGTH_SHORT).show();
        }
    }

    private void setFullScreenWindow() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void loadRegisterImage() {
        GlideApp.with(this)
                .load(getString(R.string.register_activity_image))
                .placeholder(R.drawable.place_holder)
                .error(R.drawable.error_holder)
                .into(registerImage);
    }

    private void setupExistedMembershipLabel() {
        SpannableString textLink = new SpannableString(getString(R.string.membership_register_label));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Toast.makeText(RegisterActivity.this, "Clicked!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        textLink.setSpan(clickableSpan, 17, 26, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        existedMembershipLabel.setText(textLink);
        existedMembershipLabel.setMovementMethod(LinkMovementMethod.getInstance());
        existedMembershipLabel.setHighlightColor(Color.TRANSPARENT);
    }

    private boolean isTextEmpty(EditText editText) {
        return TextUtils.isEmpty(editText.getText().toString());
    }

    private boolean isEmail(EditText emailET) {
        String email = emailET.getText().toString().trim();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    @OnClick(R.id.signinWithGoogleImageView)
    public void onSigninGoogleClick() {
    }

    @OnClick(R.id.signupButton)
    public void onSignupButtonClick() {
        if (!isEmail(registerEmailEditText)) {
            registerEmailEditText.setError(getString(R.string.register_email_error_msg));
        } else if (isTextEmpty(registerPasswordEditText)) {
            registerPasswordEditText.setError(getString(R.string.register_password_error_msg));
        } else {
            registerNewUser();
        }
    }

    private void registerNewUser() {
        String email = registerEmailEditText.getText().toString().trim();
        String password = registerPasswordEditText.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
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
}
