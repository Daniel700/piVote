package adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import piv.pivote.Poll;
import piv.pivote.PollDetailedActivity;
import piv.pivote.R;

/**
 * Created by Daniel on 08.08.2015.
 */
public class CreateAnswersAdapter extends RecyclerView.Adapter<CreateAnswersAdapter.ViewHolder> {
    private ArrayList<Integer> numAnswers;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        protected TextView number;
        protected EditText answerText;

        public ViewHolder(View v) {
            super(v);
            number = (TextView) v.findViewById(R.id.answer_number_cp);
            answerText = (EditText) v.findViewById(R.id.editText_answer_cp);
        }
    }

    public CreateAnswersAdapter(ArrayList<Integer> numberOfAnswers) {
        this.numAnswers = numberOfAnswers;
    }

    @Override
    public CreateAnswersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_answers_cp, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(CreateAnswersAdapter.ViewHolder holder, int position) {
// - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.number.setText(String.valueOf(numAnswers.get(position)));
    }

    @Override
    public int getItemCount() {
        return numAnswers.size();
    }

}