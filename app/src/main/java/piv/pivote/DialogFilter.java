package piv.pivote;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Created by Daniel on 03.08.2015.
 */
public class DialogFilter extends DialogFragment {

    private String language = null;
    private String category = null;

    //ToDo: Check correct implementation of dialogFragment (Fragment doesn't get deleted from the FragmentBackStack) // Probably save state of selected Filter options

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_filter, null);
        builder.setView(view);
        builder.setTitle(getString(R.string.dialogTitle));


        Spinner spinnerLanguage = (Spinner) view.findViewById(R.id.spinner_filter_language);
        Spinner spinnerCategory = (Spinner) view.findViewById(R.id.spinner_filter_category);
                spinnerLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        language = parent.getSelectedItem().toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        category = parent.getSelectedItem().toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });




        builder.setPositiveButton(getString(R.string.dialogButtonPositive), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getActivity().getIntent().putExtra("language", language);
                getActivity().getIntent().putExtra("category", category);
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, getActivity().getIntent());

            }
        });
        builder.setNegativeButton(getString(R.string.dialogButtonNegative), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    dismiss();
            }
        });


        return builder.create();
    }


}
