package com.hospitalmanagement.mainapp.Fragment;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Spinner;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.hospitalmanagement.mainapp.R;
import com.hospitalmanagement.mainapp.pojo.Family;
import com.hospitalmanagement.mainapp.pojo.SearchName;
import com.hospitalmanagement.mainapp.recyclerView.RecyclerViewObjectId;
import com.hospitalmanagement.mainapp.recyclerView.SearchAdapter;
import com.hospitalmanagement.mainapp.retrofitService.apiClient.ApiClient;
import com.hospitalmanagement.mainapp.retrofitService.apiInterface.ApiInterface;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentFamilyDetail extends Fragment {

    private View rootView;
    private RelativeLayout searchMemberRelativeLayout;
    private MaterialButton searchMemberButton;
    private MaterialSpinner informationSpinner;
    private TextInputEditText editTextSearchByMemberName;
    private RecyclerView recyclerViewFamilyMembers;
    private SearchAdapter searchAdapter;
    private LayoutInflater layoutInflater;


    private RecyclerViewObjectId recyclerViewObjectId;
    private Bundle dataBundle;
    private FragmentFamilyDetail fragmentFamilyDetail;
    private ProgressDialog progressDialog;
    private AlertDialog.Builder alertDialogBuilder;
    private List<SearchName> matchedMembers;

    private Fragment currentFragment;
    private FragmentManager fragmentManager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragmentFamilyDetail = this;
        fragmentManager = getChildFragmentManager();
        inflater = LayoutInflater.from(new ContextThemeWrapper(getContext(), R.style.AppTheme));

        rootView = inflater.inflate(R.layout.fragment_familydetail, container, false);

//        rootView.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//
//                if(event.getAction() == KeyEvent.ACTION_DOWN) {
//                    if(keyCode == KeyEvent.KEYCODE_BACK) {
//
////                        if(currentFragment != null) {
//                            getChildFragmentManager()
//                                    .beginTransaction()
//                                    .remove(currentFragment)
//                                    .commit();
//                            return true;
////                        }
//                    }
//
//                }
//                return false;
//
//            }
//        });

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchMemberRelativeLayout = view.findViewById(R.id.searchMemberRelativeLayout);
        searchMemberButton = view.findViewById(R.id.btnSearchMember);
        informationSpinner = view.findViewById(R.id.informationSpinner);
        editTextSearchByMemberName = view.findViewById(R.id.editTextSearchByMemberName);
        recyclerViewFamilyMembers = view.findViewById(R.id.memberSearchRecyclerView);

        layoutInflater = LayoutInflater.from(new ContextThemeWrapper(getContext(), R.style.AppTheme));

        dataBundle = new Bundle();
        progressDialog = new ProgressDialog(getContext());
        alertDialogBuilder = new AlertDialog.Builder(getContext());

        recyclerViewObjectId = new RecyclerViewObjectId() {
            @Override
            public void onFamilyItemClick(String objectId) {
                dataBundle.putString("objectId", objectId);
            }
        };

        informationSpinner.setItems("---Select Information---", "General Information", "Member Information", "Couple Information", "Child Information", "Pregnancy Information");
        addListenerToComponents();

    }

    private void bindRecyclerView() {

        searchAdapter = new SearchAdapter(matchedMembers, layoutInflater, searchMemberRelativeLayout, informationSpinner, recyclerViewObjectId);
        recyclerViewFamilyMembers.setAdapter(searchAdapter);
        recyclerViewFamilyMembers.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewFamilyMembers.setVisibility(View.VISIBLE);

    }

    private void addListenerToComponents() {

        searchMemberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                searchMemberRelativeLayout.setVisibility(View.GONE);
//                informationSpinner.setVisibility(View.VISIBLE);
                if(editTextSearchByMemberName.getText().toString().trim().length() != 0) {
                    showProgressBar("Searching...");
                    apiCallToGetMemberNames(String.valueOf(editTextSearchByMemberName.getText()));
                }

            }
        });

        informationSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {

                if(!view.getItems().get(position).toString().equals("---Select Information---")) {

                    if(view.getItems().get(position).toString().equals("General Information")) {
                        FragmentFamilyDetailGeneralInformation fragmentFamilyDetailGeneralInformation = new FragmentFamilyDetailGeneralInformation();
                        fragmentFamilyDetailGeneralInformation.setArguments(dataBundle);
                        replaceFragment(fragmentFamilyDetailGeneralInformation);
                    } else if(view.getItems().get(position).toString().equals("Member Information")) {
                        FragmentFamilyDetailMemberInformation fragmentFamilyDetailMemberInformation = new FragmentFamilyDetailMemberInformation();
                        fragmentFamilyDetailMemberInformation.setArguments(dataBundle);
                        replaceFragment(fragmentFamilyDetailMemberInformation);
                    } else if(view.getItems().get(position).toString().equals("Couple Information")) {
                        FragmentFamilyDetailCoupleInformation fragmentFamilyDetailCoupleInformation = new FragmentFamilyDetailCoupleInformation();
                        fragmentFamilyDetailCoupleInformation.setArguments(dataBundle);
                        replaceFragment(fragmentFamilyDetailCoupleInformation);
                    } else if(view.getItems().get(position).toString().equals("Child Information")) {
                        FragmentFamilyDetailChildInformation fragmentFamilyDetailChildInformation = new FragmentFamilyDetailChildInformation();
                        fragmentFamilyDetailChildInformation.setArguments(dataBundle);
                        replaceFragment(fragmentFamilyDetailChildInformation);
                    } else if(view.getItems().get(position).toString().equals("Pregnancy Information")) {
                        FragmentFamilyDetailPregnancyInformation fragmentFamilyDetailPregnancyInformation = new FragmentFamilyDetailPregnancyInformation();
                        fragmentFamilyDetailPregnancyInformation.setArguments(dataBundle);
                        replaceFragment(fragmentFamilyDetailPregnancyInformation);
                    }

                } else {
                    getChildFragmentManager()
                            .beginTransaction()
                            .remove(currentFragment)
                            .commit();

                }

            }
        });


    }


    private void replaceFragment(Fragment fragment) {
        currentFragment = fragment;

        String backStackName = fragment.getClass().getName();

        fragmentManager
                .beginTransaction()
                .replace(R.id.familyDetailsLayoutFragmentContainer, fragment, backStackName)
//                .addToBackStack(backStackName)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();

    }


    private void apiCallToGetMemberNames(String name) {

        String query = "query { searchName(name: \""+ name +"\" ) { _id members { membername }}}";

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<Family> getMemberNames = apiInterface.getInformation(query);

        getMemberNames.enqueue(new Callback<Family>() {
            @Override
            public void onResponse(Call<Family> call, Response<Family> response) {

                if(response.isSuccessful() && response.body() != null) {

                    matchedMembers = response.body().getData().getSearchName();

                    stopProgressBar();
                    if(matchedMembers.isEmpty()) {
                        showAlertDialog();
                    } else {
                        bindRecyclerView();
                    }

////                    System.out.println(matchedMembers.get(0).getMembers().get(0).getMembername());

                }

            }

            @Override
            public void onFailure(Call<Family> call, Throwable t) {
//                System.out.println(t.getMessage());
            }
        });

    }

    private void showProgressBar(String message) {
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    private void stopProgressBar() {
        progressDialog.dismiss();
    }

    private void showAlertDialog() {
        if((searchAdapter instanceof SearchAdapter)) {
            matchedMembers.clear();
            searchAdapter.updateList(matchedMembers);
        }
        alertDialogBuilder
                .setTitle("Message")
                .setMessage("Not Found.")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();

    }

}
