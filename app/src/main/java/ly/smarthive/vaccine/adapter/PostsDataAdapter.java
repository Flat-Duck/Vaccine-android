package ly.smarthive.vaccine.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ly.smarthive.vaccine.R;
import ly.smarthive.vaccine.models.Post;

public class PostsDataAdapter extends RecyclerView.Adapter<PostsDataAdapter.MyViewHolder> {

    private final List<Post> PostsList;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, body, date;
        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            body = view.findViewById(R.id.body);
            date = view.findViewById(R.id.date);
        }
    }


    public PostsDataAdapter(List<Post> PostsList) {
        this.PostsList = PostsList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);

        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Post Post = PostsList.get(position);
        holder.title.setText(Post.getTitle());
        holder.body.setText(Post.getBody());
        holder.date.setText(Post.getDate());
    }

    @Override
    public int getItemCount() {
        return PostsList.size();
    }
}