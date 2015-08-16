package adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.api.client.util.DateTime;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;


import model.ModelTransformer;
import model.Poll;
import model.pollBeanApi.model.PollBean;
import piv.pivote.PollDetailedActivity;
import piv.pivote.R;

/**
 * Created by Daniel on 03.08.2015.
 */
public class MyPollAdapter extends RecyclerView.Adapter<MyPollAdapter.ViewHolder> {
    private List<PollBean> pollList;

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

    public MyPollAdapter(List<PollBean> pollList) {
        this.pollList = pollList;
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
        final PollBean pollBean = pollList.get(position);
        holder.vQuestion.setText(pollBean.getQuestion());
        holder.vOverallVotes.setText(String.valueOf(pollBean.getOverallVotes()));
        holder.vCategory.setText(pollBean.getCategory());


        DateTime dateTime = pollBean.getLastVoted();
        Date date = new Date(dateTime.getValue());
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
        holder.vlastVote.setText(df.format(date));


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModelTransformer modelTransformer = new ModelTransformer();
                Poll poll = modelTransformer.transformPollBeanToPoll(pollBean);
                Context context = v.getContext();
                Intent intent = new Intent(context, PollDetailedActivity.class);
                intent.putExtra("Poll", poll);
                ((Activity) context).startActivityForResult(intent, 200);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pollList.size();
    }
}
