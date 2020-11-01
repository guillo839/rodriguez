package guillermo.proyectoa.mpos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.digital.mpos.R;

public class productDetailActivity extends AppCompatActivity {
    TextView txtprecio;
    TextView txtproducto;
    String Producto="";
    String Precio="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

         txtproducto=(TextView)findViewById(R.id.txtproducto);
         txtprecio=(TextView)findViewById(R.id.txtPrecioProducto);
        Producto=getIntent().getStringExtra("producto");
        Precio=getIntent().getStringExtra("precio");
        txtproducto.setText(Producto);
        txtprecio.setText(Precio);
        Button pay=findViewById(R.id.pay1);

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    String valornet=txtprecio.getText().toString();
                    //amount.setText();
                    if ( valornet.trim().length() > 0)
                    {

                        goToPayActivity();
                    }


                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(),
                            ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void goToPayActivity() {

        Bundle bundle = new Bundle();
        bundle.putDouble("amount", Double.parseDouble(txtprecio.getText().toString()));
        bundle.putDouble("amount2", Double.parseDouble(txtprecio.getText().toString()));
        bundle.putString("username", "");//Email
        bundle.putString("movil", "");//phone
        bundle.putString("nombres", "");//NombresCompletos

        bundle.putString("cliente", "");//Cliente
        bundle.putString("mailCliente", "");//emailCliente
        bundle.putString("phoneCliente", "");//phoneCliente


        Intent intent = new Intent(this, PayActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
