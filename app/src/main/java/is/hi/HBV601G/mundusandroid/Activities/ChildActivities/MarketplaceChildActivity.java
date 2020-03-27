package is.hi.HBV601G.mundusandroid.Activities.ChildActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import is.hi.HBV601G.mundusandroid.Activities.ParentActivities.FragmentAvailableRewardsParent;
import is.hi.HBV601G.mundusandroid.Activities.ParentActivities.FragmentPurchasedRewardsParent;
import is.hi.HBV601G.mundusandroid.Activities.ParentActivities.ViewPagerAdapter;
import is.hi.HBV601G.mundusandroid.InfoBar;
import is.hi.HBV601G.mundusandroid.R;

public class MarketplaceChildActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marketplace_child);

        tabLayout = (TabLayout) findViewById(R.id.marketPlaceChildTablayout_id);
        viewPager = (ViewPager) findViewById(R.id.marketPlaceChild_viewpager_id);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        // Add Fragment Here
        adapter.AddFragment(new FragmentAvailableRewardsChild(),"Available");
        adapter.AddFragment(new FragmentPurchasedRewardsChild(),"My Rewards");


        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        InfoBar infoBar = new InfoBar(this, "child");

        // Remove Shadow From the action bar

        //ActionBar actionBar = getSupportActionBar();
        //actionBar.setElevation(0);tentView(R.layout.activity_marketplace_parent);
    }
}
