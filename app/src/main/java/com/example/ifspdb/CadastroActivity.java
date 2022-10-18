package com.example.ifspdb;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

public class CadastroActivity extends AppCompatActivity {
    EditText edtNome, edtGravadora, edtAno;
    Button btnCadastrarVinilBd;
    SQLiteDatabase bancoDados;
    public RatingBar ratingBar;
    Spinner spnTipo;
    String [] tipos = {"MPB", "Rock", "Clássico", "Instrumental","Reggae"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        setTitle("Coleção de Vinil - Cadastrar Vinil");


        edtNome = (EditText) findViewById(R.id.edtNomeA);
        edtGravadora = (EditText) findViewById(R.id.edtGravadoraA);
        edtAno = (EditText) findViewById(R.id.edtAnoA);
        spnTipo = (Spinner) findViewById(R.id.spnTipo);
        btnCadastrarVinilBd = (Button) findViewById(R.id.btnAlterar);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item, tipos);
        spnTipo.setAdapter(adapter);

/*
        spnTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                String ret =  tipos[i];
                System.out.println(ret);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
 */

        btnCadastrarVinilBd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(edtNome.getText().toString()) ||
                        !TextUtils.isEmpty(edtGravadora.getText().toString()) ||
                        !TextUtils.isEmpty(edtAno.getText().toString())) {
                    cadastrar();
                } else {
                    Toast.makeText(CadastroActivity.this, "Inserir os dados para cadastrar", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void cadastrar() {

            try {

                bancoDados = openOrCreateDatabase("colecao", MODE_PRIVATE, null);
                String sql = "INSERT INTO vinil (nome, gravadora,ano,tipo,rating) VALUES(?,?,?,?,?)";
                SQLiteStatement stmt = bancoDados.compileStatement(sql);
                stmt.bindString(1, edtNome.getText().toString());
                stmt.bindString(2, edtGravadora.getText().toString());
                stmt.bindString(3, edtAno.getText().toString());
                stmt.bindString(4, tipos[spnTipo.getSelectedItemPosition()]);
                stmt.bindString(5, String.valueOf(ratingBar.getRating()));
                stmt.executeInsert();
                bancoDados.close();
                finish();
            } catch (Exception e) {
                e.printStackTrace();
            }

    }
}