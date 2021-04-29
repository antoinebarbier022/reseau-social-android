package com.example.socialmediaproject.ui.settings.pageSettings.pageMembers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.socialmediaproject.R;
import com.example.socialmediaproject.adapters.UserAdapter;
import com.example.socialmediaproject.models.UserHelperClass;

import java.util.ArrayList;
import java.util.List;

public class membersListFragment extends Fragment {

    private MembersListViewModel mViewModel;

    public static membersListFragment newInstance() {
        return new membersListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

        // affichage de la flèche retour en arrière dans le menu
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.fragment_members_list, container, false);
            // list of posts
            List<UserHelperClass> userList = new ArrayList<>();
            userList.add(new UserHelperClass("Antoine Barbier"));
            userList.add(new UserHelperClass("Antoine Brahimi"));
            userList.add(new UserHelperClass("Thomas Pesquet"));
            userList.add(new UserHelperClass("Usain Bolt"));

            // get list view
            ListView allUser = (ListView) view.findViewById(R.id.listView_members);
            allUser.setAdapter(new UserAdapter(getContext(), userList));

            // title fragment in the header
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Membres du groupe");

            return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MembersListViewModel.class);

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        // inflate menu
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()) {
            case android.R.id.home: // action sur la flèche de retour en arrière
                getActivity().onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}