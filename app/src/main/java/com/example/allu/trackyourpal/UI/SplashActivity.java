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

        configSplash.setBackgroundColor(R.color.colorPrimary); //any color you want form colors.xml
        configSplash.setAnimCircularRevealDuration(2000); //int ms
        configSplash.setRevealFlagX(Flags.REVEAL_RIGHT);  //or Flags.REVEAL_LEFT
        configSplash.setRevealFlagY(Flags.REVEAL_BOTTOM); //or Flags.REVEAL_TOP

        configSplash.setTitleSplash("Track Your Pal");
        configSplash.setTitleTextColor(R.color.colorAccent);
        configSplash.setTitleTextSize(40f); //float value
        configSplash.setAnimTitleDuration(2000);
        configSplash.setAnimTitleTechnique(Techniques.FadeInLeft);
        configSplash.setTitleFont("fonts/Gorditas-Regular.ttf"); //provide string to your font located in assets/fonts/
    }

    @Override
    public void animationsFinished() {
        utils.Goto(LoginActivity.class);
    }
}
