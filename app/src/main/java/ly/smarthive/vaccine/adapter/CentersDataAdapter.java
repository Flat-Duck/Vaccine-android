package ly.smarthive.vaccine.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ly.smarthive.vaccine.R;
import ly.smarthive.vaccine.models.Center;
import ly.smarthive.vaccine.models.Request;

public class CentersDataAdapter extends RecyclerView.Adapter<CentersDataAdapter.MyViewHolder> {
    List<Center> CentersList;
    public SelectedItem selectedItem;
    Context context;

    public CentersDataAdapter(List<Center> CentersList, SelectedItem mSelectedItem, Context context)
    {
        this.CentersList = CentersList;
        this.context = context;
        this.selectedItem = mSelectedItem;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, address;
        AppCompatImageButton goToBtn;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name_text);
            address = view.findViewById(R.id.address_text);
            goToBtn = view.findViewById(R.id.go_to_btn);
            goToBtn.setOnClickListener(view1 -> selectedItem.selectedItem(CentersList.get(getAdapterPosition()),true));
        }
    }




    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_center, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Center Center = CentersList.get(position);
        holder.name.setText(Center.getName());
        holder.address.setText(Center.getAddress());

    }

    @Override
    public int getItemCount() {
        return CentersList.size();
    }
    public interface SelectedItem{
        void selectedItem(Center center, boolean accept);
    }
}