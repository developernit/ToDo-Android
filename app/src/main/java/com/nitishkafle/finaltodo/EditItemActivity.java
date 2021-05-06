package com.nitishkafle.finaltodo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

    EditText etItem;
    Button btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        etItem = findViewById(R.id.etItemText);
        btnUpdate = findViewById(R.id.btnUpdate);

        etItem.setText(getIntent().getStringExtra(MainActivity.KEY_ITEM_TEXT));
        final int position = getIntent().getIntExtra(MainActivity.KEY_ITEM_POSITION, 0);

        getSupportActionBar().setTitle("Edit Item");

        btnUpdate.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();

                intent.putExtra(MainActivity.KEY_ITEM_POSITION, position);
                intent.putExtra((MainActivity.KEY_ITEM_TEXT), etItem.getText().toString());

                setResult(RESULT_OK, intent);

                finish();
            }
        });
    }
}