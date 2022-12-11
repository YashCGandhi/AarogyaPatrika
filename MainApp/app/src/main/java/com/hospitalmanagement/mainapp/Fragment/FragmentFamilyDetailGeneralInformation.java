package com.hospitalmanagement.mainapp.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;


import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.hospitalmanagement.mainapp.R;
import com.hospitalmanagement.mainapp.pojo.Family;
import com.hospitalmanagement.mainapp.pojo.General;
import com.hospitalmanagement.mainapp.pojo.MutationQueryFamilyDetail;
import com.hospitalmanagement.mainapp.pojo.UpdateResponse;
import com.hospitalmanagement.mainapp.pojo.Variables;
import com.hospitalmanagement.mainapp.recyclerView.SearchAdapter;
import com.hospitalmanagement.mainapp.retrofitService.apiClient.ApiClient;
import com.hospitalmanagement.mainapp.retrofitService.apiInterface.ApiInterface;


import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentFamilyDetailGeneralInformation extends Fragment {

    private TextWatcher textWatcher;

    private TextInputEditText editTextAreaName;
    private RadioGroup radioGroupToilet;
    private RadioGroup radioGroupBpl;
    private RadioGroup radioGroupHouse;
    private RadioGroup radioGroupWater;
    private RadioGroup radioGroupCaste;
//    private TextInputEditText editTextNumberOfMembers;
    private MaterialButton saveGeneralInformation;
    private RelativeLayout relativeLayoutGeneralInformation;
    private NestedScrollView nestedScrollViewGeneral;


    private General generalInformation;
    private String areaName;
    private String objectId;
    private Bundle bundle;
//    private BigInteger mobileNumber;
    private HashMap<String, Object> changedInformation;
    private ProgressDialog progressDialog;
    private AlertDialog.Builder alertDialogBuilder;
    private ApiInterface apiInterface;
    private AwesomeValidation awesomeValidation;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        bundle = this.getArguments();

        if(bundle != null) {
            objectId = bundle.getString("objectId");
        }

        inflater = LayoutInflater.from(new ContextThemeWrapper(getContext(), R.style.AppTheme));

        return inflater.inflate(R.layout.familydetail_generalinformation, container, false);
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        changedInformation = new HashMap<>();
        progressDialog = new ProgressDialog(getContext());
        alertDialogBuilder = new AlertDialog.Builder(getContext());
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        editTextAreaName = view.findViewById(R.id.areaNameEditText);
        radioGroupToilet = view.findViewById(R.id.radioGroupToilet);
        radioGroupBpl = view.findViewById(R.id.radioGroupBpl);
        radioGroupHouse = view.findViewById(R.id.radioGroupHouse);
        radioGroupWater = view.findViewById(R.id.radioGroupWater);
        radioGroupCaste = view.findViewById(R.id.radioGroupCaste);
//        editTextNumberOfMembers = view.findViewById(R.id.editTextNumberOfMembers);
        saveGeneralInformation = view.findViewById(R.id.btnSaveGeneralInformation);

        relativeLayoutGeneralInformation = view.findViewById(R.id.relativeLayoutGeneralInformation);
        nestedScrollViewGeneral = view.findViewById(R.id.generalInformationNestedScrollView);

        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                Log.i("gaya", "onTextChenged");
            }

            @Override
            public void afterTextChanged(Editable s) {

                Log.i("gaya", "afterTextChenged");

                if(s != null) {

                    if(s.hashCode()  == editTextAreaName.getText().hashCode()) {

                        if(!s.toString().equals(areaName)) {

                            changedInformation.put("areaname", editTextAreaName.getText().toString());
//                            System.out.println("Changes " + changedInformation.get("areaname"));

                            if(saveGeneralInformation.getVisibility() == View.GONE) {
                                saveGeneralInformation.setVisibility(View.VISIBLE);
                            }

                        } else {

                            changedInformation.remove("areaname");
                            if(changedInformation.isEmpty()) {
                                saveGeneralInformation.setVisibility(View.GONE);
                            }

                        }

                    }
//                    else if(s.hashCode() == editTextNumberOfMembers.getText().hashCode()) {
//
//                        if(!s.toString().equals(String.valueOf(generalInformation.getTotalMembers()))) {
//
//                            changedInformation.put("totalMembers", editTextNumberOfMembers.getText().toString());
////                            System.out.println("Changes " + changedInformation.get("totalMembers"));
//
//                            if(saveGeneralInformation.getVisibility() == View.GONE) {
//                                saveGeneralInformation.setVisibility(View.VISIBLE);
//                            }
//
//                        } else {
//
//                            changedInformation.remove("totalMembers");
//                            if(changedInformation.isEmpty()) {
//                                saveGeneralInformation.setVisibility(View.GONE);
//                            }
//
//                        }
//
//                    }
//                    else if(s.hashCode() == editTextMobileNumber.getText().hashCode()) {
//
//                        if(!s.toString().equals(String.valueOf(mobileNumber))) {
//
//                            changedInformation.put("mobileNo", editTextMobileNumber.getText().toString());
////                            System.out.println("Changes mobile " + changedInformation.get("mobileNo"));
//
//                            if(saveGeneralInformation.getVisibility() == View.GONE) {
//                                saveGeneralInformation.setVisibility(View.VISIBLE);
//                            }
//
//                        } else {
//
//                            changedInformation.remove("mobileNo");
//                            if(changedInformation.isEmpty()) {
//                                saveGeneralInformation.setVisibility(View.GONE);
//                            }
//
//                        }
//
//                    }

                }

            }
        };

        addValidation();
        apiCallToGetGeneralInformation();
        addListenersToComponents();
        nestedScrollViewGeneral.scrollTo(0, 0);
    }


    private void addListenersToComponents() {

        editTextAreaName.addTextChangedListener(textWatcher);
//        editTextNumberOfMembers.addTextChangedListener(textWatcher);
//        editTextMobileNumber.addTextChangedListener(textWatcher);

        radioGroupToilet.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                int responseToiletId;

                if(generalInformation.getToilet()) {
                    responseToiletId = R.id.radioButtonToiletYes;
                } else {
                    responseToiletId = R.id.radioButtonToiletNo;
                }

                switch(checkedId) {

                    case R.id.radioButtonToiletYes:
                        if(responseToiletId == R.id.radioButtonToiletYes) {
                            changedInformation.remove("toilet");

                            if(changedInformation.isEmpty()) {
                                saveGeneralInformation.setVisibility(View.GONE);
                            }

                        } else {

                            if(saveGeneralInformation.getVisibility() == View.GONE) {
                                saveGeneralInformation.setVisibility(View.VISIBLE);
                            }

                            changedInformation.put("toilet", true);
                        }
                        break;

                    case R.id.radioButtonToiletNo:
                        if(responseToiletId == R.id.radioButtonToiletNo) {
                            changedInformation.remove("toilet");

                            if(changedInformation.isEmpty()) {
                                saveGeneralInformation.setVisibility(View.GONE);
                            }

                        } else {

                            if(saveGeneralInformation.getVisibility() == View.GONE) {
                                saveGeneralInformation.setVisibility(View.VISIBLE);
                            }

                            changedInformation.put("toilet", false);
                        }
                        break;

                }

////                System.out.println("toilet " + changedInformation.get("toilet"));
            }
        });

        radioGroupBpl.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                int responseBplId;

                if(generalInformation.getBelowPovertyLine()) {
                    responseBplId = R.id.radioButtonBplYes;
                } else {
                    responseBplId = R.id.radioButtonBplNo;
                }

                switch(checkedId) {

                    case R.id.radioButtonBplYes:
                        if(responseBplId == R.id.radioButtonBplYes) {
                            changedInformation.remove("belowPovertyLine");

                            if(changedInformation.isEmpty()) {
                                saveGeneralInformation.setVisibility(View.GONE);
                            }

                        } else {

                            if(saveGeneralInformation.getVisibility() == View.GONE) {
                                saveGeneralInformation.setVisibility(View.VISIBLE);
                            }

                            changedInformation.put("belowPovertyLine", true);
                        }
                        break;

                    case R.id.radioButtonBplNo:
                        if(responseBplId == R.id.radioButtonBplNo) {
                            changedInformation.remove("belowPovertyLine");

                            if(changedInformation.isEmpty()) {
                                saveGeneralInformation.setVisibility(View.GONE);
                            }

                        } else {

                            if(saveGeneralInformation.getVisibility() == View.GONE) {
                                saveGeneralInformation.setVisibility(View.VISIBLE);
                            }

                            changedInformation.put("belowPovertyLine", false);
                        }
                        break;

                }

