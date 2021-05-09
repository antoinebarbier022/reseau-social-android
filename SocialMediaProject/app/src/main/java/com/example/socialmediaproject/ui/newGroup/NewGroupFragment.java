package com.example.socialmediaproject.ui.newGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.socialmediaproject.R;
import com.example.socialmediaproject.api.GroupHelper;
import com.example.socialmediaproject.enums.Access;
import com.example.socialmediaproject.models.User;
import com.google.android.gms.tasks.OnFailureListener;

public class NewGroupFragment extends Fragment {

    private AutoCompleteTextView spinnerGroupType;
    private AutoCompleteTextView spinnerGroupAccess;
    private AutoCompleteTextView spinnerGroupPublication;
    private AutoCompleteTextView spinnerGroupSubject;



    private NewGroupViewModel mViewModel;

    public static NewGroupFragment newInstance() {
        return new NewGroupFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_group_fragment, container, false);

        // title fragment in the header
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Nouveau Groupe");
        // affichage de la flèche retour en arrière dans le menu
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        EditText editText_groupName = view.findViewById(R.id.editText_group_name);

        spinnerGroupType = view.findViewById(R.id.spinner_group_type);
        spinnerGroupAccess = view.findViewById(R.id.spinner_group_access);
        spinnerGroupSubject = view.findViewById(R.id.spinner_group_subject);
        spinnerGroupPublication = view.findViewById(R.id.spinner_group_publication);
        ArrayAdapter<CharSequence> adapterTypeGroup = ArrayAdapter.createFromResource(getContext(), R.array.list_group_type, android.R.layout.simple_spinner_item);
        adapterTypeGroup.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<CharSequence> adapterAccessGroup = ArrayAdapter.createFromResource(getContext(), R.array.list_group_access, android.R.layout.simple_spinner_item);
        adapterAccessGroup.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<CharSequence> adapterPublicationGroup = ArrayAdapter.createFromResource(getContext(), R.array.list_group_publication, android.R.layout.simple_spinner_item);
        adapterPublicationGroup.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<CharSequence> adapterSubjectGroup = ArrayAdapter.createFromResource(getContext(), R.array.list_group_subject, android.R.layout.simple_spinner_item);
        adapterSubjectGroup.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinnerGroupType.setAdapter(adapterTypeGroup);
        spinnerGroupAccess.setAdapter(adapterAccessGroup);
        spinnerGroupPublication.setAdapter(adapterPublicationGroup);
        spinnerGroupSubject.setAdapter(adapterSubjectGroup);



        Button createGroup = view.findViewById(R.id.button_create_group);
        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Vous devez remplir tous les champs demandé !" , Toast.LENGTH_SHORT).show();
                Navigation.findNavController(view).navigate(R.id.action_newGroupFragment_to_navigation_groupe_post);
            /*GroupHelper.createGroup("test",
                    "test type",
                    "test domaine",
                    new User("steve"),
                    Access.PRIVATE).addOnFailureListener(onFailureListener());*/
            /*if(editText_groupName.getText().toString().matches("") &&
                    spinnerGroupType.getText().toString().matches("") &&
                    spinnerGroupAccess.getText().toString().matches("") &&
                    spinnerGroupPublication.getText().toString().matches("") &&
                    spinnerGroupSubject.getText().toString().matches("")){
                Toast.makeText(getContext(),"Vous devez remplir tous les champs demandé !" , Toast.LENGTH_SHORT).show();
            }else{
                //Navigation.findNavController(view1).navigate(R.id.action_newGroupFragment_to_navigation_groupe_post);
                GroupHelper.createGroup(editText_groupName.getText().toString(),
                                        spinnerGroupType.getText().toString(),
                                        spinnerGroupSubject.getText().toString(),
                                        new User("steve"),
                                        Access.PRIVATE).addOnFailureListener(onFailureListener());


            }*/
            }
        });

        return view;
    }

    protected OnFailureListener onFailureListener(){
        return e -> Toast.makeText(getContext(), "error", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(NewGroupViewModel.class);
        // TODO: Use the ViewModel
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        // inflate menu
        inflater.inflate(R.menu.close_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()) {
            case R.id.close_menu_exit: // action sur la flèche de retour en arrière
                getActivity().onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}