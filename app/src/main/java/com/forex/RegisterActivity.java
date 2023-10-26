package com.forex;

import static com.forex.PublicUrl.URLGETLOCATION;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private EditText emailEdit, passwordEdit,passwordConfirmEdit;
    private Button btn_register;
    private ProgressBar loading;
    String emailStr,passwordStr;
    SessionManager sessionManager;
    private TextView loginBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailEdit = findViewById(R.id.email);
        passwordEdit = findViewById(R.id.password);
        passwordConfirmEdit = findViewById(R.id.passwordConfirm);
        btn_register = findViewById(R.id.registerBtn);
        loading = findViewById(R.id.loadingId);
        loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), LoginActivity.class);
                view.getContext().startActivity(intent);
            }
        });
        btn_register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                emailStr = emailEdit.getText().toString().trim();
                passwordStr = passwordEdit.getText().toString().trim();
                final String passwordConfirmStr = passwordConfirmEdit.getText().toString().trim();
                if (!emailStr.isEmpty() && !passwordStr.isEmpty() && !passwordConfirmStr.isEmpty() &&passwordStr.equals(passwordConfirmStr)){
                    register();
                }else {
                    Toast.makeText(RegisterActivity.this,"Enter all details", Toast.LENGTH_SHORT).show();
                }
                if (emailStr.isEmpty()) {
                    emailEdit.setError("Enter valid email");
                    Toast.makeText(RegisterActivity.this,"Only Gmail allowed", Toast.LENGTH_SHORT).show();
                }  if (passwordStr.isEmpty()) {
                    passwordEdit.setError("Enter valid password");
                }  if (passwordConfirmStr.isEmpty()) {
                    passwordConfirmEdit.setError("Enter valid confirm password");
                }  if (!passwordStr.equals(passwordConfirmStr)) {
                    Toast.makeText(RegisterActivity.this,"Password not match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void register(){
        loading.setVisibility(View.VISIBLE);
        btn_register.setVisibility(View.GONE);
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, URLGETLOCATION, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    String message = jsonObject.getString("message");
                    if (success.equals("1")){
                        loading.setVisibility(View.GONE);
                        Toast.makeText(RegisterActivity.this,message, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        RegisterActivity.this.finish();
                    }else {
                        loading.setVisibility(View.GONE);
                        btn_register.setVisibility(View.VISIBLE);
                        Toast.makeText(RegisterActivity.this,message, Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    loading.setVisibility(View.GONE);
                    Toast.makeText(RegisterActivity.this,"Register failed "+e.toString(), Toast.LENGTH_SHORT).show();

                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.setVisibility(View.GONE);
                Toast.makeText(RegisterActivity.this,"Register error "+error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parms = new HashMap<String, String>();
                parms.put("register", "register");
                parms.put("email", emailStr);
                parms.put("password", passwordStr);
                return parms;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }
}