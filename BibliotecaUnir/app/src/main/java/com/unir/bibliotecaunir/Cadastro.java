package com.unir.bibliotecaunir;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Cadastro extends AppCompatActivity {

    private Button btnSalvar, btnVoltar;
    private EditText edtTitulo, edtAutor, edtAno;

    private TextView titulo_topo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        BancoControlador bd = new BancoControlador(getApplicationContext());

        btnSalvar = findViewById(R.id.btn_salvar);
        btnVoltar = findViewById(R.id.btn_voltar);
        edtTitulo = findViewById(R.id.edt_titulo);
        edtAno = findViewById(R.id.edt_ano);
        edtAutor = findViewById(R.id.edt_autor);

        titulo_topo = findViewById(R.id.titulo_ac);

        Intent intent = getIntent();
        String x = intent.getStringExtra("id");
        String y = intent.getStringExtra("atualizar");
        if (y != null && y.equals("true")) {
            titulo_topo.setText("Atualizar");
        }

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues cv = new ContentValues();
                if(edtTitulo.getText().toString().isEmpty() || edtAno.getText().toString().isEmpty()|| edtAutor.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Preencha todos os campos!!", Toast.LENGTH_SHORT).show();
                }else {

                    String titulo = edtTitulo.getText().toString();
                    String autor = edtAutor.getText().toString();
                    String ano = edtAno.getText().toString();
                    cv.put("titulo", titulo);
                    cv.put("autor", autor);
                    cv.put("ano", ano);

                    if (y != null && y.equals("true")) {
                        bd.alterarRegistro(Integer.parseInt(x), titulo, autor, Integer.parseInt(ano));
                        Toast.makeText(getApplicationContext(), "Ataualizado com sucesso!", Toast.LENGTH_SHORT).show();
                        edtAno.setText("");
                        edtAutor.setText("");
                        edtTitulo.setText("");
                        edtTitulo.requestFocus();

                    } else {

                        String msg = "";
                        long res = bd.inserir(cv);
                        if (res > 0) {
                            msg = "Operação realizada com sucesso!";
                            edtAno.setText("");
                            edtAutor.setText("");
                            edtTitulo.setText("");
                            edtTitulo.requestFocus();
                        } else {
                            msg = "Ocorreu um erro durante a operação!";
                        }
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

}