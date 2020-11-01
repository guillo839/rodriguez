package guillermo.proyectoa.mpos.Helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.digital.mpos.R;

import java.util.List;

public class ListAdapter extends ArrayAdapter<Product> {
    private List<Product> mList;
    private Context mContext;
    private int resourceLayout;

    public ListAdapter(Context context, int resource, List<Product> objects) {
        super(context, resource, objects);
        mList = objects;
        mContext = context;
        resourceLayout = resource;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_row,  null);
        }
        Product producto = mList.get(position);

        ImageView imagen=view.findViewById(R.id.imageView);
        imagen.setImageResource(producto.getImage());

        TextView textNombre= view.findViewById(R.id.txtNombre);
        textNombre.setText(producto.getProducto());

        TextView textprecio= view.findViewById(R.id.txtPrecio);
        textprecio.setText(String.valueOf(producto.getPrecio()));

        return view;
    }
}
