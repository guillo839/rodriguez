package guillermo.proyectoa.mpos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
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
import guillermo.proyectoa.mpos.Helper.SharedHelper;
import guillermo.proyectoa.mpos.Helper.URLHelper;

import com.digital.mpos.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class PaymentezActivity extends AppCompatActivity {
    private String Cliente;
    private EditText amount;
    private EditText amount2;
    private TextView iva;
    private String emailCliente;
    private String phoneCliente;
    private String idguid;
    private Button link;
    private EditText telefono;
    private TextView tvlink;
    private ProgressBar spinner;
    String NombresCompletos;
    String Email;
    String DescripcionCliente;
    String phone;
    private double MontoIva=0.00;
    private double retRta=0.00;
    private double retIva=0.00;
    private double subTotal=0.00;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymentez);
        Intent iin= getIntent();
        Bundle b = iin.getExtras();



        if(b!=null)
        {
            String j =(String) b.get("username");
            Email=j;
            phone=b.getString("movil").toString();
            NombresCompletos=b.getString("nombres");


            Cliente=b.getString("cliente");
            emailCliente=b.getString("mailCliente");
            phoneCliente=b.getString("phoneCliente");
            DescripcionCliente=b.getString("descripcion");

        }
        amount = (EditText) findViewById(R.id.amount);
        amount2 = (EditText) findViewById(R.id.amount2);
        iva = (TextView) findViewById(R.id.subtotal);
        /*if (SharedHelper.getKey(getApplicationContext(),"Iva")=="1")
        {
            iva.setText("12%");
        }
        else
        {
            iva.setText("0.00");
        }*/

        link= (Button) findViewById(R.id.link);
        telefono= (EditText) findViewById(R.id.phone);
        tvlink=(TextView)findViewById(R.id.tvlink);

        spinner= (ProgressBar) findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);


        amount.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                String valornet=amount.getText().toString();
                //amount.setText();
                if ( valornet.trim().length() > 0)
                {

                    double valor = Double.parseDouble(amount.getText().toString());
                    if (SharedHelper.getKey(getApplicationContext(),"Iva")=="1")
                    {
                        MontoIva=(valor*12)/100;
                         retIva=(MontoIva*30)/100;
                        retRta=(valor*2)/100;
                        subTotal=retIva+retRta;

                        valor=valor +((valor*12)/100);
                        DecimalFormat df = new DecimalFormat("###,###.##");
                        String valoriva=df.format(valor);
                        iva.setText(valoriva);
                    }
                    else
                    {
                        iva.setText("0.00");
                    }

                    double valor2=0.0;
                    if (valor>= URLHelper.costMinPaymentez)
                    {

                        valor2=valor*URLHelper.porcentajePaymentez/100;
                        valor2=valor-valor2;
                        //se añadio linea por nuevo calculo
                        valor2=valor2-subTotal;
                        valor2=valor2-URLHelper.trancostPaymentez;


                        //NumberFormat nf2 = NumberFormat.getInstance(Locale.ENGLISH);
                        DecimalFormat df = new DecimalFormat("###,###.##");
                        valornet=df.format(valor2);
                    }
                    String temp =String.valueOf(valornet.replace(",","."));
                    //temp=temp.replace("$","");
                    amount2.setText(temp);
                }
                else
                {
                    amount2.setText("");
                }


            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });


        link.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                link.setEnabled(false);
                spinner.setVisibility(View.VISIBLE);
                try {
                    if (telefono.length()<=0 || amount.toString().length()<=0)
                    {
                        Toast.makeText(getApplicationContext(),"Para generar el link es necesario ingresar el teléfono y el monto", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else
                    {

                        idguid= UUID.randomUUID().toString();
                        CrearLink();
                        link.setEnabled(true);
                        spinner.setVisibility(View.GONE);
                    }

                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(),
                            ex.getMessage(), Toast.LENGTH_SHORT).show();
                    link.setEnabled(true);
                    spinner.setVisibility(View.GONE);
                }
            }
        });






    }

    protected void CrearLink() {
        String url = URLHelper.UrlPago;
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
                            if (Codigo.equals("00")) {

                                //AQUI RECIBO LA URL A PAGAR
                                String url=URLHelper.LinkPago+"?id="+idguid+"%26monto="+amount.getText().toString().trim()+"";
                                String lynk= "https://api.whatsapp.com/send?phone="+telefono.getText()+"&text="+url;
                                tvlink.setText(Html.fromHtml("<a href="+lynk+">"+lynk));
                                tvlink.setMovementMethod(LinkMovementMethod.getInstance());
                                SendMail();

                                //spinner.setVisibility(View.GONE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            link.setEnabled(true);
                            spinner.setVisibility(View.GONE);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.getMessage());
                        link.setEnabled(true);
                        spinner.setVisibility(View.GONE);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", idguid);
                params.put("email", SharedHelper.getKey(getApplicationContext(),"usuario").toString());
                params.put("amount", amount2.getText().toString());
                params.put("estado", "A");
                params.put("nombre_cliente", Cliente);
                params.put("email_cliente", emailCliente);
                params.put("diferido", "1");
                params.put("amountsc", amount.getText().toString());
                params.put("iva", SharedHelper.getKey(getApplicationContext(),"Iva"));
                params.put("descripcion", DescripcionCliente);
                params.put("opcion","5");


                return params;
            }
        };
        queue.add(postRequest);
    }

    protected void SendMail() {
        final String username = "admin@pagadito.app";
        final String password = "Orlando2825@";

        Properties props = new Properties();

        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", URLHelper.SMTPHOST);
        props.put("mail.smtp.port", URLHelper.SMTPPORT);

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("admin@pagadito.app"));
            message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(emailCliente));
            message.setRecipients(Message.RecipientType.CC,InternetAddress.parse(SharedHelper.getKey(getApplicationContext(),"usuario").toString()));
            message.setSubject("ORDEN DE PAGO");
            DecimalFormat df = new DecimalFormat("###,###.##");
            double valor = Double.parseDouble(amount.getText().toString());
            String valornet=df.format(valor);
            message.setText("Se ha generado una orden pago por un monto de: $ " + valornet);

//            MimeBodyPart messageBodyPart = new MimeBodyPart();
//            Multipart multipart = new MimeMultipart();
//            messageBodyPart = new MimeBodyPart();
////            String file = "path of file to be attached";
////            String fileName = "attachmentName";
////            DataSource source = new FileDataSource(file);
//            messageBodyPart.setDataHandler(new DataHandler(source));
//            messageBodyPart.setFileName(fileName);
//            multipart.addBodyPart(messageBodyPart);
            //message.setContent(multipart);
            Transport.send(message);
            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

}
