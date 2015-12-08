package fragments;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import piv.pivote.R;

/**
 * Created by Daniel on 03.08.2015.
 * Represents a Dialog for filtering the random List "All Polls"
 * The variables language and category will have the same value for every instance! (STATIC)
 * If you want to use this class for different Filter you will have to handle the Fragment in the Backstack and make the variables non-static.
 */
public class DialogFilter extends DialogFragment {

    private static int language = 0;
    private static int category = 0;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_filter, container);

        Spinner spinnerLanguage = (Spinner) rootView.findViewById(R.id.spinner_filter_language);
        Spinner spinnerCategory = (Spinner) rootView.findViewById(R.id.spinner_filter_category);
        spinnerLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                language = parent.getSelectedItemPosition();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = parent.getSelectedItemPosition();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerLanguage.setSelection(language);
        spinnerCategory.setSelection(category);

        Button buttonPositive = (Button) rootView.findViewById(R.id.button_filter_positive);
        Button buttonNegative = (Button) rootView.findViewById(R.id.button_filter_negative);

        buttonPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getIntent().putExtra("language", language);
                getActivity().getIntent().putExtra("category", category);
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, getActivity().getIntent());
                dismiss();
            }
        });

        buttonNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return rootView;
    }
}
