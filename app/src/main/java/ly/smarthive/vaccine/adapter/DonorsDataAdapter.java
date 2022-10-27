package ly.smarthive.vaccine.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ly.smarthive.vaccine.R;
import ly.smarthive.vaccine.models.Donor;


public class DonorsDataAdapter extends RecyclerView.Adapter<DonorsDataAdapter.MyViewHolder> implements Filterable {
    private Context context;
    private List<Donor> donorsList;
    private List<Donor> filteredList;
    public SelectedItem selectedItem;


    public DonorsDataAdapter(List<Donor> DonorsList, SelectedItem mSelectedItem, Context context) {
        this.context = context;
        this.donorsList = DonorsList;
        this.filteredList = DonorsList;
        this.selectedItem = mSelectedItem;
    }


    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();

                if (charSequence == null || charSequence.length() == 0) {
                    filterResults.count = filteredList.size();
                    filterResults.values = filteredList;
                } else {
                    String searchChr = charSequence.toString().toLowerCase();
                    List<Donor> result = new ArrayList<>();
                    for (Donor donor : filteredList) {
                        if (donor.getAddress().toLowerCase().contains(searchChr) || donor.getBlood().toLowerCase().contains(searchChr)) {
                            result.add(donor);
                        }
                    }
                    filterResults.count = result.size();
                    filterResults.values = result;
                }
                return filterResults;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                donorsList = (List<Donor>) filterResults.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, age;
        ImageButton request;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name_text);
            age = view.findViewById(R.id.age_txt);
            request = view.findViewById(R.id.request_btn);
            request.setOnClickListener(view1 -> selectedItem.selectedItem(donorsList.get(getAdapterPosition())));
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_donor, parent, false);
        return new MyViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Donor donor = donorsList.get(position);
        holder.name.setText(donor.getBlood());
        holder.age.setText(donor.getAddress());
        if (donor.isRequested()) {
            holder.request.setEnabled(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                holder.request.getBackground().setColorFilter(new BlendModeColorFilter(Color.GREEN, BlendMode.MULTIPLY));
            }
        }
    }


    @Override
    public int getItemCount() {
        return donorsList.size();
    }

    public interface SelectedItem {
        void selectedItem(Donor donor);
    }
}