////                System.out.println("bpl " + changedInformation.get("belowPovertyLine"));
            }
        });

        radioGroupHouse.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                int responseHouseId;

                if(generalInformation.getHouse()) {
                    responseHouseId = R.id.radioButtonHousePakka;
                } else {
                    responseHouseId = R.id.radioButtonHouseKaccha;
                }

                switch(checkedId) {

                    case R.id.radioButtonHousePakka:
                        if(responseHouseId == R.id.radioButtonHousePakka) {
                            changedInformation.remove("house");

                            if(changedInformation.isEmpty()) {
                                saveGeneralInformation.setVisibility(View.GONE);
                            }

                        } else {

                            if(saveGeneralInformation.getVisibility() == View.GONE) {
                                saveGeneralInformation.setVisibility(View.VISIBLE);
                            }

                            changedInformation.put("house", true);
                        }
                        break;

                    case R.id.radioButtonHouseKaccha:
                        if(responseHouseId == R.id.radioButtonHouseKaccha) {
                            changedInformation.remove("house");

                            if(changedInformation.isEmpty()) {
                                saveGeneralInformation.setVisibility(View.GONE);
                            }

                        } else {

                            if(saveGeneralInformation.getVisibility() == View.GONE) {
                                saveGeneralInformation.setVisibility(View.VISIBLE);
                            }

                            changedInformation.put("house", false);
                        }
                        break;

                }

