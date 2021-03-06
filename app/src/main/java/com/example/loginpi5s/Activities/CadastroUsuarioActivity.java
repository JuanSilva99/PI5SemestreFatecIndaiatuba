package com.example.loginpi5s.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginpi5s.R;
import com.example.loginpi5s.Usuario;
import com.google.firebase.auth.FirebaseAuth;

import java.io.Serializable;
import java.util.Objects;

public class CadastroUsuarioActivity extends AppCompatActivity implements Serializable {

    //declaração de variáveis
    private EditText nomeUserEdt;
    private EditText emailUserEdt;
    private EditText senhaUserEdt;
    private EditText senhaConfEdt;
    private Button cadastroBtn;
    private CheckBox termoUsoCB;
    private TextView termosUsoTxt;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);
        mAuth = FirebaseAuth.getInstance();

        //chama o método que sincrorniza as views
        IniciarComponentes();

        //função para abrir o termo de uso .pdf (pegar PDF do FireBase)
        termosUsoTxt.setOnClickListener(view -> {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://firebasestorage.googleapis.com/v0/b/mymoneyapp-pi.appspot.com/o/Termos%20de%20Uso%20e%20Responsabilidade%20-%20My%20Money%20App.pdf?alt=media&token=6d9ee74b-4662-44bc-84bf-84f27fd1e0f4"));
            startActivity(i);
        });

        //funções para o botão cadastrar
        cadastroBtn.setOnClickListener(view -> {

            //validação, confirma se a senha foi digitada corretamente
            if (senhaUserEdt.getText().toString().equals(senhaConfEdt.getText().toString())) {
                //validação, todos os campos precisam ser preenchidos
                if (nomeUserEdt.getText().toString().equals("") || emailUserEdt.getText().toString().equals("") || senhaUserEdt.getText().toString().equals("") || !termoUsoCB.isChecked()) {
                    Toast.makeText(CadastroUsuarioActivity.this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                } else {
                    registerUser();
                }
            } else {
                Toast.makeText(CadastroUsuarioActivity.this, "Confirmação de senha incorreta", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void registerUser() {
        Usuario u = new Usuario(
            nomeUserEdt.getText().toString(),
            emailUserEdt.getText().toString().toLowerCase()
        );

        mAuth.createUserWithEmailAndPassword(u.getEmail(), senhaUserEdt.getText().toString())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        u.save(mAuth.getUid());
                        Toast.makeText(CadastroUsuarioActivity.this, "Usuário registrado com sucesso!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        String error = Objects.requireNonNull(task.getException()).getMessage();
                        Toast.makeText(
                                CadastroUsuarioActivity.this,
                                "" + error,
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //sincronizar botões Java com XML
    private void IniciarComponentes() {
        nomeUserEdt = (EditText) findViewById(R.id.edtNomeCadastro);
        emailUserEdt = (EditText) findViewById(R.id.edtEmailCadastro);
        senhaUserEdt = (EditText) findViewById(R.id.edtSenhaCadastro);
        senhaConfEdt = (EditText) findViewById(R.id.edtSenhaCadastro2);
        cadastroBtn = (Button) findViewById(R.id.btnCadastro);
        termoUsoCB = (CheckBox) findViewById(R.id.cbTermosUso);
        termosUsoTxt = (TextView) findViewById(R.id.txtTermosUso);
    }
}