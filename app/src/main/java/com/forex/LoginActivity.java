package com.forex;

import static com.forex.PublicUrl.URLGETLOCATION;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private EditText email, password;
    private Button btn_login;
    private ProgressBar loading;
    private TextView registerBtn, forgotBtn;

    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sessionManager = new SessionManager(this);

        email = findViewById(R.id.emailId);
        password = findViewById(R.id.passwordId);
        btn_login = findViewById(R.id.btnLoginId);
        forgotBtn = findViewById(R.id.forgotBtn);
        loading = findViewById(R.id.loadingId);
        registerBtn = findViewById(R.id.registerBtn);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), RegisterActivity.class);
                view.getContext().startActivity(intent);
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final String emailStr = email.getText().toString().trim();
                final String passwordStr = password.getText().toString().trim();
                if (!emailStr.isEmpty() && !passwordStr.isEmpty()){
                    login(emailStr, passwordStr);
                }else {
                    email.setError("Enter valid email");
                    password.setError("Enter valid password");
                }

            }
        });
    }
    private void login(final String email, final String password){
        loading.setVisibility(View.VISIBLE);
        btn_login.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLGETLOCATION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    String message = jsonObject.getString("message");
                    JSONArray jsonArray = jsonObject.getJSONArray("login");
                    if (success.equals("1")){
                        for (int i = 0; i < jsonArray.length(); i++){
                            loading.setVisibility(View.GONE);
                            JSONObject object = jsonArray.getJSONObject(i);
                            //String name = object.getString("name").trim();
                            String email = object.getString("email").trim();
                            sessionManager.createSession(email);
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            //intent.putExtra("name",name);
                            intent.putExtra("email", email);
                            startActivity(intent);
                            btn_login.setVisibility(View.VISIBLE);
                            LoginActivity.this.finish();
                        }

                    }else {
                        Toast.makeText(LoginActivity.this,message, Toast.LENGTH_SHORT).show();
                        loading.setVisibility(View.GONE);
                        btn_login.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this,"login failed "+e.toString(), Toast.LENGTH_SHORT).show();
                    loading.setVisibility(View.GONE);
                    btn_login.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this,"login error "+error.toString(), Toast.LENGTH_SHORT).show();
                loading.setVisibility(View.GONE);
                btn_login.setVisibility(View.VISIBLE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parms = new HashMap<String, String>();
                parms.put("login", "login");
                parms.put("email", email);
                parms.put("password", password);
                return parms;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }
}