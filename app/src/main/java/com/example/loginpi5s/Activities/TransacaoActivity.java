package com.example.loginpi5s.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginpi5s.DAO.TransacaoDAO;
import com.example.loginpi5s.R;
import com.example.loginpi5s.Transacao;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class TransacaoActivity extends AppCompatActivity {

    //declaração de variáveis
    private AutoCompleteTextView categoriaACTV;
    private EditText valorEdt;
    private EditText dataEdt;
    private EditText observacaoEdt;
    private Button confirmarBtn;
    private TransacaoDAO tdao;
    private TextView userInfoTxt;
    private TextView tituloTxt;
    private String id;
    private Context context;
    private SharedPreferences dados;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transacao);
        mAuth = FirebaseAuth.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();;
        context = TransacaoActivity.this;
        dados = getSharedPreferences("Dados", MODE_PRIVATE);
        tdao = new TransacaoDAO(this);

        //chama o método que sincrorniza as views
        IniciarComponentes();

        //coloca o nome do usuário logado no topo da tela
        UpdateName();

        //recebe o tipo e classe que a tela incorporará (entrada ou saída, cadastro ou atualização)
        String tipo = getIntent().getExtras().getString("tipo");
        String classe = getIntent().getExtras().getString("classe");

        //configura título e itens das categorias de acordo com o tipo
        String[] items;
        if (tipo.equals("Entrada")){
            items = new String[]{
                    "Salário",
                    "Renda Extra",
                    "Outros"
            };
        } else {
            tituloTxt.setText(R.string.textoCadastrarSaida);
            items = new String[]{
                    "Moradia",
                    "Supermercado",
                    "TV / Internet / Telefone",
                    "Transporte",
                    "Lazer",
                    "Saúde",
                    "Bares e Restaurantes",
                    "Outros"
            };
        }

        //configura título e outros de acordo com a classe
        if (classe.equals("att")){
            tituloTxt.setText(R.string.textoAtualizarTrans);
            confirmarBtn.setText(R.string.textoAtualizar);
            Transacao s = (Transacao) getIntent().getExtras().getSerializable("HISTORICO");
            id = s.getId();
            categoriaACTV.setText(s.getCategoria());
            valorEdt.setText(s.getValor());
            dataEdt.setText(s.getData());
            observacaoEdt.setText(s.getObservacao());
        }

        //função de selecionar a lista da categoria
        ArrayAdapter<String> adapterItems = new ArrayAdapter<>(this, R.layout.list_item, items);
        categoriaACTV.setAdapter(adapterItems);

        //inicia as funções do calendário
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        //cria o calendário ao tocar no campo
        dataEdt.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    TransacaoActivity.this, (datePicker, year1, month1, day1) -> {
                        month1 = month1 +1;
                        String date = day1 +"/"+ month1 +"/"+ year1;
                        dataEdt.setText(date);
                    },year,month,day);
            datePickerDialog.show();
        });

        //funções do botão "Cadastrar/Atualizar"
        confirmarBtn.setOnClickListener(view -> {
            //funções em caso de cadastro
            if (classe.equals("new")){
                //recebe os valores dos EditTexts
                Transacao t = new Transacao();
                t.setId_user(currentUser.getUid());
                t.setTipo(tipo);
                t.setCategoria(categoriaACTV.getText().toString());
                t.setValor(valorEdt.getText().toString());
                t.setData(dataEdt.getText().toString());
                t.setObservacao(observacaoEdt.getText().toString());

                //validação, todos os campos precisam ser preenchidos
                if (t.getCategoria().isEmpty() || t.getValor().isEmpty() || t.getData().isEmpty()){
                    Toast.makeText(context, "Preencha todos os campos necessários!", Toast.LENGTH_SHORT).show();
                } else {
                    //insere a transação no banco de dados
                    try {
                        t.setId(mAuth.getUid());
                        t.save();
                        Toast.makeText(context, "Entrada registrada com sucesso", Toast.LENGTH_SHORT).show();
                        finish();
                    } catch (Exception e){
                        Toast.makeText(context, "Falha ao registrar a entrada", Toast.LENGTH_SHORT).show();
                    }
                }
            } else { //funções em caso de atualização
                String categoria = categoriaACTV.getText().toString().trim();
                String valor = valorEdt.getText().toString().trim();
                String data = dataEdt.getText().toString().trim();
                String obs = observacaoEdt.getText().toString().trim();

                //validação, todos os campos precisam ser preenchidos
                if (categoria.isEmpty() || valor.isEmpty() || data.isEmpty()){
                    Toast.makeText(context, "Preencha todos os campos necessários!", Toast.LENGTH_SHORT).show();
                } else {
                    Transacao s = new Transacao(id, categoria, valor, data, obs);
                    //TransacaoDAO dao = new TransacaoDAO(this);

                    //atualiza a transação no banco de dados
                    try {
                        s.setId(mAuth.getUid());
                        s.save();
                        Toast.makeText(this, "Atualizado com sucesso", Toast.LENGTH_SHORT).show();
                        finish();
                    } catch (Exception e){
                        Toast.makeText(this, "Falha na atualização", Toast.LENGTH_SHORT).show();
                    }
                }
            }
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
    private void IniciarComponentes(){
        categoriaACTV = (AutoCompleteTextView) findViewById(R.id.actCategoriaTransacao);
        valorEdt = (EditText) findViewById(R.id.edtValorTransacao);
        dataEdt = (EditText) findViewById(R.id.edtDataTransacao);
        observacaoEdt = (EditText) findViewById(R.id.edtObservacaoTransacao);
        confirmarBtn = (Button) findViewById(R.id.btnCadastrarTransacao);
        userInfoTxt = (TextView) findViewById(R.id.txtUserInfoTransacao);
        tituloTxt = (TextView) findViewById(R.id.txtTitleTransacao);
    }
}