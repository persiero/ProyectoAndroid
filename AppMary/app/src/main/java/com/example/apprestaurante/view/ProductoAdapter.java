package com.example.apprestaurante.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apprestaurante.R;
import com.example.apprestaurante.model.Producto;

import java.util.ArrayList;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder> {

    private ArrayList<Producto> listaProductos;
    private OnTotalChangeListener listener; // Interfaz para avisar a la actividad

    // Constructor
    public ProductoAdapter(ArrayList<Producto> listaProductos, OnTotalChangeListener listener) {
        this.listaProductos = listaProductos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto, parent, false);
        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        Producto producto = listaProductos.get(position);

        holder.tvNombre.setText(producto.getNombre());
        holder.tvPrecio.setText("S/. " + String.format("%.2f", producto.getPrecio()));
        holder.tvStock.setText("Stock: " + producto.getStock());

        // Mostrar la cantidad actual (que guardamos en el modelo en el Paso 16)
        holder.tvCantidad.setText(String.valueOf(producto.getCantidadSolicitada()));

        // Botón MAS (+)
        holder.btnMas.setOnClickListener(v -> {
            if (producto.getCantidadSolicitada() < producto.getStock()) {
                producto.setCantidadSolicitada(producto.getCantidadSolicitada() + 1);
                holder.tvCantidad.setText(String.valueOf(producto.getCantidadSolicitada()));
                listener.onTotalChange(); // Avisar para recalcular total
            }
        });

        // Botón MENOS (-)
        holder.btnMenos.setOnClickListener(v -> {
            if (producto.getCantidadSolicitada() > 0) {
                producto.setCantidadSolicitada(producto.getCantidadSolicitada() - 1);
                holder.tvCantidad.setText(String.valueOf(producto.getCantidadSolicitada()));
                listener.onTotalChange(); // Avisar para recalcular total
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaProductos.size();
    }

    // Clase interna ViewHolder (Vincula el XML con Java)
    public static class ProductoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvPrecio, tvStock, tvCantidad;
        Button btnMas, btnMenos;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombrePlato);
            tvPrecio = itemView.findViewById(R.id.tvPrecioPlato);
            tvStock = itemView.findViewById(R.id.tvStockPlato);
            tvCantidad = itemView.findViewById(R.id.tvCantidad);
            btnMas = itemView.findViewById(R.id.btnMas);
            btnMenos = itemView.findViewById(R.id.btnMenos);
        }
    }

    // Interfaz para comunicación
    public interface OnTotalChangeListener {
        void onTotalChange();
    }
}
