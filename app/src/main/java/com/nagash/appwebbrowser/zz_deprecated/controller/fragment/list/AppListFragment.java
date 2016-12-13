//package com.nagash.appwebbrowser.controller.fragments.list;
//
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.design.widget.TabLayout;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentStatePagerAdapter;
//import android.support.v4.view.ViewPager;
//import android.support.v7.widget.Toolbar;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.nagash.appwebbrowser.R;
//import com.nagash.appwebbrowser.controller.MainFragment;
//import com.nagash.appwebbrowser.controller.fragments.list.listTypes.FavouriteListFragment;
//import com.nagash.appwebbrowser.controller.fragments.list.listTypes.NearbyListFragment;
//import com.nagash.appwebbrowser.model.webapp.WebApp;
//
//
///**
// * Created by nagash on 14/09/16.
// */
//public class AppListFragment extends MainFragment {
//
//// https://guides.codepath.com/android/google-play-style-tabs-using-tablayout
//// http://www.truiton.com/2015/06/android-tabs-example-fragments-viewpager/
//    NearbyListFragment nearbyListFragment = new NearbyListFragment(this);
//    FavouriteListFragment favouriteListFragment = new FavouriteListFragment(this);
//
//
//    private boolean isShowingDetailsFragment = false;
//
//
//    public AppListFragment() {
//        super(MainFragment.builder()
//                .setColor(R.color.colorListPrimary)
//                .setColorDark( R.color.colorListPrimaryDark)
//                .setTitle("AppWeb Browser (AppNet)")
//                .build() );
//    }
//
//
//    // * * * * * GETTERS * * * * *
//    public NearbyListFragment    getNearbyListFragment(){
//        return nearbyListFragment;
//    }
//    public FavouriteListFragment getFavouriteListFragment() {
//        return favouriteListFragment;
//    }
//
//
//    // * * * * * METHODS * * * * *
//    public void showDetailsFragment( WebApp webApp ) {
//        getMainActivity().showAppDetails(webApp);
//        isShowingDetailsFragment = true;
//    }
//
//
//
//
//    // * * * * * DOWNLOAD SERVER * * * * *
//    public void onAppsDownloaded() {
//        nearbyListFragment.setStatusConneted();
//        favouriteListFragment.updateAppList();
//    }
//    public void onServerNotAvailable() {
//        nearbyListFragment.setStatusCantConnect();
//    }
//
//
//
//    // * * * * * FRAGMENT LIFECYCLE * * * * *
//    @Override public void onActivityCreated (@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
//        getMainActivity().setSupportActionBar(toolbar);
//
//
//        TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.tab_layout);
//        tabLayout.addTab(tabLayout.newTab().setText("Nearby"));
//        tabLayout.addTab(tabLayout.newTab().setText("Favourites"));
//        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
//
//        tabLayout.setTabTextColors(getResources().getColor(R.color.tab_text), getResources().getColor(R.color.tab_text_selected));
//        // without setting tab text from java, text is always black :(
//
//        final ViewPager viewPager = (ViewPager) getActivity().findViewById(R.id.pager);
//        final PagerAdapter adapter = new PagerAdapter
//                (getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
//        viewPager.setAdapter(adapter);
//        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                if(position == 1)
//                    favouriteListFragment.updateAppList();
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
//        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                viewPager.setCurrentItem(tab.getPosition());
//
//
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
//    }
//    @Override public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_list, container, false);
//    }
//
//
//
//
//
//    public class PagerAdapter extends FragmentStatePagerAdapter {
//        int mNumOfTabs;
//
//        public PagerAdapter(FragmentManager fm, int NumOfTabs) {
//            super(fm);
//            this.mNumOfTabs = NumOfTabs;
//        }
//
//        @Override public Fragment getItem(int position) {
//
//            switch (position) {
//                case 0:
//                    return nearbyListFragment;
//                case 1:
//                    return favouriteListFragment;
//                default:
//                    return null;
//            }
//        }
//
//
//        @Override public int getCount() {
//            return mNumOfTabs;
//        }
//    }
//}
