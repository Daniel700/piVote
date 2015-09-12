package adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.List;


import database.FilterOptions;
import database.SQLiteAccess;
import model.Poll;
import piv.pivote.PollDetailedActivity;
import piv.pivote.R;
import utils.Settings;

/**
 * Created by Daniel on 03.08.2015.
 */
public class MyPollAdapter extends RecyclerView.Adapter<MyPollAdapter.ViewHolder> {
    private List<Poll> pollList;
    private Context context;
    private String[] resCategories;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        // each data item is just a string in this case
        protected TextView vQuestion;
        protected TextView vOverallVotes;
        protected TextView vCategory;
        protected TextView vlastVote;
        protected ImageButton vButtonFav;
        public ViewHolder(View v) {
            super(v);
            vQuestion =  (TextView) v.findViewById(R.id.question_cp);
            vOverallVotes = (TextView)  v.findViewById(R.id.overallVotes_cp);
            vCategory = (TextView)  v.findViewById(R.id.category_cp);
            vlastVote = (TextView) v.findViewById(R.id.lastVote_cp);
            vButtonFav = (ImageButton) v.findViewById(R.id.button_fav_my_polls);
        }
    }

    public MyPollAdapter(List<Poll> pollList, Context context) {
        this.pollList = pollList;
        this.context = context;

        SQLiteAccess dbAccess = new SQLiteAccess(context);
        for (Poll poll: pollList) {
            poll.setIsFavorite(dbAccess.findFavoritePoll(poll));
            Pair<Boolean, String> pair = dbAccess.findPoll(poll);
            poll.setAlreadyVoted(pair.first);
        }
        dbAccess.close();
    }

    @Override
    public MyPollAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        resCategories = parent.getResources().getStringArray(R.array.categories);
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_my_poll, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final MyPollAdapter.ViewHolder holder, int position) {

        final Poll poll = pollList.get(position);


        //Color question red if already voted
        if (poll.isAlreadyVoted())
            holder.vQuestion.setBackgroundResource(R.drawable.question_background_negative_gradient);
        else
            holder.vQuestion.setBackgroundResource(R.drawable.question_background_gradient);
        //Mark as favorite if in fav List
        if (poll.isFavorite())
            holder.vButtonFav.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_star_white_24dp, null));
        else
            holder.vButtonFav.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_star_border_white_24dp, null));


        holder.vQuestion.setText(poll.getQuestion());
        holder.vOverallVotes.setText(String.valueOf(poll.getOverallVotes()));
        int pos = FilterOptions.categories.indexOf(poll.getCategory());
        holder.vCategory.setText(resCategories[pos]);
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
        holder.vlastVote.setText(df.format(poll.getLastVoted()));


        holder.vButtonFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteAccess db = new SQLiteAccess(context);
                boolean tmpFound = db.findFavoritePoll(poll);
                if (tmpFound) {
                    db.deleteFavoritePoll(poll);
                    holder.vButtonFav.setImageDrawable(ResourcesCompat.getDrawable(v.getResources(), R.drawable.ic_star_border_white_24dp, null));
                    Snackbar.make(v, context.getString(R.string.favoriteRemoved), Snackbar.LENGTH_SHORT).show();
                } else {
                    if (db.getAllFavoritePolls().size() <= Settings.FAVORITE_LIST_MAXIMUM) {
                        db.insertFavoritePoll(poll);
                        holder.vButtonFav.setImageDrawable(ResourcesCompat.getDrawable(v.getResources(), R.drawable.ic_star_white_24dp, null));
                        Snackbar.make(v, context.getString(R.string.favoriteAdded), Snackbar.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(v.getContext(), context.getString(R.string.favoriteListFull), Toast.LENGTH_SHORT).show();
                        Snackbar.make(v, context.getString(R.string.favoriteListFull), Snackbar.LENGTH_SHORT).show();
                    }
                }
                db.close();
            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
