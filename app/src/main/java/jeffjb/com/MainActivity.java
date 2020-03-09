package jeffjb.com;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  //  public interface OnLongClickistener;
    public static final String KEY_ITEM_TEXT = "Item_text";
    public static final String KEY_ITEM_POSITION = "Item_position";
    public static final int EDIT_TEXT_CODE = 20;

    List<String> item;

    Button btnAdd;
    EditText itemText;
    RecyclerView recyText;
    itemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        itemText = findViewById(R.id.ItemText);
        recyText = findViewById(R.id.recyText);

        LoadItem();

        itemsAdapter.OnLongClickListener onLongClickListener = new itemsAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClickLed(int position) {
                item.remove(position);
                itemsAdapter.notifyItemRemoved(position);

                Toast.makeText(getApplicationContext(),"removed succesfully", Toast.LENGTH_SHORT).show();
                saveItem();
            }
        };
        itemsAdapter.OnClickListener onClickListener = new itemsAdapter.OnClickListener() {
            @Override
            public void onItemClickLed(int position) {
                Log.d("MainActiviyt","Single click position"+ position);
                Intent i = new Intent(MainActivity.this, EditActivity.class);
                i.putExtra(KEY_ITEM_TEXT, item.get(position));
                i.putExtra(KEY_ITEM_POSITION, position);
                startActivityForResult(i, EDIT_TEXT_CODE);
            }
        };
        itemsAdapter = new itemsAdapter(item, onLongClickListener, onClickListener);
        recyText.setAdapter(itemsAdapter);
        recyText.setLayoutManager(new LinearLayoutManager(this));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todoItem = itemText.getText().toString();
                item.add(todoItem);

                itemsAdapter.notifyItemInserted(item.size()-1);
                itemText.setText("");
                Toast.makeText(getApplicationContext(),"added succesfully", Toast.LENGTH_SHORT).show();
                saveItem();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE){
            String itemText = data.getStringExtra(KEY_ITEM_TEXT);
            int position = data.getExtras().getInt(KEY_ITEM_POSITION);
            item.set(position, itemText);
            itemsAdapter.notifyItemChanged(position);
            saveItem();
            Toast.makeText(getApplicationContext(),"update succesfully", Toast.LENGTH_SHORT).show();
        }else{
            Log.v("MainActivity","unknown");
        }
    }

    private File getDataFile(){
         return new File(getFilesDir(),"data.txt");
        }
        private void LoadItem(){
            try {
                item = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
            } catch (IOException e) {
                Log.e("MainActivity", "Error reading msg", e);
                item=new ArrayList<>();
            }
        }

        private void saveItem(){
            try {
                FileUtils.writeLines(getDataFile(),item);
            } catch (IOException e) {
                Log.e("MainActivity", "Error writing msg", e);
            }
        }
}
