package is.hi.HBV601G.mundusandroid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import is.hi.HBV601G.mundusandroid.Entities.Person;

public class SmallChildrenAdapter extends RecyclerView.Adapter<SmallChildrenAdapter.PersonViewHolder> {

    private ArrayList<Person> mPersonList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public static class PersonViewHolder extends  RecyclerView.ViewHolder{


        public TextView mPersonName;


        public PersonViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            mPersonName = itemView.findViewById(R.id.personName_textView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });

        }
    }


    public SmallChildrenAdapter(ArrayList<Person> personList){
        mPersonList = personList;
    }

    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.small_child_item, parent, false);
        PersonViewHolder pvh = new PersonViewHolder(v, mListener);
        return pvh;

    }

    @Override
    public void onBindViewHolder(@NonNull PersonViewHolder holder, int position) {
        Person currentItem = mPersonList.get(position);

        holder.mPersonName.setText(currentItem.getName());
    }

    @Override
    public int getItemCount() {
        return mPersonList.size();
    }
}
