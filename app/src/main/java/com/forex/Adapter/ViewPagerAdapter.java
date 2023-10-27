package com.forex.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.forex.Fragment.ClosedFragment;
import com.forex.Fragment.OpenFragment;
import com.forex.Fragment.PendingFragment;

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
