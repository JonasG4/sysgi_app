package com.jonasgarcia.sys_gestion_incidentes.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

import com.jonasgarcia.sys_gestion_incidentes.R;

public class DialogLoading {
    private Context context;
    private AlertDialog dialog;

    public DialogLoading(Context context) {
      this.context = context;
    }

    public void startDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_dialog_loading, null);
        builder.setView(view);
        builder.setCancelable(false);

        dialog = builder.create();
        if(dialog.getWindow() != null){
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        dialog.show();
    }

    public void dismissDialog(){
        dialog.dismiss();
    }
}
