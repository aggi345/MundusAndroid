package is.hi.HBV601G.mundusandroid.Activities.ParentActivities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import is.hi.HBV601G.mundusandroid.R;

public class QuestLogParentActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_log_parent);

        tabLayout = (TabLayout) findViewById(R.id.questLogParentTablayout_id);
        viewPager = (ViewPager) findViewById(R.id.questLogParent_viewpager_id);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        // Add Fragment Here
        adapter.AddFragment(new FragmentAvailableQuestsParent(),"Available");
        adapter.AddFragment(new FragmentInProgressQuestsParent(),"In Progress");
        adapter.AddFragment(new FragmentFinishedQuestsParent(),"Finished");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        // Remove Shadow From the action bar

        //ActionBar actionBar = getSupportActionBar();
        //actionBar.setElevation(0);


    }
}
