package com.dyzs.app.fragment;

import android.support.v4.app.Fragment;

/**
 * @author dyzs(akuma)
 * Created on 2018/1/29.
 */

public class FragmentFactory {
    // private static HashMap<Type, Fragment> fragments = new HashMap<>();

    public static Fragment createFragment(Type type) {
        Fragment fragment = null;

        /*if (fragments.containsKey(type)) {
            return fragments.get(type);
        }*/

        switch (type) {
            case SOLAR_ECLIPSE:
                fragment = new SolarEclipseFragment();
                break;
            case CHASING_LOADING:
                fragment = new ChasingLoadingFragment();
                break;
            case COMPASS_SERVANT:
                fragment = new CompassServantFragment();
                break;
            case MULTI_PLAYER:
                fragment = new YinJiMultiPlayerFragment();
                break;
            case DOTTED_LINE:
                fragment = new DottedLineFragment();
                break;
            case COLOR_PROGRESS:
                fragment = new ColorProgressFragment();
                break;
            case CORNER_IMAGE:
                fragment = new CornerImageFragment();
                break;
            case SYNC_VIEW:
                fragment = new SyncViewFragment();
                break;
        }
        //fragments.put(type, fragment);
        return fragment;
    }


    public enum Type {
        SOLAR_ECLIPSE,
        CHASING_LOADING,
        COMPASS_SERVANT,
        MULTI_PLAYER,
        DOTTED_LINE,
        COLOR_PROGRESS,
        CORNER_IMAGE,
        SYNC_VIEW
    }
}
