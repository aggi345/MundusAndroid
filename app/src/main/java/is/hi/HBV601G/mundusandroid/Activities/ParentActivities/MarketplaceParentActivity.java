package is.hi.HBV601G.mundusandroid.Activities.ParentActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import is.hi.HBV601G.mundusandroid.InfoBar;
import is.hi.HBV601G.mundusandroid.R;

public class MarketplaceParentActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private FloatingActionButton mNewRewardButton;
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

        InfoBar infoBar = new InfoBar(this, "parent");




        mNewRewardButton = findViewById(R.id.newRewardButton);

        mNewRewardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MarketplaceParentActivity.this, CreateRewardActivity.class);
                startActivity(intent);
            }
        });
    }
}
