package ly.smarthive.vaccine.adapter;

import android.content.Context;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ly.smarthive.vaccine.R;
import ly.smarthive.vaccine.models.Request;


public class RequestsDataAdapter extends RecyclerView.Adapter<RequestsDataAdapter.MyViewHolder>  {
    List<Request> requestsList;
    public SelectedItem selectedItem;
    Context context;

    public RequestsDataAdapter(List<Request> RequestsList,SelectedItem mSelectedItem,Context context) {
        this.requestsList = RequestsList;
        this.context = context;
        this.selectedItem = mSelectedItem;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView from, date,phone;
        ImageButton accept,refuse;

        public MyViewHolder(View view) {
            super(view);
            from = view.findViewById(R.id.from_text);
            date = view.findViewById(R.id.date_txt);
            phone = view.findViewById(R.id.phone_txt);
            accept = view.findViewById(R.id.accept_btn);
            refuse = view.findViewById(R.id.refuse_btn);
            accept.setOnClickListener(view1 -> selectedItem.selectedItem(requestsList.get(getAdapterPosition()),true));
            refuse.setOnClickListener(view1 -> selectedItem.selectedItem(requestsList.get(getAdapterPosition()),false));
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_request, parent, false);
        return new MyViewHolder(itemView);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Request Request = requestsList.get(position);
        holder.from.setText(Request.getFrom());
        holder.date.setText(Request.getDate());
        if (Request.getState() != 3) {
            holder.accept.setEnabled(false);
            holder.refuse.setEnabled(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (Request.getState() == 0)   holder.refuse.getBackground().setColorFilter(new BlendModeColorFilter(Color.RED, BlendMode.MULTIPLY));
                if (Request.getState() == 1) {  holder.accept.getBackground().setColorFilter(new BlendModeColorFilter(Color.GREEN, BlendMode.MULTIPLY));
                holder.phone.setVisibility(View.VISIBLE);
                holder.phone.setText(Request.getPhone());
                }

            }
        }
    }
    @Override
    public int getItemCount() {
        return requestsList.size();
    }


    public interface SelectedItem{
        void selectedItem(Request request,boolean accept);
    }
}