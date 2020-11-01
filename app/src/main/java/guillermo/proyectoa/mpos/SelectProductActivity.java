package guillermo.proyectoa.mpos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import guillermo.proyectoa.mpos.Helper.ListAdapter;
import guillermo.proyectoa.mpos.Helper.Product;

import com.digital.mpos.R;

import java.util.ArrayList;
import java.util.List;

import static android.widget.AdapterView.*;

public class SelectProductActivity extends AppCompatActivity implements OnItemClickListener {
private ListView mListView;
private List<Product>mLista= new ArrayList<>();
ListAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_product);
        mListView=findViewById(R.id.listview);
        mLista.add(new Product("Mant. Pc",10.00,R.mipmap.ic_launcher));
        mLista.add(new Product("Mouse",5.00,R.mipmap.ic_launcher));
        mLista.add(new Product("Teclado",7.00,R.mipmap.ic_launcher));
        mAdapter= new ListAdapter(SelectProductActivity.this,R.layout.item_row,mLista);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long rowId) {
                // Do the onItemClick action

                Log.d("ROWSELECT", "" + rowId);
                //Toast.makeText(this,"Elemento Clicado" + position,Toast.LENGTH_SHORT).show();
                Intent intent= new Intent(getApplicationContext(),productDetailActivity.class);
                intent.putExtra("producto",mAdapter.getItem(position).getProducto());
                intent.putExtra("precio",String.valueOf(mAdapter.getItem(position).getPrecio()));
                startActivity(intent);
            }
        });

    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view , int position , long l){

    }



}
