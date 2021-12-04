package com.example.loginpi5s.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.example.loginpi5s.R;

public class SobreActivity extends AppCompatActivity {

    //declaração de variáveis
    private TextView userInfoTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre);
        SharedPreferences dados = getSharedPreferences("Dados", MODE_PRIVATE);

        //chama o método que sincrorniza as views
        IniciarComponentes();

        //coloca o nome do usuário no topo da tela
        userInfoTxt.setText(dados.getString("nome", "nome"));
    }

    //Sincronizar botões Java com XML
    private void IniciarComponentes() {
        userInfoTxt = (TextView) findViewById(R.id.txtUserInfoSobre);
    }
}