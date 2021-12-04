package com.example.loginpi5s.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginpi5s.AESCrypt;
import com.example.loginpi5s.DAO.UsuarioDAO;
import com.example.loginpi5s.R;

public class LoginActivity extends AppCompatActivity {

    //declaração de variáveis
    private EditText emailUserEdt;
    private EditText senhaUserEdt;
    private TextView cadastroTxt;
    private TextView recupSenhaTxt;
    private Button entrarBtn;
    private String userEmail;
    private String userPswrd;
    private UsuarioDAO udao;
    private Context context;
    private SharedPreferences.Editor dadosEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = LoginActivity.this;
        udao = new UsuarioDAO(this);
        SharedPreferences dados = getSharedPreferences("Dados", MODE_PRIVATE);

        //chama o método que sincrorniza as views
        IniciarComponentes();

        //recupera SharedPreferences para login automatico
        userEmail = dados.getString("email", "email");
        userPswrd = dados.getString("senha", "senha");

        //checa SharedPreferences para login automatico
        if (udao.checkLogin(userEmail, userPswrd)){
            Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
            startActivity(intent);
        }

        //função para efetuar login e prosseguir ao menu
        entrarBtn.setOnClickListener(view -> {

            //recebe os valores dos EditTexts
            userEmail = emailUserEdt.getText().toString().toLowerCase();
            try { //criptografa a senha
                userPswrd = AESCrypt.encrypt(senhaUserEdt.getText().toString()).trim();
            } catch (Exception e) {
                e.printStackTrace();
            }

            //validação, todos os campos precisam ser preenchidos
            if (userPswrd.isEmpty() || userEmail.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            } else {
                //validação, checa se o usuário existe
                if (udao.checkEmail(userEmail)){
                    Intent intent = new Intent(LoginActivity.this, MenuActivity.class);

                    //validação, checa se a senha está correta
                    if(udao.checkLogin(userEmail, userPswrd)){
                        //grava dados relevantes no SharedPreferences para uso futuro
                        dadosEdit = getSharedPreferences("Dados", MODE_PRIVATE).edit();
                        dadosEdit.putString("email", userEmail);
                        dadosEdit.putString("nome", udao.recuperarNome(userEmail));
                        dadosEdit.putInt("id", udao.recuperarId(userEmail));
                        dadosEdit.putString("senha", udao.recuperarSenha(userEmail));
                        dadosEdit.apply();

                        Toast.makeText(context, "Login realizado com sucesso", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                    } else{
                        Toast.makeText(LoginActivity.this, "Senha incorreta!", Toast.LENGTH_SHORT).show();
                    }
                } else{
                    Toast.makeText(LoginActivity.this, "Usuário não registrado.", Toast.LENGTH_SHORT).show();
                }
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

            //segundo campo da caixa: nova senha
            EditText novaSenhaEdt = new EditText(context);
            novaSenhaEdt.setHint("Insira a nova senha");
            novaSenhaEdt.setTransformationMethod(PasswordTransformationMethod.getInstance());
            layout.addView(novaSenhaEdt);

            //terceiro campo da caixa: repetir a senha
            EditText novaSenhaEdt2 = new EditText(context);
            novaSenhaEdt2.setHint("Confirme a nova senha");
            novaSenhaEdt2.setTransformationMethod(PasswordTransformationMethod.getInstance());
            layout.addView(novaSenhaEdt2);

            //constrói a caixa
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Alteração de Senha");
            builder.setMessage("");
            builder.setCancelable(false);
            builder.setView(layout);

            //funções do botão para confirmar a alteração
            builder.setPositiveButton("Alterar", (dialogInterface, i) -> {
                //validação, checa se o usuário existe
                if (udao.checkEmail(emailEdt.getText().toString())){

                    //validação, confirma se a senha foi digitada corretamente
                    if (novaSenhaEdt.getText().toString().equals(novaSenhaEdt2.getText().toString())){
                        String senhaEncrypt = null;
                        try { //encripta a senha
                            senhaEncrypt = AESCrypt.encrypt(novaSenhaEdt.getText().toString()).trim();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        //chama a mudança da senha no banco
                        if(udao.mudarSenha(emailEdt.getText().toString(), senhaEncrypt)){
                            Toast.makeText(context, "Senha alterada com sucesso", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Falha ao alterar senha", Toast.LENGTH_SHORT).show();
                        }
                    } else{
                        Toast.makeText(context, "Confirmação de senha incorreta", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Este usuário não está registrado", Toast.LENGTH_SHORT).show();
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
    private void IniciarComponentes(){
        emailUserEdt = (EditText) findViewById(R.id.edtEmailLogin);
        senhaUserEdt = (EditText) findViewById(R.id.edtSenhaLogin);
        entrarBtn = (Button) findViewById(R.id.btnLogin);
        cadastroTxt = (TextView) findViewById(R.id.txtTelaCadastro);
        recupSenhaTxt = (TextView) findViewById(R.id.txtEsqueciSenha);
    }
}