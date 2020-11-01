package guillermo.proyectoa.mpos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import guillermo.proyectoa.mpos.Helper.SharedHelper;
import guillermo.proyectoa.mpos.Helper.URLHelper;

import com.digital.mpos.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private Button button_login_login;
    private Button register;
    private EditText editText_login_username;
    private EditText editText_login_password;
    private String username;
    private String password;
    private String baseUrl;
    private Context context;
    private ProgressBar spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        //baseUrl = "http://192.168.1.202/rest.php";
        spinner = (ProgressBar) findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        editText_login_username = (EditText) findViewById(R.id.username);
        editText_login_password = (EditText) findViewById(R.id.password);

        editText_login_username.setText(SharedHelper.getKey(getApplicationContext(), "usuario"));
        editText_login_password.setText(SharedHelper.getKey(getApplicationContext(), "pwd"));
        username = editText_login_username.getText().toString();
        password = editText_login_password.getText().toString();
        button_login_login = (Button) findViewById(R.id.login);
        register = (Button) findViewById(R.id.register);
        spinner.setVisibility(View.VISIBLE);
        Login();
//        if ((SharedHelper.getKey(getApplicationContext(),"usuario")!=null && (SharedHelper.getKey(getApplicationContext(),"usuario").length()>0) && (SharedHelper.getKey(getApplicationContext(),"pwd").length()>0)) && (SharedHelper.getKey(getApplicationContext(),"pwd")!=null))
//        {
//
//            if (!isOnline()) {
//                Toast.makeText(getApplicationContext(), "Verifique la conexión a internet", Toast.LENGTH_LONG).show();
//                return;
//            }
//            goToSecondActivity();
//        }
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register();
            }
        });
        button_login_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!isOnline()) {
                        Toast.makeText(getApplicationContext(), "Verifique la conexión a internet", Toast.LENGTH_LONG).show();
                        return;
                    }
                    username = editText_login_username.getText().toString();
                    password = editText_login_password.getText().toString();

                    //goToSecondActivity();
                    if (username.length() <= 0) {
                        Toast.makeText(getApplicationContext(), "Campos Vacíos. Favor rellenar los campos vacíos", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (password.length() <= 0) {
                        Toast.makeText(getApplicationContext(), "Campos Vacíos. Favor rellenar los campos vacíos", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (!validEmail(username.toString())) {
                        Toast.makeText(getApplicationContext(), "Ingrese un correo electrónico válido", Toast.LENGTH_LONG).show();
                        return;
                    }
                    spinner.setVisibility(View.VISIBLE);
                    Login();

                } catch (Exception ex) {
                }
            }
        });
    }
private void Register ()
{
    Intent intent = new Intent(this, RegisterActivity.class);
    startActivity(intent);
}
    private boolean validEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    private void goToSecondActivity() {
        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        bundle.putString("password", password);
        bundle.putString("movil", SharedHelper.getKey(getApplicationContext(), "movil"));
        bundle.putString("nombres", SharedHelper.getKey(getApplicationContext(), "nombres"));
        //bundle.putString("monto", SharedHelper.getKey(getApplicationContext(), "monto"));


        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    protected void Login() {
        String url = URLHelper.UrlLogin;
        RequestQueue queue = Volley.newRequestQueue(this);
//        Map<String, String> params = new HashMap();
//


        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            String Codigo = jsonObj.optString("Codigo");
                            if (jsonObj.optString("Codigo").toString().equals("00")) {
                                spinner.setVisibility(View.GONE);
                                SharedHelper.putKey(getApplicationContext(), "usuario", jsonObj.optString("Usuario").toString());
                                SharedHelper.putKey(getApplicationContext(), "pwd", password);
                                SharedHelper.putKey(getApplicationContext(), "nombres", jsonObj.optString("Nombres").toString());
                                SharedHelper.putKey(getApplicationContext(), "apellidos", jsonObj.optString("Apellidos").toString());
                                SharedHelper.putKey(getApplicationContext(), "dni", jsonObj.optString("Dni").toString());
                                SharedHelper.putKey(getApplicationContext(), "movil", jsonObj.optString("Movil").toString());
                                //SharedHelper.putKey(getApplicationContext(), "monto", jsonObj.optString("monto").toString());
                                goToSecondActivity();
                            } else {
                                SharedHelper.putKey(getApplicationContext(), "usuario", "");
                                SharedHelper.putKey(getApplicationContext(), "pwd", "");
                                Toast.makeText(getApplicationContext(), "Error en Login. Verifique usuario y contraseña " + jsonObj.optString("Mensaje"), Toast.LENGTH_LONG).show();
                                spinner.setVisibility(View.GONE);
                                return;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        //Log.d("Error.Response", error.getMessage());
                        Toast.makeText(getApplicationContext(), "Error de acceso. Usuario o Contraseña incorrecta. Consulte Sistemas", Toast.LENGTH_LONG).show();
                        spinner.setVisibility(View.GONE);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", username);
                params.put("PWD", password);
                params.put("opcion", "2");

                return params;
            }
        };
        queue.add(postRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                Saldo();

        }

        return super.onOptionsItemSelected(item);

    }

    protected void Saldo() {
        String url = URLHelper.UrlLogin;
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            String Codigo = jsonObj.optString("Codigo");
                            if (jsonObj.optString("Codigo").toString().equals("00")) {
                                //spinner.setVisibility(View.GONE);
                                SharedHelper.putKey(getApplicationContext(), "saldo", jsonObj.optString("valor").toString());


                            } else {
                                Toast.makeText(getApplicationContext(), "Error en Login. Verifique usuario y contraseña " + jsonObj.optString("Mensaje"), Toast.LENGTH_LONG).show();
                                //spinner.setVisibility(View.GONE);
                                return;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        //Log.d("Error.Response", error.getMessage());
                        Toast.makeText(getApplicationContext(), "Error de acceso. Usuario o Contraseña incorrecta. Consulte Sistemas", Toast.LENGTH_LONG).show();
                        //spinner.setVisibility(View.GONE);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", SharedHelper.getKey(getApplicationContext(), "usuario").toString());
                params.put("opcion", "3");

                return params;
            }
        };
        queue.add(postRequest);
    }


}



