package com.nitishkafle.finaltodo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    //adding numeric code to identify the edit activity
    public static final int EDIT_REQUEST_CODE = 20;

    //assigning key to use for passing data between activities
    public static final String KEY_ITEM_TEXT = "itemText";
    public static final String KEY_ITEM_POSITION = "itemPosition";

    Button btnAdd;
    EditText etTodo;
    RecyclerView rvItems;

    List<String> items;
    ItemsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        etTodo = findViewById(R.id.etTodo);
        rvItems = findViewById(R.id.rvItems);

        loadItems();
        rvItems.setLayoutManager(new LinearLayoutManager(this));
        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClicked(int position) {
                Log.i("MainActivity", "Long press at position " + position);
                items.remove(position);
                adapter.notifyItemRemoved(position);
                saveItems();
            }
        };

        ItemsAdapter.OnClickListener onClickListener = new ItemsAdapter.OnClickListener() {
            @Override
            public void onClickListener(int position) {
                Log.d("MainActivity", "Single press at position "+ position);

                //creating the new activity
                Intent intent = new Intent(MainActivity.this, EditItemActivity.class);

                //passing the data being edited as key-value pair
                intent.putExtra(KEY_ITEM_POSITION, position);
                intent.putExtra(KEY_ITEM_TEXT, items.get(position));

                //displaying the activity
                startActivityForResult(intent, EDIT_REQUEST_CODE);
            }
        };

        adapter = new ItemsAdapter(this, items, onLongClickListener, onClickListener);
        rvItems.setAdapter(adapter);

        btnAdd.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String itemText = etTodo.getText().toString();
                if (itemText.isEmpty()){
                    Toast.makeText(MainActivity.this, "Cannot add an empty item", Toast.LENGTH_SHORT).show();
                    return;
                }
                etTodo.setText("");

                //adding the new item to the list
                items.add(itemText);
                adapter.notifyItemInserted(items.size() - 1);
                Toast.makeText(MainActivity.this, "Added item", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == EDIT_REQUEST_CODE){
            String itemText = data.getStringExtra(KEY_ITEM_TEXT);
            //retrieving updated item
            int position = data.getIntExtra(KEY_ITEM_POSITION, 0);
            //updating the model with the new item text at the edited position
            items.set(position, itemText);
            //notifying the adapter
            adapter.notifyItemChanged(position);
            //saving
            saveItems();
            //notifying the user about operation completion
            Toast.makeText(this, "Item updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            Log.w("MainActivity", "Unknown call to onActivityResult");
        }
    }


    private File getDataFile(){
        return new File((File) null, "data.txt");
    }

    private void loadItems() {
        try{
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e){
            Log.e("Main Activity", "Error reading items", e);
            items = new ArrayList<>();
        }
    }

    private void saveItems(){
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e){
            Log.e("MainActivity", "Error writing file", e);
        }
    }
}