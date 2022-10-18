package com.example.ifspdb;

import static com.example.ifspdb.R.id.ratingBar2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;


public class AlterarActivity extends AppCompatActivity {
    private SQLiteDatabase bancoDados;
    public Button btnAlterar;
    public EditText edtNomeA, edtGravadoraA, edtAnoA;
    public Integer id;
    public RatingBar ratingBar2;
    public Spinner spnTipoAlterar;
    public String [] tipos = {"MPB", "Rock", "Clássico", "Instrumental","Reggae"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar);
        setTitle("Coleção de Vinil - Alteração em vinil");
        btnAlterar = (Button) findViewById(R.id.btnAlterar);
        edtNomeA = (EditText) findViewById(R.id.edtNomeA);
        edtGravadoraA = (EditText) findViewById(R.id.edtGravadoraA);
        edtAnoA = (EditText) findViewById(R.id.edtAnoA);
        ratingBar2 = (RatingBar) findViewById(R.id.ratingBar2);
        spnTipoAlterar = (Spinner) findViewById(R.id.spnTipoAlterar);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item, tipos);
        spnTipoAlterar.setAdapter(adapter);

        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        carregarDados();

        btnAlterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alterar();
            }
        });

    }

    public void carregarDados(){
        try{
            bancoDados = openOrCreateDatabase("colecao", MODE_PRIVATE, null);
            Cursor cursor = bancoDados.rawQuery("SELECT * FROM vinil WHERE id=" + id.toString(),null);
            cursor.moveToFirst();
            edtNomeA.setText(cursor.getString(1));
            edtGravadoraA.setText(cursor.getString(2));
            edtAnoA.setText(cursor.getString(3));
            int num=0;
            String valor = cursor.getString(4);
            for(int i=0; i < tipos.length; i++){
                System.out.println(tipos[i]);
                if (valor.equals(tipos[i])) {
                    num = i;
                }
            }
            spnTipoAlterar.setSelection(num);
            ratingBar2.setRating(cursor.getFloat(5));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void alterar(){
        try {
            bancoDados = openOrCreateDatabase("colecao", MODE_PRIVATE, null);
            String sql = "UPDATE vinil set nome=?, gravadora=?, ano=?, tipo=? WHERE id=?";
            SQLiteStatement stmt = bancoDados.compileStatement(sql);
            stmt.bindString(1, edtNomeA.getText().toString());
            stmt.bindString(2, edtGravadoraA.getText().toString());
            stmt.bindString(3, edtAnoA.getText().toString());
            stmt.bindString(4, tipos[spnTipoAlterar.getSelectedItemPosition()]);
            stmt.bindString(5,String.valueOf(ratingBar2.getRating()));
            stmt.bindLong(5, id);
            stmt.executeUpdateDelete();
            bancoDados.close();
            finish();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}