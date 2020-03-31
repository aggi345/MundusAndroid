package is.hi.HBV601G.mundusandroid.Activities.ParentActivities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.tabs.TabLayout;

import is.hi.HBV601G.mundusandroid.Activities.PersonLoginActivity;
import is.hi.HBV601G.mundusandroid.DatePickerFragment;
import is.hi.HBV601G.mundusandroid.R;

public class QuestLogParentActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private Button newQuestButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_log_parent);

        tabLayout = (TabLayout) findViewById(R.id.questLogParentTablayout_id);
        viewPager = (ViewPager) findViewById(R.id.questLogParent_viewpager_id);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        // Add Fragment Here
        adapter.AddFragment(new FragmentAvailableQuestsParent(this),"Available");
        adapter.AddFragment(new FragmentInProgressQuestsParent(this),"In Progress");
        adapter.AddFragment(new FragmentFinishedQuestsParent(this),"Finished");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        newQuestButton = (Button) findViewById(R.id.newQuestButton);

        newQuestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuestLogParentActivity.this, CreateQuestActivity.class);
                startActivity(intent);
            }
        });

    }
}
