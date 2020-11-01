package guillermo.proyectoa.mpos.Helper;

public class URLHelper {

    public static final String Url = "https://us-central1-backservicespagos.cloudfunctions.net/requestPayment/api/v1/transaction/create/pay";

//    public static final String URLBase="http://pagadito.sysdae.com/";
//    public static final String URLBaseApi="http://webapi.sysdae.com/";
//        public static final String UrlLogin = URLBaseApi+"api/Auth";//PRUEBAS
//    public static final String UrlPago = URLBaseApi+"api/Auth";//PRUEBAS
//        public static final String Token="elSPOdDw1BPVaPFLNZdOiJ4CnGD2";//token pedro
//    public static final String PUBKEY="ksb6dqQBoQanHGAmvIvsKqZVZiH0IElJ";



    public static final String URLBase="https://pagadito.app/";//PRODUCCION
    public static final String URLBaseTest="http://104.217.254.197/apipayment/api/authV2/";
    public static final String UrlLogin = URLBaseTest;


    //public static final String UrlLogin = URLBase+"api/Auth";//PRODUCCION
    public static final String UrlPago = URLBase+"api/Auth";//PRODUCCION
    public static final String Token="kc1U2eObx7RzSgSotSqX10HCrGC2";
    public static final String PUBKEY="49OrjF5l8VbLMVXQFw4oz0Rtu5dKFR3d";


    public static final String SMTPHOST="mail.pagadito.app";//SMTP.GMAIL.COM
    public static final String SMTPPORT="8889";//587 GOOGLE
    public static final String AMBIENTE="pro";

    //http://104.217.254.197/Payment
    //public static final String UrlPago =    "http://localhost:62402/api/Auth";
    //public static final String UrlLogin  =    "http://localhost:62402/api/Auth";
    public static final String LinkPago = URLBase;//"http://104.217.254.197/Payment/index.html";



    public static final double porcentaje = 6.0;
    public static final double trancost = 0.50;
    public static final double costMin = 2.0;

    public static final double porcentajePaymentez = 7.5;
    public static final double trancostPaymentez = 0.25;
    public static final double costMinPaymentez = 2.0;
}