////                System.out.println("house " + changedInformation.get("house"));
            }
        });

        radioGroupWater.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                int responseWaterId = -1;

                if(generalInformation.getPublicBoreHandpump() != null) {

                    switch (generalInformation.getPublicBoreHandpump()) {
                        case "विहीर":
                            responseWaterId = R.id.radioButtonWaterWell;
                            break;
                        case "बोअर":
                            responseWaterId = R.id.radioButtonWaterBore;
                            break;
                        case "सार्वजनिक":
                            responseWaterId = R.id.radioButtonWaterPublic;
                            break;
                        case "हातपंप":
                            responseWaterId = R.id.radioButtonWaterHandpump;
                            break;
                    }

                }


                switch(checkedId) {

                    case R.id.radioButtonWaterHandpump:
                        if(responseWaterId == R.id.radioButtonWaterHandpump) {
                            changedInformation.remove("publicBoreHandpump");

                            if(changedInformation.isEmpty()) {
                                saveGeneralInformation.setVisibility(View.GONE);
                            }

                        } else {

                            if(saveGeneralInformation.getVisibility() == View.GONE) {
                                saveGeneralInformation.setVisibility(View.VISIBLE);
                            }

                            changedInformation.put("publicBoreHandpump", "हातपंप");
                        }
                        break;

                    case R.id.radioButtonWaterBore:
                        if(responseWaterId == R.id.radioButtonWaterBore) {
                            changedInformation.remove("publicBoreHandpump");

                            if(changedInformation.isEmpty()) {
                                saveGeneralInformation.setVisibility(View.GONE);
                            }

                        } else {

                            if(saveGeneralInformation.getVisibility() == View.GONE) {
                                saveGeneralInformation.setVisibility(View.VISIBLE);
                            }

                            changedInformation.put("publicBoreHandpump", "बोअर");
                        }
                        break;

                    case R.id.radioButtonWaterPublic:
                        if(responseWaterId == R.id.radioButtonWaterPublic) {
                            changedInformation.remove("publicBoreHandpump");

                            if(changedInformation.isEmpty()) {
                                saveGeneralInformation.setVisibility(View.GONE);
                            }

                        } else {

                            if(saveGeneralInformation.getVisibility() == View.GONE) {
                                saveGeneralInformation.setVisibility(View.VISIBLE);
                            }

                            changedInformation.put("publicBoreHandpump", "सार्वजनिक");
                        }
                        break;

                    case R.id.radioButtonWaterWell:
                        if(responseWaterId == R.id.radioButtonWaterWell) {
                            changedInformation.remove("publicBoreHandpump");

                            if(changedInformation.isEmpty()) {
                                saveGeneralInformation.setVisibility(View.GONE);
                            }

                        } else {

                            if(saveGeneralInformation.getVisibility() == View.GONE) {
                                saveGeneralInformation.setVisibility(View.VISIBLE);
                            }

                            changedInformation.put("publicBoreHandpump", "विहीर");
                        }
                        break;

                }

