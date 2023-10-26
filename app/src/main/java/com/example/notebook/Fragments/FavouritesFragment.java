package com.example.notebook.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.lottie.LottieAnimationView;
import com.example.notebook.Entity.Note;
import com.example.notebook.R;
import com.example.notebook.Ui.NoteActivity;
import com.example.notebook.ViewModel.Note_Viewmodel;
import com.example.notebook.recyclerview.RecyclerAdapter;
import com.example.notebook.recyclerview.RecyclerListener;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavouritesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavouritesFragment extends Fragment implements RecyclerListener {


    private RecyclerAdapter adapter;
    private List<Note> Main_list = new ArrayList<Note>();

    private Note_Viewmodel model;
    private LottieAnimationView lottieAnimationView;

    private Intent intent;

    private RecyclerView recyclerView;

    private SendData adapterListener;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FavouritesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavouritesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavouritesFragment newInstance(String param1, String param2) {
        FavouritesFragment fragment = new FavouritesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof SendData) {
            adapterListener = (SendData) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement SendData");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_favourites, container, false);
        recyclerView = view.findViewById(R.id.RecyclerView);
        lottieAnimationView = view.findViewById(R.id.animationView);
        lottieAnimationView.setVisibility(View.GONE);
        model = new ViewModelProvider(this).get(Note_Viewmodel.class);
        initializeRecycler();
        getDataViewModelAndonChanged();
        return view;
    }


    private void initializeRecycler() {
        adapter = new RecyclerAdapter(Main_list, getContext(), this, model);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        recyclerView.setHasFixedSize(true);
    }

    private void getDataViewModelAndonChanged() {

        model.get_all_notes_Favourites().observe(getActivity(), new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> list) {
                if (list.size() > 0) {
                    lottieAnimationView.setVisibility(View.GONE);
                } else {
                    lottieAnimationView.setVisibility(View.VISIBLE);
                }
                Main_list = list;
                adapter.setList(list);
                adapterListener.sendAdapter(adapter);
            }
        });

    }

    @Override
    public void ClickListener(View view, Note note) {
        intent = new Intent(getContext(), NoteActivity.class);
        intent.putExtra("note", note);
        startActivity(intent);
    }





}


