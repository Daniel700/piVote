package adapter;




import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import tabs.TabFragmentCreatePoll;
import tabs.TabFragmentQuestionList;
import tabs.TabFragmentRecentlyVoted;

/**
 * Created by Daniel on 28.07.2015.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                TabFragmentQuestionList tab1 = new TabFragmentQuestionList();
                return tab1;
            case 1:
                TabFragmentCreatePoll tab2 = new TabFragmentCreatePoll();
                return tab2;
            case 2:
                TabFragmentRecentlyVoted tab3 = new TabFragmentRecentlyVoted();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}