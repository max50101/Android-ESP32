package com.example.android_esp32;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import org.jetbrains.annotations.NotNull;

public class DialogClass extends AppCompatDialogFragment {
    EditText name;
    private ExampleDialogListener listener;
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View v=inflater.inflate(R.layout.dialog_name,null);
        builder.setView(v)
                .setTitle("Name")
                .setNegativeButton("cancel", (dialog, which) -> {

                })
                .setPositiveButton("Okay", (dialog, which) -> {
                    String nameF=name.getText().toString();
                    listener.applyTexts(nameF);
                });
        name=v.findViewById(R.id.editTextTextPersonName);
        return builder.create();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (ExampleDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");
        }
    }

    public interface ExampleDialogListener {
        void applyTexts(String username);
    }
}

