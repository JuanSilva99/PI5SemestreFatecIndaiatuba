package com.example.loginpi5s.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import android.os.Bundle;

import com.example.loginpi5s.HistoricoAdapter;
import com.example.loginpi5s.R;
import com.example.loginpi5s.Transacao;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HistoricoListActivity extends AppCompatActivity {

    //declaração de variáveis
    private DatabaseReference reference;
    private RecyclerView recyclerView;
    private FirebaseUser currentUser;
    private ArrayList<String> keyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico_list);
        reference = FirebaseDatabase.getInstance().getReference();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        //chama o método que sincrorniza as views
        IniciarComponentes();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //cria um array de cards de transações (HistoricoAdapter) para exibir na tela
        keyList = new ArrayList<String>();
        ArrayList<Transacao> historico = GetAllHistorico();

        HistoricoAdapter historicoAdapter = new HistoricoAdapter(historico, this, keyList);
        recyclerView.setAdapter(historicoAdapter);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(llm);
    }

    private ArrayList<Transacao> GetAllHistorico() {
        ArrayList<Transacao> registro = new ArrayList<>();
        keyList.clear();

        reference.child("Usuarios").child(currentUser.getUid()).child("Entrada").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Transacao t = new Transacao(
                            "Entrada",
                            ds.child("categoria").getValue(String.class),
                            ds.child("valor").getValue(String.class),
                            ds.child("data").getValue(String.class),
                            ds.child("observacao").getValue(String.class)
                    );
                    keyList.add(ds.getKey());
                    registro.add(t);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        reference.child("Usuarios").child(currentUser.getUid()).child("Saída").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Transacao t = new Transacao(
                            "Saída",
                            ds.child("categoria").getValue(String.class),
                            ds.child("valor").getValue(String.class),
                            ds.child("data").getValue(String.class),
                            ds.child("observacao").getValue(String.class)
                    );
                    keyList.add(ds.getKey());
                    registro.add(t);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        return registro;
    }

    //sincronizar botões Java com XML
    private void IniciarComponentes() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
    }
}