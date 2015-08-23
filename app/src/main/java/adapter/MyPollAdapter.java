package adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.api.client.util.DateTime;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;


import database.SQLiteAccess;
import model.ModelTransformer;
import model.Poll;
import model.pollBeanApi.model.PollBean;
import piv.pivote.PollDetailedActivity;
import piv.pivote.R;

/**
 * Created by Daniel on 03.08.2015.
 */
public class MyPollAdapter extends RecyclerView.Adapter<MyPollAdapter.ViewHolder> {
    private List<Poll> pollList;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        // each data item is just a string in this case
        protected TextView vQuestion;
        protected TextView vOverallVotes;
        protected TextView vCategory;
        protected TextView vlastVote;
        public ViewHolder(View v) {
            super(v);
            vQuestion =  (TextView) v.findViewById(R.id.question_cp);
            vOverallVotes = (TextView)  v.findViewById(R.id.overallVotes_cp);
            vCategory = (TextView)  v.findViewById(R.id.category_cp);
            vlastVote = (TextView) v.findViewById(R.id.lastVote_cp);
        }
    }

    public MyPollAdapter(List<Poll> pollList, Context context) {
        this.pollList = pollList;
        this.context = context;
    }

    @Override
    public MyPollAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_my_poll, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyPollAdapter.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Poll poll = pollList.get(position);


        //Color question red if already voted
        SQLiteAccess dbAccess = new SQLiteAccess(context);
        //dbAccess.printAllPolls();
        Pair<Boolean, String> pair = dbAccess.findPoll(poll);
        boolean found = pair.first;
        final String selectedAnswer = pair.second;
        dbAccess.close();
        if (found)
            holder.vQuestion.setBackgroundResource(R.drawable.question_background_negative_gradient);
        else
            holder.vQuestion.setBackgroundResource(R.drawable.question_background_gradient);



        holder.vQuestion.setText(poll.getQuestion());
        holder.vOverallVotes.setText(String.valueOf(poll.getOverallVotes()));
        holder.vCategory.setText(poll.getCategory());
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
        holder.vlastVote.setText(df.format(poll.getLastVoted()));


        final boolean tmpFound = found;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, PollDetailedActivity.class);
                intent.putExtra("Poll", poll);
                intent.putExtra("Voted", tmpFound);
                intent.putExtra("selectedAnswer", selectedAnswer);
                ((Activity) context).startActivityForResult(intent, 200);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pollList.size();
    }
}
