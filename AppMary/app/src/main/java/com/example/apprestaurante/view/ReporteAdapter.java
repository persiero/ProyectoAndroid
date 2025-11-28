package com.example.apprestaurante.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.apprestaurante.R;
import com.example.apprestaurante.model.Pedido;
import java.util.ArrayList;

public class ReporteAdapter extends RecyclerView.Adapter<ReporteAdapter.ReporteViewHolder> {

    private ArrayList<Pedido> lista;

    public ReporteAdapter(ArrayList<Pedido> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public ReporteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reporte, parent, false);
        return new ReporteViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ReporteViewHolder holder, int position) {
        Pedido p = lista.get(position);
        holder.tvId.setText("#" + p.getId());
        holder.tvTipo.setText(p.getTipoComprobante());
        holder.tvMonto.setText("S/. " + String.format("%.2f", p.getTotal()));
    }

    @Override
    public int getItemCount() { return lista.size(); }

    public static class ReporteViewHolder extends RecyclerView.ViewHolder {
        TextView tvId, tvTipo, tvMonto;
        public ReporteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvIdPedidoRep);
            tvTipo = itemView.findViewById(R.id.tvTipoRep);
            tvMonto = itemView.findViewById(R.id.tvMontoRep);
        }
    }
}
