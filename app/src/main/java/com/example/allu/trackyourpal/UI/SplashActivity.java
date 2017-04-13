package com.example.allu.trackyourpal.UI;

import com.daimajia.androidanimations.library.Techniques;
import com.example.allu.trackyourpal.R;
import com.example.allu.trackyourpal.Utils.Utils;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

/**
 * Created by allu on 3/5/17.
 */

public class SplashActivity extends AwesomeSplash {
    Utils utils;
    @Override
    public void initSplash(ConfigSplash configSplash) {
        utils = new Utils(this);

        getSupportActionBar().hide();

        configSplash.setBackgroundColor(R.color.colorPrimary);
        configSplash.setAnimCircularRevealDuration(2000);
        configSplash.setRevealFlagX(Flags.REVEAL_RIGHT);
        configSplash.setRevealFlagY(Flags.REVEAL_BOTTOM);

        configSplash.setTitleSplash("Track Your Pal");
        configSplash.setTitleTextColor(R.color.colorAccent);
        configSplash.setTitleTextSize(40f);
        configSplash.setAnimTitleDuration(2000);
        configSplash.setAnimTitleTechnique(Techniques.FadeInLeft);
        configSplash.setTitleFont("fonts/Gorditas-Regular.ttf");
    }

    @Override
    public void animationsFinished() {
        utils.Goto(LoginActivity.class);
    }
}
