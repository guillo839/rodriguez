package guillermo.proyectoa.mpos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import guillermo.proyectoa.mpos.Helper.URLHelper;

import com.digital.mpos.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText Identificacion;
    EditText Nombres;
    EditText Apellidos;
    EditText Email;
    EditText Password;
    Button Aceptar;
    EditText Phone;
    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Identificacion=(EditText)findViewById(R.id.Identificacion);
        Nombres=(EditText)findViewById(R.id.EditTextName);
        Apellidos=(EditText)findViewById(R.id.EditTextLastName);
        Email=(EditText)findViewById(R.id.EditTextEmail);
        Password=(EditText)findViewById(R.id.EditPassword);
        Phone=(EditText)findViewById(R.id.EditTextPhone);
        Aceptar =(Button)findViewById(R.id.Next);
        Aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.setVisibility(View.VISIBLE);
                Register();
            }
        });
        spinner = (ProgressBar) findViewById(R.id.progressBar);





    }
    private void Register()
    {
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
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                spinner.setVisibility(View.GONE);
                                startActivity(intent);

                            } else {

                                Toast.makeText(getApplicationContext(), "Error al registrarse. Verifique la conexión a internet" + jsonObj.optString("Mensaje"), Toast.LENGTH_LONG).show();
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
                params.put("email",Email.getText().toString());
                params.put("identificacion",Identificacion.getText().toString() );
                params.put("PWD",Password.getText().toString() );
                params.put("nombres",Nombres.getText().toString());
                params.put("apellidos",Apellidos.getText().toString() );
                params.put("movil",Phone.getText().toString() );

                params.put("opcion", "1");

                return params;
            }
        };
        queue.add(postRequest);
    }

}
