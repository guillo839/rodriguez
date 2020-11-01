package guillermo.proyectoa.mpos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.digital.mpos.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Activity_Transaction_Detail extends AppCompatActivity {
    String NombresCompletos;
    String NumeroTarjeta;
    String Email;
    //String Dni;
    String phone;
    Double amount = 0.0;
    Double amount2 = 0.0;
    public Boolean bPago = false;
    Boolean bGuardo = false;
    List transactionInfo;
    Button btnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_detail);
        Intent iin = getIntent();
        Bundle b = iin.getExtras();

        if (b != null) {
            String j = (String) b.get("username");
            Email = j;
            phone = b.getString("movil").toString();
            NombresCompletos = b.getString("nombres");
            amount = Double.parseDouble(b.getString("amount"));
            amount2 = Double.parseDouble(b.getString("amount2"));
            NumeroTarjeta = b.getString("NumeroTarjeta");

        }

        TextView lblCardNumber = findViewById(R.id.cardNumber);
        TextView lblDate = findViewById(R.id.date);

        TextView lblAmount = findViewById(R.id.amount);
        TextView lblVat = findViewById(R.id.vat);
        TextView lblTotal = findViewById(R.id.total);
        TextView lblStatus = findViewById(R.id.status);
        TextView lblEmail = findViewById(R.id.email);
        TextView lblPhone = findViewById(R.id.phone);
        TextView lblStatusNumber = findViewById(R.id.statusNumber);
        ImageView img = findViewById(R.id.imgCard);
        Button buttonAccept = findViewById(R.id.buttonConfirm);

//        transactionInfo= new ArrayList();
//        transactionInfo.add(NumeroTarjeta);
//        transactionInfo.add(amount);
//        transactionInfo.add(amount);

        Date date = new Date();
        SimpleDateFormat objSDF = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String Fecha = objSDF.format(date);
        Calendar cal = Calendar.getInstance();
        String hora = objSDF.format(cal.getTime());

        DecimalFormat df = new DecimalFormat("###,###.##");
        String valornet=df.format(amount);



        lblCardNumber.setText(NumeroTarjeta);
        lblTotal.setText(valornet.toString());
        lblStatus.setText("APROBADA");
        lblAmount.setText(valornet.toString());
        lblVat.setText("0.00");
        lblEmail.setText(Email);
        lblPhone.setText(phone);
        lblDate.setText(Fecha);


        if (lblCardNumber.getText().toString().substring(0, 1).equals("4")) {
            img.setImageResource(R.drawable.visa);

        }
        if (lblCardNumber.getText().toString().substring(0, 2).equals("51") || lblCardNumber.getText().toString().substring(0, 2).equals( "55") || lblCardNumber.getText().toString().substring(0, 1).equals("5")) {
            img.setImageResource(R.drawable.mastercard);

        }
        if (lblCardNumber.getText().toString().substring(0, 2).equals( "34") || lblCardNumber.getText().toString().substring(0, 2).equals("37")) {
            img.setImageResource(R.drawable.amex);

        }
        if (lblCardNumber.getText().toString().substring(0, 2).equals("60")) {
            img.setImageResource(R.drawable.discover);
        }

    }



}

