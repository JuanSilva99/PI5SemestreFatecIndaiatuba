package com.example.loginpi5s.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.loginpi5s.AESCrypt;
import com.example.loginpi5s.DAO.UsuarioDAO;
import com.example.loginpi5s.R;
import com.example.loginpi5s.Usuario;

import java.io.Serializable;

public class CadastroUsuarioActivity extends AppCompatActivity implements Serializable {

    //declaração de variáveis
    private EditText nomeUserEdt;
    private EditText emailUserEdt;
    private EditText senhaUserEdt;
    private EditText senhaConfEdt;
    private Button cadastroBtn;
    private UsuarioDAO udao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        //chama o método que sincrorniza as views
        IniciarComponentes();

        udao = new UsuarioDAO(this);

        //funções para o botão cadastrar
        cadastroBtn.setOnClickListener(view -> {

            //validação, confirma se a senha foi digitada corretamente
            if (senhaUserEdt.getText().toString().equals(senhaConfEdt.getText().toString())){
                // recebe os valores dos EditTexts
                Usuario u = new Usuario();
                u.setNome(nomeUserEdt.getText().toString());
                u.setEmail(emailUserEdt.getText().toString().toLowerCase());
                //criptografa a senha
                try {
                    u.setSenha(AESCrypt.encrypt(senhaUserEdt.getText().toString()).trim());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //validação, todos os campos precisam ser preenchidos
                if (u.getNome().isEmpty() || u.getEmail().isEmpty() || u.getSenha().isEmpty()) {
                    Toast.makeText(CadastroUsuarioActivity.this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                }
                else {
                    //validação, email não pode estar cadastrado
                    if (udao.checkEmail(emailUserEdt.getText().toString())){
                        Toast.makeText(CadastroUsuarioActivity.this, "E-mail já registrado", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        //chama a gravação no BD
                        if (udao.inserir(u)){
                            //confirma a gravação
                            Toast.makeText(CadastroUsuarioActivity.this, "Novo usuário registrado", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(CadastroUsuarioActivity.this, "Falha ao registrar novo usuário", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            } else {
                Toast.makeText(CadastroUsuarioActivity.this, "Confirmação de senha incorreta", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //sincronizar botões Java com XML
    private void IniciarComponentes(){
        nomeUserEdt = (EditText) findViewById(R.id.edtNomeCadastro);
        emailUserEdt = (EditText) findViewById(R.id.edtEmailCadastro);
        senhaUserEdt = (EditText) findViewById(R.id.edtSenhaCadastro);
        senhaConfEdt = (EditText) findViewById(R.id.edtSenhaCadastro2);
        cadastroBtn = (Button) findViewById(R.id.btnCadastro);
    }
}