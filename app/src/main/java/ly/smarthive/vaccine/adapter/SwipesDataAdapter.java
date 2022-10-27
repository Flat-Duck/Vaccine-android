package ly.smarthive.vaccine.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ly.smarthive.vaccine.R;
import ly.smarthive.vaccine.models.Swipe;

public class SwipesDataAdapter extends RecyclerView.Adapter<SwipesDataAdapter.MyViewHolder> {

    private final List<Swipe> SwipesList;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView number, result, takenAt;

        public MyViewHolder(View view) {
            super(view);
            number = view.findViewById(R.id.number_text);
            result = view.findViewById(R.id.result_text);
            takenAt = view.findViewById(R.id.taken_at_txt);
        }
    }


    public SwipesDataAdapter(List<Swipe> SwipesList) {
        this.SwipesList = SwipesList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_swipe, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Swipe Swipe = SwipesList.get(position);
        holder.number.setText(Swipe.getNumber());
        holder.result.setText(Swipe.getResult());
        holder.takenAt.setText(Swipe.getTakenAt());
    }

    @Override
    public int getItemCount() {
        return SwipesList.size();
    }
}