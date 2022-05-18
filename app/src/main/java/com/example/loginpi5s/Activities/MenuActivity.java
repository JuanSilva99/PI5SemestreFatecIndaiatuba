package com.example.loginpi5s.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.loginpi5s.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MenuActivity extends AppCompatActivity {

    //declaração de variáveis
    private Button contaBtn;
    private Button inserirEntradaBtn;
    private Button inserirSaidaBtn;
    private Button historicoBtn;
    private Button sobreBtn;
    private Button efetuarLogoutBtn;
    private TextView userInfoTxt;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        mAuth = FirebaseAuth.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        //chama o método que sincrorniza as views
        IniciarComponentes();

        //coloca o nome do usuário no topo da tela
        UpdateName();

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
        /*historicoBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MenuActivity.this, HistoricoListActivity.class);
            startActivity(intent);
        });*/

        //função do botão para informações sobre o app
        sobreBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MenuActivity.this, SobreActivity.class);
            startActivity(intent);
        });

        //função do botão para fazer logoff
        efetuarLogoutBtn.setOnClickListener(view -> {
            mAuth.signOut();
            finish();
        });
    }

    //pega o nome do usuario no firebase
    private void UpdateName() {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Usuarios").child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userInfoTxt.setText(snapshot.child("nome").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //sincronizar botões Java com XML
    private void IniciarComponentes() {
        contaBtn = (Button) findViewById(R.id.btnMinhaConta);
        inserirEntradaBtn = (Button) findViewById(R.id.btnInserirEntrada);
        inserirSaidaBtn = (Button) findViewById(R.id.btnInserirSaida);
        historicoBtn = (Button) findViewById(R.id.btnConsultar);
        sobreBtn = (Button) findViewById(R.id.btnSobre);
        efetuarLogoutBtn = (Button) findViewById(R.id.btnLogout);
        userInfoTxt = (TextView) findViewById(R.id.txtUserInfoMenu);
    }
}