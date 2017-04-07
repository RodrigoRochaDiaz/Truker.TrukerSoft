package com.example.rodrigo.trukertrukersoft.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rodrigo.trukertrukersoft.R;
import com.example.rodrigo.trukertrukersoft.service.ITruckerSoftService;
import com.example.rodrigo.trukertrukersoft.service.request.LoginRequest;
import com.example.rodrigo.trukertrukersoft.service.response.LoginResponse;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    @InjectView(R.id.input_user) EditText _userText;
    @InjectView(R.id.input_password) EditText _passwordText;
    @InjectView(R.id.btn_login) Button _loginButton;
    @InjectView(R.id.link_signup) TextView _signupLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
    }

    public void login() {
        Log.d(TAG, "LoginRequest");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Autenticando...");
        progressDialog.show();

        String user = _userText.getText().toString();
        String password = _passwordText.getText().toString();

        LoginRequest loginRequest = new LoginRequest(user, password);
        loginRequest.setUsername(user);
        loginRequest.setPassword(password);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://truckersoftservices.azurewebsites.net/api/").build();
        ITruckerSoftService service = restAdapter.create(ITruckerSoftService.class);
        service.Login(loginRequest, new Callback<LoginResponse>() {

            @Override
            public void success(LoginResponse loginResponse, Response response) {
                Toast.makeText(getBaseContext(), loginResponse.getMessage(), Toast.LENGTH_LONG).show();
                if(loginResponse.isSuccess()){
                    Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
                    intent.putExtra("age", loginResponse.getAge());
                    intent.putExtra("email", loginResponse.getEmail());
                    intent.putExtra("name", loginResponse.getFullName());
                    intent.putExtra("license", loginResponse.getLicense());
                    intent.putExtra("password", loginResponse.getPassword());
                    intent.putExtra("phone", loginResponse.getPhone());
                    intent.putExtra("user", loginResponse.getUsername());
                    intent.putExtra("userid", loginResponse.getUserId());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                progressDialog.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getBaseContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "LoginRequest failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String user = _userText.getText().toString();
        String password = _passwordText.getText().toString();

        if (user.isEmpty() || user.length() < 4 || user.length() > 10) {// || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _userText.setError("Ingresar un nombre usuario válido");
            valid = false;
        } else {
            _userText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("La contraceña debe de ser alfanumérica, entre 4 y 10 caracteres");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

