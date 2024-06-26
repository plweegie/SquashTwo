package com.plweegie.android.squashtwo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.plweegie.android.squashtwo.R;
import com.plweegie.android.squashtwo.data.RepoEntry;

import java.util.ArrayList;
import java.util.List;

public class BaseGithubAdapter extends
        RecyclerView.Adapter<BaseGithubAdapter.BaseViewHolder> {

    public interface GithubAdapterOnClickListener {
        void onItemClick(RepoEntry repo);
    }

    protected List<RepoEntry> mRepos;
    protected Context mContext;
    private GithubAdapterOnClickListener mListener;

    public BaseGithubAdapter(Context context) {
        mContext = context;
        mRepos = new ArrayList<>();
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup vg, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);

        return new BaseViewHolder(inflater, vg, R.layout.repo_view_holder);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder vh, int i) {
        RepoEntry repo = mRepos.get(i);
        vh.bind(repo);
    }

    @Override
    public int getItemCount() {
        return mRepos == null ? 0 : mRepos.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public RepoEntry getItem(int position) {
        return mRepos.get(position);
    }

    public void setContent(List<RepoEntry> repos) {
        mRepos = repos;
        notifyDataSetChanged();
    }

    public void setListener(GithubAdapterOnClickListener listener) {
        mListener = listener;
    }

    public class BaseViewHolder extends RecyclerView.ViewHolder {

        protected TextView mNameTextView;
        protected TextView mLangTextView;
        protected TextView mStarCountTextView;

        public BaseViewHolder(LayoutInflater inflater, ViewGroup parent,
                          int layoutResId) {
            super(inflater.inflate(layoutResId, parent, false));

            mNameTextView = itemView.findViewById(R.id.repo_name_tv);
            mLangTextView = itemView.findViewById(R.id.repo_language_tv);
            mStarCountTextView = itemView.findViewById(R.id.stars_tv);
        }

        public void bind(RepoEntry repo) {

            mNameTextView.setText(repo.getName());
            mLangTextView.setText(repo.getLanguage());
            mStarCountTextView.setText(Integer.toString(repo.getStargazersCount()));

            mNameTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemClick(repo);
                }
            });
        }
    }
}
