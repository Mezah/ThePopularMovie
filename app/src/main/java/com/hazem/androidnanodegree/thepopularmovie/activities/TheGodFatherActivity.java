//package com.hazem.androidnanodegree.thepopularmovie.activities;
//
//import android.os.Bundle;
//import android.support.annotation.LayoutRes;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v7.app.AppCompatActivity;
//
//import com.hazem.androidnanodegree.thepopularmovie.R;
//
///**
// * Created by Mezah on 5/31/2016.
// */
//public abstract class TheGodFatherActivity extends AppCompatActivity {
//
//
//    // add a tag to the fragment
//    protected abstract String getFragmentTag();
//
//    // set the required fragment
//    protected abstract Fragment getFragment();
//
//    // set the required layout
//    @LayoutRes
//    protected int getLayoutId(){
//
//        return R.layout.activity_master_detail_flow;
//
//    }
//
//    // setting the main container for the fragment
//    protected int getContainerId(){
//        return R.id.main_container;
//    }
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        // this can be changed in the inherited activity
//        setContentView(getLayoutId());
//
//        FragmentManager fragmentManager=getSupportFragmentManager();
//        // find the fragment using its container
//        Fragment fragment=fragmentManager.findFragmentById(R.id.main_container);
//
//        if(fragment==null){
//            // the fragment can't be found. create a new one and add it
//            fragmentManager
//                    .beginTransaction()
//                    .add(getContainerId(),getFragment(),getFragmentTag())
//                    .commit();
//
//
//        }
//
//
//
//
//
//    }
//}
