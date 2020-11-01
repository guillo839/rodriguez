package guillermo.proyectoa.mpos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import guillermo.proyectoa.mpos.Helper.SharedHelper;

import com.digital.mpos.R;

import java.util.regex.Pattern;

public class DetailActivity extends AppCompatActivity {

    private  EditText nameField;
    private  EditText emailField;
    private  EditText phoneClient;
    private  EditText descripcionCliente;
    private Button btnNext;
    private String phone;
    private String NombresCompletos;
    private String Saldo;
    private String Descripcion;
    private Switch swIva;
    private Switch swDiferido;

    String Email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
            String j =(String) b.get("username");
            Email=j;
            phone=b.getString("movil").toString();
            NombresCompletos=b.getString("nombres");
            Saldo=b.getString("monto");
        }

        nameField = (EditText) findViewById(R.id.EditTextName);


        emailField = (EditText) findViewById(R.id.EditTextEmail);


        phoneClient= (EditText) findViewById(R.id.EditTextPhone);
        descripcionCliente=(EditText) findViewById(R.id.EditDescription);
        swIva = (Switch) findViewById(R.id.swIva);
        swIva.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Log.v("Switch State=", ""+isChecked);
                if (swDiferido.isChecked()) {
                    SharedHelper.putKey(getApplicationContext(), "Iva", "1");
                }
                else
                {
                    SharedHelper.putKey(getApplicationContext(), "Iva", "0");
                }
            }

        });
        swDiferido = (Switch) findViewById(R.id.swDiferido);
        swDiferido.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Log.v("Switch State=", ""+isChecked);
                if (swDiferido.isChecked()) {
                    SharedHelper.putKey(getApplicationContext(), "Diferido", "1");
                }
                else
                {
                    SharedHelper.putKey(getApplicationContext(), "Diferido", "0");
                }
            }

        });
        btnNext=(Button)findViewById(R.id.Next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SharedHelper.getKey(getApplicationContext(),"Diferido")=="1")
                {

                    if (descripcionCliente.getText().toString().length()<=0)
                    {
                        Toast.makeText(getApplicationContext(),"Descripción no puede estar vacío.", Toast.LENGTH_SHORT).show();
                        return ;
                    }
                }


                if (nameField.getText().toString().length()<=0)
                {
                    Toast.makeText(getApplicationContext(),"Nombre del cliente no puede estar vacío.", Toast.LENGTH_SHORT).show();
                    return ;
                }
                if (emailField.getText().toString().length()<=0)
                {
                    Toast.makeText(getApplicationContext(),"Correo del cliente no puede estar vacío.", Toast.LENGTH_SHORT).show();
                    return ;
                }


                if (!validEmail(emailField.getText().toString())) {
                    Toast.makeText(getApplicationContext(),"Ingrese un correo electrónico válido",Toast.LENGTH_LONG).show();
                    return;
                }
                goToSecondActivity();
            }
        });


    }
    private boolean validEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    private void goToSecondActivity() {
        Bundle bundle = new Bundle();
        bundle.putString("cliente", nameField.getText().toString());
        bundle.putString("mailCliente", emailField.getText().toString());
        bundle.putString("phoneCliente", phoneClient.getText().toString());
        bundle.putString("descripcion", descripcionCliente.getText().toString());
//falta descripcion
//


        bundle.putString("username", Email);
        bundle.putString("movil", SharedHelper.getKey(getApplicationContext(), "movil"));
        bundle.putString("nombres", SharedHelper.getKey(getApplicationContext(), "nombres"));
        bundle.putString("monto", SharedHelper.getKey(getApplicationContext(), "monto"));



        Intent intent = new Intent(this, SelectProductActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);

//        //dependiendo si es diferido llama a paymentez sino a la normal
//        if (SharedHelper.getKey(getApplicationContext(),"Diferido")=="1")
//        {
//            Intent intent = new Intent(this, PaymentezActivity.class);
//            intent.putExtras(bundle);
//            startActivity(intent);
//        }
//        else
//        {
//            Intent intent = new Intent(this, AmountActivity.class);
//            intent.putExtras(bundle);
//            startActivity(intent);
//        }



    }

}
