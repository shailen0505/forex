package com.forex.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.forex.ClosedFragment;
import com.forex.OpenFragment;
import com.forex.PendingFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {


    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0: return new OpenFragment();
            case 1: return new PendingFragment();
            case 2: return new ClosedFragment();
            default: return new OpenFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
