package com.jonasgarcia.sys_gestion_incidentes.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jonasgarcia.sys_gestion_incidentes.R;
import com.jonasgarcia.sys_gestion_incidentes.models.Usuario;

import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {

    public List<Usuario> usuarios;
    public LayoutInflater inflater;
    public Context context;
    final UserListAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Usuario item);
    }

    public UserListAdapter(List<Usuario> usuarios, Context context, UserListAdapter.OnItemClickListener listerner) {
        this.inflater = LayoutInflater.from(context);
        this.usuarios = usuarios;
        this.context = context;
        this.listener = listerner;
    }

    @NonNull
    @Override
    public UserListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_list_user, null);
        return new UserListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListAdapter.ViewHolder holder, int position) {
        holder.bindData(usuarios.get(position));
    }

    @Override
    public int getItemCount() {
        return usuarios.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView role, fullname, email;
        LinearLayout containerRole;
        ViewHolder(View itemView) {
            super(itemView);

            role = itemView.findViewById(R.id.cv_UserRol);
            containerRole = itemView.findViewById(R.id.cv_containerRole);
            fullname = itemView.findViewById(R.id.cv_UserFullName);
            email = itemView.findViewById(R.id.cv_UserEmail);
        }

        void bindData(Usuario item) {
            if (item.getRol() > 1) {
                role.setText("Admin");
                containerRole.setBackgroundResource(R.color.rose_600);
            } else {
                role.setText("Empleado");
            }
            fullname.setText(item.getFullName());
            email.setText(item.getEmail());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(item);
                }
            });
        }
    }

}
