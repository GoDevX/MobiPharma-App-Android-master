package com.example.ahmedkhames.pharmacyclientapp.bottom_navigation;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.example.ahmedkhames.pharmacyclientapp.view.fragment.PlaceOrderFragment;

import java.util.ArrayList;

/**
 *
 */
public class DemoViewPagerAdapter extends FragmentPagerAdapter {

	private ArrayList<PlaceOrderFragment> fragments = new ArrayList<>();
	private PlaceOrderFragment currentFragment;

	public DemoViewPagerAdapter(FragmentManager fm) {
		super(fm);

		fragments.clear();
		fragments.add(PlaceOrderFragment.newInstance(0));
		fragments.add(PlaceOrderFragment.newInstance(1));
		fragments.add(PlaceOrderFragment.newInstance(2));
		fragments.add(PlaceOrderFragment.newInstance(3));
		fragments.add(PlaceOrderFragment.newInstance(4));
	}

	@Override
	public PlaceOrderFragment getItem(int position) {
		return fragments.get(position);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

	@Override
	public void setPrimaryItem(ViewGroup container, int position, Object object) {
		if (getCurrentFragment() != object) {
			currentFragment = ((PlaceOrderFragment) object);
		}
		super.setPrimaryItem(container, position, object);
	}

	/**
	 * Get the current fragment
	 */
	public PlaceOrderFragment getCurrentFragment() {
		return currentFragment;
	}
}