////                System.out.println("house " + changedInformation.get("publicBoreHandpump"));
            }
        });

        radioGroupCaste.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                int responseCasteId = -1;

                if(generalInformation.getCategory() != null) {
                    switch (generalInformation.getCategory()) {
                        case "प्रवर्ग अनुसूचित जाती":
                            responseCasteId = R.id.radioButtonCasteSc;
                            break;
                        case "इतर":
                            responseCasteId = R.id.radioButtonCasteOthers;
                            break;
                        case "अनु.जमाती":
                            responseCasteId = R.id.radioButtonCasteSt;
                            break;
                    }
                }



                switch(checkedId) {

                    case R.id.radioButtonCasteOthers:
                        if(responseCasteId == R.id.radioButtonCasteOthers) {
                            changedInformation.remove("category");

                            if(changedInformation.isEmpty()) {
                                saveGeneralInformation.setVisibility(View.GONE);
                            }

                        } else {

                            if(saveGeneralInformation.getVisibility() == View.GONE) {
                                saveGeneralInformation.setVisibility(View.VISIBLE);
                            }

                            changedInformation.put("category", "इतर");
                        }
                        break;

                    case R.id.radioButtonCasteSc:
                        if(responseCasteId == R.id.radioButtonCasteSc) {
                            changedInformation.remove("category");

                            if(changedInformation.isEmpty()) {
                                saveGeneralInformation.setVisibility(View.GONE);
                            }

                        } else {

                            if(saveGeneralInformation.getVisibility() == View.GONE) {
                                saveGeneralInformation.setVisibility(View.VISIBLE);
                            }

                            changedInformation.put("category", "प्रवर्ग अनुसूचित जाती");
                        }
                        break;

                    case R.id.radioButtonCasteSt:
                        if(responseCasteId == R.id.radioButtonCasteSt) {
                            changedInformation.remove("category");

                            if(changedInformation.isEmpty()) {
                                saveGeneralInformation.setVisibility(View.GONE);
                            }

                        } else {

                            if(saveGeneralInformation.getVisibility() == View.GONE) {
                                saveGeneralInformation.setVisibility(View.VISIBLE);
                            }

                            changedInformation.put("category", "अनु.जमाती");
                        }
                        break;

                }

