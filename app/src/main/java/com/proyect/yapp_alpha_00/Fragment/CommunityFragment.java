package com.proyect.yapp_alpha_00.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.proyect.yapp_alpha_00.Adapters.ArrayAdapter;
import com.proyect.yapp_alpha_00.Adapters.PostAdapter;
import com.proyect.yapp_alpha_00.Adapters.PostAdapterPIla;
import com.proyect.yapp_alpha_00.Model.Post;
import com.proyect.yapp_alpha_00.PostActivity;
import com.proyect.yapp_alpha_00.R;

import java.util.ArrayList;
import java.util.List;

public class CommunityFragment extends Fragment {

    private PostAdapter postAdapter;
    private List<Post> postList;
    private ArrayAdapter pila;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_community, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        RecyclerView recyclerViewPila = view.findViewById(R.id.recycler_view_pila);

        FloatingActionButton btn_publicar = view.findViewById(R.id.btn_upload);

        btn_publicar.setOnClickListener(v -> startActivity(new Intent(getActivity(), PostActivity.class)));

        recyclerView.setHasFixedSize(true);
        recyclerViewPila.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        LinearLayoutManager linearLayoutManagerPila = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerViewPila.setLayoutManager(linearLayoutManagerPila);
        postList = new ArrayList<>();
        pila = new ArrayAdapter();
        postAdapter = new PostAdapter(getContext(), postList);
        PostAdapterPIla postAdapterPila = new PostAdapterPIla(getContext(), pila);
        recyclerView.setAdapter(postAdapter);
        recyclerViewPila.setAdapter(postAdapterPila);


        checkPosts();

        return view;
    }

    private void checkPosts(){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Usuarios");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                readPosts();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void readPosts(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("publicaciones");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                while(!pila.isEmpty()){
                    pila.pop();
                }
                for(DataSnapshot datasnapshot : snapshot.getChildren()){
                    Post post = datasnapshot.getValue(Post.class);
                    Log.w("Array", post.toString());
                    postList.add(post);
                    Log.w("pila", post.toString());
                    pila.push(post);
                }

                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}