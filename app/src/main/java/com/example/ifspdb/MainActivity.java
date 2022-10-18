package com.example.ifspdb;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase bancoDados;
    public ListView lswDados;
    public Button btnCadastrar;
    public ArrayList<Integer> arrayIds;
    public Integer idSelecionado;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Coleção de Vinil");

        lswDados = (ListView) findViewById(R.id.lswDados);
        btnCadastrar = (Button) findViewById(R.id.btnCadastrar);


        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirTelaCadastro();
            }
        });

        lswDados.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                idSelecionado =arrayIds.get(i);
                confirmarExcluir();
                return true;
            }
        });

        lswDados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                idSelecionado = arrayIds.get(i);
                abrirTelaAlterar();
            }
        });

        //excluirBancoDados();
        criarBancoDados();
        //inserirDados();
        listaDados();
    }

    public void excluirBancoDados() {

        try {
            bancoDados = openOrCreateDatabase("colecao", MODE_PRIVATE, null);
            bancoDados.execSQL("DROP TABLE vinil");
            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void criarBancoDados() {

        try {
            bancoDados = openOrCreateDatabase("colecao", MODE_PRIVATE, null);
            bancoDados.execSQL("CREATE TABLE IF NOT EXISTS  vinil(" +
                    " id INTEGER PRIMARY KEY AUTOINCREMENT" +
                    ", nome VARCHAR, gravadora VARCHAR, ano VARCHAR, tipo VARCHAR, rating VARCHAR)");
            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void listaDados() {
            try{
                arrayIds = new ArrayList<>();
                bancoDados = openOrCreateDatabase("colecao", MODE_PRIVATE, null);
                Cursor cursor = bancoDados.rawQuery("SELECT id, nome, gravadora, ano, tipo, rating FROM vinil",null);
                ArrayList<String> linhas = new ArrayList<String>();
                ArrayAdapter adapter = new ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_list_item_1,
                        android.R.id.text1,
                        linhas);
                lswDados.setAdapter(adapter);
                cursor.moveToFirst();
                while(cursor != null){
                    linhas.add(cursor.getString(1) +
                            " - " + cursor.getString(2)
                            + " - " + cursor.getString(3)
                            + " - " + cursor.getString(4)
                            + " - " + cursor.getString(5));
                    arrayIds.add(cursor.getInt(0));
                    cursor.moveToNext();
                }
                bancoDados.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    private void inserirDados() {
        try {
            bancoDados = openOrCreateDatabase("colecao", MODE_PRIVATE, null);
            String sql = "INSERT INTO vinil (nome, gravadora,ano,tipo,rating) VALUES(?,?,?,?,?)";
            SQLiteStatement stmt = bancoDados.compileStatement(sql);
            stmt.bindString(1, "Disco 1");
            stmt.bindString(2, "Odeon");
            stmt.bindString(3, "1995");
            stmt.bindString(4, "Rock");
            stmt.bindString(5, "1");
            stmt.executeInsert();

            stmt.bindString(1, "Disco 2");
            stmt.bindString(2, "RCA");
            stmt.bindString(3, "1993");
            stmt.bindString(4, "MPB");
            stmt.bindString(5, "2");
            stmt.executeInsert();

            stmt.bindString(1, "Disco 2");
            stmt.bindString(2, "Aple");
            stmt.bindString(3, "1990");
            stmt.bindString(4, "Rock");
            stmt.bindString(5, "3");
            stmt.executeInsert();
            bancoDados.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void abrirTelaCadastro(){
        Intent intent = new Intent(this, CadastroActivity.class);
        startActivity(intent);
    }

    protected void onResume(){
        super.onResume();
        listaDados();
    }

    public void excluir(){
        try {
            bancoDados = openOrCreateDatabase("colecao", MODE_PRIVATE, null);
            String sql = "DELETE FROM vinil WHERE id=?";
            SQLiteStatement stmt = bancoDados.compileStatement(sql);
            stmt.bindLong(1, idSelecionado);
            stmt.executeUpdateDelete();
            listaDados();
            bancoDados.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void confirmarExcluir(){
        AlertDialog.Builder msgBox = new AlertDialog.Builder(MainActivity.this);
        msgBox.setTitle("Excluir");
        msgBox.setIcon(android.R.drawable.ic_menu_delete);
        msgBox.setMessage("Você realmente deseja excluir esse registro?");
        msgBox.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                excluir();
                listaDados();
            }
        });
        msgBox.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        msgBox.show();
    }

    public void abrirTelaAlterar(){
        Intent intent = new Intent(this, AlterarActivity.class);
        intent.putExtra("id", idSelecionado);
        startActivity(intent);
    }
    }
