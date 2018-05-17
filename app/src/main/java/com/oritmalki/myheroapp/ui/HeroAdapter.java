package com.oritmalki.myheroapp.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.oritmalki.myheroapp.R;
import com.oritmalki.myheroapp.data.Hero;

import java.util.ArrayList;
import java.util.List;

public class HeroAdapter extends RecyclerView.Adapter<HeroAdapter.HeroHolder> {

    List<Hero> heroes;
    Context context;
    Glide glide;

    public HeroAdapter(List<Hero> heroes, Context context) {
        this.heroes = heroes;
        this.context = context;
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
        holder.heart_ic.setVisibility(View.GONE);



    }

    @Override
    public int getItemCount() {
        return heroes.size();
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
