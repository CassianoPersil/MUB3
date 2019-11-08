package com.projetos.mub;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RecycleViewAdapterGeral extends RecyclerView.Adapter<RecycleViewAdapterGeral.MyViewHolder> {
    private Context context;
    private List<CardGeral> Card;

    public RecycleViewAdapterGeral(Context context, List<CardGeral> Card) {
        this.context = context;
        this.Card = Card;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.card_geral, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.Alerta.setText(Card.get(position).getAlerta());
        holder.Vigencia.setText(Card.get(position).getVigencia());
        holder.statusInfo.setText(Card.get(position).getStatusInfo());
        holder.textOrgao.setText(Card.get(position).getTextOrgao());

        holder.Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OcorrenciaOrgao.class);
                Bundle informacoes = new Bundle();
                informacoes.putLong("idAlerta", Card.get(position).getIdAlerta());

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtras(informacoes);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return Card.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textOrgao;
        TextView textAlerta;
        TextView Alerta;
        TextView textVigencia;
        TextView Vigencia;
        TextView statusInformacao;
        TextView statusInfo;
        CardView Card;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textOrgao = (TextView) itemView.findViewById(R.id.textOrgao);
            textAlerta = (TextView) itemView.findViewById(R.id.textAlerta);
            Alerta = (TextView) itemView.findViewById(R.id.Alerta);
            textVigencia = (TextView) itemView.findViewById(R.id.textVigencia);
            Vigencia = (TextView) itemView.findViewById(R.id.Vigencia);
            statusInformacao = (TextView) itemView.findViewById(R.id.statusInformacao);
            statusInfo = (TextView) itemView.findViewById(R.id.StatusInfo);
            Card = (CardView) itemView.findViewById(R.id.Card);
        }
    }

}

