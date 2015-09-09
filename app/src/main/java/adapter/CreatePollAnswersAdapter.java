package adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import piv.pivote.R;

/**
 * Created by Daniel on 08.08.2015.
 */
public class CreatePollAnswersAdapter extends RecyclerView.Adapter<CreatePollAnswersAdapter.ViewHolder> {
    private ArrayList<Integer> numAnswers;
    TreeMap<Integer, String> answers = new TreeMap<>();


    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView number;
        protected final EditText answerText;
        protected int pos;

        public ViewHolder(View v) {
            super(v);
            number = (TextView) v.findViewById(R.id.answer_number_cp);
            answerText = (EditText) v.findViewById(R.id.editText_answer_cp);

        }
    }

    public CreatePollAnswersAdapter(ArrayList<Integer> numberOfAnswers) {
        this.numAnswers = numberOfAnswers;
    }

    @Override
    public CreatePollAnswersAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_answers_cp, parent, false);
        final ViewHolder vh = new ViewHolder(v);

        vh.answerText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                String answer = editable.toString().trim();

                if (!answer.isEmpty())
                    answers.put(vh.pos, editable.toString());
                else
                    answers.remove(vh.pos);

            }
        });

        return vh;
    }


    @Override
    public void onBindViewHolder(final CreatePollAnswersAdapter.ViewHolder holder, final int position) {
        holder.number.setText(String.valueOf(numAnswers.get(position)));
        holder.pos = position;
    }


    @Override
    public int getItemCount() {
        return numAnswers.size();
    }


    public TreeMap<Integer, String> getAnswers(){
        return answers;
    }


}