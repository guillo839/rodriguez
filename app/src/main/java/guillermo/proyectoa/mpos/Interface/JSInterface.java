package guillermo.proyectoa.mpos.Interface;

import android.content.Context;
import android.webkit.JavascriptInterface;

public class JSInterface {
    Context context;
    public JSInterface (Context act)
    {
        this.context=act;
    }

    @JavascriptInterface
    public String encripta (String datos)
    {
return "";
    }
}
