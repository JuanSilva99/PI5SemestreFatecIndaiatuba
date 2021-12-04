package com.example.loginpi5s.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
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

public class UsuarioActivity extends AppCompatActivity {

    //declaração de variáveis
    private TextView userName;
    private TextView userEmail;
    private Button nomeBtn;
    private Button senhaBtn;
    private UsuarioDAO udao;
    private Context context;
    private SharedPreferences.Editor dadosEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);
        SharedPreferences dados = getSharedPreferences("Dados", MODE_PRIVATE);
        dadosEdit = getSharedPreferences("Dados", MODE_PRIVATE).edit();
        context = UsuarioActivity.this;
        udao = new UsuarioDAO(this);

        //chama o método que sincrorniza as views
        IniciarComponentes();

        //coloca o nome e email do usuário na tela
        userName.setText(dados.getString("nome", "nome"));
        userEmail.setText(dados.getString("email", "email"));

        //função para trocar de nome
        nomeBtn.setOnClickListener(view -> {
            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);

            //primeiro campo da caixa: nome
            final EditText nomeEdt = new EditText(context);
            nomeEdt.setHint("Insira o novo nome");
            layout.addView(nomeEdt);

            //segundo campo da caixa: senha
            final EditText senhaEdt = new EditText(context);
            senhaEdt.setHint("Insira sua senha");
            senhaEdt.setTransformationMethod(PasswordTransformationMethod.getInstance());
            layout.addView(senhaEdt);

            //constrói a caixa
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Alteração de Nome");
            builder.setMessage("");
            builder.setCancelable(false);
            builder.setView(layout);

            //funções do botão para confirmar a alteração
            builder.setPositiveButton("Alterar", (dialogInterface, i) -> {
                String senhaEncrypt = null;
                try { //encripta a senha
                    senhaEncrypt = AESCrypt.encrypt(senhaEdt.getText().toString()).trim();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //validação, confirma se a senha está correta
                if (senhaEncrypt.equals(udao.recuperarSenha(userEmail.getText().toString()))){

                    //troca o nome nas SharedPreferences
                    dadosEdit.putString("nome",nomeEdt.getText().toString());
                    dadosEdit.apply();

                    //troca o nome no banco de dados e atualiza a activity
                    if(udao.mudarNome(userEmail.getText().toString(), nomeEdt.getText().toString())){
                        Toast.makeText(context, "Nome alterado com sucesso", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(getIntent());
                    } else {
                        Toast.makeText(context, "Falha ao alterar o nome", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Senha incorreta", Toast.LENGTH_SHORT).show();
                }
            });

            //função do botão para cancelar a alteração
            builder.setNegativeButton("Cancelar", null);
            builder.show();
        });

        //função para trocar de senha
        senhaBtn.setOnClickListener(view -> {
            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);

            //primeiro campo da caixa: antiga senha
            final EditText senhaEdt = new EditText(context);
            senhaEdt.setHint("Insira a senha atual");
            senhaEdt.setTransformationMethod(PasswordTransformationMethod.getInstance());
            layout.addView(senhaEdt);

            //segundo campo da caixa: nova senha
            final EditText novaSenhaEdt = new EditText(context);
            novaSenhaEdt.setHint("Insira a nova senha");
            novaSenhaEdt.setTransformationMethod(PasswordTransformationMethod.getInstance());
            layout.addView(novaSenhaEdt);

            //segundo campo da caixa: repetir a nova senha
            final EditText novaSenhaEdt2 = new EditText(context);
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
                String senhaVelhaEncrypt = null;
                try { //encripta a senha antiga
                    senhaVelhaEncrypt = AESCrypt.encrypt(senhaEdt.getText().toString()).trim();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String senhaNovaEncrypt = null;
                try { //encripta a nova senha
                    senhaNovaEncrypt = AESCrypt.encrypt(novaSenhaEdt.getText().toString()).trim();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //validação, confere se a senha antiga está correta para permitir a troca
                if (senhaVelhaEncrypt.equals(udao.recuperarSenha(userEmail.getText().toString()))){

                    //validação, confere se a senha nova foi digitada corretamente
                    if (novaSenhaEdt.getText().toString().equals(novaSenhaEdt2.getText().toString())){

                        //chama a mudança da senha no banco
                        if(udao.mudarSenha(userEmail.getText().toString(), senhaNovaEncrypt)){
                            Toast.makeText(context, "Senha alterada com sucesso", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(context, "Falha ao alterar a senha", Toast.LENGTH_SHORT).show();
                        }
                    } else{
                        Toast.makeText(context, "Confirmação de senha incorreta", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Senha antiga incorreta", Toast.LENGTH_SHORT).show();
                }
            });

            //função do botão para cancelar a alteração
            builder.setNegativeButton("Cancelar", null);
            builder.show();
        });
    }

    //sincronizar botões Java com XML
    private void IniciarComponentes(){
        userName = (TextView) findViewById(R.id.txtNomeUsuarioMyAcc);
        userEmail = (TextView) findViewById(R.id.txtEmailUsuarioMyAcc);
        nomeBtn = (Button) findViewById(R.id.btnMudarNome);
        senhaBtn = (Button) findViewById(R.id.btnMudarSenha);
    }
}