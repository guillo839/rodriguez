package guillermo.proyectoa.mpos;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.braintreepayments.cardform.view.CardForm;

import guillermo.proyectoa.mpos.Helper.SharedHelper;
import guillermo.proyectoa.mpos.Helper.URLHelper;
import guillermo.proyectoa.mpos.Interface.JSInterface;

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


public class PayActivity extends AppCompatActivity {

    CardForm cardForm;
    String NombresCompletos;
    String Email;
    String Dni;
    String phone;
    Double amount = 0.0;
    Double amount2 = 0.0;

    private String Cliente;
    private String emailCliente;
    private String phoneCliente;

    Boolean bGuardo = false;
    WebView webview;
    private ProgressBar spinner;

    Button buy;
    AlertDialog.Builder alertBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        Email = SharedHelper.getKey(getApplicationContext(), "usuario");
        if (b != null) {
            String j = (String) b.get("username");
            //Email = j;
            phone = b.getString("movil").toString();
            NombresCompletos = b.getString("nombres");
            amount = b.getDouble("amount");
            amount2 = b.getDouble("amount2");
            Dni = SharedHelper.getKey(getApplicationContext(), "dni");

            Cliente = b.getString("cliente").toString();
            emailCliente = b.getString("mailCliente").toString();
            phoneCliente = b.getString("phoneCliente").toString();

        }

