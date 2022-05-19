package com.example.loginpi5s.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.loginpi5s.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SobreActivity extends AppCompatActivity {

    //declaração de variáveis
    private TextView userInfoTxt;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        //chama o método que sincrorniza as views
        IniciarComponentes();

        //coloca o nome do usuário no topo da tela
        UpdateName();
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

    //Sincronizar botões Java com XML
    private void IniciarComponentes() {
        userInfoTxt = (TextView) findViewById(R.id.txtUserInfoSobre);
    }
}