//                System.out.println("house " + changedInformation.get("category"));
            }
        });


        saveGeneralInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // get data from changedInformation object and make api call to update database

                if(awesomeValidation.validate()) {

//                    if (changedInformation.get("mobileNo") != null) {
//                        changedInformation.put("mobileNo", new BigInteger(String.valueOf(changedInformation.get("mobileNo"))));
//                    }
//                    if (changedInformation.get("totalMembers") != null) {
//                        changedInformation.put("totalMembers", Integer.parseInt(String.valueOf(changedInformation.get("totalMembers"))));
//                    }

                    Variables variables = new Variables();
                    variables.setInput(changedInformation);

                    MutationQueryFamilyDetail mutationQuery = new MutationQueryFamilyDetail();
                    mutationQuery.setVariables(variables);

                    apiCallToUpdateGeneralInformation(mutationQuery);
                }
            }
        });

    }

    private void  apiCallToUpdateGeneralInformation(MutationQueryFamilyDetail mutationQuery) {

        showProgressBar("Updating General Information ...");


        String query = "mutation($input: GeneralInput) { updateGeneralInfo(id: \"" + objectId + "\", input: $input) { n nModified ok } }";

        mutationQuery.setQuery(query);

        Call<Family> updateGeneralInformation = apiInterface.updateInformation(mutationQuery);

        updateGeneralInformation.enqueue(new Callback<Family>() {
            @Override
            public void onResponse(Call<Family> call, Response<Family> response) {

                if(response.isSuccessful() && response.body() != null) {

                    stopProgressBar();
                    apiCallToGetGeneralInformation();
                    saveGeneralInformation.setVisibility(View.GONE);
                    changedInformation.clear();
                    showAlertDialog("Data Updated Successfully.");

                } else {
//                    System.out.println(response.code());
                    showAlertDialog("Error Updating Data");

                    stopProgressBar();
                }

            }

            @Override
            public void onFailure(Call<Family> call, Throwable t) {

            }
        });


    }



    private void apiCallToGetGeneralInformation() {

        showProgressBar("Loading Please Wait ...");

//        System.out.println(objectId);
        String query = "query { getFamily(_id:\"" + objectId + "\") { areaname general { toilet house publicBoreHandpump category belowPovertyLine }}}";

        Call<Family> getGeneralInformation = apiInterface.getInformation(query);


        getGeneralInformation.enqueue(new Callback<Family>() {



            @Override
            public void onResponse(Call<Family> call, Response<Family> response) {

                if(response.isSuccessful() && response.body() != null) {

                    generalInformation = response.body().getData().getGetFamily().getGeneral();
                    areaName = response.body().getData().getGetFamily().getAreaname();
//                    mobileNumber = response.body().getData().getGetFamily().getMobileNo();

                    setDataToLayout();

                    relativeLayoutGeneralInformation.setVisibility(View.VISIBLE);
                    stopProgressBar();
                }
                else {
//                    Log.i("error", response.errorBody().toString());
//                    System.out.println(response.code());
                    stopProgressBar();
                    showAlertDialog("Not Found");
                }

            }



            @Override
            public void onFailure(Call<Family> call, Throwable t) {
                Log.i("noresponse", t.getMessage());
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

    private void showAlertDialog(String message) {

        alertDialogBuilder
                .setTitle("Message")
                .setMessage(message)
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

    private void setDataToLayout() {

        //set values from response to the layout
        editTextAreaName.setText(areaName);
//        editTextNumberOfMembers.setText(String.valueOf(generalInformation.getTotalMembers()));
//        editTextMobileNumber.setText(String.valueOf(mobileNumber));

        if(generalInformation.getToilet()) {
            radioGroupToilet.check(R.id.radioButtonToiletYes);
        } else {
            radioGroupToilet.check(R.id.radioButtonToiletNo);
        }

        if(generalInformation.getBelowPovertyLine()) {
            radioGroupBpl.check(R.id.radioButtonBplYes);
        } else {
            radioGroupBpl.check(R.id.radioButtonBplNo);
        }

        if(generalInformation.getHouse()) {
            radioGroupHouse.check(R.id.radioButtonHousePakka);
        } else {
            radioGroupHouse.check(R.id.radioButtonHouseKaccha);
        }

        if(generalInformation.getPublicBoreHandpump() != null) {

            switch (generalInformation.getPublicBoreHandpump()) {
                case "विहीर":
                    radioGroupWater.check(R.id.radioButtonWaterWell);
                    break;
                case "बोअर":
                    radioGroupWater.check(R.id.radioButtonWaterBore);
                    break;
                case "सार्वजनिक":
                    radioGroupWater.check(R.id.radioButtonWaterPublic);
                    break;
                case "हातपंप":
                    radioGroupWater.check(R.id.radioButtonWaterHandpump);
                    break;
            }

        }

//                    verify values
        if(generalInformation.getCategory() != null) {
            switch (generalInformation.getCategory()) {
                case "इतर":
                    radioGroupCaste.check(R.id.radioButtonCasteOthers);
                    break;
                case "प्रवर्ग अनुसूचित जाती":
                    radioGroupCaste.check(R.id.radioButtonCasteSc);
                    break;
                case "अनु.जमाती":
                    radioGroupCaste.check(R.id.radioButtonCasteSt);
                    break;
            }
        }

    }

    private void addValidation() {
        awesomeValidation.addValidation(editTextAreaName, RegexTemplate.NOT_EMPTY, "गाव नाव प्रविष्ट " +
                "करा");
    }

}
