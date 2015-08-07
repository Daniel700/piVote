package adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import piv.pivote.Poll;
import piv.pivote.R;

/**
 * Created by Daniel on 06.08.2015.
 */
public class AnswersAdapter extends RecyclerView.Adapter<AnswersAdapter.ViewHolder> {

    private Poll myPoll;
    public int lastPosition = -1;

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
        int answerVote = myPoll.getAnswerVotes().get(position);
        int overallVotes = myPoll.getOverallVotes();
        int percentage = (answerVote * 100)/overallVotes;

        holder.answerText.setText(myPoll.getAnswers().get(position));
        holder.answerVotes.setText(String.valueOf(answerVote));
        holder.answerPercentage.setText(String.valueOf(percentage));
        holder.answerChecked.setChecked(false);

        holder.answerChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastPosition >= 0 && lastPosition != position)
                notifyItemChanged(lastPosition);

                lastPosition = position;
            }
        });



    }

    @Override
    public int getItemCount() {
        return myPoll.getAnswers().size();
    }
}
