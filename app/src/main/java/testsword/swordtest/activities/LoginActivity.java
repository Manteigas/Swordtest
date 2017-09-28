package testsword.swordtest.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import testsword.swordtest.R;
import testsword.swordtest.utils.Utils;

public class LoginActivity extends Activity {

    /** UI Elements **/
    private EditText etEmail, etPassword;
    private Button btLogin;
    private TextView tvEmail, tvPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /** Tests Only **/
        Utils.saveUserInfo(this, "andre@testes.pt", "123456");

        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvPassword = (TextView) findViewById(R.id.tvPassword);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btLogin = (Button) findViewById(R.id.btLogin);

        Typeface avenirMedium = Typeface.createFromAsset(getApplicationContext().getAssets(), "AvenirLTStd-Medium.otf");
        tvEmail.setTypeface(avenirMedium);
        tvPassword.setTypeface(avenirMedium);

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
                v.startAnimation(buttonClick);

                /** Email and Password verification **/
                if (TextUtils.isEmpty(etEmail.getText())) {
                    etEmail.setError(getString(R.string.er_mandatory));
                    etEmail.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(etEmail.getText()).matches()) {
                    etEmail.setError(getString(R.string.er_invalide_email));
                    etEmail.requestFocus();
                } else if (TextUtils.isEmpty(etPassword.getText())) {
                    etPassword.setError(getString(R.string.er_mandatory));
                    etPassword.requestFocus();
                } else if (etPassword.getText().toString().length() < 6) {
                    etPassword.setError(getString(R.string.er_lengthpassword));
                    etPassword.requestFocus();
                } else {
                    String username = etEmail.getText().toString();
                    String password = etPassword.getText().toString();

                    int answer = Utils.loginAttempt(getApplicationContext(), username, password);

                    switch (answer) {
                        case 0: displayErrorDialog(getString(R.string.er_nonexistent)); break;
                        case 1: displayErrorDialog(getString(R.string.er_invalid)); break;
                        case 2:
                            Intent intent = new Intent(LoginActivity.this , MainActivity.class);
                            startActivity(intent);
                            finish();
                    }
                }
            }
        });
    }

    /** Display the error resulting of the login attempt**/
    private void displayErrorDialog(String message) {
        AlertDialog.Builder builder;

        builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.er_error))
                .setMessage(message)
                .setNegativeButton(getString(R.string.bt_close), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).setIcon(android.R.drawable.stat_notify_error).show();
    }
}
