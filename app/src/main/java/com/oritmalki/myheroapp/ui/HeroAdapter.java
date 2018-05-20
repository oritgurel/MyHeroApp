package com.oritmalki.myheroapp.ui;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.util.DiffUtil.Callback;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.oritmalki.myheroapp.R;
import com.oritmalki.myheroapp.data.Hero;
import com.oritmalki.myheroapp.viewmodel.HeroViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

public class HeroAdapter extends RecyclerView.Adapter<HeroAdapter.HeroHolder> {

    static List<? extends Hero> heroes;
    Context context;
    Glide glide;
    AdapterCallback mCallback;
    Hero lastSelected;
    HeroHolder lastSelectedHeart;

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private HeroViewModel viewModel;

    public HeroAdapter(Context context, AdapterCallback callback) {
//        this.heroes = heroes;
        this.context = context;
        this.mCallback = callback;
    }

    public void setHeroesList(final List<? extends Hero> heroList, Context context) {
        if (this.heroes == null) {
            this.heroes = heroList;
            this.context = context;

            notifyItemRangeInserted(0, heroList.size());
//           notifyItemRangeRemoved(0, measuresList.size());
        }
        else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new Callback() {
                @Override
                public int getOldListSize() {
                    return HeroAdapter.this.heroes.size();
                }

                @Override
                public int getNewListSize() {
                    return heroes.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return HeroAdapter.this.heroes.get(oldItemPosition) == heroes.get(newItemPosition);
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    Hero hero = heroes.get(newItemPosition);
                    Hero old = heroes.get(oldItemPosition);
                    return hero.isFavorite() == old.isFavorite() && Objects.equals(hero.getTitle(), old.getTitle());
                }
            });
            this.heroes = heroList;
            result.dispatchUpdatesTo(this);
        }
    }



    @NonNull
    @Override
    public HeroHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_hero, parent, false);
        return new HeroHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HeroHolder holder, int position) {

        glide.with(context).load(heroes.get(position).getImage()).into(holder.heroImage);
        holder.heroTitleTv.setText(heroes.get(position).getTitle());
        StringBuilder builder = new StringBuilder();
        List<String> abilities = new ArrayList<>(heroes.get(position).getAbilities());
        for (String value : abilities) {
            builder.append(value);
            if (abilities.indexOf(value) != abilities.size()-1)
            builder.append(", ");
        }
        holder.heroAbilitiesTv.setText(builder.toString());
        if (heroes.get(position).isFavorite()) {
            holder.heart_ic.setVisibility(View.VISIBLE);
        } else {
            holder.heart_ic.setVisibility(View.GONE);
        }

        holder.cardView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //pass selected hero to activity in order to update actionBar
                mCallback.OnHeroSelected(heroes.get(position), holder);
                //show heart


            }
        });



    }

    @Override
    public int getItemCount() {
        return heroes.size();
    }

    private void setFavorite(Hero hero, HeroHolder holder) {
        if (lastSelected !=null) {
            lastSelected.setFavorite(false);
            setHeartVisibility(lastSelected, lastSelectedHeart);

        }

        hero.setFavorite(true);
        setHeartVisibility(hero, holder);
        lastSelected = hero;
        lastSelectedHeart = holder;


    }

    private void setHeartVisibility(Hero hero, HeroHolder holder) {
        if (!hero.isFavorite()) {
            holder.heart_ic.setVisibility(View.GONE);
        } else {
            holder.heart_ic.setVisibility(View.VISIBLE);
        }
    }



        static class HeroHolder extends RecyclerView.ViewHolder {

        ImageView heroImage;
        TextView heroTitleTv;
        TextView heroAbilitiesTv;
        ImageView heart_ic;
        CardView cardView;
        ImageView mainTitleImage;

        public HeroHolder(View itemView) {
            super(itemView);

            heroImage = itemView.findViewById(R.id.item_image);
            heroTitleTv = itemView.findViewById(R.id.hero_name_item);
            heroAbilitiesTv = itemView.findViewById(R.id.hero_abilities_item);
            heart_ic = itemView.findViewById(R.id.ic_heart);
            cardView = itemView.findViewById(R.id.card_view);
            mainTitleImage = itemView.findViewById(R.id.title_image);

        }
    }
}
