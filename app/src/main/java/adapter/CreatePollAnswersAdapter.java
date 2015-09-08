package adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import piv.pivote.R;

/**
 * Created by Daniel on 08.08.2015.
 */
public class CreatePollAnswersAdapter extends RecyclerView.Adapter<CreatePollAnswersAdapter.ViewHolder> {
    private ArrayList<Integer> numAnswers;
    private ArrayList<String> answers = new ArrayList<>();


    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView number;
        protected final EditText answerText;

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
    public CreatePollAnswersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_answers_cp, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final CreatePollAnswersAdapter.ViewHolder holder, final int position) {

        holder.number.setText(String.valueOf(numAnswers.get(position)));
        holder.answerText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String answer = holder.answerText.getText().toString().trim();

                if (answers.contains(answer))
                    answers.remove(answer);
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                String answer = holder.answerText.getText().toString().trim();

                if (!answer.isEmpty())
                    answers.add(answer);
            }
        });

    }

    @Override
    public int getItemCount() {
        return numAnswers.size();
    }


    public ArrayList<String> getAnswers(){
        return answers;
    }


}