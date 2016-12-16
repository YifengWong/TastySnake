package com.example.stevennl.tastysnake.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.stevennl.tastysnake.Config;
import com.example.stevennl.tastysnake.R;

/**
 * Activity having only one fragment.
 * Author: LCY
 */
public abstract class SingleFragmentActivity extends BaseActivity {

    /**
     * Return the fragment in the activity.
     */
    protected abstract Fragment createFragment();

    /**
     * Return the id of the FrameLayout holding the fragment.
     */
    public int getFrameLayoutId() {
        return R.id.fragmentContainer;
    }

    /**
     * Replace current fragment(if exist) with a new fragment.
     *
     * @param fragment The new fragment
     * @param anim If true, use fragment transition animation, false otherwise
     */
    public void replaceFragment(Fragment fragment, boolean anim) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        if (anim) {
            trans.setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit);
        }
        Fragment origin = manager.findFragmentById(getFrameLayoutId());
        if (origin == null) {
            trans.add(getFrameLayoutId(), fragment);
        } else {
            trans.replace(getFrameLayoutId(), fragment);
        }
        trans.commit();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        replaceFragment(createFragment(), false);
    }
}
