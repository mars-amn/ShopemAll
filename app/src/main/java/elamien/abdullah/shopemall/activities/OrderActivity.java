package elamien.abdullah.shopemall.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.braintreepayments.cardform.view.CardForm;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;
import com.ramotion.cardslider.CardSliderLayoutManager;
import com.ramotion.cardslider.CardSnapHelper;
import com.stripe.android.model.Card;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import elamien.abdullah.shopemall.R;
import elamien.abdullah.shopemall.adapters.ProductImagesAdapter;
import elamien.abdullah.shopemall.model.CreditCard;
import elamien.abdullah.shopemall.model.Order;
import elamien.abdullah.shopemall.model.Product;
import elamien.abdullah.shopemall.model.User;
import elamien.abdullah.shopemall.utils.Constants;

public class OrderActivity extends AppCompatActivity {

    private static final int EXP_MAX_MONTH_NUMBER = 12;
    private final List<Product> mCartProducts = new ArrayList<>();
    private final TextWatcher mExpMonthTextWatcher;
    private final TextWatcher mExpYearTextWatcher;
    @BindView(R.id.nameInputEditText)
    EditText mNameEditText;
    @BindView(R.id.addressInputEditText)
    EditText mAddressEditText;
    @BindView(R.id.zipInputEditText)
    EditText mZipEdiText;
    @BindView(R.id.phoneNumberInputEditText)
    EditText mPhoneEditText;
    @BindView(R.id.emailInputEdiText)
    EditText mEmailEditText;
    @BindView(R.id.creditCardExpirationMonthInputEditText)
    EditText mExpMonthEditText;
    @BindView(R.id.creditCardForm)
    CardForm mCardForm;
    @BindView(R.id.proceedToOrderButton)
    Button mOrderButton;
    @BindView(R.id.creditCardExpirationYearInputEditText)
    EditText mExpYearEditText;
    @BindView(R.id.countryCodePicker)
    CountryCodePicker mCountryCodePicker;
    @BindView(R.id.totalPriceOrderTextView)
    TextView mTotalPriceTextView;
    @BindView(R.id.creditCardFormParent)
    LinearLayout mParentLayout;
    @BindView(R.id.loadingIndicator)
    ProgressBar mLoadingIndicator;
    @BindView(R.id.cartImagesRecyclerView)
    RecyclerView mImagesRecyclerView;
    FirebaseDatabase mDatabase;
    DatabaseReference mReference;
    FirebaseUser mCurrentUser;
    FirebaseAuth mAuth;
    private float mProductsPrice;
    private float mProductsDiscount;
    private ProductImagesAdapter mImagesAdapter;
    private User mUser;

    {
        mExpYearTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 2) {
                    mExpYearEditText.setError(getString(R.string.order_credit_card_exp_year_error_msg));
                    if (mOrderButton.isEnabled()) {
                        mOrderButton.setEnabled(false);
                    }
                } else {
                    mOrderButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
    }

    {
        mExpMonthTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int expMonth;
                if (charSequence.length() == 0 || charSequence.equals("")) {
                    return;
                } else {
                    expMonth = Integer.parseInt(charSequence.toString());
                    if (expMonth > EXP_MAX_MONTH_NUMBER || expMonth <= 0) {
                        mExpMonthEditText.setError(getString(R.string.order_credit_card_exp_month_error_msg));
                        if (mOrderButton.isEnabled()) {
                            mOrderButton.setEnabled(false);
                        }

                    } else {
                        mOrderButton.setEnabled(true);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.order_activity_label);
        }

        setupFirebase();
        loadShoppingCart();
        setupCreditCardForm();
        setupUserEditTexts();
    }

    private void setupRecyclerView() {
        mImagesAdapter = new ProductImagesAdapter(mCartProducts, this);
        mImagesRecyclerView.setLayoutManager(new CardSliderLayoutManager(this));
        new CardSnapHelper().attachToRecyclerView(mImagesRecyclerView);
        mImagesRecyclerView.setAdapter(mImagesAdapter);
    }

    private void setupCreditCardForm() {
        mCardForm.cardRequired(true)
                .cvvRequired(true)
                .setup(this);
    }

