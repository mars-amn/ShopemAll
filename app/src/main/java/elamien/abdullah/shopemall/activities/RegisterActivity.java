package elamien.abdullah.shopemall.activities;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import elamien.abdullah.shopemall.GlideApp;
import elamien.abdullah.shopemall.R;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.loginMemberLabelTextView)
    TextView existedMembershipLabel;
    @BindView(R.id.registerLayoutParent)
    ConstraintLayout registerLayoutParent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
      //  loadBackgroundImage();
        setupExistedMembershipLabel();
    }

    private void loadBackgroundImage() {
        GlideApp.with(this)
                .load("https://goo.gl/qExfGq")
                .placeholder(R.drawable.place_holder)
                .error(R.drawable.error_holder)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        registerLayoutParent.setBackground(resource);
                    }
                });
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

    @OnClick(R.id.signinWithGoogleImageView)
    public void onSigninGoogleClick() {
    }
}
