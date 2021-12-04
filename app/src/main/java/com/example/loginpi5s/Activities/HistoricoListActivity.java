package com.example.loginpi5s.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.loginpi5s.DAO.TransacaoDAO;
import com.example.loginpi5s.HistoricoAdapter;
import com.example.loginpi5s.R;
import com.example.loginpi5s.Transacao;

public class HistoricoListActivity extends AppCompatActivity {

    //declaração de variáveis
    private RecyclerView recyclerView;
    private TransacaoDAO tdao;
    private SharedPreferences dados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico_list);
        dados = getSharedPreferences("Dados", MODE_PRIVATE);
        tdao = new TransacaoDAO(this);

        //chama o método que sincrorniza as views
        IniciarComponentes();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //cria um array de cards de transações (HistoricoAdapter) para exibir na tela
        ArrayList<Transacao> historico = tdao.getAllHistorico(dados.getInt("id", 0));

        HistoricoAdapter historicoAdapter = new HistoricoAdapter(historico, this);
        recyclerView.setAdapter(historicoAdapter);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(llm);
    }

    //sincronizar botões Java com XML
    private void IniciarComponentes(){
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
    }
}