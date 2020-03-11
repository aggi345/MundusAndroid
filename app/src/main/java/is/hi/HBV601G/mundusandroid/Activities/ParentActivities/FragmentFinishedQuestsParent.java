package is.hi.HBV601G.mundusandroid.Activities.ParentActivities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import is.hi.HBV601G.mundusandroid.R;

public class FragmentFinishedQuestsParent extends Fragment {
    View v;
    public FragmentFinishedQuestsParent() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.finishedquests_parent_fragment, container, false);
        return v;
    }

}
