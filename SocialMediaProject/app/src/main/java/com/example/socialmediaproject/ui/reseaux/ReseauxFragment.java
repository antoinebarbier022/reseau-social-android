package com.example.socialmediaproject.ui.reseaux;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.socialmediaproject.R;
import com.example.socialmediaproject.adapters.GroupAdapter;
import com.example.socialmediaproject.api.GroupHelper;
import com.example.socialmediaproject.base.BaseActivity;
import com.example.socialmediaproject.models.Group;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.tabs.TabLayout;

import com.google.firebase.firestore.Query;

public class ReseauxFragment extends Fragment implements GroupAdapter.Listener{

    private ReseauxViewModel reseauxViewModel;
    private String m_Text = "";

    private RecyclerView recyclerView;

    // FOR DATA
    // 2 - Declaring Adapter and data
    private GroupAdapter groupAdapter;
    private String currentGroupName;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        reseauxViewModel = new ViewModelProvider(this).get(ReseauxViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        recyclerView = root.findViewById(R.id.recyclerView_groups);

        this.configureToolbar();
        configureRecyclerView("all");

        // we get the selected tab
        TabLayout tabLayout = root.findViewById(R.id.tabLayout_type_group);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getPosition()){
                    case 0: // all
                        configureRecyclerView( "all");
                        break;
                    case 1: // posts
                        configureRecyclerView( "post");
                        break;
                    case 2: // Tchat
                        configureRecyclerView( "chat");
                        break;
                    case 3: // email
                        configureRecyclerView( "email");
                        break;
                    case 4: // sms
                        configureRecyclerView( "sms");
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });

        return root;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        // inflate menu
        inflater.inflate(R.menu.reseaux_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.home_menu_add_private_group:
                // ouverture de l'activité des paramètres de l'application
                openPrivateGroup();
                return true;
            case R.id.home_menu_search:
                Navigation.findNavController(getView()).navigate(R.id.action_navigation_dashboard_to_searchPageFragment);
                Toast.makeText(getContext(), "Page de recherche ouverte !", Toast.LENGTH_SHORT).show();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


    public void configureToolbar(){
        // on enlève la fleche de retour en arrière
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        // title fragment in the header
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Mes réseaux");
    }



    // --------------------
    // UI
    // --------------------
    // 5 - Configure RecyclerView with a Query
    private void configureRecyclerView(String type){

        //Configure Adapter & RecyclerView
        if(type.equals("all")){
            this.groupAdapter = new GroupAdapter(generateOptionsForAdapter(GroupHelper.getAllGroup(BaseActivity.getUid())),
                    Glide.with(this), this, "test user");
        }else{
            this.groupAdapter = new GroupAdapter(generateOptionsForAdapter(GroupHelper.getAllGroupByType(type, BaseActivity.getUid())),
                    Glide.with(this), this, "test user");
        }

        groupAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                recyclerView.smoothScrollToPosition(groupAdapter.getItemCount()); // Scroll to bottom on new messages
            }
        });

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerView.setAdapter(this.groupAdapter);
    }

    // 6 - Create options for RecyclerView from a Query
    private FirestoreRecyclerOptions<Group> generateOptionsForAdapter(Query query){
        return new FirestoreRecyclerOptions.Builder<Group>()
                .setQuery(query, Group.class)
                .setLifecycleOwner(this)
                .build();
    }

    // --------------------
    // CALLBACK
    // --------------------

    @Override
    public void onDataChanged() {
        // 7 - Show TextView in case RecyclerView is empty
        //textViewRecyclerViewEmpty.setVisibility(this.groupAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    // --------------------
    // OTHERS
    // --------------------

    void openPrivateGroup(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Entre le code d'accès à un groupe privé pour le rejoindre");

        // Set up the input
        final EditText input = new EditText(getContext());
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}