package com.example.loginpi5s.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.loginpi5s.R;

public class MenuActivity extends AppCompatActivity {

    //declaração de variáveis
    private Button contaBtn;
    private Button inserirEntradaBtn;
    private Button inserirSaidaBtn;
    private Button historicoBtn;
    private Button sobreBtn;
    private Button efetuarLogoutBtn;
    private TextView userInfoTxt;
    private SharedPreferences dados;
    private SharedPreferences.Editor dadosEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        dados = getSharedPreferences("Dados", MODE_PRIVATE);
        dadosEdit = getSharedPreferences("Dados", MODE_PRIVATE).edit();

        //chama o método que sincrorniza as views
        IniciarComponentes();

        //coloca o nome do usuário no topo da tela
        userInfoTxt.setText(dados.getString("nome", "nome"));

        //função do botão para gerenciar a conta
        contaBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MenuActivity.this, UsuarioActivity.class);
            startActivity(intent);
        });

        //função do botão para cadastrar entradas
        inserirEntradaBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MenuActivity.this, TransacaoActivity.class);
            intent.putExtra("tipo", "Entrada");
            intent.putExtra("classe", "new");
            startActivity(intent);
        });

        //função do botão para cadastrar saídas
        inserirSaidaBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MenuActivity.this, TransacaoActivity.class);
            intent.putExtra("tipo", "Saída");
            intent.putExtra("classe", "new");
            startActivity(intent);
        });

        //função do botão para consultar as transações
        historicoBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MenuActivity.this,HistoricoListActivity.class);
            startActivity(intent);
        });

        //função do botão para informações sobre o app
        sobreBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MenuActivity.this, SobreActivity.class);
            startActivity(intent);
        });

        //função do botão para fazer logoff
        efetuarLogoutBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
            dadosEdit.clear(); //limpa as SharedPreferences para impedir o login automatico
            dadosEdit.apply();
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        dados = getSharedPreferences("Dados", MODE_PRIVATE);

        //atualiza o nome do usuário no topo da tela
        userInfoTxt.setText(dados.getString("nome", "nome"));
    }

    //sincronizar botões Java com XML
    private void IniciarComponentes(){
        contaBtn = (Button) findViewById(R.id.btnMinhaConta);
        inserirEntradaBtn = (Button) findViewById(R.id.btnInserirEntrada);
        inserirSaidaBtn = (Button) findViewById(R.id.btnInserirSaida);
        historicoBtn = (Button) findViewById(R.id.btnConsultar);
        sobreBtn = (Button) findViewById(R.id.btnSobre);
        efetuarLogoutBtn = (Button) findViewById(R.id.btnLogout);
        userInfoTxt = (TextView) findViewById(R.id.txtUserInfoMenu);
    }
}