package com.example.rodrigo.trukertrukersoft.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rodrigo.trukertrukersoft.R;
import com.example.rodrigo.trukertrukersoft.service.ITruckerSoftService;
import com.example.rodrigo.trukertrukersoft.service.request.UserRegistryRequest;
import com.example.rodrigo.trukertrukersoft.service.response.UserRegistryResponse;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

    @InjectView(R.id.input_name) EditText _nameText;
    @InjectView(R.id.input_age) EditText _ageText;
    @InjectView(R.id.input_email) EditText _emailText;
    @InjectView(R.id.input_phone) EditText _phoneText;
    @InjectView(R.id.input_carnet) EditText _carnetText;
    @InjectView(R.id.input_user) EditText _userText;
    @InjectView(R.id.input_password) EditText _passwordText;
    @InjectView(R.id.input_password_confirm) EditText _passwordConfirmText;
    @InjectView(R.id.btn_signup) Button _signupButton;
    @InjectView(R.id.link_login) TextView _loginLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.inject(this);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the LoginRequest activity
                finish();
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creado cuenta...");
        progressDialog.show();

        String name = _nameText.getText().toString();
        short age =  Short.parseShort(_ageText.getText().toString());
        String email = _emailText.getText().toString();
        String phone = _phoneText.getText().toString();
        String carnet = _carnetText.getText().toString();
        String user = _userText.getText().toString();
        String password = _passwordText.getText().toString();
        //String passwordConfirm = _passwordConfirmText.getText().toString();

        UserRegistryRequest userRegistryRequest = new UserRegistryRequest();
        userRegistryRequest.setAge(age);
        userRegistryRequest.setEmail(email);
        userRegistryRequest.setFullName(name);
        userRegistryRequest.setLadaId(2);
        userRegistryRequest.setLicense(carnet);
        userRegistryRequest.setPassword(password);
        userRegistryRequest.setPhone(phone);
        userRegistryRequest.setUsername(user);

        // TODO: Implement your own signup logic here.
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://truckersoftservices.azurewebsites.net/api/").build();
        ITruckerSoftService service = restAdapter.create(ITruckerSoftService.class);
        service.UserRegistry(userRegistryRequest, new Callback<UserRegistryResponse>() {

            @Override
            public void success(UserRegistryResponse userRegistryResponse, Response response) {
                Toast.makeText(getBaseContext(), userRegistryResponse.getMessage(), Toast.LENGTH_LONG).show();
                if(userRegistryResponse.isSuccess()){
                    Intent intent = new Intent(SignupActivity.this, MainMenuActivity.class);
                    intent.putExtra("age", _ageText.getText().toString());
                    intent.putExtra("email", _emailText.getText().toString());
                    intent.putExtra("name", _nameText.getText().toString());
                    intent.putExtra("license", _carnetText.getText().toString());
                    intent.putExtra("password", _passwordText.getText().toString());
                    intent.putExtra("phone", _phoneText.getText().toString());
                    intent.putExtra("user", _userText.getText().toString());
                    //intent.putExtra("userid", userRegistryResponse.getUserId());
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
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Falló inicio de sesión", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String age = _ageText.getText().toString();
        String phone = _phoneText.getText().toString();
        String license = _carnetText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String passwordconfirm = _passwordConfirmText.getText().toString();
        String user = _userText.getText().toString();

        if (user.isEmpty() || user.length() < 5) {
            _userText.setError("Nombre de suario incorrecto, debe de ser mayor a 5 caracteres");
            valid = false;
        } else {
            _userText.setError(null);
        }

        if (name.isEmpty() || name.length() < 5) {
            _nameText.setError("Nombre debe de ser mayor a 5 caracteres");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (age.isEmpty()) {
            _ageText.setError("El campo edad es obligatorio");
            valid = false;
        } else {
            _ageText.setError(null);
        }

        if (phone.isEmpty() && phone.length() < 10) {
            _phoneText.setError("El campo teléfono es obligatorio, mayor de 10");
            valid = false;
        } else {
            _phoneText.setError(null);
        }

        if (license.isEmpty()) {
            _carnetText.setError("El campo carnet es obligatorio");
            valid = false;
        } else {
            _carnetText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("Ingresa un correo válido");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("La contraseña es incorrecta, mayor a 4 caracteres y menor de 10 caracteres");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (passwordconfirm.isEmpty() || !password.equals(passwordconfirm)) {
            _passwordConfirmText.setError("la confirmación de contraseña es incorrecta");
            valid = false;
        } else {
            _passwordConfirmText.setError(null);
        }

        return valid;
    }
}
