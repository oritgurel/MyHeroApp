package com.oritmalki.myheroapp.ui;

import com.oritmalki.myheroapp.data.Hero;
import com.oritmalki.myheroapp.ui.HeroAdapter.HeroHolder;

public interface AdapterCallback {
    void OnHeroSelected(Hero hero, HeroHolder heroHolder);
}
