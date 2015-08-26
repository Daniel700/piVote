package adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.List;

import database.SQLiteAccess;
import model.ModelTransformer;
import model.Poll;
import model.pollBeanApi.model.PollBean;
import piv.pivote.PollDetailedActivity;
import piv.pivote.R;

/**
 * Created by Daniel on 29.07.2015.
 */
public class QuestionListAdapter extends RecyclerView.Adapter<QuestionListAdapter.ViewHolder> {
    private List<Poll> pollList;
    private Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder{
        // each data item is just a string in this case
        protected TextView vQuestion;
        protected TextView vOverallVotes;
        protected TextView vCategory;
        protected TextView vCreatedBy;
        public ViewHolder(View v) {
            super(v);
            vQuestion =  (TextView) v.findViewById(R.id.question);
            vOverallVotes = (TextView)  v.findViewById(R.id.overallVotes);
            vCategory = (TextView)  v.findViewById(R.id.category);
            vCreatedBy = (TextView) v.findViewById(R.id.createdBy);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public QuestionListAdapter(List<Poll> pollList, Context context) {
        this.pollList = pollList;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public QuestionListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_question_list, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        final Poll poll = pollList.get(position);


        //Color question red if already voted
        SQLiteAccess dbAccess = new SQLiteAccess(context);
        Pair<Boolean, String> pair = dbAccess.findPoll(poll);
        boolean found = pair.first;
        dbAccess.close();
        if (found)
            holder.vQuestion.setBackgroundResource(R.drawable.question_background_negative_gradient);
        else
            holder.vQuestion.setBackgroundResource(R.drawable.question_background_gradient);



        holder.vQuestion.setText(poll.getQuestion());
        holder.vOverallVotes.setText(String.valueOf(poll.getOverallVotes()));
        holder.vCategory.setText(poll.getCategory());
        holder.vCreatedBy.setText(poll.getCreatedBy());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, PollDetailedActivity.class);
                intent.putExtra("Poll", poll);
                ((Activity) context).startActivityForResult(intent, 100);
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return pollList.size();
    }


}
