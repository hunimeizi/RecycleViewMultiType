/*
 * Copyright 2016 drakeet. https://github.com/drakeet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.recycleview.multitype.bannerviewpager;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.recycleview.multitype.R;
import com.recycleview.multitype.view.NetImageView;

import java.util.List;


/**
 * @author drakeet
 */
public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private List<String> posts;

    public void setPosts(List<String> posts) {
        this.posts = posts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_horizontal_post, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.cover.setImageUrl(holder.itemView.getContext(),posts.get(position),holder.cover);
    }


    @Override public int getItemCount() {
        return posts != null ? posts.size() : 0;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        NetImageView cover;
        ViewHolder(View itemView) {
            super(itemView);
            cover = (NetImageView) itemView.findViewById(R.id.cover);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    Toast.makeText(v.getContext(), String.valueOf(getAdapterPosition()),
                        Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
