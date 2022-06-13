package com.jonasgarcia.sys_gestion_incidentes.adminUI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jonasgarcia.sys_gestion_incidentes.Indicentes.Incidente;
import com.jonasgarcia.sys_gestion_incidentes.R;

import java.util.ArrayList;

public class IncidentAdapter extends RecyclerView.Adapter<IncidentAdapter.ViewHolderData> {
    ArrayList<Incidente> listIncident;

    public IncidentAdapter(ArrayList<Incidente> listIncident) {
        this.listIncident = listIncident;
    }

    @NonNull
    @Override
    public ViewHolderData onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_incident,null,false);
        return new ViewHolderData(view);
    }

    @Override
    public void onBindViewHolder( ViewHolderData holder, int position) {
        holder.titleIncident.setText(listIncident.get(position).getTipo());
        holder.descriptionIncident.setText(listIncident.get(position).getDescripcion());
        holder.stateIncident.setText(listIncident.get(position).getEstado());
        holder.imageIncident.setImageResource(listIncident.get(position).getImagen());
    }

    @Override
    public int getItemCount() {
        return listIncident.size();
    }

    public class ViewHolderData extends RecyclerView.ViewHolder {
        TextView titleIncident, descriptionIncident, stateIncident;
        ImageView imageIncident;

        public ViewHolderData(@NonNull View itemView) {
            super(itemView);
            titleIncident = itemView.findViewById(R.id.titleIncident);
            descriptionIncident = itemView.findViewById(R.id.descriptionIncident);
            stateIncident = itemView.findViewById(R.id.stateIncident);
            imageIncident = itemView.findViewById(R.id.imageIncident);
        }
    }
}
