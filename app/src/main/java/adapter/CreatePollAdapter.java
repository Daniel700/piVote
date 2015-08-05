package adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import piv.pivote.Poll;
import piv.pivote.PollDetailedActivity;
import piv.pivote.R;

/**
 * Created by Daniel on 03.08.2015.
 */
public class CreatePollAdapter extends RecyclerView.Adapter<CreatePollAdapter.ViewHolder> {
    private List<Poll> pollList;

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

    public CreatePollAdapter(List<Poll> pollList) {
        this.pollList = pollList;
    }

    @Override
    public CreatePollAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_create_poll, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(CreatePollAdapter.ViewHolder holder, int position) {
// - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Poll plist = pollList.get(position);
        holder.vQuestion.setText(plist.getQuestion());
        holder.vOverallVotes.setText(String.valueOf(plist.getOverallVotes()));
        holder.vCategory.setText(plist.getCategory());
        holder.vlastVote.setText("05.08.2015");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), v.toString(), Toast.LENGTH_SHORT).show();

                Context context = v.getContext();
                Intent intent = new Intent(context, PollDetailedActivity.class);
                intent.putExtra("Poll", plist);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pollList.size();
    }
}
