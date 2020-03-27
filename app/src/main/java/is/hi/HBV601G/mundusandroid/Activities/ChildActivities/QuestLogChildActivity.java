package is.hi.HBV601G.mundusandroid.Activities.ChildActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import is.hi.HBV601G.mundusandroid.Activities.ParentActivities.FragmentAvailableQuestsParent;
import is.hi.HBV601G.mundusandroid.Activities.ParentActivities.FragmentFinishedQuestsParent;
import is.hi.HBV601G.mundusandroid.Activities.ParentActivities.FragmentInProgressQuestsParent;
import is.hi.HBV601G.mundusandroid.Activities.ParentActivities.ViewPagerAdapter;
import is.hi.HBV601G.mundusandroid.InfoBar;
import is.hi.HBV601G.mundusandroid.R;

public class QuestLogChildActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_log_child);

        tabLayout = (TabLayout) findViewById(R.id.questLogChildTablayout_id);
        viewPager = (ViewPager) findViewById(R.id.questLogChild_viewpager_id);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        // Add Fragment Here
        adapter.AddFragment(new FragmentAvailableQuestsChild(), "Available");
        adapter.AddFragment(new FragmentAssignedQuestsChild(), "My Quests");
        adapter.AddFragment(new FragmentFinishedQuestsChild(), "Finished");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        InfoBar infoBar = new InfoBar(this, "child");

        // Remove Shadow From the action bar

        //ActionBar actionBar = getSupportActionBar();
        //actionBar.setElevation(0);


    }
}
