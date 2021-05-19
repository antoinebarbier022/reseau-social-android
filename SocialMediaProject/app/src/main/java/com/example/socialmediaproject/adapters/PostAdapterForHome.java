package com.example.socialmediaproject.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;
import android.widget.ImageView;

import android.widget.TextView;


import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.socialmediaproject.R;

import com.example.socialmediaproject.api.UserHelper;
import com.example.socialmediaproject.base.BaseActivity;

import com.example.socialmediaproject.models.Post;

import com.example.socialmediaproject.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


/**
 * Created by Antoine Barbier and Antoine Brahimi on 5/19/21.
 */

public class PostAdapterForHome extends RecyclerView.Adapter<PostAdapterForHome.ViewHolder> {

    private ArrayList<Post> postList;

    public PostAdapterForHome(ArrayList<Post> postList){
        this.postList = postList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater
                .inflate(R.layout.adapter_post_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.updateWithPost(postList.get(position));
        boolean currentUserIsAuthor = postList.get(position).getUserSender().equals(BaseActivity.getUid());

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView itemTitleView, itemSubtitleView, itemContentView, itemDateAgo;
        ImageView imgContent, imgProfile;
        ImageButton shareButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            // get item title view
            itemTitleView = itemView.findViewById(R.id.item_title);
            itemSubtitleView = itemView.findViewById(R.id.item_subtitle);
            itemContentView = itemView.findViewById(R.id.item_content);

            //images
            imgContent = itemView.findViewById(R.id.item_picture);
            imgProfile = itemView.findViewById(R.id.item_icon);

            itemDateAgo = itemView.findViewById(R.id.item_date_ago);

            shareButton = itemView.findViewById(R.id.item_share);
        }

        public void updateWithPost(Post currentItem){

            itemContentView.setText(currentItem.getContent());
            itemDateAgo.setText(BaseActivity.getTimeAgo(currentItem.getDateCreated()));

            // title
            if(currentItem.getGroup() == null){
                itemTitleView.setText("");
            }else{
                itemTitleView.setVisibility(View.VISIBLE);
                itemTitleView.setText(currentItem.getGroup());
            }

            // subtitle
            if(currentItem.getUserSender() == null){
                itemSubtitleView.setText("");
            }else{
                // tant qu'on a pas charger les données on affiche rien
                itemSubtitleView.setVisibility(View.GONE);

                // sinon on affiche le nom de l'utilisateur
                UserHelper.getUser(currentItem.getUserSender()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        itemSubtitleView.setVisibility(View.VISIBLE);
                        itemSubtitleView.setText(documentSnapshot.toObject(User.class).getUsername());
                    }
                });

            }


            UserHelper.getUser(currentItem.getUserSender()).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {

                    User sender = task.getResult().toObject(User.class);

                    Glide.with(itemView.getContext())
                            .load(BaseActivity.getRefImg(sender.getUrlPicture()))
                            .into(imgProfile);
                }
            });

            // print picture into message content
            if(!currentItem.getUrlImage().equals("null")){
                imgContent.setVisibility(View.VISIBLE);

                Glide.with(itemView.getContext())
                        .load(BaseActivity.getRefImg(currentItem.getUrlImage()))
                        .into(imgContent);
            }
            else{
                imgContent.setVisibility(View.GONE);
            }

        }

    }
}
