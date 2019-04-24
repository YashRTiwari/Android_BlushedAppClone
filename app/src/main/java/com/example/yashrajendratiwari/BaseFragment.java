package com.example.yashrajendratiwari;


import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yashrajendratiwari.databinding.FragmentBaseBinding;

public class BaseFragment extends Fragment {

    private static final String TAG = "testingapp";
    private FragmentBaseBinding binding;
    private LinearLayoutManager manager;
    private RVMessageAdapter adapter;
    private long startTime ;
    private int downX, downY;


    public BaseFragment() {
        // Required empty public constructor
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_base, container, false);

        setUpUI();

        manager = new LinearLayoutManager(getContext());
        adapter = new RVMessageAdapter(getContext());

        binding.rv.setLayoutManager(manager);
        binding.rv.setAdapter(adapter);

        binding.rv.setOnTouchListener((v, event) -> {

            if (!setUpUI()){
                return false;
            }
            if (event.getAction() == 0 ){
                startTime = event.getEventTime();
                downX = (int) event.getX();
                downY = (int) event.getY();
            }else if (event.getAction() == 1 &&
                    event.getEventTime() - startTime < 150 &&
                    Math.abs(((int)event.getX())-downX )< 10 &&
                    Math.abs(((int)event.getY())-downY ) <10){
                adapter.addMessage("Lorem Ipsum is simply dummy text of the printing and typesetting industry. ");
                binding.rv.smoothScrollToPosition(adapter.getItemCount()-1);
                downX=0;
                    downY=0;
                startTime=0;
            } else if (event.getAction() == 1 &&
                    event.getEventTime() - startTime < 2000 &&
                    Math.abs(((int)event.getX())-downX )< 10 &&
                    Math.abs(((int)event.getY())-downY ) <10){
                hideSystemUI();
            }

            return false;
        });
        binding.llRetry.setOnClickListener(v -> setUpUI());

        return binding.getRoot();
    }


    private boolean setUpUI(){

        if(!NetworkUtils.isNetworkAvailable(getContext())){
            binding.llNoNetwork.setVisibility(View.VISIBLE);
            binding.llRV.setVisibility(View.GONE);
            binding.llRV.setClickable(false);
            return false;
        } else {
            binding.llRV.setClickable(true);
            binding.llNoNetwork.setVisibility(View.GONE);
            binding.llRV.setVisibility(View.VISIBLE);
            return true;

        }
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getActivity().getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    private void showSystemUI() {
        View decorView = getActivity().getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }


}
