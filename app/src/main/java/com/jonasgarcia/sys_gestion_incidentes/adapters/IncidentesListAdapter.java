package com.jonasgarcia.sys_gestion_incidentes.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jonasgarcia.sys_gestion_incidentes.R;
import com.jonasgarcia.sys_gestion_incidentes.models.Incidente;
import com.jonasgarcia.sys_gestion_incidentes.models.Usuario;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class IncidentesListAdapter extends RecyclerView.Adapter<IncidentesListAdapter.ViewHolder>  {

    public List<Incidente> incidentes;
    public LayoutInflater inflater;
    public Context context;
    final IncidentesListAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Incidente item);
    }

    public IncidentesListAdapter(List<Incidente> incidentes,  Context context, IncidentesListAdapter.OnItemClickListener listerner){
        this.inflater = LayoutInflater.from(context);
        this.incidentes = incidentes;
        this.context = context;
        this.listener = listerner;
    }

    @NonNull
    @Override
    public IncidentesListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_incident, null);
        return new IncidentesListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IncidentesListAdapter.ViewHolder holder, int position) {
        holder.bindData(incidentes.get(position));
    }

    @Override
    public int getItemCount() {
        return incidentes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView type, description, status, date;
        ImageView image;

        ViewHolder(View itemView) {
            super(itemView);
            type = itemView.findViewById(R.id.cv_type);
            description = itemView.findViewById(R.id.cv_description);
            status = itemView.findViewById(R.id.cv_state);
            date = itemView.findViewById(R.id.cv_date);
            image = itemView.findViewById(R.id.cv_image);
        }

        void bindData(Incidente item) {

            switch (item.getTipo().toLowerCase(Locale.ROOT)){
                case "baja":
                    type.setBackgroundResource(R.drawable.bg_tag_tipo_bajo);
                    break;
                case "media":
                    type.setBackgroundResource(R.drawable.bg_tag_tipo_medio);
                    break;
                case "importante":
                    type.setBackgroundResource(R.drawable.bg_tag_tipo_importante);
                    break;
                case "alta":
                    type.setBackgroundResource(R.drawable.bg_tag_tipo_alta);
                    break;
                default:
                    type.setBackgroundResource(R.drawable.bg_tag_tipo_bajo);
                    break;
            }

            type.setText(item.getTipo());

            description.setText(item.getDescripcion());
            if(item.getEstado().equals("0")){
                status.setText("Activo");
                status.setBackgroundResource(R.drawable.bg_tag_active);
            }else{
                status.setText("Resuelto");
            }

            date.setText(item.getFechaIngreso().split(" ")[0]);

            Picasso.get().load(item.getImagen()).into(image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(item);
                }
            });
        }
    }

}
