package ly.smarthive.vaccine.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ly.smarthive.vaccine.R;
import ly.smarthive.vaccine.models.Shot;

public class ShotsDataAdapter extends RecyclerView.Adapter<ShotsDataAdapter.MyViewHolder> {

    private final List<Shot> ShotsList;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView number, type, takenAt,brand;

        public MyViewHolder(View view) {
            super(view);
            number = view.findViewById(R.id.number_text);
            type = view.findViewById(R.id.type_text);
            takenAt = view.findViewById(R.id.taken_at_txt);
            brand = view.findViewById(R.id.brand_text);
        }
    }


    public ShotsDataAdapter(List<Shot> ShotsList) {
        this.ShotsList = ShotsList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_shot, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Shot Shot = ShotsList.get(position);
        holder.number.setText(Shot.getNumber());
        holder.type.setText(Shot.getType());
        holder.takenAt.setText(Shot.getTakenAt());
        holder.brand.setText(Shot.getBrand());
    }

    @Override
    public int getItemCount() {
        return ShotsList.size();
    }
}