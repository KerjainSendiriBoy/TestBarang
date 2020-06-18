package com.example.testbarang;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TambahData extends AppCompatActivity {
    private DatabaseReference database;
    private Button bSubmit;
    private EditText eKode;
    private EditText eNama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_data);

        eKode = findViewById(R.id.editText);
        eNama = findViewById(R.id.editText2);
        bSubmit = findViewById(R.id.button3);

        database = FirebaseDatabase.getInstance().getReference();

        final Barang barang = (Barang) getIntent().getSerializableExtra("data");

        if (barang != null) {
            eKode.setText(barang.getKode());
            eNama.setText(barang.getNama());
            bSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    barang.setKode(eKode.getText().toString());
                    barang.setNama(eNama.getText().toString());
                    updateBarang(barang);
                }
            });
        } else {
            bSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!(eKode.getText().toString().isEmpty()) && !(eNama.getText().toString().isEmpty()))
                        submitBrg(new Barang(eKode.getText().toString(), eNama.getText().toString()));
                    else
                        Toast.makeText(getApplicationContext(), "Data tidak boleh kosong", Toast.LENGTH_LONG).show();

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(eNama.getWindowToken(), 0);
                }
            });
        }


    }

    public void submitBrg(Barang brg) {
        database.child("Barang").push().setValue(brg).addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                eKode.setText("");
                eNama.setText("");
                Toast.makeText(getApplicationContext(), "Data Berhasil ditambahkan", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateBarang(Barang barang) {

        database.child("Barang")
                .child(barang.getKey())
                .setValue(barang)
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Data Berhasil diupdate", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public static Intent getActIntent(Activity activity) {
        return new Intent(activity, TambahData.class);

    }
}
