package is.hi.HBV601G.mundusandroid.Activities.ChildActivities;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import is.hi.HBV601G.mundusandroid.Entities.Account;
import is.hi.HBV601G.mundusandroid.Entities.Parent;
import is.hi.HBV601G.mundusandroid.Entities.Quest;
import is.hi.HBV601G.mundusandroid.QuestRecyclerViewAdapter;
import is.hi.HBV601G.mundusandroid.R;

public class FragmentAvailableQuestsChild extends Fragment {
    View v;
    private RecyclerView myreyclerview;
    private List<Quest> lstQuest;

    public FragmentAvailableQuestsChild() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.availablequests_child_fragment, container, false);
        myreyclerview = (RecyclerView) v.findViewById(R.id.availableQuestsChildRecycleView);
        QuestRecyclerViewAdapter recyclerAdapter = new QuestRecyclerViewAdapter(getContext(), lstQuest, 0);
        myreyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        myreyclerview.setAdapter(recyclerAdapter);
        return v;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        lstQuest = new ArrayList<>();
        Account account = new Account("Tester", "test@test.is", "123", null);
        Parent parent = new Parent("Tester", "123", account);
        account.setParent(parent);
        lstQuest.add(new Quest("Vaccum", "Description", 1337, 69, "Deadline", parent));
        lstQuest.add(new Quest("Clean", "Description", 1337, 69, "Deadline", parent));
        lstQuest.add(new Quest("Mow the lawn", "Description", 1337, 69, "Deadline", parent));
        lstQuest.add(new Quest("Do the dishes", "Description", 1337, 69, "Deadline", parent));

    }
}

