package is.hi.HBV601G.mundusandroid.Activities.ParentActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import is.hi.HBV601G.mundusandroid.R;

public class MarketplaceParentActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marketplace_parent);

        tabLayout = (TabLayout) findViewById(R.id.marketPlaceParentTablayout_id);
        viewPager = (ViewPager) findViewById(R.id.marketPlaceParent_viewpager_id);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        // Add Fragment Here
        adapter.AddFragment(new FragmentAvailableRewardsParent(),"Available");
        adapter.AddFragment(new FragmentPurchasedRewardsParent(),"Purchased");


        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        // Remove Shadow From the action bar

        //ActionBar actionBar = getSupportActionBar();
        //actionBar.setElevation(0);tentView(R.layout.activity_marketplace_parent);
    }
}
