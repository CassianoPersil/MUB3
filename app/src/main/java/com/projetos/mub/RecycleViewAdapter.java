package com.projetos.mub;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        view = layoutInflater.inflate(R.layout.cards, parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        //holder.textOcorrencia.setText(mCard.get(position).getTextOcorrencia());
        //holder.Tipo.setText(mCard.get(position).getTipo());
        //holder.Status.setText(mCard.get(position).getStatus());
        holder.Cards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, InserirOcorrencia.class);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mCard.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textOcorrencia;
        TextView textTipo;
        TextView Tipo;
        TextView textStatus;
        TextView Status;
        CardView Cards;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textOcorrencia = (TextView) itemView.findViewById(R.id.textOcorrencia);
            textTipo = (TextView) itemView.findViewById(R.id.textTipo);
            Tipo = (TextView) itemView.findViewById(R.id.Tipo);
            textStatus = (TextView) itemView.findViewById(R.id.textStatus);
            Status = (TextView) itemView.findViewById(R.id.Status);
            Cards = (CardView) itemView.findViewById(R.id.Card);



        }
    }

}

