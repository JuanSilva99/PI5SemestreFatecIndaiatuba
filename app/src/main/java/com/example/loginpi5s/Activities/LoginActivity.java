package com.example.loginpi5s.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginpi5s.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    //declaração de variáveis
    private EditText emailUserEdt;
    private EditText senhaUserEdt;
    private TextView cadastroTxt;
    private TextView recupSenhaTxt;
    private Button entrarBtn;
    private String userEmail;
    private String userPswrd;
    private Context context;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = LoginActivity.this;
        mAuth = FirebaseAuth.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        //Login automatico
        if (currentUser != null) {
            Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
            Toast.makeText(context, "Login realizado com sucesso", Toast.LENGTH_SHORT).show();
            startActivity(intent);
        }

        //chama o método que sincrorniza as views
        IniciarComponentes();

        //função para efetuar login e prosseguir ao menu
        entrarBtn.setOnClickListener(view -> {

            //recebe os valores dos EditTexts
            userEmail = emailUserEdt.getText().toString().toLowerCase();
            userPswrd = senhaUserEdt.getText().toString();

            //validação, todos os campos precisam ser preenchidos
            if (userPswrd.isEmpty() || userEmail.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            } else {
                mAuth.signInWithEmailAndPassword(userEmail, userPswrd).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        startActivity(new Intent(LoginActivity.this, MenuActivity.class));
                        Toast.makeText(context, "Login realizado com sucesso", Toast.LENGTH_SHORT).show();
                    } else {
                        String error = Objects.requireNonNull(task.getException()).getMessage();
                        Toast.makeText(
                                LoginActivity.this,
                                "" + error,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        //função para trocar de senha
        recupSenhaTxt.setOnClickListener(view -> {
            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);

            //primeiro campo da caixa: email
            EditText emailEdt = new EditText(context);
            emailEdt.setHint("Insira o email do usuário");
            layout.addView(emailEdt);

            //constrói a caixa
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Alteração de Senha");
            builder.setMessage("");
            builder.setCancelable(false);
            builder.setView(layout);

            //funções do botão para confirmar a alteração
            builder.setPositiveButton("Alterar", (dialogInterface, i) -> {
                if (emailEdt.getText().toString().isEmpty()) {
                    Toast.makeText(context, "Preencha o campo de email.", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.sendPasswordResetEmail(emailEdt.getText().toString()).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Enviamos um email para alterar a sua senha.", Toast.LENGTH_SHORT).show();
                        } else Toast.makeText(context, "Email não encontrado em nossos cadastros.", Toast.LENGTH_SHORT).show();
                    });
                }
            });

            //função do botão para cancelar a alteração
            builder.setNegativeButton("Cancelar", null);
            builder.show();
        });

        //função para entrar na tela de cadastro
        cadastroTxt.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, CadastroUsuarioActivity.class);
            startActivity(intent);
        });
    }

    //sincronizar botões Java com XML
    private void IniciarComponentes() {
        emailUserEdt = (EditText) findViewById(R.id.edtEmailLogin);
        senhaUserEdt = (EditText) findViewById(R.id.edtSenhaLogin);
        entrarBtn = (Button) findViewById(R.id.btnLogin);
        cadastroTxt = (TextView) findViewById(R.id.txtTelaCadastro);
        recupSenhaTxt = (TextView) findViewById(R.id.txtEsqueciSenha);
    }
}