package com.dc.okhttpdownloadmanager;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Created by pxh on 2016/3/19.
 */
public class TaskConfirmDialog extends DialogFragment
{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_layout, null);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final EditText url = (EditText) view.findViewById(R.id.url);
        final EditText fileName = (EditText) view.findViewById(R.id.fileName);
        builder.setView(view)
                // Add action buttons
                .setPositiveButton("confirm",
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int id)
                            {
                                InputCompletedListener listener = (InputCompletedListener) getActivity();
                                listener.inputCompleted(url.getText().toString(), fileName
                                        .getText().toString());
                            }
                        }).setNegativeButton("Cancel", null);
        return builder.create();
    }

    interface InputCompletedListener
    {
        void inputCompleted(String url, String fileName);
    }
}
