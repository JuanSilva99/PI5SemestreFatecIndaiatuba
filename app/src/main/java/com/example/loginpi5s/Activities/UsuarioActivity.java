package com.example.loginpi5s.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginpi5s.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UsuarioActivity extends AppCompatActivity {

    //declaração de variáveis
    private TextView userName;
    private TextView userEmail;
    private Button senhaBtn;
    private Context context;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);
        context = UsuarioActivity.this;

        mAuth = FirebaseAuth.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        //chama o método que sincrorniza as views
        IniciarComponentes();

        //coloca o nome e email do usuário na tela
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Usuarios").child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userName.setText(snapshot.child("nome").getValue(String.class));
                userEmail.setText(snapshot.child("email").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //função para trocar de senha
        senhaBtn.setOnClickListener(view -> {
            mAuth.sendPasswordResetEmail(userEmail.getText().toString()).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "Enviamos um email para alterar a sua senha.", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    //sincronizar botões Java com XML
    private void IniciarComponentes() {
        userName = (TextView) findViewById(R.id.txtNomeUsuarioMyAcc);
        userEmail = (TextView) findViewById(R.id.txtEmailUsuarioMyAcc);
        senhaBtn = (Button) findViewById(R.id.btnMudarSenha);
    }
}