package com.example.apprestaurante.view;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.apprestaurante.R;
import com.example.apprestaurante.model.Mesa;
import java.util.ArrayList;

public class MesaAdapter extends RecyclerView.Adapter<MesaAdapter.MesaViewHolder> {

    private ArrayList<Mesa> listaMesas;
    private OnMesaClickListener listener;

    public MesaAdapter(ArrayList<Mesa> listaMesas, OnMesaClickListener listener) {
        this.listaMesas = listaMesas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MesaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mesa, parent, false);
        return new MesaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MesaViewHolder holder, int position) {
        Mesa mesa = listaMesas.get(position);

        holder.tvNombre.setText(mesa.getNumeroMesa());
        holder.tvEstado.setText(mesa.getEstado());

        // CAMBIO DE COLOR DINÁMICO
        if (mesa.getEstado().equals("OCUPADA")) {
            holder.cardView.setCardBackgroundColor(Color.parseColor("#D32F2F")); // Rojo
        } else {
            holder.cardView.setCardBackgroundColor(Color.parseColor("#4CAF50")); // Verde
        }

        // Evento Click
        holder.itemView.setOnClickListener(v -> listener.onMesaClick(mesa));
    }

    @Override
    public int getItemCount() { return listaMesas.size(); }

    public static class MesaViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvEstado;
        CardView cardView;

        public MesaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreMesa);
            tvEstado = itemView.findViewById(R.id.tvEstadoMesa);
            cardView = itemView.findViewById(R.id.cardMesa);
        }
    }

    public interface OnMesaClickListener {
        void onMesaClick(Mesa mesa);
    }
}
