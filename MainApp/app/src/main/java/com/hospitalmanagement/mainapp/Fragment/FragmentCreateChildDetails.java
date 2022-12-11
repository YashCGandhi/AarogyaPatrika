package com.hospitalmanagement.mainapp.Fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hospitalmanagement.mainapp.GetterSetter.ChildDetails;
import com.hospitalmanagement.mainapp.GetterSetter.CoupleDetails;
import com.hospitalmanagement.mainapp.GetterSetter.FamilyMemberList;
import com.hospitalmanagement.mainapp.GetterSetter.GeneralInfo;
import com.hospitalmanagement.mainapp.GetterSetter.PregnantDetails;
import com.hospitalmanagement.mainapp.GetterSetter.Vaccination;
import com.hospitalmanagement.mainapp.R;
import com.hospitalmanagement.mainapp.Sessions.DatabaseHelper;
import com.hospitalmanagement.mainapp.Sessions.SessionManager;
import com.hospitalmanagement.mainapp.pojo.CreateMemberInput;
import com.hospitalmanagement.mainapp.pojo.CreateMemberPojo;
import com.hospitalmanagement.mainapp.pojo.CreateQuery;
import com.hospitalmanagement.mainapp.pojo.Family;
import com.hospitalmanagement.mainapp.retrofitService.apiClient.ApiClient;
import com.hospitalmanagement.mainapp.retrofitService.apiInterface.ApiInterface;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.bson.types.ObjectId;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentCreateChildDetails extends Fragment {
    View v;

    //Manager
    SessionManager sessionManager;
    DatabaseHelper databaseHelper;
    ProgressDialog progressDialog;
    LayoutInflater layoutInflater;

    //Components
    MaterialButton btnOpv, btnB2vit, btnBcg, btnPenta1, btnPenta2, btnPenta3, btnVita1, btnVita2,
            btnAddChildDetail, btnUploadDetail, btnAddChild, btnDeleteMember, btnNext;
    LinearLayout opvLayout, b2vitLayout, bcgLayout, penta1Layout, penta2Layout, penta3Layout,
            vita1Layout, vita2layout;
    RelativeLayout relChildLayout;
    TextView textPreviousChildText, textPregnancyId, textPregnancyNumber, textChildInfo;
    MaterialSpinner reqChildSpinner, childSpinner, fatherSpinner, motherSpinner;
    TextInputEditText editRCH;
    AwesomeValidation awesomeValidation;
    NestedScrollView childScrollView;
    //ids
    int opvId = 0, b2vitId = 0, bcgId = 0, penta1Id = 0, penta2Id = 0, penta3Id = 0,
            vita1Id = 0, vita2Id = 0;

    //Arraylists
    ArrayList<String> opvList, b2vitList, bcgList, penta1List, penta2List, penta3List, vita1List,
            vita2List;
    ArrayList<String> childNameList, childIdList, prevChildNameList, prevChildIdList;
    ArrayList<String> motherNameList, fatherNameList, coupleIdList;
    ArrayList<Integer> chIdList;
    ArrayList<FamilyMemberList> familyMemberListArrayList;
    ArrayList<CoupleDetails> coupleDetailsArrayList;
    ArrayList<ChildDetails> childDetailsArrayList;
    //Calendars
    Calendar calendar;
    int day, month, year;

    //Acceptedinputs
    String husbandName, wifeName, eligibleCoupleId, childName, childId;
    int coupleId, rchId;
    boolean prevFlag = false, finalFlag = false;

    ApiInterface apiInterface;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflater = LayoutInflater.from(new ContextThemeWrapper(getContext(), R.style.AppTheme));
        v = inflater.inflate(R.layout.fragment_create_child, container, false);
        layoutInflater = LayoutInflater.from(new ContextThemeWrapper(getContext(),
                R.style.AppTheme));
        initApp();
        addListenersToComponents();
        addValidation();
        getPreviousInformation();
        getPreviousCoupleInformation();
        checkForPreviousChildInformation();
        textPregnancyId.setText(getOid());

        childSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                childName = String.valueOf(item);
                childId = childIdList.get(position);
            }
        });

        fatherSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                husbandName = fatherNameList.get(position);
                wifeName = motherNameList.get(position);
                eligibleCoupleId = coupleIdList.get(position);
                motherSpinner.setSelectedIndex(position);
            }
        });

        motherSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                husbandName = fatherNameList.get(position);
                wifeName = motherNameList.get(position);
                eligibleCoupleId = coupleIdList.get(position);
                fatherSpinner.setSelectedIndex(position);
            }
        });

        reqChildSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                textPregnancyNumber.setText(String.valueOf(position+1));
                textChildInfo.setText("Child "+String.valueOf(position+1)+" Details");
                showProgressBar("Loading Details. Please wait...");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        checkForPreviousChildInformation();
                    }
                }, 1000);
            }
        });

        btnAddChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (awesomeValidation.validate()) {
                    saveInformation(true);
                }
            }
        });

        btnDeleteMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteMember(Integer.parseInt(textPregnancyNumber.getText().toString().trim()));
            }
        });

        btnAddChildDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadPage(0);
                relChildLayout.setVisibility(View.VISIBLE);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (awesomeValidation.validate()) {
                    saveInformation(false);
                }
            }
        });
        btnUploadDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callApiToInsertData();
            }
        });
        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        FragmentTransaction fa = getFragmentManager().beginTransaction();
                        fa.replace(R.id.fragment_container, new FragmentCreatePregnantDetails());
                        fa.commit();
                        return true;
                    }
                }
                return false;
            }
        });
        return v;
    }

    private void saveInformation(boolean type) {
        rchId = Integer.parseInt(editRCH.getText().toString().trim());
        String objId = textPregnancyId.getText().toString().trim();
        Vaccination vaccination = new Vaccination();
        opvList.clear();
        b2vitList.clear();
        bcgList.clear();
        penta1List.clear();
        penta2List.clear();
        penta3List.clear();
        vita1List.clear();
        vita2List.clear();
        if (opvId != 0) {
            for (int i=0; i<opvId; i++) {
                TextInputEditText editText = (TextInputEditText) opvLayout.findViewById(i);
                if (editText.getText().toString().trim().length() != 0) {
                    opvList.add(editText.getText().toString().trim());
                }
            }
            if (opvList.size() > 0) {
                vaccination.setOPV(opvList);
            }
        }
        if (b2vitId != 0) {
            for (int i=0; i<b2vitId; i++) {
                TextInputEditText editText = (TextInputEditText) b2vitLayout.findViewById(i);
                if (editText.getText().toString().trim().length() != 0) {
                    b2vitList.add(editText.getText().toString().trim());
                }
            }
            if (b2vitList.size() > 0) {
                vaccination.setB2VIT(b2vitList);
            }
        }
        if (bcgId != 0) {
            for (int i=0; i<bcgId; i++) {
                TextInputEditText editText = (TextInputEditText) bcgLayout.findViewById(i);
                if (editText.getText().toString().trim().length() != 0) {
                    bcgList.add(editText.getText().toString().trim());
                }
            }
            if (bcgList.size() > 0) {
                vaccination.setBCG(bcgList);
            }
        }
        if (penta1Id != 0) {
            for (int i=0; i<penta1Id; i++) {
                TextInputEditText editText = (TextInputEditText) penta1Layout.findViewById(i);
                if (editText.getText().toString().trim().length() != 0) {
                    penta1List.add(editText.getText().toString().trim());
                }
            }
            if (penta1List.size() > 0) {
                vaccination.setOPV1_IPV1_Penta1_Rota1(penta1List);
            }
        }
        if (penta2Id != 0) {
            for (int i=0; i<penta2Id; i++) {
                TextInputEditText editText = (TextInputEditText) penta2Layout.findViewById(i);
                if (editText.getText().toString().trim().length() != 0) {
                    penta2List.add(editText.getText().toString().trim());
                }
            }
            if (penta2List.size() > 0) {
                vaccination.setOPV_2_Penta_2_Rota_2(penta2List);
            }
        }
        if (penta3Id != 0) {
            for (int i=0; i<penta3Id; i++) {
                TextInputEditText editText = (TextInputEditText) penta3Layout.findViewById(i);
                if (editText.getText().toString().trim().length() != 0) {
                    penta3List.add(editText.getText().toString().trim());
                }
            }
            if (penta3List.size() > 0) {
                vaccination.setOPV_3_IPV2_Penta3_Rota3(penta3List);
            }
        }
        if (vita1Id != 0) {
            for (int i=0; i<vita1Id; i++) {
                TextInputEditText editText = (TextInputEditText) vita1Layout.findViewById(i);
                if (editText.getText().toString().trim().length() != 0) {
                    vita1List.add(editText.getText().toString().trim());
                }
            }
            if (vita1List.size() > 0) {
                vaccination.setMR1_VitA1(vita1List);
            }
        }
        if (vita2Id != 0) {
            for (int i=0; i<vita2Id; i++) {
                TextInputEditText editText = (TextInputEditText) vita2layout.findViewById(i);
                if (editText.getText().toString().trim().length() != 0) {
                    vita2List.add(editText.getText().toString().trim());
                }
            }
            if (vita2List.size() > 0) {
                vaccination.setDPTB_OPVB_MR2_VitA2(vita2List);
            }
        }
        ChildDetails childDetails = new ChildDetails(objId, eligibleCoupleId, coupleId, childName
                , childId, String.valueOf(rchId), vaccination);
        storeInArrayList(childDetails, type);
    }

    private void storeInArrayList(ChildDetails childDetails, boolean type) {
        int cnt = Integer.parseInt(textPregnancyNumber.getText().toString().trim());
        if (finalFlag) {
            childDetailsArrayList.remove(cnt-1);
            childDetailsArrayList.add(cnt-1, childDetails);
            finalFlag = false;
        } else {
            childDetailsArrayList.add(cnt-1, childDetails);
            finalFlag = false;
        }
        changeFragment(childDetailsArrayList, "Saving child "+cnt+" details. Please wait...", cnt
                , type);
    }

    private void changeFragment(final ArrayList<ChildDetails> childDetailsArrayList,
                                String content, final int memCnt, boolean type) {
        Gson gson = new Gson();
        String json = gson.toJson(childDetailsArrayList);
        if (type) {
            showProgressBar(content);
            databaseHelper.saveChildDetails(json);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    stopProgressBar();
                    if (prevFlag) {
                        int cnt = childDetailsArrayList.size();
                        reloadPage(cnt);
                    } else {
                        reloadPage(memCnt);
                    }
                    checkForPreviousChildInformation();
                }
            }, 1000);
        } else {
            databaseHelper.saveChildDetails(json);
            showProgressBar(content);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    stopProgressBar();
                    callApiToInsertData();
                }
            }, 1000);
        }
    }

    private void callApiToInsertData() {
        GeneralInfo generalInfo = new GeneralInfo(sessionManager.getToilet(), sessionManager.getHouse(), sessionManager.getWater(), sessionManager.getCaste(), sessionManager.getBPL());
        ArrayList<FamilyMemberList> famListFinal = new ArrayList<FamilyMemberList>();
        ArrayList<CoupleDetails> coupListFinal = new ArrayList<CoupleDetails>();
        ArrayList<PregnantDetails> pregListFinal = new ArrayList<PregnantDetails>();
        ArrayList<ChildDetails> childListFinal = new ArrayList<ChildDetails>();
        final Gson gson = new Gson();
        if (databaseHelper.getMemberDetails() != null) {
            if (!databaseHelper.getMemberDetails().equals("")) {
                famListFinal = gson.fromJson(databaseHelper.getMemberDetails(),
                        new TypeToken<ArrayList<FamilyMemberList>>(){}.getType());
            }
        }
        if (databaseHelper.getCoupleDetails() != null) {
            if (!databaseHelper.getCoupleDetails().equals("")) {
                coupListFinal = gson.fromJson(databaseHelper.getCoupleDetails(),
                        new TypeToken<ArrayList<CoupleDetails>>(){}.getType());
            }
        }
        if (databaseHelper.getPregnantDetails() != null) {
            if (!databaseHelper.getPregnantDetails().equals("")) {
                pregListFinal = gson.fromJson(databaseHelper.getPregnantDetails(),
                        new TypeToken<ArrayList<PregnantDetails>>(){}.getType());
            }
        }
        if (databaseHelper.getChildDetails() != null) {
            if (!databaseHelper.getChildDetails().equals("")) {
                childListFinal = gson.fromJson(databaseHelper.getChildDetails(),
                        new TypeToken<ArrayList<ChildDetails>>(){}.getType());
            }
        }
        CreateMemberInput createMemberInput = new CreateMemberInput("5ec21f574f9f5c7ae8a4b43c",
                sessionManager.getAreaName(), generalInfo, coupListFinal, childListFinal,
                pregListFinal, famListFinal);
        CreateMemberPojo createMemberPojo = new CreateMemberPojo(createMemberInput);
        Log.i("FRAGMENTCHILD", gson.toJson(createMemberPojo));
        showProgressBar("Adding family. Please wait...");
        CreateQuery createQuery = new CreateQuery();
        String query = "mutation($input: AutogeneratedMainInput) { createFamily(input: $input) { " +
                "_id}}";
        createQuery.setQuery(query);
        createQuery.setVariables(createMemberPojo);
        Call<Family> call = apiInterface.createFamily(createQuery);
        call.enqueue(new Callback<Family>() {
            @Override
            public void onResponse(Call<Family> call, Response<Family> response) {
                String resp = gson.toJson(response.body());
                Log.i("FRAGMENTCHILD", resp);
                stopProgressBar();
                if (response.code() == 200) {
//                    Toast.makeText(v.getContext(),
//                            response.body().getData().getCreateFamily().getId().toString(),
//                            Toast.LENGTH_SHORT).show();
                    ShowAlertDialog(response.code(), "कुटुंब यशस्वीरित्या जोडले");
                } else if (response.code() == 400) {
                    ShowAlertDialog(response.code(), "क्वेरीमध्ये काहीतरी चूक झाली");
//                    Toast.makeText(v.getContext(), "Something wrong in query", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 500) {
                    ShowAlertDialog(response.code(),
                            "काहीतरी चूक झाली");
//                    Toast.makeText(v.getContext(), "Server side error.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Family> call, Throwable t) {
                stopProgressBar();
                ShowAlertDialog(0, "कृपया आपले इंटरनेट कनेक्शन तपासा!");
                Log.i("FRAGMENTCHILD", "onFailure: "+t.toString());
            }
        });
    }

    private void ShowAlertDialog(final int status, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setMessage(message)
                .setTitle("Message")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (status == 200) {
                            sessionManager.clearAllInformation();
                            databaseHelper.deleteAllData();
                            FragmentTransaction fragmentTransaction =
                                    getFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.fragment_container, new FragmentGenInfo());
                            fragmentTransaction.commit();
                        }
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void checkForPreviousChildInformation() {
        if (databaseHelper.getChildCount() > 0) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<ChildDetails>>(){}.getType();
            if (databaseHelper.getChildDetails() == null) {
                reloadPage(0);
                prevFlag = false;
                btnAddChildDetail.setVisibility(View.VISIBLE);
                relChildLayout.setVisibility(View.GONE);
                textPreviousChildText.setVisibility(View.GONE);
                reqChildSpinner.setVisibility(View.GONE);
                btnUploadDetail.setVisibility(View.VISIBLE);
            } else {
                if (databaseHelper.getChildDetails().equals("")) {
                    reloadPage(0);
                    prevFlag = false;
                    btnAddChildDetail.setVisibility(View.VISIBLE);
                    relChildLayout.setVisibility(View.GONE);
                    textPreviousChildText.setVisibility(View.GONE);
                    reqChildSpinner.setVisibility(View.GONE);
                    btnUploadDetail.setVisibility(View.VISIBLE);
                } else {
                    Log.i("FRAGMENTCHILD", databaseHelper.getChildDetails());
                    childDetailsArrayList = gson.fromJson(databaseHelper.getChildDetails(),
                            type);
                    prevFlag = true;
                    populateChildDetails(childDetailsArrayList);
                    btnAddChildDetail.setVisibility(View.GONE);
                    relChildLayout.setVisibility(View.VISIBLE);
                    textPreviousChildText.setVisibility(View.VISIBLE);
                    reqChildSpinner.setVisibility(View.VISIBLE);
                    btnUploadDetail.setVisibility(View.GONE);
                }
            }
        } else {
            prevFlag = false;
            reloadPage(0);
            finalFlag = false;
            btnAddChildDetail.setVisibility(View.VISIBLE);
            relChildLayout.setVisibility(View.GONE);
            textPreviousChildText.setVisibility(View.GONE);
            reqChildSpinner.setVisibility(View.GONE);
            btnUploadDetail.setVisibility(View.VISIBLE);
        }
    }
    private void populateChildDetails(ArrayList<ChildDetails> childDetailsArrayList) {
        childScrollView.scrollTo(0,0);
        if (childDetailsArrayList.size() > 0) {
            int count = Integer.parseInt(textPregnancyNumber.getText().toString().trim());
            prevChildNameList.clear();
            prevChildIdList.clear();
            for (int i=0; i<childDetailsArrayList.size(); i++) {
                for (int j=0; j<familyMemberListArrayList.size(); j++) {
                    if (childDetailsArrayList.get(i).getChildId().equals(familyMemberListArrayList.get(j).getMemberid())) {
                        childDetailsArrayList.get(i).setChildname(familyMemberListArrayList.get(j).getName());
                    }
                }
            }
            for (int i=0; i<childDetailsArrayList.size(); i++) {
                prevChildNameList.add(childDetailsArrayList.get(i).getChildname());
                prevChildIdList.add(childDetailsArrayList.get(i).getChildId());
            }
            reqChildSpinner.setItems(prevChildNameList);
            if (prevChildNameList.size() <= count-1) {

            } else {
                reqChildSpinner.setSelectedIndex(count-1);
            }
            if (childDetailsArrayList.size() >= count) {
                opvId = 0;
                b2vitId = 0;
                bcgId = 0;
                penta1Id = 0;
                penta2Id = 0;
                penta3Id = 0;
                vita1Id = 0;
                vita2Id = 0;
                opvLayout.removeAllViews();
                b2vitLayout.removeAllViews();
                bcgLayout.removeAllViews();
                penta1Layout.removeAllViews();
                penta2Layout.removeAllViews();
                penta3Layout.removeAllViews();
                vita1Layout.removeAllViews();
                vita2layout.removeAllViews();
                textPregnancyId.setText(childDetailsArrayList.get(count-1).getChildrenObjectId());
                finalFlag = true;
                btnDeleteMember.setVisibility(View.VISIBLE);
                int pos = -1;
                pos = childIdList.indexOf(prevChildIdList.get(count-1));
                if (pos == -1) {

                } else {
                    childSpinner.setSelectedIndex(pos);
                    childName = childNameList.get(pos);
                    childId = childIdList.get(pos);
                }
                pos = -1;
                pos = coupleIdList.indexOf(childDetailsArrayList.get(count-1));
                if (pos == -1) {

                } else {
                    fatherSpinner.setSelectedIndex(pos);
                    motherSpinner.setSelectedIndex(pos);
                    husbandName = fatherNameList.get(pos);
                    wifeName = motherNameList.get(pos);
                    eligibleCoupleId = coupleIdList.get(pos);
                }
                coupleId = childDetailsArrayList.get(count-1).getCoupleid();
                editRCH.setText(String.valueOf(childDetailsArrayList.get(count-1).getChildRCH()));
                if (childDetailsArrayList.get(count-1).getVaccination().getOPV() != null) {
                    for (int i=0; i<childDetailsArrayList.get(count-1).getVaccination().getOPV().size(); i++) {
                        layoutInflater.inflate(R.layout.familydetail_vaccinationdate, opvLayout, true).findViewById(R.id.editVaccinationDate).setId(opvId);
                        final TextInputEditText textInputEditText =
                                ((TextInputEditText) opvLayout.findViewById(opvId));

                        ((TextInputEditText) opvLayout.findViewById(opvId)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                        String date = dayOfMonth+"/"+(month+1)+"/"+year;
                                        textInputEditText.setText(date);
                                    }
                                };
                                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(),
                                        R.style.Theme_MaterialComponents_Light_Dialog_MinWidth,
                                        onDateSetListener, year, month, day);
                                datePickerDialog.getWindow();
                                datePickerDialog.show();
                            }
                        });
                        ((TextInputEditText) opvLayout.findViewById(opvId)).setHint("Enter date "+String.valueOf(opvId-99));
                        ((TextInputEditText) opvLayout.findViewById(opvId)).setText(childDetailsArrayList.get(count-1).getVaccination().getOPV().get(i));
                        opvId++;
                    }
                }

                if (childDetailsArrayList.get(count-1).getVaccination().getB2VIT() != null) {
                    for (int i=0; i<childDetailsArrayList.get(count-1).getVaccination().getB2VIT().size(); i++) {
                        layoutInflater.inflate(R.layout.familydetail_vaccinationdate, b2vitLayout, true).findViewById(R.id.editVaccinationDate).setId(b2vitId);
                        final TextInputEditText textInputEditText =
                                ((TextInputEditText) b2vitLayout.findViewById(b2vitId));

                        ((TextInputEditText) b2vitLayout.findViewById(b2vitId)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                        String date = dayOfMonth+"/"+(month+1)+"/"+year;
                                        textInputEditText.setText(date);
                                    }
                                };
                                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(),
                                        R.style.Theme_MaterialComponents_Light_Dialog_MinWidth,
                                        onDateSetListener, year, month, day);
                                datePickerDialog.getWindow();
                                datePickerDialog.show();
                            }
                        });
                        ((TextInputEditText) b2vitLayout.findViewById(b2vitId)).setHint("Enter date "+String.valueOf(b2vitId-199));
                        ((TextInputEditText) b2vitLayout.findViewById(b2vitId)).setText(childDetailsArrayList.get(count-1).getVaccination().getB2VIT().get(i));
                        b2vitId++;
                    }
                }

                if (childDetailsArrayList.get(count-1).getVaccination().getBCG() != null) {
                    for (int i=0; i<childDetailsArrayList.get(count-1).getVaccination().getBCG().size(); i++) {
                        layoutInflater.inflate(R.layout.familydetail_vaccinationdate, bcgLayout, true).findViewById(R.id.editVaccinationDate).setId(bcgId);
                        final TextInputEditText textInputEditText =
                                ((TextInputEditText) bcgLayout.findViewById(bcgId));

                        ((TextInputEditText) bcgLayout.findViewById(bcgId)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                        String date = dayOfMonth+"/"+(month+1)+"/"+year;
                                        textInputEditText.setText(date);
                                    }
                                };
                                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(),
                                        R.style.Theme_MaterialComponents_Light_Dialog_MinWidth,
                                        onDateSetListener, year, month, day);
                                datePickerDialog.getWindow();
                                datePickerDialog.show();
                            }
                        });
                        ((TextInputEditText) bcgLayout.findViewById(bcgId)).setHint("Enter date "+String.valueOf(bcgId-299));
                        ((TextInputEditText) bcgLayout.findViewById(bcgId)).setText(childDetailsArrayList.get(count-1).getVaccination().getBCG().get(i));
                        bcgId++;
                    }
                }

                if (childDetailsArrayList.get(count-1).getVaccination().getMR1_VitA1() != null) {
                    for (int i=0; i<childDetailsArrayList.get(count-1).getVaccination().getMR1_VitA1().size(); i++) {
                        layoutInflater.inflate(R.layout.familydetail_vaccinationdate, vita1Layout
                                , true).findViewById(R.id.editVaccinationDate).setId(vita1Id);
                        final TextInputEditText textInputEditText =
                                ((TextInputEditText) vita1Layout.findViewById(vita1Id));

                        ((TextInputEditText) vita1Layout.findViewById(vita1Id)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                        String date = dayOfMonth+"/"+(month+1)+"/"+year;
                                        textInputEditText.setText(date);
                                    }
                                };
                                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(),
                                        R.style.Theme_MaterialComponents_Light_Dialog_MinWidth,
                                        onDateSetListener, year, month, day);
                                datePickerDialog.getWindow();
                                datePickerDialog.show();
                            }
                        });
                        ((TextInputEditText) vita1Layout.findViewById(vita1Id)).setHint("Enter " +
                                "date "+String.valueOf(vita1Id-699));
                        ((TextInputEditText) vita1Layout.findViewById(vita1Id)).setText(childDetailsArrayList.get(count-1).getVaccination().getMR1_VitA1().get(i));
                        vita1Id++;
                    }
                }

                if (childDetailsArrayList.get(count-1).getVaccination().getDPTB_OPVB_MR2_VitA2() != null) {
                    for (int i=0; i<childDetailsArrayList.get(count-1).getVaccination().getDPTB_OPVB_MR2_VitA2().size(); i++) {
                        layoutInflater.inflate(R.layout.familydetail_vaccinationdate, vita2layout
                                , true).findViewById(R.id.editVaccinationDate).setId(vita2Id);
                        final TextInputEditText textInputEditText =
                                ((TextInputEditText) vita2layout.findViewById(vita2Id));

                        ((TextInputEditText) vita2layout.findViewById(vita2Id)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                        String date = dayOfMonth+"/"+(month+1)+"/"+year;
                                        textInputEditText.setText(date);
                                    }
                                };
                                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(),
                                        R.style.Theme_MaterialComponents_Light_Dialog_MinWidth,
                                        onDateSetListener, year, month, day);
                                datePickerDialog.getWindow();
                                datePickerDialog.show();
                            }
                        });
                        ((TextInputEditText) vita2layout.findViewById(vita2Id)).setHint("Enter " +
                                "date "+String.valueOf(vita2Id-699));
                        ((TextInputEditText) vita2layout.findViewById(vita2Id)).setText(childDetailsArrayList.get(count-1).getVaccination().getDPTB_OPVB_MR2_VitA2().get(i));
                        vita2Id++;
                    }
                }

                if (childDetailsArrayList.get(count-1).getVaccination().getOPV1_IPV1_Penta1_Rota1() != null) {
                    for(int i=0; i<childDetailsArrayList.get(count-1).getVaccination().getOPV1_IPV1_Penta1_Rota1().size(); i++) {
                        layoutInflater.inflate(R.layout.familydetail_vaccinationdate, penta1Layout, true).findViewById(R.id.editVaccinationDate).setId(penta1Id);
                        final TextInputEditText textInputEditText =
                                ((TextInputEditText) penta1Layout.findViewById(penta1Id));

                        ((TextInputEditText) penta1Layout.findViewById(penta1Id)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                        String date = dayOfMonth+"/"+(month+1)+"/"+year;
                                        textInputEditText.setText(date);
                                    }
                                };
                                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(),
                                        R.style.Theme_MaterialComponents_Light_Dialog_MinWidth,
                                        onDateSetListener, year, month, day);
                                datePickerDialog.getWindow();
                                datePickerDialog.show();
                            }
                        });
                        ((TextInputEditText) penta1Layout.findViewById(penta1Id)).setHint("Enter date "+String.valueOf(penta1Id-399));
                        ((TextInputEditText) penta1Layout.findViewById(penta1Id)).setText(childDetailsArrayList.get(count-1).getVaccination().getOPV1_IPV1_Penta1_Rota1().get(i));
                        penta1Id++;
                    }
                }

                if (childDetailsArrayList.get(count-1).getVaccination().getOPV_2_Penta_2_Rota_2() != null) {
                    for(int i=0; i<childDetailsArrayList.get(count-1).getVaccination().getOPV_2_Penta_2_Rota_2().size(); i++) {
                        layoutInflater.inflate(R.layout.familydetail_vaccinationdate, penta2Layout,
                                true).findViewById(R.id.editVaccinationDate).setId(penta2Id);
                        final TextInputEditText textInputEditText =
                                ((TextInputEditText) penta2Layout.findViewById(penta2Id));
                        ((TextInputEditText) penta2Layout.findViewById(penta2Id)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                        String date = dayOfMonth+"/"+(month+1)+"/"+year;
                                        textInputEditText.setText(date);
                                    }
                                };
                                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(),
                                        R.style.Theme_MaterialComponents_Light_Dialog_MinWidth,
                                        onDateSetListener, year, month, day);
                                datePickerDialog.getWindow();
                                datePickerDialog.show();
                            }
                        });
                        ((TextInputEditText) penta2Layout.findViewById(penta2Id)).setHint("Enter " +
                                "date "+String.valueOf(penta2Id-499));
                        ((TextInputEditText) penta2Layout.findViewById(penta2Id)).setText(childDetailsArrayList.get(count-1).getVaccination().getOPV_2_Penta_2_Rota_2().get(i));
                        penta2Id++;
                    }
                }

                if (childDetailsArrayList.get(count-1).getVaccination().getOPV_3_IPV2_Penta3_Rota3() != null) {
                    for (int i=0; i<childDetailsArrayList.get(count-1).getVaccination().getOPV_3_IPV2_Penta3_Rota3().size(); i++) {
                        layoutInflater.inflate(R.layout.familydetail_vaccinationdate, penta3Layout,
                                true).findViewById(R.id.editVaccinationDate).setId(penta3Id);
                        final TextInputEditText textInputEditText =
                                ((TextInputEditText) penta3Layout.findViewById(penta3Id));
                        ((TextInputEditText) penta3Layout.findViewById(penta3Id)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                        String date = dayOfMonth+"/"+(month+1)+"/"+year;
                                        textInputEditText.setText(date);
                                    }
                                };
                                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(),
                                        R.style.Theme_MaterialComponents_Light_Dialog_MinWidth,
                                        onDateSetListener, year, month, day);
                                datePickerDialog.getWindow();
                                datePickerDialog.show();
                            }
                        });
                        ((TextInputEditText) penta3Layout.findViewById(penta3Id)).setHint("Enter " +
                                "date "+String.valueOf(penta3Id-599));
                        ((TextInputEditText) penta3Layout.findViewById(penta3Id)).setText(childDetailsArrayList.get(count-1).getVaccination().getOPV_3_IPV2_Penta3_Rota3().get(i));
                        penta3Id++;
                    }
                }
            } else {
                finalFlag = false;
            }
        } else {
            reqChildSpinner.setItems();
            finalFlag = false;
            btnAddChildDetail.setVisibility(View.VISIBLE);
            relChildLayout.setVisibility(View.GONE);
            textPreviousChildText.setVisibility(View.GONE);
            reqChildSpinner.setVisibility(View.GONE);
            btnUploadDetail.setVisibility(View.VISIBLE);
        }
    }

    private void deleteMember(final int number) {
        AlertDialog alertDialog = new AlertDialog.Builder(v.getContext())
                .setTitle("Alert")
                .setMessage("Do you want to delete this pregnancy entry ?")
                .setIcon(R.drawable.ic_baseline_warning_24)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        String json = "";
                        if (childDetailsArrayList.size() == 1) {
                            coupleDetailsArrayList.remove(number-1);
                        } else {
                            childDetailsArrayList.remove(number-1);
                            Gson gson = new Gson();
                            json = gson.toJson(childDetailsArrayList);
                        }
                        final String js = json;
                        showProgressBar("Deleting pregnancy detail. Please wait...");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                databaseHelper.saveChildDetails(js);
                                stopProgressBar();
                                textPregnancyNumber.setText("1");
                                textChildInfo.setText("Child " + "1" + " Details");
                                checkForPreviousChildInformation();
                                childScrollView.scrollTo(0, 0);
                            }
                        }, 500);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        alertDialog.show();
    }

    private void reloadPage(int num) {
        childScrollView.scrollTo(0, 0);
        textChildInfo.setText("Child "+String.valueOf(num+1)+" Details");
        textPregnancyNumber.setText(String.valueOf(num+1));
        textPregnancyId.setText(getOid());
        editRCH.setText("");
        editRCH.clearFocus();
        opvId = 0;
        b2vitId = 0;
        bcgId = 0;
        penta1Id = 0;
        penta2Id = 0;
        penta3Id = 0;
        vita1Id = 0;
        vita2Id = 0;
        opvLayout.removeAllViews();
        b2vitLayout.removeAllViews();
        bcgLayout.removeAllViews();
        penta1Layout.removeAllViews();
        penta2Layout.removeAllViews();
        penta3Layout.removeAllViews();
        vita1Layout.removeAllViews();
        vita2layout.removeAllViews();
        btnDeleteMember.setVisibility(View.GONE);
        getPreviousInformation();
        getPreviousCoupleInformation();
    }

    private void getPreviousInformation() {
        if (databaseHelper.getMemberCount() > 0) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<FamilyMemberList>>(){}.getType();
            familyMemberListArrayList = gson.fromJson(databaseHelper.getMemberDetails(), type);
            populateData(familyMemberListArrayList);
        }
    }
    private void populateData(ArrayList<FamilyMemberList> familyMemberListArrayList) {
        if (familyMemberListArrayList.size() > 0) {
            childNameList.clear();
            childIdList.clear();
            for (int i=0; i<familyMemberListArrayList.size(); i++) {
                childNameList.add(familyMemberListArrayList.get(i).getName());
                childIdList.add(familyMemberListArrayList.get(i).getMemberid());
            }
            childName = childNameList.get(0);
            childId = childIdList.get(0);
            childSpinner.setItems(childNameList);
        }
    }

    private void getPreviousCoupleInformation() {
        if (databaseHelper.getCoupleCount() > 0) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<CoupleDetails>>(){}.getType();
            if (databaseHelper.getCoupleDetails() == null) {
//                reloadPage(0);
//                prevFlag = false;
//                btnAddCoup.setVisibility(View.VISIBLE);
//                relCoupleLayout.setVisibility(View.GONE);
            } else {
                if (databaseHelper.getCoupleDetails().equals("")) {
//                    reloadPage(0);
//                    prevFlag = false;
//                    regHusbandSpinner.setVisibility(View.GONE);
//                    regWifeSpinner.setVisibility(View.GONE);
//                    btnAddCoup.setVisibility(View.VISIBLE);
//                    relCoupleLayout.setVisibility(View.GONE);
                } else {
                    Log.i("FRAGMENTCOUPLE", "checkForPreviousCouple: "+databaseHelper.getCoupleDetails());
                    coupleDetailsArrayList =
                            gson.fromJson(databaseHelper.getCoupleDetails(), type);
//                    prevFlag = true;
                    populateCoupleDetails(coupleDetailsArrayList);
                }
            }
        } else {
//            prevFlag = false;
//            reloadPage(0);
//            finalFlag = false;
//            btnAddCoup.setVisibility(View.VISIBLE);
//            relCoupleLayout.setVisibility(View.GONE);
        }
    }
    private void populateCoupleDetails(ArrayList<CoupleDetails> coupleDetailsArrayList) {
        if (coupleDetailsArrayList.size() > 0) {
            motherNameList.clear();
            fatherNameList.clear();
            coupleIdList.clear();
            for (int i=0; i<coupleDetailsArrayList.size(); i++) {
                motherNameList.add(coupleDetailsArrayList.get(i).getWife());
                fatherNameList.add(coupleDetailsArrayList.get(i).getHusband());
                coupleIdList.add(coupleDetailsArrayList.get(i).getEligibleCoupleNameId());
                chIdList.add(coupleDetailsArrayList.get(i).getCoupleid());
            }
            husbandName = fatherNameList.get(0);
            wifeName = motherNameList.get(0);
            coupleId = chIdList.get(0);
            eligibleCoupleId = coupleIdList.get(0);
            fatherSpinner.setItems(fatherNameList);
            motherSpinner.setItems(motherNameList);
        }
    }

    private void addListenersToComponents() {
        btnOpv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutInflater.inflate(R.layout.familydetail_vaccinationdate, opvLayout, true).findViewById(R.id.editVaccinationDate).setId(opvId);
                final TextInputEditText textInputEditText =
                        ((TextInputEditText) opvLayout.findViewById(opvId));

                ((TextInputEditText) opvLayout.findViewById(opvId)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                String date = dayOfMonth+"/"+(month+1)+"/"+year;
                                textInputEditText.setText(date);
                            }
                        };
                        DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(),
                                R.style.Theme_MaterialComponents_Light_Dialog_MinWidth,
                                onDateSetListener, year, month, day);
                        datePickerDialog.getWindow();
                        datePickerDialog.show();
                    }
                });
                ((TextInputEditText) opvLayout.findViewById(opvId)).setHint("Enter date "+String.valueOf(opvId+1));
                opvId++;
            }
        });

        btnB2vit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutInflater.inflate(R.layout.familydetail_vaccinationdate, b2vitLayout, true).findViewById(R.id.editVaccinationDate).setId(b2vitId);
                final TextInputEditText textInputEditText =
                        ((TextInputEditText) b2vitLayout.findViewById(b2vitId));

                ((TextInputEditText) b2vitLayout.findViewById(b2vitId)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                String date = dayOfMonth+"/"+(month+1)+"/"+year;
                                textInputEditText.setText(date);
                            }
                        };
                        DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(),
                                R.style.Theme_MaterialComponents_Light_Dialog_MinWidth,
                                onDateSetListener, year, month, day);
                        datePickerDialog.getWindow();
                        datePickerDialog.show();
                    }
                });
                ((TextInputEditText) b2vitLayout.findViewById(b2vitId)).setHint("Enter date "+String.valueOf(b2vitId+1));
                b2vitId++;
            }
        });

        btnBcg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutInflater.inflate(R.layout.familydetail_vaccinationdate, bcgLayout, true).findViewById(R.id.editVaccinationDate).setId(bcgId);
                final TextInputEditText textInputEditText =
                        ((TextInputEditText) bcgLayout.findViewById(bcgId));

                ((TextInputEditText) bcgLayout.findViewById(bcgId)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                String date = dayOfMonth+"/"+(month+1)+"/"+year;
                                textInputEditText.setText(date);
                            }
                        };
                        DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(),
                                R.style.Theme_MaterialComponents_Light_Dialog_MinWidth,
                                onDateSetListener, year, month, day);
                        datePickerDialog.getWindow();
                        datePickerDialog.show();
                    }
                });
                ((TextInputEditText) bcgLayout.findViewById(bcgId)).setHint("Enter date "+String.valueOf(bcgId+1));
                bcgId++;
            }
        });

        btnPenta1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutInflater.inflate(R.layout.familydetail_vaccinationdate, penta1Layout, true).findViewById(R.id.editVaccinationDate).setId(penta1Id);
                final TextInputEditText textInputEditText =
                        ((TextInputEditText) penta1Layout.findViewById(penta1Id));

                ((TextInputEditText) penta1Layout.findViewById(penta1Id)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                String date = dayOfMonth+"/"+(month+1)+"/"+year;
                                textInputEditText.setText(date);
                            }
                        };
                        DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(),
                                R.style.Theme_MaterialComponents_Light_Dialog_MinWidth,
                                onDateSetListener, year, month, day);
                        datePickerDialog.getWindow();
                        datePickerDialog.show();
                    }
                });
                ((TextInputEditText) penta1Layout.findViewById(penta1Id)).setHint("Enter date "+String.valueOf(penta1Id+1));
                penta1Id++;
            }
        });

        btnPenta2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutInflater.inflate(R.layout.familydetail_vaccinationdate, penta2Layout, true).findViewById(R.id.editVaccinationDate).setId(penta2Id);
                final TextInputEditText textInputEditText =
                        ((TextInputEditText) penta2Layout.findViewById(penta2Id));

                ((TextInputEditText) penta2Layout.findViewById(penta2Id)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                String date = dayOfMonth+"/"+(month+1)+"/"+year;
                                textInputEditText.setText(date);
                            }
                        };
                        DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(),
                                R.style.Theme_MaterialComponents_Light_Dialog_MinWidth,
                                onDateSetListener, year, month, day);
                        datePickerDialog.getWindow();
                        datePickerDialog.show();
                    }
                });
                ((TextInputEditText) penta2Layout.findViewById(penta2Id)).setHint("Enter date "+String.valueOf(penta2Id+1));
                penta2Id++;
            }
        });

        btnPenta3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutInflater.inflate(R.layout.familydetail_vaccinationdate, penta3Layout, true).findViewById(R.id.editVaccinationDate).setId(penta3Id);
                final TextInputEditText textInputEditText =
                        ((TextInputEditText) penta3Layout.findViewById(penta3Id));

                ((TextInputEditText) penta3Layout.findViewById(penta3Id)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                String date = dayOfMonth+"/"+(month+1)+"/"+year;
                                textInputEditText.setText(date);
                            }
                        };
                        DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(),
                                R.style.Theme_MaterialComponents_Light_Dialog_MinWidth,
                                onDateSetListener, year, month, day);
                        datePickerDialog.getWindow();
                        datePickerDialog.show();
                    }
                });
                ((TextInputEditText) penta3Layout.findViewById(penta3Id)).setHint("Enter date "+String.valueOf(penta3Id+1));
                penta3Id++;
            }
        });

        btnVita1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutInflater.inflate(R.layout.familydetail_vaccinationdate, vita1Layout, true).findViewById(R.id.editVaccinationDate).setId(vita1Id);
                final TextInputEditText textInputEditText =
                        ((TextInputEditText) vita1Layout.findViewById(vita1Id));

                ((TextInputEditText) vita1Layout.findViewById(vita1Id)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                String date = dayOfMonth+"/"+(month+1)+"/"+year;
                                textInputEditText.setText(date);
                            }
                        };
                        DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(),
                                R.style.Theme_MaterialComponents_Light_Dialog_MinWidth,
                                onDateSetListener, year, month, day);
                        datePickerDialog.getWindow();
                        datePickerDialog.show();
                    }
                });
                ((TextInputEditText) vita1Layout.findViewById(vita1Id)).setHint("Enter date "+String.valueOf(vita1Id+1));
                vita1Id++;
            }
        });

        btnVita2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutInflater.inflate(R.layout.familydetail_vaccinationdate, vita2layout, true).findViewById(R.id.editVaccinationDate).setId(vita2Id);
                final TextInputEditText textInputEditText =
                        ((TextInputEditText) vita2layout.findViewById(vita2Id));

                ((TextInputEditText) vita2layout.findViewById(vita2Id)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                String date = dayOfMonth+"/"+(month+1)+"/"+year;
                                textInputEditText.setText(date);
                            }
                        };
                        DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(),
                                R.style.Theme_MaterialComponents_Light_Dialog_MinWidth,
                                onDateSetListener, year, month, day);
                        datePickerDialog.getWindow();
                        datePickerDialog.show();
                    }
                });
                ((TextInputEditText) vita2layout.findViewById(vita2Id)).setHint("Enter date "+String.valueOf(vita2Id+1));
                vita2Id++;
            }
        });
    }
    private void initApp() {
        //Calendar
        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

        //Managers
        sessionManager = new SessionManager(v.getContext());
        databaseHelper = new DatabaseHelper(v.getContext());
        progressDialog = new ProgressDialog(v.getContext());

        //apiclient
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        //Arraylist
        opvList = new ArrayList<String>();
        b2vitList = new ArrayList<String>();
        bcgList = new ArrayList<String>();
        penta1List = new ArrayList<String>();
        penta2List = new ArrayList<String>();
        penta3List = new ArrayList<String>();
        vita1List = new ArrayList<String>();
        vita2List = new ArrayList<String>();
        childNameList = new ArrayList<String>();
        childIdList = new ArrayList<String>();
        prevChildNameList = new ArrayList<String>();
        prevChildIdList = new ArrayList<String>();
        motherNameList = new ArrayList<String>();
        fatherNameList = new ArrayList<String>();
        coupleIdList = new ArrayList<String>();
        familyMemberListArrayList = new ArrayList<FamilyMemberList>();
        coupleDetailsArrayList = new ArrayList<CoupleDetails>();
        chIdList = new ArrayList<Integer>();
        childDetailsArrayList = new ArrayList<ChildDetails>();

        //Layout
        opvLayout = (LinearLayout) v.findViewById(R.id.opvLayout);
        b2vitLayout = (LinearLayout) v.findViewById(R.id.b2vitLayout);
        bcgLayout = (LinearLayout) v.findViewById(R.id.bcgLayout);
        penta1Layout = (LinearLayout) v.findViewById(R.id.penta1Layout);
        penta2Layout = (LinearLayout) v.findViewById(R.id.penta2Layout);
        penta3Layout = (LinearLayout) v.findViewById(R.id.penta3Layout);
        vita1Layout = (LinearLayout) v.findViewById(R.id.vita1Layout);
        vita2layout = (LinearLayout) v.findViewById(R.id.vita2Layout);

        //Relativelayout
        relChildLayout = (RelativeLayout) v.findViewById(R.id.relChildLayout);

        //Scrollview
        childScrollView = (NestedScrollView) v.findViewById(R.id.childScrollView);

        //Buttons
        btnOpv = (MaterialButton) v.findViewById(R.id.btnOPV);
        btnB2vit = (MaterialButton) v.findViewById(R.id.btnB2VIT);
        btnBcg = (MaterialButton) v.findViewById(R.id.btnBCG);
        btnPenta1 = (MaterialButton) v.findViewById(R.id.btnPenta1);
        btnPenta2 = (MaterialButton) v.findViewById(R.id.btnPenta2);
        btnPenta3 = (MaterialButton) v.findViewById(R.id.btnPenta3);
        btnVita1 = (MaterialButton) v.findViewById(R.id.btnVita1);
        btnVita2 = (MaterialButton) v.findViewById(R.id.btnVita2);
        btnAddChildDetail = (MaterialButton) v.findViewById(R.id.btnAddChildDetail);
        btnAddChild = (MaterialButton) v.findViewById(R.id.btnAddChild);
        btnUploadDetail = (MaterialButton) v.findViewById(R.id.btnUploadDetail);
        btnNext = (MaterialButton) v.findViewById(R.id.btnNext);
        btnDeleteMember = (MaterialButton) v.findViewById(R.id.btnDeleteMember);

        //Textview
        textPreviousChildText = (TextView) v.findViewById(R.id.textPreviousChildText);
        textPregnancyId = (TextView) v.findViewById(R.id.textPregnancyId);
        textPregnancyNumber = (TextView) v.findViewById(R.id.textPregnancyNumber);
        textChildInfo = (TextView) v.findViewById(R.id.textChildInfo);

        //Materialspiiner
        childSpinner = (MaterialSpinner) v.findViewById(R.id.childSpinner);
        reqChildSpinner = (MaterialSpinner) v.findViewById(R.id.regChildSpinner);
        fatherSpinner = (MaterialSpinner) v.findViewById(R.id.fatherSpinner);
        motherSpinner = (MaterialSpinner) v.findViewById(R.id.motherSpinner);

        //TextInputEditText
        editRCH = (TextInputEditText) v.findViewById(R.id.editRCH);

        //validation
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
    }
    private void addValidation() {
        awesomeValidation.addValidation(editRCH, RegexTemplate.NOT_EMPTY, "Please enter child RCH" +
                " Id.");
    }
    private String getOid() {
        ObjectId memberId = ObjectId.get();
        return memberId.toString();
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
}
