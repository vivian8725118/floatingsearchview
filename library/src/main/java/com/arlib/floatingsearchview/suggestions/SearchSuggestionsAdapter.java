/*
 * Copyright (C) 2015 Arlib
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.arlib.floatingsearchview.suggestions;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.arlib.floatingsearchview.R;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

import java.util.ArrayList;
import java.util.List;

public class SearchSuggestionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "";

    private List<SearchSuggestion> mSearchSuggestions;

    private Listener mListener;

    private Context mContext;

    private SearchSuggestion mSelectedItem;

    private Drawable mRightIconDrawable;

    public interface Listener{

        void onItemSelected(SearchSuggestion item);

        void onMoveItemToSearchClicked(SearchSuggestion item);
    }

    public static class SearchSuggestionViewHolder extends RecyclerView.ViewHolder{

        private static final String TAG = "";

        public TextView body;

        public ImageView leftIcon;

        public ImageView rightIcon;

        private Listener mListener;

        public interface Listener{

            void onItemClicked(int adapterPosition);

            void onMoveItemToSearchClicked(int adapterPosition);
        }

        public SearchSuggestionViewHolder(View v, Listener listener) {
            super (v);

            mListener = listener;
            body = (TextView) v.findViewById(R.id.body);
            leftIcon = (ImageView) v.findViewById(R.id.left_icon);
            rightIcon = (ImageView) v.findViewById(R.id.right_icon);

            rightIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(mListener!=null)
                        mListener.onMoveItemToSearchClicked(getAdapterPosition());

                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(mListener!=null)
                        mListener.onItemClicked(getAdapterPosition());
                }
            });
        }

    }

    public SearchSuggestionsAdapter(Context context, Listener listener) {

        this.mContext = context;
        this.mListener = listener;

        mSearchSuggestions = new ArrayList<>();

        mRightIconDrawable = mContext.getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
        mRightIconDrawable = DrawableCompat.wrap(mRightIconDrawable);
        DrawableCompat.setTint(mRightIconDrawable, mContext.getResources().getColor(R.color.gray_active_icon));


    }

    public List<? extends SearchSuggestion> getDataSet(){
        return mSearchSuggestions;
    }

    public void swapData(List<? extends SearchSuggestion> searchSuggestions){

        mSearchSuggestions.clear();
        mSearchSuggestions.addAll(searchSuggestions);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_suggestion_item, viewGroup, false);
        SearchSuggestionViewHolder viewHolder = new SearchSuggestionViewHolder(view, new SearchSuggestionViewHolder.Listener() {

            @Override
            public void onItemClicked(int adapterPosition) {

                if(mListener!=null)
                    mListener.onItemSelected(mSearchSuggestions.get(adapterPosition));
            }

            @Override
            public void onMoveItemToSearchClicked(int adapterPosition) {

                if(mListener!=null)
                    mListener.onMoveItemToSearchClicked(mSearchSuggestions.get(adapterPosition));
            }

        });

        viewHolder.rightIcon.setImageDrawable(mRightIconDrawable);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vh, int position) {

        SearchSuggestion item = mSearchSuggestions.get(position);

        SearchSuggestionViewHolder viewHolder = (SearchSuggestionViewHolder) vh;

        resetImageView(viewHolder.leftIcon);

        viewHolder.body.setText(item.getBody());

        item.setBodyText(viewHolder.body);
        if(item.setLeftIcon(viewHolder.leftIcon))
            viewHolder.leftIcon.setVisibility(View.VISIBLE);
        else viewHolder.leftIcon.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {

        return mSearchSuggestions!=null ? mSearchSuggestions.size() : 0;
    }

    public void clearDataSet(){

        int rage = mSearchSuggestions.size();
        mSearchSuggestions.clear();
        notifyItemRangeRemoved(0, rage);
    }

    //todo
    //reset all properties that the client might have
    //changed.
    private void resetImageView(ImageView imageView){

        imageView.setImageDrawable(null);
        imageView.setAlpha(1.0f);
    }
}
