package com.unir.bibliotecaunir;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class Pesquisa extends AppCompatActivity {

    private Button btnVoltar;
    private TableLayout tabela;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisa);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_pesquisa);

        btnVoltar = findViewById(R.id.btn_voltar_pesquisar);
        tabela = findViewById(R.id.tb_pesquisa);

        Intent intent = getIntent();
        int tipo = intent.getIntExtra("tipo", 0);
        String busca = intent.getStringExtra("busca");
        List<ContentValues> lista = new ArrayList<>();
        BancoControlador bd = new BancoControlador(getApplicationContext());

        if (tipo == R.id.rd_titulo){
            lista = bd.pesquiarPorTitulo(busca);
        }else if (tipo == R.id.rd_ano){
            lista = bd.pesquiarPorAno(Integer.parseInt(busca));
        }else if (tipo == R.id.rd_todos){
            lista = bd.pesquiarPorTodos();
        }

        if (lista != null && lista.size() > 0){
            for (ContentValues cv : lista){
                TableRow tr = new TableRow(getApplicationContext());
                TextView col1 = new TextView(getApplicationContext());
                col1.setText(String.valueOf(cv.getAsInteger("id")));
                tr.addView(col1);

                TextView col2 = new TextView(getApplicationContext());
                col2.setText(cv.getAsString("titulo"));
                tr.addView(col2);

                TextView col3 = new TextView(getApplicationContext());
                col3.setText(cv.getAsString("autor"));
                tr.addView(col3);

                TextView col4 = new TextView(getApplicationContext());
                col4.setText(String.valueOf(cv.getAsInteger("ano")));
                tr.addView(col4);
                tr.setPadding(24, 8, 8, 8);

                tabela.addView(tr);

                tr.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {

                        AlertDialog.Builder builder =new AlertDialog.Builder(Pesquisa.this);
                        builder.setTitle(cv.getAsString("titulo"));

                        builder.setItems(R.array.dialog_items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                switch (i){
                                    case 0:
                                        Intent intent= new Intent(Pesquisa.this,Cadastro.class);
                                        intent.putExtra("id", ""+cv.getAsInteger("id"));
                                        intent.putExtra("atualizar","true" );
                                        startActivity(intent);
                                        break;
                                    case 1:

                                        bd.deletarRegistro(cv.getAsInteger("id"));
                                        tabela.removeView(tr);
                                        break;
                                }
                            }
                        });
                        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        builder.create().show();
                        return false;
                    }
                });
            }
        }
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}