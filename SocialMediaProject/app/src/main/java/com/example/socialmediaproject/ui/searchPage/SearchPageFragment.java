package com.example.socialmediaproject.ui.searchPage;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.socialmediaproject.R;
import com.example.socialmediaproject.adapters.PostAdapter;
import com.example.socialmediaproject.adapters.SearchGroupAdapter;
import com.example.socialmediaproject.api.GroupHelper;
import com.example.socialmediaproject.api.PostHelper;
import com.example.socialmediaproject.api.UserHelper;
import com.example.socialmediaproject.models.Group;
import com.example.socialmediaproject.models.Post;
import com.example.socialmediaproject.models.User;
import com.example.socialmediaproject.ui.home.HomeViewModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;


public class SearchPageFragment extends Fragment implements SearchGroupAdapter.Listener {

    private RecyclerView recyclerView;
    private SearchPageViewModel mViewModel;
    private String m_Text = "";

    // FOR DATA
    // 2 - Declaring Adapter and data
    private SearchGroupAdapter searchGroupAdapter;

    private TextView textViewRecyclerViewEmpty;

    public static SearchPageFragment newInstance() {
        return new SearchPageFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SearchPageViewModel.class);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(this).get(SearchPageViewModel.class);
        View root = inflater.inflate(R.layout.search_page_fragment, container, false);

        textViewRecyclerViewEmpty = root.findViewById(R.id.textViewRecyclerViewEmpty);

        this.configureToolbar();


        recyclerView = root.findViewById(R.id.recyclerView_search_group);
        this.configureRecyclerView();

        return root;
    }

    public void configureToolbar(){
        // on enlève l'affichage de la flèche retour en arrière dans le menu
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Les groupes");
    }


    // --------------------
    // UI
    // --------------------
    // 5 - Configure RecyclerView with a Query
    private void configureRecyclerView(){
        //Configure Adapter & RecyclerView
        this.searchGroupAdapter = new SearchGroupAdapter(generateOptionsForAdapter(GroupHelper.getAllPublicGroup()),
                Glide.with(this), this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(this.searchGroupAdapter);
    }

    // 6 - Create options for RecyclerView from a Query
    private FirestoreRecyclerOptions<Group> generateOptionsForAdapter(Query query){
        return new FirestoreRecyclerOptions.Builder<Group>()
                .setQuery(query, Group.class)
                .setLifecycleOwner(this)
                .build();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        // inflate menu
        inflater.inflate(R.menu.search_group_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

        // Mise en place de la logique métier de la search bar
        MenuItem menuItem = menu.findItem(R.id.search);
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // on met en minuscule pour gérer le cas "case sensitive"
                processSearch(query.toLowerCase());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // on met en minuscule pour gérer le cas "case sensitive"
                processSearch(newText.toLowerCase());
                return false;
            }
        });
    }

    private void processSearch(String query) {
        FirestoreRecyclerOptions<Group> options =
                new FirestoreRecyclerOptions.Builder<Group>()
                    .setQuery(GroupHelper.getAllPublicGroup().orderBy("search").startAt(query).endAt(query+"\uf8ff"), Group.class)
                    .setLifecycleOwner(this)
                    .build();

        searchGroupAdapter = new SearchGroupAdapter(options, Glide.with(this), this);
        searchGroupAdapter.startListening();
        recyclerView.setAdapter(searchGroupAdapter);
        textViewRecyclerViewEmpty.setText("Aucun groupe trouvé.");
        textViewRecyclerViewEmpty.setVisibility(this.searchGroupAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.home_menu_add_private_group:
                // ouverture de l'activité des paramètres de l'application
                openPrivateGroup();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

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

    // --------------------
    // CALLBACK
    // --------------------

    @Override
    public void onDataChanged() {
        // 7 - Show TextView in case RecyclerView is empty
        textViewRecyclerViewEmpty.setText("Il n'existe pas de groupe public dans l'application");
        textViewRecyclerViewEmpty.setVisibility(this.searchGroupAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }
}