        cardForm = findViewById(R.id.card_form);
        buy = findViewById(R.id.btnBuy);
        spinner= (ProgressBar) findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);

        webview = findViewById(R.id.my_web_view);
        webview.setWebViewClient(new WebViewClient());
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webview.addJavascriptInterface(new JSInterface(getApplicationContext()), "");
        webview.loadUrl("file:///android_asset/Aes.html");


        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                //.postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("Número de teléfono es obligatorio")

                .setup(PayActivity.this);
        cardForm.getCvvEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cardForm.isValid()) {
                    alertBuilder = new AlertDialog.Builder(PayActivity.this);
                    alertBuilder.setTitle("Confirmación de pago");
                    alertBuilder.setMessage("No tarjeta: " + cardForm.getCardNumber() + "\n" +
                            "Fecha exp.: " + cardForm.getExpirationDateEditText().getText().toString() + "\n" +
                            "CVV: " + cardForm.getCvv() + "\n" +
                            //"Postal code: " + cardForm.getPostalCode() + "\n" +
                            "Phone number: " + cardForm.getMobileNumber());
                    alertBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            String Cardnumber = cardForm.getCardNumber();
                            String Monthtmp = cardForm.getExpirationMonth();
                            String yeartmp = cardForm.getExpirationYear();
                            String holderName = Cliente;
                            String cvv = cardForm.getCvv();
                            buy.setEnabled(false);
                            spinner.setVisibility(View.VISIBLE);
                            final String[] encryptedValue = {""};
                            webview.evaluateJavascript("javascript: " +
                                    //"encripta('" + parametroJava + "');", new ValueCallback<String>() {
                                    "encripta('" + Cardnumber + "','" + Monthtmp + "','" + yeartmp + "','" + holderName + "','" + cvv + "');", new ValueCallback<String>() {
                                @Override
                                public void onReceiveValue(String s) {
                                    Log.d("LogName", s); // Prints: {"var1":"variable1","var2":"variable2"}
                                    encryptedValue[0] = s;
                                    if (encryptedValue[0].length() > 1) {

                                        if (isOnline()) {
                                            //Pagar(encryptedValue[0]);
                                        }


                                        RequestAsync ra = new RequestAsync();
//                                        ra.NumberCard = cardForm.getCardNumber();
//                                        ra.expirationMonth = cardForm.getExpirationMonth();
//                                        ra.expirationYear = cardForm.getExpirationYear();
//                                        ra.CVV = cardForm.getCvv();
                                        ra.codificado = encryptedValue[0];
                                        ra.execute();


                                    } else {
                                        Toast.makeText(PayActivity.this, "Error al cifrar la tarjeta", Toast.LENGTH_LONG).show();
                                        return;
                                    }

                                }
                            });

                            //Toast.makeText(PayActivity.this, "Thank you for purchase", Toast.LENGTH_LONG).show();
                        }
                    });
                    alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog alertDialog = alertBuilder.create();
                    alertDialog.show();

                } else {
                    Toast.makeText(PayActivity.this, "Favor completar los campos", Toast.LENGTH_LONG).show();
                }
            }
        });
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

    protected void CrearPago() {
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
                                buy.setEnabled(true);
                                SendMail();
                                spinner.setVisibility(View.GONE);
                                goToDetailTransaction();



                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            spinner.setVisibility(View.GONE);
                            buy.setEnabled(true);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        //Log.d("Error.Response", error.getMessage());
                        buy.setEnabled(true);
                        spinner.setVisibility(View.GONE);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", UUID.randomUUID().toString());
                params.put("email", Email);
                params.put("nombre_cliente", Cliente);
                params.put("email_cliente", emailCliente);
                params.put("amount", amount2.toString());
                params.put("opcion","2");

                return params;
            }
        };
        queue.add(postRequest);
    }




    protected void goToDetailTransaction() {
        Bundle bundle = new Bundle();
        bundle.putString("username", emailCliente);//Email
        bundle.putString("amount", amount.toString());
        bundle.putString("amount2", amount2.toString());
        bundle.putString("NumeroTarjeta", cardForm.getCardNumber());
        bundle.putString("movil", SharedHelper.getKey(getApplicationContext(), "movil"));
        bundle.putString("nombres", SharedHelper.getKey(getApplicationContext(), "nombres"));


        Intent intent = new Intent(this, Activity_Transaction_Detail.class);
        intent.putExtras(bundle);
        startActivity(intent);
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
            //message.setFrom(new InternetAddress("cristian.perez.martinez2011@gmail.com"));
            message.setFrom(new InternetAddress("pagadito@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(emailCliente));
            message.setRecipients(Message.RecipientType.CC,InternetAddress.parse(SharedHelper.getKey(getApplicationContext(),"usuario").toString()));
            message.setSubject("CONFIRMACIÓN DE PAGO");
            DecimalFormat df = new DecimalFormat("###,###.##");
            String valornet=df.format(amount);
            message.setText("Se ha realizado un pago por un monto de: $ " + valornet);

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

    public class RequestAsync extends AsyncTask<String, String, String> {
        //        private String NumberCard = "";
//        private String expirationMonth = "";
//        private String expirationYear = "";
//        private String CVV = "";
        private String codificado = "";
        //        private String Monto2 = "";
        String strNumberCountry = cardForm.getCountryCode();
        String strNumberPhone = cardForm.getMobileNumber();
        String Mobile = "";
        private String dni = Dni;

        @Override
        protected String doInBackground(String... strings) {
            try {
                JSONObject params = new JSONObject();


                params.put("token", URLHelper.Token);
                params.put("publicToken", URLHelper.PUBKEY);
                params.put("data", codificado);
                params.put("enviroment", URLHelper.AMBIENTE);
                params.put("amount", amount.toString());
                params.put("descripcion", "SERVICIO DE PAGOS");
                params.put("dni", "");
                params.put("email", emailCliente);

                if (strNumberCountry.substring(0, 1).equals("+")) {

                } else {
                    strNumberCountry = "+" + strNumberCountry;
                }

                if (strNumberPhone.substring(0, 1).equals("0")) {
                    Mobile = strNumberPhone = strNumberPhone.substring(1);
                }
                Mobile = strNumberCountry + Mobile;
                params.put("phone", Mobile);
                params.put("address", "Milagro");
                String strResponse = RequestHandler.sendPost(URLHelper.Url, params);
                if (strResponse == null) {
                    runOnUiThread(new Thread(new Runnable() {
                        public void run() {
                            buy.setEnabled(true);
                        }
                    }));

                }
                return strResponse;
            } catch (Exception e) {
                runOnUiThread(new Thread(new Runnable() {
                    public void run() {
                        buy.setEnabled(true);
                    }
                }));
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
                //spinner.setVisibility(View.VISIBLE);
                buy.setEnabled(false);
                try {
                    JSONObject jsonObj = new JSONObject(s);

                    String Codigo = jsonObj.optString("code");

                    if (Codigo.equals("200")) {
                        Toast.makeText(getApplicationContext(), "Pago Realizado con éxito", Toast.LENGTH_LONG).show();
                        CrearPago();
                        spinner.setVisibility(View.GONE);
                        buy.setEnabled(true);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), jsonObj.optString("message"), Toast.LENGTH_LONG).show();
                    }
                    buy.setEnabled(true);
                    spinner.setVisibility(View.GONE);
                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), "Error en el pago.Vuelva a intentarlo", Toast.LENGTH_LONG).show();
                    buy.setEnabled(true);
                    spinner.setVisibility(View.GONE);
                } finally {

                }

            }
        }


    }


}
