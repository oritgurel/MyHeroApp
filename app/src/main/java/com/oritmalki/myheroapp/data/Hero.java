package com.oritmalki.myheroapp.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

@Entity(tableName = "hero", indices = {@Index(value = {"title","abilities","image"}, unique = true)})
public class Hero {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    int id;

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("abilities")
    @Expose
    private List<String> abilities = null;
    @SerializedName("image")
    @Expose
    private String image;

    private boolean favorite;

    private Date lastRefresh;

    public Hero() {

    }

    public Hero(@NonNull String title, List<String> abilities, String image) {
        this.title = title;
        this.abilities = abilities;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getAbilities() {
        return abilities;
    }

    public void setAbilities(List<String> abilities) {
        this.abilities = abilities;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public Date getLastRefresh() {
        return lastRefresh;
    }

    public void setLastRefresh(Date lastRefresh) {
        this.lastRefresh = lastRefresh;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (String value : abilities) {
            builder.append(value);
            builder.append(", ");
        }

        return "Hero{" +
                "title='" + title + '\'' +
                ", abilities=" + builder.toString() +
                ", image='" + image + '\'' +
                ", favorite=" + favorite +
                ", lastRefresh=" + lastRefresh +
                '}';
    }

    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }
}