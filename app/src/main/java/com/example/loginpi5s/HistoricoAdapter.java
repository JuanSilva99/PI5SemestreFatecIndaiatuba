/*package com.example.loginpi5s;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginpi5s.Activities.TransacaoActivity;
import com.example.loginpi5s.DAO.TransacaoDAO;

import java.util.ArrayList;

public class HistoricoAdapter extends RecyclerView.Adapter<HistoricoAdapter.HistoricoVH> {

    //declaração de variáveis
    private final ArrayList<Transacao> HISTORICO;
    private final Context CONTEXT;

    //construtor
    public HistoricoAdapter(ArrayList<Transacao> historico, Context context) {
        this.HISTORICO = historico;
        this.CONTEXT = context;
    }

    @NonNull
    @Override
    public HistoricoVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(CONTEXT);
        View view = inflater.inflate(R.layout.row_historico, parent, false);
        return new HistoricoVH(view);
    }

    @SuppressLint({"NotifyDataSetChanged", "DefaultLocale", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull HistoricoVH holder, int position) {
        Transacao s = HISTORICO.get(position);
        float f = Float.parseFloat(s.getValor());
        holder.tipoTxt.setText(s.getTipo());
        holder.categoriaTxt.setText(s.getCategoria());
        holder.valorTxt.setText("R$ " + String.format("%.02f",f));
        holder.dataTxt.setText(s.getData());

        //funções do botão Atualizar
        holder.updateCrd.setOnClickListener((view) -> {
            Intent intent = new Intent(CONTEXT, TransacaoActivity.class);
            intent.putExtra("HISTORICO", s);
            intent.putExtra("classe", "att");
            if(holder.tipoTxt.getText().equals("Entrada")){
                intent.putExtra("tipo", "Entrada");
            }
            else{
                intent.putExtra("tipo", "Saída");
            }

            CONTEXT.startActivity(intent);
        });

        //funções do botão Apagar
        holder.deleteCrd.setOnClickListener((view) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(CONTEXT);
            builder.setTitle("Confirmação");
            builder.setMessage("Tem certeza que deseja apagar?");
            builder.setIcon(android.R.drawable.ic_menu_delete);
            builder.setCancelable(false);

            builder.setPositiveButton("Sim", (dialogInterface, i) -> {
                TransacaoDAO dao = new TransacaoDAO(CONTEXT);

                if (dao.deleteTransacao(s.getId())){
                    Toast.makeText(CONTEXT, "Transação apagada", Toast.LENGTH_SHORT).show();
                    HISTORICO.remove(s);
                    notifyDataSetChanged();
                }
                else {
                    Toast.makeText(CONTEXT, "Falha ao apagar a transação", Toast.LENGTH_SHORT).show();
                }
            });

            builder.setNegativeButton("Não", null);
            builder.show();
        });
    }

    @Override
    public int getItemCount() {
        return HISTORICO.size();
    }

    static class HistoricoVH extends RecyclerView.ViewHolder{

        TextView categoriaTxt;
        TextView valorTxt;
        TextView dataTxt;
        TextView tipoTxt;
        CardView updateCrd;
        CardView deleteCrd;

        public HistoricoVH(@NonNull View v) {
            super(v);

            //configuração dos componentes
            tipoTxt = (TextView) v.findViewById(R.id.txtTipoHist);
            categoriaTxt = (TextView) v.findViewById(R.id.txtDbCategoriaHist);
            valorTxt = (TextView) v.findViewById(R.id.txtDbValorHist);
            dataTxt = (TextView) v.findViewById(R.id.txtDbDataHist);
            updateCrd = (CardView) v.findViewById(R.id.crdUpdate);
            deleteCrd = (CardView) v.findViewById(R.id.crdDelete);
        }
    }
}*/