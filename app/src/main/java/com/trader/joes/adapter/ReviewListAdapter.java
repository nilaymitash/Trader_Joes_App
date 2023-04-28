package com.trader.joes.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.trader.joes.R;
import com.trader.joes.model.Review;

import java.util.List;

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ViewHolder> {

    private List<Review> reviews;

    public ReviewListAdapter(List<Review> reviews) {
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public ReviewListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_list_item,
                parent, false);

        return new ReviewListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Review review = this.reviews.get(position);
        holder.ratingBar.setRating(review.getRating());
        holder.reviewText.setText(review.getReview());
    }

    @Override
    public int getItemCount() {
        return this.reviews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private RatingBar ratingBar;
        private TextView reviewText;

        public ViewHolder(View itemView) {
            super(itemView);
            ratingBar = itemView.findViewById(R.id.review_rating);
            reviewText = itemView.findViewById(R.id.review_text);
        }
    }
}
