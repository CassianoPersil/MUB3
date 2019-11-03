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
import android.widget.Toast;

import java.util.List;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder> {
    private Context context;
    private List<Cards> mCard;

    public RecycleViewAdapter(Context context, List<Cards> mCard) {
        this.context = context;
        this.mCard = mCard;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.cards, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.Tipo.setText(mCard.get(position).getTipo());
        holder.Status.setText(mCard.get(position).getStatus());
        holder.Protocolo.setText(mCard.get(position).getProtocolo());
        holder.Data.setText(mCard.get(position).getData());
        holder.Horario.setText(mCard.get(position).getHorario());

        holder.Cards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OcorrenciasUsuario.class);

                Bundle informacoes = new Bundle();
                informacoes.putLong("idOcorrencia", mCard.get(position).getIdOcorrencia());

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtras(informacoes);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mCard.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textOcorrencia;
        TextView textTipo;
        TextView Tipo;
        TextView textStatus;
        TextView Status;
        TextView Protocolo;
        TextView textData;
        TextView Data;
        TextView textHora;
        TextView Horario;
        CardView Cards;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textOcorrencia = (TextView) itemView.findViewById(R.id.textOcorrencia);
            textTipo = (TextView) itemView.findViewById(R.id.textTipo);
            Tipo = (TextView) itemView.findViewById(R.id.Tipo);
            textStatus = (TextView) itemView.findViewById(R.id.textStatus);
            Status = (TextView) itemView.findViewById(R.id.Status);
            Protocolo = (TextView) itemView.findViewById(R.id.Protocolo);
            textData = (TextView) itemView.findViewById(R.id.textData);
            Data = (TextView) itemView.findViewById(R.id.Data);
            textHora = (TextView) itemView.findViewById(R.id.textHora);
            Horario = (TextView) itemView.findViewById(R.id.Horario);
            Cards = (CardView) itemView.findViewById(R.id.Card);
        }
    }

}