    private void loadShoppingCart() {
        showLoadingIndicator();
        mReference.orderByChild(Constants.USER_ID_CHILD).equalTo(mCurrentUser.getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                mUser = dataSnapshot.getValue(User.class);
                mCartProducts.addAll(mUser.getCart().getCartProducts());
                mImagesAdapter.setProducts(mCartProducts);
                hideLoadingIndicator();
                setPrice();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        setupRecyclerView();

    }

    @SuppressLint("SetTextI18n")
    private void setPrice() {
        int productQuantity;
        mProductsPrice = 0;
        float sale;
        mProductsDiscount = 0;
        for (int i = 0; i < mCartProducts.size(); i++) {
            productQuantity = mCartProducts.get(i).getShoppingCartQuantity();
            mProductsPrice = mProductsPrice + (productQuantity * mCartProducts.get(i).getPrice());
            sale = mCartProducts.get(i).getSale();
            if (sale > 0) {
                mProductsDiscount = mProductsDiscount + (productQuantity * sale);
            }
        }

        String priceAfterDiscount = new DecimalFormat().format(mProductsPrice - mProductsDiscount);
        mTotalPriceTextView.setText(getString(R.string.label_charged_msg) + " $" + priceAfterDiscount);
    }

    private void showLoadingIndicator() {
        mImagesRecyclerView.setVisibility(View.GONE);
        mLoadingIndicator.setVisibility(View.VISIBLE);
        mParentLayout.setVisibility(View.GONE);
    }

    private void hideLoadingIndicator() {
        mParentLayout.setVisibility(View.VISIBLE);
        mLoadingIndicator.setVisibility(View.GONE);
        mImagesRecyclerView.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.proceedToOrderButton)
    public void onProceedOrderClick() {
        if (isInputsAreValid()) {
            addCreditCardInformation();
        }
    }

    private void addCreditCardInformation() {
        final String name = mNameEditText.getText().toString().trim();
        final String address = mAddressEditText.getText().toString().trim();
        final int zip = Integer.parseInt(mZipEdiText.getText().toString());
        String countryCode = mCountryCodePicker.getSelectedCountryCode();
        final String phoneNumber = countryCode + mPhoneEditText.getText().toString();
        final String email = mEmailEditText.getText().toString().trim();
        String cardNumber = mCardForm.getCardNumber();
        int cvv = Integer.parseInt(mCardForm.getCvv());
        int expMonth = Integer.parseInt(mExpMonthEditText.getText().toString());
        int expYear = Integer.parseInt(mExpYearEditText.getText().toString());
        final String userId = mCurrentUser.getUid();

        Card stripCard = new Card(cardNumber, expMonth, expYear, String.valueOf(cvv));
        if (!stripCard.validateCard()) {
            Toast.makeText(this, R.string.credit_card_validation_error_msg, Toast.LENGTH_SHORT).show();
        } else {
            CreditCard card = new CreditCard(cardNumber, expMonth, expYear, cvv);
            showLoadingIndicator();

            Order order = new Order(name, address, zip, phoneNumber
                    , email, mCurrentUser.getEmail(), userId,
                    mUser, mProductsPrice, card, mProductsDiscount,
                    mProductsPrice - mProductsDiscount);
            DatabaseReference reference = mDatabase.getReference().child(Constants.ORDER_PRODUCTS_CHILD);
            reference.push().setValue(order).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        showInformativeDialog();
                    } else {
                        Toast.makeText(OrderActivity.this, R.string.order_unsuccessful_error_msg, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void showInformativeDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(R.string.order_status_msg);
        dialog.setMessage(R.string.order_status_dialog_msg);
        dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialogInterface, int i) {
                mReference.orderByChild(Constants.USER_ID_CHILD).equalTo(mCurrentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().removeValue();
                        dialogInterface.dismiss();
                        startActivity(new Intent(OrderActivity.this, MainActivity.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        hideLoadingIndicator();
                    }
                });
            }
        });
        hideLoadingIndicator();
        dialog.show();
    }

    private boolean isInputsAreValid() {
        if (isTextEmpty(mNameEditText)) {
            mNameEditText.setError(getString(R.string.order_name_hint_error_msg));
            return false;
        } else if (isTextEmpty(mAddressEditText)) {
            mAddressEditText.setError(getString(R.string.order_address_hint_error_msg));
            return false;
        } else if (isTextEmpty(mZipEdiText)) {
            mZipEdiText.setError(getString(R.string.order_zip_hint_error_msg));
            return false;
        } else if (!isValidPhone(mPhoneEditText)) {
            mPhoneEditText.setError(getString(R.string.order_phone_hint_error_msg));
            return false;
        } else if (!isValidEmail(mEmailEditText)) {
            mEmailEditText.setError(getString(R.string.order_email_hint_error_msg));
            return false;
        } else if (!mCardForm.isValid()) {
            mCardForm.setCardNumberError(getString(R.string.order_credit_card_details_error_msg));
            mCardForm.setCvvError(getString(R.string.order_credit_card_cvv_error_msg));
            return false;
        } else if (isTextEmpty(mExpMonthEditText)) {
            mExpMonthEditText.setError(getString(R.string.order_exp_month_hint_error_msg));
            return false;
        } else if (isTextEmpty(mExpYearEditText)) {
            mExpYearEditText.setError(getString(R.string.order_exp_year_hint_error_msg));
            return false;
        }
        return true;
    }


    private boolean isValidPhone(EditText phoneEditText) {
        CharSequence phone = phoneEditText.getText().toString();
        return (!TextUtils.isEmpty(phone) && Patterns.PHONE.matcher(phone).matches());
    }

    private boolean isTextEmpty(EditText editText) {
        CharSequence text = editText.getText().toString();
        return TextUtils.isEmpty(text);
    }

    private boolean isValidEmail(EditText emailEditText) {
        CharSequence email = emailEditText.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    private void setupFirebase() {
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference().child(Constants.USER_CHILD);
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
    }

    private void setupUserEditTexts() {
        mEmailEditText.setText(mCurrentUser.getEmail());
        mPhoneEditText.setText(mCurrentUser.getPhoneNumber());
        mExpMonthEditText.addTextChangedListener(mExpMonthTextWatcher);
        mExpYearEditText.addTextChangedListener(mExpYearTextWatcher);
    }
}
