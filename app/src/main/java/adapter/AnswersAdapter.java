package adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import piv.pivote.Answer;
import piv.pivote.Poll;
import piv.pivote.R;

/**
 * Created by Daniel on 06.08.2015.
 */
public class AnswersAdapter extends RecyclerView.Adapter<AnswersAdapter.ViewHolder> {

    private Poll myPoll;
    private int lastPosition = -1;


    public static class ViewHolder extends RecyclerView.ViewHolder{
        // each data item is just a string in this case
        protected TextView answerText;
        protected TextView answerVotes;
        protected TextView answerPercentage;
        protected CheckBox answerChecked;
        public ViewHolder(View v) {
            super(v);
            answerText =  (TextView) v.findViewById(R.id.answer_text_detailed);
            answerVotes = (TextView)  v.findViewById(R.id.answer_votes_detailed);
            answerPercentage = (TextView)  v.findViewById(R.id.answer_votes_percentage_detailed);
            answerChecked = (CheckBox) v.findViewById(R.id.answer_checked);
        }
    }


    public AnswersAdapter(Poll myPoll) {
        this.myPoll = myPoll;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_question_detailed, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        double answerVote = myPoll.getAnswers().get(position).getAnswerVotes();
        double overallVotes = myPoll.getOverallVotes();
        double percentage = 0;
        try {
            percentage = (answerVote * 100)/overallVotes;

            //round percentage with 2 digits after comma
            percentage = percentage * 100;
            percentage = Math.round(percentage);
            percentage = percentage / 100;
            myPoll.getAnswers().get(position).setPercentage(percentage);
        }
        catch (Exception e) {
            //Handle Exception (should never get here)
            e.printStackTrace();
        }


        holder.answerText.setText(myPoll.getAnswers().get(position).getAnswerText());
        holder.answerVotes.setText(String.valueOf((int) answerVote));
        holder.answerPercentage.setText(String.valueOf(percentage));
        holder.answerChecked.setChecked(false);
        myPoll.getAnswers().get(position).setSelected(false);

        holder.answerChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastPosition != position) {
                    notifyItemChanged(lastPosition);
                    myPoll.getAnswers().get(position).setSelected(true);
                } else {
                    if (holder.answerChecked.isChecked()) {
                        myPoll.getAnswers().get(position).setSelected(true);
                    } else {
                        myPoll.getAnswers().get(position).setSelected(false);
                    }
                }

                lastPosition = position;
            }
        });


    }

    @Override
    public int getItemCount() {
        return myPoll.getAnswers().size();
    }

    public Answer getChosenAnswer() {

        for (Answer answer : myPoll.getAnswers()){
            if (answer.isSelected())
                return answer;
        }

        return null;
    }

}
