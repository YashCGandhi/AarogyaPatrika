package com.hospitalmanagement.mainapp.Fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextThemeWrapper;
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

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.hospitalmanagement.mainapp.R;
import com.hospitalmanagement.mainapp.pojo.Child;
import com.hospitalmanagement.mainapp.pojo.EligibleCoupleName;
import com.hospitalmanagement.mainapp.pojo.Family;
import com.hospitalmanagement.mainapp.pojo.Member;
import com.hospitalmanagement.mainapp.pojo.MutationQueryFamilyDetail;
import com.hospitalmanagement.mainapp.pojo.UpdateResponse;
import com.hospitalmanagement.mainapp.pojo.Variables;
import com.hospitalmanagement.mainapp.retrofitService.apiClient.ApiClient;
import com.hospitalmanagement.mainapp.retrofitService.apiInterface.ApiInterface;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.bson.types.ObjectId;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentFamilyDetailChildInformation extends Fragment {

    private List<EligibleCoupleName> eligibleCoupleNames, addChildEligibleCoupleNames;
    private List<Child> children;
    private String objectId;
    private List<String> childDropdownList, newMemberChildList, newMemberFatherList, newMemberMotherList, fatherList, motherList;
    private HashMap<Integer, Object> DPTB_OPVB_MR2_VitA2List;
    private HashMap<Integer, Object> MR1_VitA1List;
    private HashMap<Integer, Object> OPV_3_IPV2_Penta3_Rota3List;
    private HashMap<Integer, Object> OPV_2_Penta_2_Rota_2List;
    private HashMap<Integer, Object> OPV1_IPV1_Penta1_Rota1List;
    private HashMap<Integer, Object> BCGList;
    private HashMap<Integer, Object> B2VITList;
    private HashMap<Integer, Object> OPVList;
    private HashMap<String, Object> changedInformation;
    private int selectedChildIndex, selectedMemberFatherIndex = -1, selectedMemberMotherIndex = -1, selectedMemberChildIndex = -1, selectedFatherIndex = -1, selectedMotherIndex = -1;
    private int DPTB_OPVB_MR2_VitA2Id = 0;
    private int MR1_VitA1Id = 0;
    private int OPV_3_IPV2_Penta3_Rota3Id = 0;
    private int OPV_2_Penta_2_Rota_2Id = 0;
    private int OPV1_IPV1_Penta1_Rota1Id = 0;
    private int BCGId = 0;
    private int B2VITId = 0;
    private int OPVId = 0;
    private int addDateClicked = 0;
    private boolean addChild = false;
    private int selectedParentIndex = -1;



    private LayoutInflater layoutInflater;
    private TextWatcher textWatcher, vaccinationDateTextWatcher;
    private Bundle bundle;
    private ProgressDialog progressDialog;
    private AlertDialog.Builder alertDialogBuilder;
    private Handler handler;
    private ApiInterface apiInterface;
    private Calendar calendar;
    private int day, month, year;
    private AwesomeValidation awesomeValidation;
//    private String date;
//    private int dateFlag = -1;
//    private DatePickerDialog datePickerDialog;
//    private DatePickerDialog.OnDateSetListener onDateSetListener;

    private MaterialButton saveChildInformation;
    private MaterialSpinner childNameSpinner;
    private RelativeLayout childDetailViewLayout;
    private TextInputEditText editTextFatherName;
    private TextInputEditText editTextMotherName;
//    private TextInputEditText editTextChildName;
    private TextInputEditText editTextChildRchId;
    private MaterialButton btnAddDPTB_OPVB_MR2_VitA2;
    private LinearLayout dateDPTB_OPVB_MR2_VitA2;
    private MaterialButton btnAddMR1_VitA1;
    private LinearLayout dateMR1_VitA1;
    private MaterialButton btnAddOPV_3_IPV2_Penta3_Rota3;
    private LinearLayout dateOPV_3_IPV2_Penta3_Rota3;
    private MaterialButton btnAddOPV_2_Penta_2_Rota_2;
    private LinearLayout dateOPV_2_Penta_2_Rota_2;
    private MaterialButton btnAddOPV1_IPV1_Penta1_Rota1;
    private LinearLayout dateOPV1_IPV1_Penta1_Rota1;
    private MaterialButton btnAddBCG;
    private LinearLayout dateBCG;
    private MaterialButton btnAddB2VIT;
    private LinearLayout dateB2VIT;
    private MaterialButton btnAddOPV;
    private LinearLayout dateOPV;
    private TextView textViewChild;
    private MaterialButton addChildButton;
    private NestedScrollView nestedScrollViewChild;
    private LinearLayout newChildSpinnerLinearLayout, childSpinnerLinearLayout;
    private MaterialSpinner newFatherNameMultiSpinner, newMotherNameMultiSpinner, newChildNameMultiSpinner, fatherNameMultiSpinner, motherNameMultiSpinner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        bundle = this.getArguments();

        if(bundle != null) {
            objectId = bundle.getString("objectId");
        }

        inflater = LayoutInflater.from(new ContextThemeWrapper(getContext(), R.style.AppTheme));

        return inflater.inflate(R.layout.familydetail_childinformation, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        childDropdownList = new ArrayList<>();
        newMemberChildList = new ArrayList<>();
        newMemberFatherList = new ArrayList<>();
        newMemberMotherList = new ArrayList<>();
        fatherList = new ArrayList<>();
        motherList = new ArrayList<>();
        changedInformation = new HashMap<>();
        DPTB_OPVB_MR2_VitA2List = new HashMap<>();
        MR1_VitA1List = new HashMap<>();
        OPV_3_IPV2_Penta3_Rota3List = new HashMap<>();
        OPV_2_Penta_2_Rota_2List = new HashMap<>();
        OPV1_IPV1_Penta1_Rota1List = new HashMap<>();
        BCGList = new HashMap<>();
        B2VITList = new HashMap<>();
        OPVList = new HashMap<>();
        progressDialog = new ProgressDialog(getContext());
        handler = new Handler();
        alertDialogBuilder = new AlertDialog.Builder(getContext());
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);


        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);


//        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//
//                date = dayOfMonth + "/" + (month + 1) + "/" + year;
//
//                if(dateFlag == 0) {
////                    editTextDateOfVisit.setText(date);
//                } else if(dateFlag == 1) {
////                    editTextDateOfGroupMeeting.setText(date);
//                }
//
//            }
//        };
//
//        datePickerDialog = new DatePickerDialog(getContext(), R.style.Theme_MaterialComponents_DayNight_Dialog_MinWidth, onDateSetListener, year, month, day);

        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s != null) {


//                    if (s.hashCode() == editTextChildName.getText().hashCode()) {
//
//                        if (!s.toString().equals(children.get(selectedChildIndex).getChildname())) {
//
//                            changedInformation.put("childname", editTextChildName.getText().toString());
////                            System.out.println("Changes " + changedInformation.get("childname"));
//
//                            if (saveChildInformation.getVisibility() == View.GONE) {
//                                saveChildInformation.setVisibility(View.VISIBLE);
//                            }
//
//                        } else {
//
//                            changedInformation.remove("childname");
//                            if (changedInformation.isEmpty() && addDateClicked == 0) {
//                                saveChildInformation.setVisibility(View.GONE);
//                            }
//
//                        }
//
//                    } else
                        if (s.hashCode() == editTextChildRchId.getText().hashCode()) {

                        if (!s.toString().equals(children.get(selectedChildIndex).getChildRCH())) {

                            changedInformation.put("childRCH", editTextChildRchId.getText().toString());
//                            System.out.println("Changes " + changedInformation.get("childRCH"));

                            if (saveChildInformation.getVisibility() == View.GONE) {
                                saveChildInformation.setVisibility(View.VISIBLE);
                            }

                        } else {

                            changedInformation.remove("childRCH");
                            if (changedInformation.isEmpty() && addDateClicked == 0) {
                                saveChildInformation.setVisibility(View.GONE);
                            }

                        }

                    }
                }
            }
        };

        saveChildInformation = view.findViewById(R.id.btnSaveChildInformation);
        childNameSpinner = view.findViewById(R.id.childNameMultiSpinner);
        childDetailViewLayout = view.findViewById(R.id.childDetailViewLayout);
//        editTextFatherName = view.findViewById(R.id.editFatherName);
//        editTextMotherName = view.findViewById(R.id.editMotherName);
//        editTextChildName = view.findViewById(R.id.editChildName);
        editTextChildRchId = view.findViewById(R.id.editChildRCH);
        btnAddDPTB_OPVB_MR2_VitA2 = view.findViewById(R.id.btnAddDPTB_OPVB_MR2_VitA2);
        dateDPTB_OPVB_MR2_VitA2 = view.findViewById(R.id.dateDPTB_OPVB_MR2_VitA2);
        btnAddMR1_VitA1 = view.findViewById(R.id.btnAddMR1_VitA1);
        dateMR1_VitA1 = view.findViewById(R.id.dateMR1_VitA1);
        btnAddOPV_3_IPV2_Penta3_Rota3 = view.findViewById(R.id.btnAddOPV_3_IPV2_Penta3_Rota3);
        dateOPV_3_IPV2_Penta3_Rota3 = view.findViewById(R.id.dateOPV_3_IPV2_Penta3_Rota3);
        btnAddOPV_2_Penta_2_Rota_2 = view.findViewById(R.id.btnAddOPV_2_Penta_2_Rota_2);
        dateOPV_2_Penta_2_Rota_2 = view.findViewById(R.id.dateOPV_2_Penta_2_Rota_2);
        btnAddOPV1_IPV1_Penta1_Rota1 = view.findViewById(R.id.btnAddOPV1_IPV1_Penta1_Rota1);
        dateOPV1_IPV1_Penta1_Rota1 = view.findViewById(R.id.dateOPV1_IPV1_Penta1_Rota1);
        btnAddBCG = view.findViewById(R.id.btnAddBCG);
        dateBCG = view.findViewById(R.id.dateBCG);
        btnAddB2VIT = view.findViewById(R.id.btnAddB2VIT);
        dateB2VIT = view.findViewById(R.id.dateB2VIT);
        btnAddOPV = view.findViewById(R.id.btnAddOPV);
        dateOPV = view.findViewById(R.id.dateOPV);
        textViewChild = view.findViewById(R.id.textChild);
        addChildButton = view.findViewById(R.id.btnAddChild);
        nestedScrollViewChild = view.findViewById(R.id.childInformationNestedScrollView);
        newChildSpinnerLinearLayout = view.findViewById(R.id.newChildSpinnerLinearLayout);
        newFatherNameMultiSpinner = view.findViewById(R.id.newFatherNameMultiSpinner);
        newMotherNameMultiSpinner = view.findViewById(R.id.newMotherNameMultiSpinner);
        newChildNameMultiSpinner = view.findViewById(R.id.newChildNameMultiSpinner);
        childSpinnerLinearLayout = view.findViewById(R.id.childSpinnerLinearLayout);
        fatherNameMultiSpinner = view.findViewById(R.id.fatherNameMultiSpinner);
        motherNameMultiSpinner = view.findViewById(R.id.motherNameMultiSpinner);




        layoutInflater = LayoutInflater.from(new ContextThemeWrapper(getContext(), R.style.AppTheme));





        saveChildInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Variables variables = new Variables();
                MutationQueryFamilyDetail mutationQuery = new MutationQueryFamilyDetail();



                boolean check = false;

                if(awesomeValidation.validate()) {
                    if (!addChild) {

                        List<String> DPTB_OPVB_MR2_VitA2L = new ArrayList<>(children.get(selectedChildIndex).getVaccination().getDPTBOPVBMR2VitA2());
                        List<String> MR1_VitA1L = new ArrayList<>(children.get(selectedChildIndex).getVaccination().getMR1VitA1());
                        List<String> OPV_3_IPV2_Penta3_Rota3L = new ArrayList<>(children.get(selectedChildIndex).getVaccination().getOPV3IPV2Penta3Rota3());
                        List<String> OPV_2_Penta_2_Rota_2L = new ArrayList<>(children.get(selectedChildIndex).getVaccination().getOPV2Penta2Rota2());
                        List<String> OPV1_IPV1_Penta1_Rota1L = new ArrayList<>(children.get(selectedChildIndex).getVaccination().getOPV1IPV1Penta1Rota1());
                        List<String> BCGL = new ArrayList<>(children.get(selectedChildIndex).getVaccination().getBCG());
                        List<String> B2VITL = new ArrayList<>(children.get(selectedChildIndex).getVaccination().getB2VIT());
                        List<String> OPVL = new ArrayList<>(children.get(selectedChildIndex).getVaccination().getOPV());
                        HashMap<String, List<String>> vaccination = new HashMap<>();

                        // DPTB_OPVB_MR2_VitA2
                        if (changedInformation.get("DPTB_OPVB_MR2_VitA2") != null) {
                            for (Map.Entry date : (Set<Map.Entry>) ((HashMap) changedInformation.get("DPTB_OPVB_MR2_VitA2")).entrySet()) {

                                DPTB_OPVB_MR2_VitA2L.set((Integer) date.getKey(), date.getValue().toString());

                            }
                        }
                        if (DPTB_OPVB_MR2_VitA2Id > DPTB_OPVB_MR2_VitA2L.size()) {
                            check = true;
                            for (int i = DPTB_OPVB_MR2_VitA2L.size(); i < DPTB_OPVB_MR2_VitA2Id; i++) {
                                DPTB_OPVB_MR2_VitA2L.add(((TextInputEditText) dateDPTB_OPVB_MR2_VitA2.findViewById(i)).getText().toString());
                            }
                        }
                        if (changedInformation.get("DPTB_OPVB_MR2_VitA2") != null || check) {
                            check = false;
                            changedInformation.remove("DPTB_OPVB_MR2_VitA2");
                            vaccination.put("DPTB_OPVB_MR2_VitA2", DPTB_OPVB_MR2_VitA2L);
                        }


                        // MR1_VitA1
                        if (changedInformation.get("MR1_VitA1") != null) {
                            for (Map.Entry date : (Set<Map.Entry>) ((HashMap) changedInformation.get("MR1_VitA1")).entrySet()) {

                                MR1_VitA1L.set((Integer) date.getKey(), date.getValue().toString());

                            }
                        }
                        if (MR1_VitA1Id > MR1_VitA1L.size()) {
                            check = true;
                            for (int i = MR1_VitA1L.size(); i < MR1_VitA1Id; i++) {
                                MR1_VitA1L.add(((TextInputEditText) dateMR1_VitA1.findViewById(i)).getText().toString());
                            }
                        }
//                        System.out.println("MR1_VitA1Id: " + MR1_VitA1Id);
//                        System.out.println("MR1_VitA1L: " + MR1_VitA1L);
//                        System.out.println("MR1_VitA1L.size(): " + MR1_VitA1L.size());
                        if (changedInformation.get("MR1_VitA1") != null || check) {
                            check = false;
                            changedInformation.remove("MR1_VitA1");
                            vaccination.put("MR1_VitA1", MR1_VitA1L);
                        }


                        // OPV_3_IPV2_Penta3_Rota3
                        if (changedInformation.get("OPV_3_IPV2_Penta3_Rota3") != null) {
                            for (Map.Entry date : (Set<Map.Entry>) ((HashMap) changedInformation.get("OPV_3_IPV2_Penta3_Rota3")).entrySet()) {

                                OPV_3_IPV2_Penta3_Rota3L.set((Integer) date.getKey(), date.getValue().toString());

                            }
                        }
                        if (OPV_3_IPV2_Penta3_Rota3Id > OPV_3_IPV2_Penta3_Rota3L.size()) {
                            check = true;
                            for (int i = OPV_3_IPV2_Penta3_Rota3L.size(); i < OPV_3_IPV2_Penta3_Rota3Id; i++) {
                                OPV_3_IPV2_Penta3_Rota3L.add(((TextInputEditText) dateOPV_3_IPV2_Penta3_Rota3.findViewById(i)).getText().toString());
                            }
                        }
                        if (changedInformation.get("OPV_3_IPV2_Penta3_Rota3") != null || check) {
                            check = false;
                            changedInformation.remove("OPV_3_IPV2_Penta3_Rota3");
                            vaccination.put("OPV_3_IPV2_Penta3_Rota3", OPV_3_IPV2_Penta3_Rota3L);
                        }


                        // OPV_2_Penta_2_Rota_2
                        if (changedInformation.get("OPV_2_Penta_2_Rota_2") != null) {
                            for (Map.Entry date : (Set<Map.Entry>) ((HashMap) changedInformation.get("OPV_2_Penta_2_Rota_2")).entrySet()) {

                                OPV_2_Penta_2_Rota_2L.set((Integer) date.getKey(), date.getValue().toString());

                            }
                        }
                        if (OPV_2_Penta_2_Rota_2Id > OPV_2_Penta_2_Rota_2L.size()) {
                            check = true;
                            for (int i = OPV_2_Penta_2_Rota_2L.size(); i < OPV_2_Penta_2_Rota_2Id; i++) {
                                OPV_2_Penta_2_Rota_2L.add(((TextInputEditText) dateOPV_2_Penta_2_Rota_2.findViewById(i)).getText().toString());
                            }
                        }
                        if (changedInformation.get("OPV_2_Penta_2_Rota_2") != null || check) {
                            check = false;
                            changedInformation.remove("OPV_2_Penta_2_Rota_2");
                            vaccination.put("OPV_2_Penta_2_Rota_2", OPV_2_Penta_2_Rota_2L);
                        }


                        // OPV1_IPV1_Penta1_Rota1
                        if (changedInformation.get("OPV1_IPV1_Penta1_Rota1") != null) {
                            for (Map.Entry date : (Set<Map.Entry>) ((HashMap) changedInformation.get("OPV1_IPV1_Penta1_Rota1")).entrySet()) {

                                OPV1_IPV1_Penta1_Rota1L.set((Integer) date.getKey(), date.getValue().toString());

                            }
                        }
                        if (OPV1_IPV1_Penta1_Rota1Id > OPV1_IPV1_Penta1_Rota1L.size()) {
                            check = true;
                            for (int i = OPV1_IPV1_Penta1_Rota1L.size(); i < OPV1_IPV1_Penta1_Rota1Id; i++) {
                                OPV1_IPV1_Penta1_Rota1L.add(((TextInputEditText) dateOPV1_IPV1_Penta1_Rota1.findViewById(i)).getText().toString());
                            }
                        }
                        if (changedInformation.get("OPV1_IPV1_Penta1_Rota1") != null || check) {
                            check = false;
                            changedInformation.remove("OPV1_IPV1_Penta1_Rota1");
                            vaccination.put("OPV1_IPV1_Penta1_Rota1", OPV1_IPV1_Penta1_Rota1L);
                        }


                        // BCG
                        if (changedInformation.get("BCG") != null) {
                            for (Map.Entry date : (Set<Map.Entry>) ((HashMap) changedInformation.get("BCG")).entrySet()) {

                                BCGL.set((Integer) date.getKey(), date.getValue().toString());

                            }
                        }
                        if (BCGId > BCGL.size()) {
                            check = true;
                            for (int i = BCGL.size(); i < BCGId; i++) {
                                BCGL.add(((TextInputEditText) dateBCG.findViewById(i)).getText().toString());
                            }
                        }
                        if (changedInformation.get("BCG") != null || check) {
                            check = false;
                            changedInformation.remove("BCG");
                            vaccination.put("BCG", BCGL);
                        }


                        // B2VIT
                        if (changedInformation.get("B2VIT") != null) {
                            for (Map.Entry date : (Set<Map.Entry>) ((HashMap) changedInformation.get("B2VIT")).entrySet()) {

                                B2VITL.set((Integer) date.getKey(), date.getValue().toString());

                            }
                        }

                        if (B2VITId > B2VITL.size()) {
                            check = true;
                            for (int i = B2VITL.size(); i < B2VITId; i++) {
                                B2VITL.add(((TextInputEditText) dateB2VIT.findViewById(i)).getText().toString());
                            }
                        }
//                        System.out.println("B2VITL: " + B2VITL);
                        if (changedInformation.get("B2VIT") != null || check) {
                            check = false;
                            changedInformation.remove("B2VIT");
                            vaccination.put("B2VIT", B2VITL);
                        }


                        // OPV
                        if (changedInformation.get("OPV") != null) {
                            for (Map.Entry date : (Set<Map.Entry>) ((HashMap) changedInformation.get("OPV")).entrySet()) {

                                OPVL.set((Integer) date.getKey(), date.getValue().toString());

                            }
                        }
                        if (OPVId > OPVL.size()) {
                            check = true;
                            for (int i = OPVL.size(); i < OPVId; i++) {
                                OPVL.add(((TextInputEditText) dateOPV.findViewById(i)).getText().toString());
                            }
                        }
                        if (changedInformation.get("OPV") != null || check) {
                            check = false;
                            changedInformation.remove("OPV");
                            vaccination.put("OPV", OPVL);
                        }

                        changedInformation.put("vaccination", vaccination);

//                        System.out.println("Vaccination: " + vaccination);


                        variables.setInput(changedInformation);


                        mutationQuery.setVariables(variables);

                        apiCallToUpdateChildInformation(mutationQuery);


                    } else {

                        // add child

                        HashMap<String, Object> information = new HashMap<>();

                        List<String> DPTB_OPVB_MR2_VitA2L = new ArrayList<>();
                        List<String> MR1_VitA1L = new ArrayList<>();
                        List<String> OPV_3_IPV2_Penta3_Rota3L = new ArrayList<>();
                        List<String> OPV_2_Penta_2_Rota_2L = new ArrayList<>();
                        List<String> OPV1_IPV1_Penta1_Rota1L = new ArrayList<>();
                        List<String> BCGL = new ArrayList<>();
                        List<String> B2VITL = new ArrayList<>();
                        List<String> OPVL = new ArrayList<>();
                        HashMap<String, List<String>> vaccination = new HashMap<>();


//                    int i = 0;
//                    for(EligibleCoupleName eligibleCoupleName : addChildEligibleCoupleNames) {
//
////                        System.out.println(i);
//                        i++;
////                        System.out.println(eligibleCoupleName.getHusband());
////                        System.out.println(eligibleCoupleName.getWife());
//
//                    }
//
////                    System.out.println("newFatherList: " + newMemberFatherList);
////                    System.out.println("newMotherList: " + newMemberMotherList);


//                        System.out.println("new father index: " + newFatherNameMultiSpinner.getSelectedIndex());
                        if (addChildEligibleCoupleNames.get(newFatherNameMultiSpinner.getSelectedIndex() - 1).getEligibleCoupleNameId() != null) {
                            information.put("eligibleCoupleNameObjectId", addChildEligibleCoupleNames.get(newFatherNameMultiSpinner.getSelectedIndex() - 1).getEligibleCoupleNameId());
                        }

//                    if(addChildEligibleCoupleNames.get(newMotherNameMultiSpinner.getSelectedIndex() - 1).getEligibleCoupleNameId() != null) {
//                        information.put("eligibleCoupleNameObjectId", addChildEligibleCoupleNames.get(newMotherNameMultiSpinner.getSelectedIndex() - 1).getEligibleCoupleNameId());
//                    }

                        if (newChildNameMultiSpinner.getSelectedIndex() != 0) {
                            information.put("childname", newChildNameMultiSpinner.getItems().get(newChildNameMultiSpinner.getSelectedIndex()));
                        }

                        if (editTextChildRchId.getText().toString().trim().length() != 0) {
                            information.put("childRCH", editTextChildRchId.getText().toString());
                        }

//                    DPTB_OPVB_MR2_VitA2
                        for (int id = 0; id < DPTB_OPVB_MR2_VitA2Id; id++) {
                            if (((TextInputEditText) dateDPTB_OPVB_MR2_VitA2.findViewById(id)).getText().toString().trim().length() != 0) {
                                DPTB_OPVB_MR2_VitA2L.add(((TextInputEditText) dateDPTB_OPVB_MR2_VitA2.findViewById(id)).getText().toString());
                            }
                        }
                        if (!DPTB_OPVB_MR2_VitA2L.isEmpty()) {
                            vaccination.put("DPTB_OPVB_MR2_VitA2", DPTB_OPVB_MR2_VitA2L);
                        }

//                    MR1_VitA1
                        for (int id = 0; id < MR1_VitA1Id; id++) {
                            if (((TextInputEditText) dateMR1_VitA1.findViewById(id)).getText().toString().trim().length() != 0) {
                                MR1_VitA1L.add(((TextInputEditText) dateMR1_VitA1.findViewById(id)).getText().toString());
                            }
                        }
                        if (!MR1_VitA1L.isEmpty()) {
                            vaccination.put("MR1_VitA1", MR1_VitA1L);
                        }

//                    OPV_3_IPV2_Penta3_Rota3
                        for (int id = 0; id < OPV_3_IPV2_Penta3_Rota3Id; id++) {
                            if (((TextInputEditText) dateOPV_3_IPV2_Penta3_Rota3.findViewById(id)).getText().toString().trim().length() != 0) {
                                OPV_3_IPV2_Penta3_Rota3L.add(((TextInputEditText) dateOPV_3_IPV2_Penta3_Rota3.findViewById(id)).getText().toString());
                            }
                        }
                        if (!OPV_3_IPV2_Penta3_Rota3L.isEmpty()) {
                            vaccination.put("OPV_3_IPV2_Penta3_Rota3", OPV_3_IPV2_Penta3_Rota3L);
                        }

//                    OPV_2_Penta_2_Rota_2
                        for (int id = 0; id < OPV_2_Penta_2_Rota_2Id; id++) {
                            if (((TextInputEditText) dateOPV_2_Penta_2_Rota_2.findViewById(id)).getText().toString().trim().length() != 0) {
                                OPV_2_Penta_2_Rota_2L.add(((TextInputEditText) dateOPV_2_Penta_2_Rota_2.findViewById(id)).getText().toString());
                            }
                        }
                        if (!OPV_2_Penta_2_Rota_2L.isEmpty()) {
                            vaccination.put("OPV_2_Penta_2_Rota_2", OPV_2_Penta_2_Rota_2L);
                        }

//                    OPV1_IPV1_Penta1_Rota1
                        for (int id = 0; id < OPV1_IPV1_Penta1_Rota1Id; id++) {
                            if (((TextInputEditText) dateOPV1_IPV1_Penta1_Rota1.findViewById(id)).getText().toString().trim().length() != 0) {
                                OPV1_IPV1_Penta1_Rota1L.add(((TextInputEditText) dateOPV1_IPV1_Penta1_Rota1.findViewById(id)).getText().toString());
                            }
                        }
                        if (!OPV1_IPV1_Penta1_Rota1L.isEmpty()) {
                            vaccination.put("OPV1_IPV1_Penta1_Rota1", OPV1_IPV1_Penta1_Rota1L);
                        }

//                    BCG
                        for (int id = 0; id < BCGId; id++) {
                            if (((TextInputEditText) dateBCG.findViewById(id)).getText().toString().trim().length() != 0) {
                                BCGL.add(((TextInputEditText) dateBCG.findViewById(id)).getText().toString());
                            }
                        }
                        if (!BCGL.isEmpty()) {
                            vaccination.put("BCG", BCGL);
                        }

//                    B2VIT
                        for (int id = 0; id < B2VITId; id++) {
                            if (((TextInputEditText) dateB2VIT.findViewById(id)).getText().toString().trim().length() != 0) {
                                B2VITL.add(((TextInputEditText) dateB2VIT.findViewById(id)).getText().toString());
                            }
                        }
                        if (!B2VITL.isEmpty()) {
                            vaccination.put("B2VIT", B2VITL);
                        }


//                    OPV
                        for (int id = 0; id < OPVId; id++) {
                            if (((TextInputEditText) dateOPV.findViewById(id)).getText().toString().trim().length() != 0) {
                                OPVL.add(((TextInputEditText) dateOPV.findViewById(id)).getText().toString());
                            }
                        }
                        if (!OPVL.isEmpty()) {
                            vaccination.put("OPV", OPVL);
                        }

                        information.put("vaccination", vaccination);

                        information.put("childrenObjectId", new ObjectId().toString());

                        List<HashMap<String, Object>> input = new ArrayList<>();

                        input.add(information);

                        variables.setInput(input);
                        mutationQuery.setVariables(variables);

                        apiCallToAddChildInformation(mutationQuery);

                    }
                }

            }
        });

        addChildButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addChild = true;
                textViewChild.setVisibility(View.GONE);
                childNameSpinner.setVisibility(View.GONE);

                childDetailViewLayout.setVisibility(View.GONE);
                childSpinnerLinearLayout.setVisibility(View.GONE);
                newChildSpinnerLinearLayout.setVisibility(View.VISIBLE);

                clearDataFromLayout();
//                removeListenersFromComponents();

                apiCallToGetFatherMotherChild();

                saveChildInformation.setVisibility(View.VISIBLE);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        childDetailViewLayout.setVisibility(View.VISIBLE);
                    }
                }, 500);

                nestedScrollViewChild.scrollTo(0, 0);

            }
        });

        addValidation();
        addListenerToComponents();
        apiCallToGetChildInformation();

    }


    private void addListenerToComponents() {



//        editTextChildName.addTextChangedListener(textWatcher);
        editTextChildRchId.addTextChangedListener(textWatcher);



        childNameSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {

                childDetailViewLayout.setVisibility(View.GONE);
//                editTextMotherName.setEnabled(false);
//                editTextFatherName.setEnabled(false);

                if(position != 0) {
                    selectedChildIndex = position - 1;

//                    int index = 0;
//                    for(EligibleCoupleName eligibleCoupleName : eligibleCoupleNames) {
//                        if(eligibleCoupleName.getEligibleCoupleNameId().equals(children.get(selectedChildIndex).getEligibleCoupleNameObjectId())) {
//                            fatherNameMultiSpinner.setSelectedIndex(index + 1);
//                            newMotherNameMultiSpinner.setSelectedIndex(index + 1);
//                        }
//                        index++;
//                    }

                    childSpinnerLinearLayout.setVisibility(View.VISIBLE);
                    newChildSpinnerLinearLayout.setVisibility(View.GONE);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            nestedScrollViewChild.scrollTo(0, 0);
                            childDetailViewLayout.setVisibility(View.VISIBLE);
                        }
                    }, 500);

                    if(!addChild) {
                        fatherNameMultiSpinner.setSelectedIndex(0);
                        motherNameMultiSpinner.setSelectedIndex(0);
                        setDataToLayout(children.get(position - 1));
                    }


                    changedInformation.clear();
                    saveChildInformation.setVisibility(View.GONE);
                }

            }
        });

        fatherNameMultiSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {

                if(position != 0) {

                    selectedFatherIndex = position;
                    motherNameMultiSpinner.setSelectedIndex(position);

                    if(selectedParentIndex != position) {

                        for(EligibleCoupleName eligibleCoupleName : eligibleCoupleNames) {
                            if(view.getItems().get(position).toString().equalsIgnoreCase(eligibleCoupleName.getHusband())) {
                                if(eligibleCoupleName.getEligibleCoupleNameId() != null) {
                                    changedInformation.put("eligibleCoupleNameObjectId", eligibleCoupleName.getEligibleCoupleNameId());
                                }

                            }
                        }

                        if(saveChildInformation.getVisibility() == View.GONE) {
                            saveChildInformation.setVisibility(View.VISIBLE);
                        }


                    } else {

                        changedInformation.remove("eligibleCoupleNameObjectId");

                        if(changedInformation.isEmpty()) {
                            saveChildInformation.setVisibility(View.GONE);
                        }

                    }
//                    System.out.println("changed couple id" + changedInformation.get("eligibleCoupleNameObjectId"));

                } else {
                    selectedFatherIndex = -1;
                }

            }
        });

        motherNameMultiSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {

                if(position != 0) {

                    selectedMotherIndex = position;
                    fatherNameMultiSpinner.setSelectedIndex(position);

                    if(selectedParentIndex != position) {

                        for(EligibleCoupleName eligibleCoupleName : eligibleCoupleNames) {
                            if(view.getItems().get(position).toString().equalsIgnoreCase(eligibleCoupleName.getWife())) {
                                if(eligibleCoupleName.getEligibleCoupleNameId() != null) {
                                    changedInformation.put("eligibleCoupleNameObjectId", eligibleCoupleName.getEligibleCoupleNameId());
                                }

                            }
                        }

                        if(saveChildInformation.getVisibility() == View.GONE) {
                            saveChildInformation.setVisibility(View.VISIBLE);
                        }


                    } else {

                        changedInformation.remove("eligibleCoupleNameObjectId");

                        if(changedInformation.isEmpty()) {
                            saveChildInformation.setVisibility(View.GONE);
                        }

                    }
//                    System.out.println("changed couple id" + changedInformation.get("eligibleCoupleNameObjectId"));

                } else {
                    selectedMotherIndex = -1;
                }

            }
        });

        newFatherNameMultiSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if(position != 0) {
                    selectedMemberFatherIndex = position;
                }
                else {
                    selectedMemberFatherIndex = -1;
                }
                newMotherNameMultiSpinner.setSelectedIndex(position);
            }
        });

        newMotherNameMultiSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if(position != 0) {
                    selectedMemberMotherIndex = position;
                } else {
                    selectedMemberMotherIndex = -1;
                }
                newFatherNameMultiSpinner.setSelectedIndex(position);
            }
        });

        newChildNameMultiSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if(position != 0) {
                    selectedMemberChildIndex = position;
                }
                else {
                    selectedMemberChildIndex = -1;
                }
            }
        });

        btnAddDPTB_OPVB_MR2_VitA2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutInflater.inflate(R.layout.familydetail_vaccinationdate, dateDPTB_OPVB_MR2_VitA2, true).findViewById(R.id.editVaccinationDate).setId(DPTB_OPVB_MR2_VitA2Id);

                final TextInputEditText textInputEditText = ((TextInputEditText) dateDPTB_OPVB_MR2_VitA2.findViewById(DPTB_OPVB_MR2_VitA2Id));

                ((TextInputEditText) dateDPTB_OPVB_MR2_VitA2.findViewById(DPTB_OPVB_MR2_VitA2Id)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                                String date = dayOfMonth + "/" + (month + 1) + "/" + year;

                                textInputEditText.setText(date);

                            }
                        };

                        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), R.style.Theme_MaterialComponents_Light_Dialog_MinWidth, onDateSetListener, year, month, day);

                        datePickerDialog.getWindow();
                        datePickerDialog.show();
                    }
                });

                ((TextInputEditText) dateDPTB_OPVB_MR2_VitA2.findViewById(DPTB_OPVB_MR2_VitA2Id)).setText("naya add hua" + DPTB_OPVB_MR2_VitA2Id);
                DPTB_OPVB_MR2_VitA2Id++;
                saveChildInformation.setVisibility(View.VISIBLE);
                addDateClicked = 1;
            }
        });
        btnAddMR1_VitA1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutInflater.inflate(R.layout.familydetail_vaccinationdate, dateMR1_VitA1, true).findViewById(R.id.editVaccinationDate).setId(MR1_VitA1Id);

                final TextInputEditText textInputEditText = ((TextInputEditText) dateMR1_VitA1.findViewById(MR1_VitA1Id));
                ((TextInputEditText) dateMR1_VitA1.findViewById(MR1_VitA1Id)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                                String date = dayOfMonth + "/" + (month + 1) + "/" + year;

                                textInputEditText.setText(date);

                            }
                        };

                        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), R.style.Theme_MaterialComponents_Light_Dialog_MinWidth, onDateSetListener, year, month, day);

                        datePickerDialog.getWindow();
                        datePickerDialog.show();
                    }
                });

//                ((TextInputEditText) dateMR1_VitA1.findViewById(MR1_VitA1Id)).setText("naya add hua" + MR1_VitA1Id);
                MR1_VitA1Id++;
                saveChildInformation.setVisibility(View.VISIBLE);
                addDateClicked = 1;
            }
        });
        btnAddOPV_3_IPV2_Penta3_Rota3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutInflater.inflate(R.layout.familydetail_vaccinationdate, dateOPV_3_IPV2_Penta3_Rota3, true).findViewById(R.id.editVaccinationDate).setId(OPV_3_IPV2_Penta3_Rota3Id);


                final TextInputEditText textInputEditText = ((TextInputEditText) dateOPV_3_IPV2_Penta3_Rota3.findViewById(OPV_3_IPV2_Penta3_Rota3Id));
                ((TextInputEditText) dateOPV_3_IPV2_Penta3_Rota3.findViewById(OPV_3_IPV2_Penta3_Rota3Id)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                                String date = dayOfMonth + "/" + (month + 1) + "/" + year;

                                textInputEditText.setText(date);

                            }
                        };

                        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), R.style.Theme_MaterialComponents_Light_Dialog_MinWidth, onDateSetListener, year, month, day);

                        datePickerDialog.getWindow();
                        datePickerDialog.show();
                    }
                });

//                ((TextInputEditText) dateOPV_3_IPV2_Penta3_Rota3.findViewById(OPV_3_IPV2_Penta3_Rota3Id)).setText("naya add hua" + OPV_3_IPV2_Penta3_Rota3Id);
                OPV_3_IPV2_Penta3_Rota3Id++;
                saveChildInformation.setVisibility(View.VISIBLE);
                addDateClicked = 1;
            }
        });
        btnAddOPV_2_Penta_2_Rota_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutInflater.inflate(R.layout.familydetail_vaccinationdate, dateOPV_2_Penta_2_Rota_2, true).findViewById(R.id.editVaccinationDate).setId(OPV_2_Penta_2_Rota_2Id);


                final TextInputEditText textInputEditText = ((TextInputEditText) dateOPV_2_Penta_2_Rota_2.findViewById(OPV_2_Penta_2_Rota_2Id));
                ((TextInputEditText) dateOPV_2_Penta_2_Rota_2.findViewById(OPV_2_Penta_2_Rota_2Id)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                                String date = dayOfMonth + "/" + (month + 1) + "/" + year;

                                textInputEditText.setText(date);

                            }
                        };

                        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), R.style.Theme_MaterialComponents_Light_Dialog_MinWidth, onDateSetListener, year, month, day);

                        datePickerDialog.getWindow();
                        datePickerDialog.show();
                    }
                });

//                ((TextInputEditText) dateOPV_2_Penta_2_Rota_2.findViewById(OPV_2_Penta_2_Rota_2Id)).setText("naya add hua" + OPV_2_Penta_2_Rota_2Id);
                OPV_2_Penta_2_Rota_2Id++;
                saveChildInformation.setVisibility(View.VISIBLE);
                addDateClicked = 1;
            }
        });
        btnAddOPV1_IPV1_Penta1_Rota1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutInflater.inflate(R.layout.familydetail_vaccinationdate, dateOPV1_IPV1_Penta1_Rota1, true).findViewById(R.id.editVaccinationDate).setId(OPV1_IPV1_Penta1_Rota1Id);

                final TextInputEditText textInputEditText = ((TextInputEditText) dateOPV1_IPV1_Penta1_Rota1.findViewById(OPV1_IPV1_Penta1_Rota1Id));
                ((TextInputEditText) dateOPV1_IPV1_Penta1_Rota1.findViewById(OPV1_IPV1_Penta1_Rota1Id)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                                String date = dayOfMonth + "/" + (month + 1) + "/" + year;

                                textInputEditText.setText(date);

                            }
                        };

                        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), R.style.Theme_MaterialComponents_Light_Dialog_MinWidth, onDateSetListener, year, month, day);

                        datePickerDialog.getWindow();
                        datePickerDialog.show();
                    }
                });

//                ((TextInputEditText) dateOPV1_IPV1_Penta1_Rota1.findViewById(OPV1_IPV1_Penta1_Rota1Id)).setText("naya add hua" + OPV1_IPV1_Penta1_Rota1Id);
                OPV1_IPV1_Penta1_Rota1Id++;
                saveChildInformation.setVisibility(View.VISIBLE);
                addDateClicked = 1;
            }
        });
        btnAddBCG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutInflater.inflate(R.layout.familydetail_vaccinationdate, dateBCG, true).findViewById(R.id.editVaccinationDate).setId(BCGId);

                final TextInputEditText textInputEditText = ((TextInputEditText) dateBCG.findViewById(BCGId));
                ((TextInputEditText) dateBCG.findViewById(BCGId)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                                String date = dayOfMonth + "/" + (month + 1) + "/" + year;

                                textInputEditText.setText(date);

                            }
                        };

                        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), R.style.Theme_MaterialComponents_Light_Dialog_MinWidth, onDateSetListener, year, month, day);

                        datePickerDialog.getWindow();
                        datePickerDialog.show();
                    }
                });

//                ((TextInputEditText) dateBCG.findViewById(BCGId)).setText("naya add hua" + BCGId);
                BCGId++;
                saveChildInformation.setVisibility(View.VISIBLE);
                addDateClicked = 1;
            }
        });
        btnAddB2VIT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutInflater.inflate(R.layout.familydetail_vaccinationdate, dateB2VIT, true).findViewById(R.id.editVaccinationDate).setId(B2VITId);

                final TextInputEditText textInputEditText = ((TextInputEditText) dateB2VIT.findViewById(B2VITId));
                ((TextInputEditText) dateB2VIT.findViewById(B2VITId)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                                String date = dayOfMonth + "/" + (month + 1) + "/" + year;

                                textInputEditText.setText(date);

                            }
                        };

                        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), R.style.Theme_MaterialComponents_Light_Dialog_MinWidth, onDateSetListener, year, month, day);

                        datePickerDialog.getWindow();
                        datePickerDialog.show();
                    }
                });

//                ((TextInputEditText) dateB2VIT.findViewById(B2VITId)).setText("naya add hua" + B2VITId);
                B2VITId++;
                saveChildInformation.setVisibility(View.VISIBLE);
                addDateClicked = 1;
            }
        });
        btnAddOPV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutInflater.inflate(R.layout.familydetail_vaccinationdate, dateOPV, true).findViewById(R.id.editVaccinationDate).setId(OPVId);

                final TextInputEditText textInputEditText = ((TextInputEditText) dateOPV.findViewById(OPVId));
                ((TextInputEditText) dateOPV.findViewById(OPVId)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                                String date = dayOfMonth + "/" + (month + 1) + "/" + year;

                                textInputEditText.setText(date);

                            }
                        };

                        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), R.style.Theme_MaterialComponents_Light_Dialog_MinWidth, onDateSetListener, year, month, day);

                        datePickerDialog.getWindow();
                        datePickerDialog.show();
                    }
                });

//                ((TextInputEditText) dateOPV.findViewById(OPVId)).setText("naya add hua" + OPVId);
                OPVId++;
                saveChildInformation.setVisibility(View.VISIBLE);
                addDateClicked = 1;
            }
        });




    }

    private void apiCallToAddChildInformation(MutationQueryFamilyDetail mutationQuery) {

        showProgressBar("Adding Child ...");
        String query = "mutation($input: [ChildrenInput]) { addChildren(id: \"" + objectId + "\", input: $input) { n nModified ok } }";

        mutationQuery.setQuery(query);

//        System.out.println("query: " + mutationQuery.getQuery());
//        System.out.println("input: " + mutationQuery.getVariables().getInput());

        Call<Family> addChildInformation = apiInterface.addInformation(mutationQuery);

        addChildInformation.enqueue(new Callback<Family>() {
            @Override
            public void onResponse(Call<Family> call, Response<Family> response) {

                if(response.isSuccessful() && response.body() != null) {

                    apiCallToGetChildInformation();

                    changedInformation.clear();
                    saveChildInformation.setVisibility(View.GONE);

                    childDetailViewLayout.setVisibility(View.GONE);

                    textViewChild.setVisibility(View.VISIBLE);
                    childNameSpinner.setVisibility(View.VISIBLE);

                    newChildSpinnerLinearLayout.setVisibility(View.GONE);

                    clearDataFromLayout();
                    addListenerToComponents();
                    addChild = false;

                    stopProgressBar();
                    showAlertDialog("Child Added Successfully.");

                } else {
                    stopProgressBar();
                    showAlertDialog("Error Adding Child.");
                }

            }

            @Override
            public void onFailure(Call<Family> call, Throwable t) {

            }
        });

    }

    private void apiCallToGetFatherMotherChild() {

        showProgressBar("Loading Please Wait ...");

        String query = "query { getFamily(_id: \""  + objectId + "\") { members { memberid membername } eligibleCoupleName { eligibleCoupleNameId husband wife } children { childname } } }";

        Call<Family> getFatherMotherChild = apiInterface.getInformation(query);

        getFatherMotherChild.enqueue(new Callback<Family>() {
            @Override
            public void onResponse(Call<Family> call, Response<Family> response) {

                if(response.isSuccessful() && response.body() != null) {

                    if(response.body().getData().getGetFamily().getEligibleCoupleName() == null) {
                        showAlertDialog("No Couple Found");
                        return;
                    }

                    addChildEligibleCoupleNames = response.body().getData().getGetFamily().getEligibleCoupleName();

                    newMemberChildList.clear();
                    newMemberChildList.add("---Select Child---");
                    for(Member member : response.body().getData().getGetFamily().getMembers()) {
                        if(member.getMembername() != null) {
                            newMemberChildList.add(member.getMembername());
                        }
                    }

                    for(Child child : response.body().getData().getGetFamily().getChildren()) {
                        if(child.getChildname() != null) {
                            newMemberChildList.remove(child.getChildname());
                        }
                    }

                    newMemberFatherList.clear();
                    newMemberFatherList.add("---Select Father---");
                    newMemberMotherList.clear();
                    newMemberMotherList.add("---Select Mother---");
                    for(EligibleCoupleName eligibleCoupleName : response.body().getData().getGetFamily().getEligibleCoupleName()) {
                        if(eligibleCoupleName.getHusband() != null) {
                            newMemberFatherList.add(eligibleCoupleName.getHusband());
                            newMemberChildList.remove(eligibleCoupleName.getHusband());
                        }
                        if(eligibleCoupleName.getWife() != null) {
                            newMemberMotherList.add(eligibleCoupleName.getWife());
                            newMemberChildList.remove(eligibleCoupleName.getWife());
                        }
                    }

                    newChildNameMultiSpinner.setItems(newMemberChildList);
                    newFatherNameMultiSpinner.setItems(newMemberFatherList);
                    newMotherNameMultiSpinner.setItems(newMemberMotherList);

                    stopProgressBar();
                } else {
                    stopProgressBar();
                    showAlertDialog("No Couple Found.");
                }

            }

            @Override
            public void onFailure(Call<Family> call, Throwable t) {

            }
        });

    }

    private void apiCallToUpdateChildInformation(MutationQueryFamilyDetail mutationQuery) {

        showProgressBar("Updating Child Data ...");

        String query = "mutation($input: ChildrenInput) { updateChildren(childrenObjectId: \"" + children.get(selectedChildIndex).getChildrenObjectId() + "\", input: $input) { n nModified ok } }";

        mutationQuery.setQuery(query);

//        System.out.println("query: " + mutationQuery.getQuery());
//        System.out.println("update data: " + mutationQuery.getVariables().getInput());

        Call<Family> updateChildInformation = apiInterface.updateInformation(mutationQuery);

        updateChildInformation.enqueue(new Callback<Family>() {
            @Override
            public void onResponse(Call<Family> call, Response<Family> response) {

                if(response.isSuccessful() && response.body() != null) {

                    stopProgressBar();
                    apiCallToGetChildInformation();
                    saveChildInformation.setVisibility(View.GONE);
                    changedInformation.clear();
                    showAlertDialog("Data Updated Successfully.");

                } else {

                    stopProgressBar();
                    showAlertDialog("Error Updating Data.");
//                    System.out.println(response.code());
//                    System.out.println(response.errorBody().toString());

                }

            }

            @Override
            public void onFailure(Call<Family> call, Throwable t) {

            }
        });

    }

    private void apiCallToGetChildInformation() {

        showProgressBar("Loading Please Wait ...");

        String query = "query { getFamily(_id: \"" + objectId + "\") { eligibleCoupleName { husband wife eligibleCoupleNameId } children { childrenObjectId eligibleCoupleNameObjectId childname childRCH vaccination { DPTB_OPVB_MR2_VitA2 MR1_VitA1 OPV_3_IPV2_Penta3_Rota3 OPV_2_Penta_2_Rota_2 OPV1_IPV1_Penta1_Rota1 BCG B2VIT OPV }}}}";

        Call<Family> getChildInformation = apiInterface.getInformation(query);

        getChildInformation.enqueue(new Callback<Family>() {
            @Override
            public void onResponse(Call<Family> call, Response<Family> response) {

//                System.out.println(response.isSuccessful() + "   " + response.body());
                if(response.isSuccessful() && response.body() != null) {

                    children = response.body().getData().getGetFamily().getChildren();
                    eligibleCoupleNames = response.body().getData().getGetFamily().getEligibleCoupleName();

                    childDropdownList.clear();
                    childDropdownList.add("---Select Child---");
                    for(Child child : children) {
                        childDropdownList.add(child.getChildname());
                    }


                    if(!addChild) {

                        fatherList.clear();
                        fatherList.add("---Select Father---");
                        motherList.clear();
                        motherList.add("---Select Mother---");
                        for(EligibleCoupleName eligibleCoupleName : response.body().getData().getGetFamily().getEligibleCoupleName()) {
                            if(eligibleCoupleName.getHusband() != null) {
                                fatherList.add(eligibleCoupleName.getHusband());
                            }
                            if(eligibleCoupleName.getWife() != null) {
                                motherList.add(eligibleCoupleName.getWife());
                            }
                        }

                        fatherNameMultiSpinner.setItems(fatherList);
                        motherNameMultiSpinner.setItems(motherList);

                    }

                    addChildButton.setVisibility(View.VISIBLE);
                    textViewChild.setVisibility(View.VISIBLE);
                    childNameSpinner.setVisibility(View.VISIBLE);
                    childNameSpinner.setItems(childDropdownList);
                    stopProgressBar();

                }
                else {
                    showAlertDialog("Not Found.");
                    stopProgressBar();
                }

            }

            @Override
            public void onFailure(Call<Family> call, Throwable t) {
//                System.out.println(t.getMessage());
            }
        });

    }

    private void setDataToLayout(Child child) {

        dateDPTB_OPVB_MR2_VitA2.removeAllViews();
        DPTB_OPVB_MR2_VitA2Id = 0;
        dateMR1_VitA1.removeAllViews();
        MR1_VitA1Id = 0;
        dateOPV_3_IPV2_Penta3_Rota3.removeAllViews();
        OPV_3_IPV2_Penta3_Rota3Id = 0;
        dateOPV_2_Penta_2_Rota_2.removeAllViews();
        OPV_2_Penta_2_Rota_2Id = 0;
        dateOPV1_IPV1_Penta1_Rota1.removeAllViews();
        OPV1_IPV1_Penta1_Rota1Id = 0;
        dateBCG.removeAllViews();
        BCGId = 0;
        dateB2VIT.removeAllViews();
        B2VITId = 0;
        dateOPV.removeAllViews();
        OPVId = 0;

//        editTextChildName.setText(child.getChildname());

        int index = 0;
        for(EligibleCoupleName eligibleCoupleName : eligibleCoupleNames) {
            if(eligibleCoupleName.getEligibleCoupleNameId().equals(child.getEligibleCoupleNameObjectId())){
//                editTextFatherName.setText(eligibleCoupleName.getHusband());
//                editTextMotherName.setText(eligibleCoupleName.getWife());
                fatherNameMultiSpinner.setSelectedIndex(index + 1);
                motherNameMultiSpinner.setSelectedIndex(index + 1);
                selectedParentIndex = index + 1;
            }
            index++;
        }

        editTextChildRchId.setText(child.getChildRCH());

        for(String date : child.getVaccination().getDPTBOPVBMR2VitA2()) {
            layoutInflater.inflate(R.layout.familydetail_vaccinationdate, dateDPTB_OPVB_MR2_VitA2, true).findViewById(R.id.editVaccinationDate).setId(DPTB_OPVB_MR2_VitA2Id);
            ((TextInputEditText) dateDPTB_OPVB_MR2_VitA2.findViewById(DPTB_OPVB_MR2_VitA2Id)).setText(String.valueOf(date));
            DPTB_OPVB_MR2_VitA2Id++;
        }

        for(String date : child.getVaccination().getMR1VitA1()) {
            layoutInflater.inflate(R.layout.familydetail_vaccinationdate, dateMR1_VitA1, true).findViewById(R.id.editVaccinationDate).setId(MR1_VitA1Id);
            ((TextInputEditText) dateMR1_VitA1.findViewById(MR1_VitA1Id)).setText(String.valueOf(date));
            MR1_VitA1Id++;
        }

        for(String date : child.getVaccination().getOPV3IPV2Penta3Rota3()) {
            layoutInflater.inflate(R.layout.familydetail_vaccinationdate, dateOPV_3_IPV2_Penta3_Rota3, true).findViewById(R.id.editVaccinationDate).setId(OPV_3_IPV2_Penta3_Rota3Id);
            ((TextInputEditText) dateOPV_3_IPV2_Penta3_Rota3.findViewById(OPV_3_IPV2_Penta3_Rota3Id)).setText(String.valueOf(date));
            OPV_3_IPV2_Penta3_Rota3Id++;
        }

        for(String date : child.getVaccination().getOPV2Penta2Rota2()) {
            layoutInflater.inflate(R.layout.familydetail_vaccinationdate, dateOPV_2_Penta_2_Rota_2, true).findViewById(R.id.editVaccinationDate).setId(OPV_2_Penta_2_Rota_2Id);
            ((TextInputEditText) dateOPV_2_Penta_2_Rota_2.findViewById(OPV_2_Penta_2_Rota_2Id)).setText(String.valueOf(date));
            OPV_2_Penta_2_Rota_2Id++;
        }

        for(String date : child.getVaccination().getOPV1IPV1Penta1Rota1()) {
            layoutInflater.inflate(R.layout.familydetail_vaccinationdate, dateOPV1_IPV1_Penta1_Rota1, true).findViewById(R.id.editVaccinationDate).setId(OPV1_IPV1_Penta1_Rota1Id);
            ((TextInputEditText) dateOPV1_IPV1_Penta1_Rota1.findViewById(OPV1_IPV1_Penta1_Rota1Id)).setText(String.valueOf(date));
            OPV1_IPV1_Penta1_Rota1Id++;
        }

        for(String date : child.getVaccination().getBCG()) {
            layoutInflater.inflate(R.layout.familydetail_vaccinationdate, dateBCG, true).findViewById(R.id.editVaccinationDate).setId(BCGId);
            ((TextInputEditText) dateBCG.findViewById(BCGId)).setText(String.valueOf(date));
            BCGId++;
        }

        for(String date : child.getVaccination().getB2VIT()) {
            layoutInflater.inflate(R.layout.familydetail_vaccinationdate, dateB2VIT, true).findViewById(R.id.editVaccinationDate).setId(B2VITId);
            ((TextInputEditText) dateB2VIT.findViewById(B2VITId)).setText(String.valueOf(date));
            B2VITId++;
        }

        for(String date : child.getVaccination().getOPV()) {
            layoutInflater.inflate(R.layout.familydetail_vaccinationdate, dateOPV, true).findViewById(R.id.editVaccinationDate).setId(OPVId);
            ((TextInputEditText) dateOPV.findViewById(OPVId)).setText(String.valueOf(date));
            OPVId++;
        }

        addVaccinationTextWatcher();

    }

    private void addVaccinationTextWatcher() {

        vaccinationDateTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                for(int id = 0; id < children.get(selectedChildIndex).getVaccination().getDPTBOPVBMR2VitA2().size(); id++) {
                    if (s.hashCode() == ((TextInputEditText) dateDPTB_OPVB_MR2_VitA2.findViewById(id)).getText().hashCode()) {

                        if (!s.toString().equals(children.get(selectedChildIndex).getVaccination().getDPTBOPVBMR2VitA2().get(id).toString())) {

                            DPTB_OPVB_MR2_VitA2List.put(id, ((TextInputEditText) dateDPTB_OPVB_MR2_VitA2.findViewById(id)).getText().toString());
                            changedInformation.put("DPTB_OPVB_MR2_VitA2", DPTB_OPVB_MR2_VitA2List);
//                            System.out.println("Changes " + changedInformation.get("DPTB_OPVB_MR2_VitA2"));

                            if (saveChildInformation.getVisibility() == View.GONE) {
                                saveChildInformation.setVisibility(View.VISIBLE);
                            }

                        } else {
                            if(DPTB_OPVB_MR2_VitA2List.isEmpty()) {
                                changedInformation.remove("DPTB_OPVB_MR2_VitA2");
                            } else {
                                DPTB_OPVB_MR2_VitA2List.remove(id);
                                if(DPTB_OPVB_MR2_VitA2List.isEmpty()) {
                                    changedInformation.remove("DPTB_OPVB_MR2_VitA2");
                                } else {
                                    changedInformation.put("DPTB_OPVB_MR2_VitA2", DPTB_OPVB_MR2_VitA2List);
                                }
                            }

                            if (changedInformation.isEmpty() && addDateClicked == 0) {
                                saveChildInformation.setVisibility(View.GONE);
                            }

                        }

                    }
//                    System.out.println("Changes DPTB_OPVB_MR2_VitA2" + changedInformation.get("DPTB_OPVB_MR2_VitA2"));
                }

                for(int id = 0; id < children.get(selectedChildIndex).getVaccination().getMR1VitA1().size(); id++) {
                    if (s.hashCode() == ((TextInputEditText) dateMR1_VitA1.findViewById(id)).getText().hashCode()) {

                        if (!s.toString().equals(children.get(selectedChildIndex).getVaccination().getMR1VitA1().get(id).toString())) {

                            MR1_VitA1List.put(id, ((TextInputEditText) dateMR1_VitA1.findViewById(id)).getText().toString());
                            changedInformation.put("MR1_VitA1", MR1_VitA1List);
//                            System.out.println("Changes " + changedInformation.get("MR1_VitA1"));

                            if (saveChildInformation.getVisibility() == View.GONE) {
                                saveChildInformation.setVisibility(View.VISIBLE);
                            }

                        } else {
                            if(MR1_VitA1List.isEmpty()){
                                changedInformation.remove("MR1_VitA1");
                            } else {
                                MR1_VitA1List.remove(id);
                                if(MR1_VitA1List.isEmpty()) {
                                    changedInformation.remove("MR1_VitA1");
                                } else {
                                    changedInformation.put("MR1_VitA1", MR1_VitA1List);
                                }
                            }

                            if (changedInformation.isEmpty() && addDateClicked == 0) {
                                saveChildInformation.setVisibility(View.GONE);
                            }

                        }

                    }
//                    System.out.println("Changes MR1_VitA1" + changedInformation.get("MR1_VitA1"));
                }

                for(int id = 0; id < children.get(selectedChildIndex).getVaccination().getOPV3IPV2Penta3Rota3().size(); id++) {
                    if (s.hashCode() == ((TextInputEditText) dateOPV_3_IPV2_Penta3_Rota3.findViewById(id)).getText().hashCode()) {

                        if (!s.toString().equals(children.get(selectedChildIndex).getVaccination().getOPV3IPV2Penta3Rota3().get(id).toString())) {

                            OPV_3_IPV2_Penta3_Rota3List.put(id, ((TextInputEditText) dateOPV_3_IPV2_Penta3_Rota3.findViewById(id)).getText().toString());
                            changedInformation.put("OPV_3_IPV2_Penta3_Rota3", OPV_3_IPV2_Penta3_Rota3List);
//                            System.out.println("Changes " + changedInformation.get("OPV_3_IPV2_Penta3_Rota3"));

                            if (saveChildInformation.getVisibility() == View.GONE) {
                                saveChildInformation.setVisibility(View.VISIBLE);
                            }

                        } else {
                            if(OPV_3_IPV2_Penta3_Rota3List.isEmpty()){
                                changedInformation.remove("OPV_3_IPV2_Penta3_Rota3");
                            } else {
                                OPV_3_IPV2_Penta3_Rota3List.remove(id);
                                if(OPV_3_IPV2_Penta3_Rota3List.isEmpty()) {
                                    changedInformation.remove("OPV_3_IPV2_Penta3_Rota3");
                                } else {
                                    changedInformation.put("OPV_3_IPV2_Penta3_Rota3", OPV_3_IPV2_Penta3_Rota3List);
                                }
                            }

                            if (changedInformation.isEmpty() && addDateClicked == 0) {
                                saveChildInformation.setVisibility(View.GONE);
                            }

                        }

                    }
//                    System.out.println("Changes OPV_3_IPV2_Penta3_Rota3" + changedInformation.get("OPV_3_IPV2_Penta3_Rota3"));
                }

                for(int id = 0; id < children.get(selectedChildIndex).getVaccination().getOPV2Penta2Rota2().size(); id++) {
                    if (s.hashCode() == ((TextInputEditText) dateOPV_2_Penta_2_Rota_2.findViewById(id)).getText().hashCode()) {

                        if (!s.toString().equals(children.get(selectedChildIndex).getVaccination().getOPV2Penta2Rota2().get(id).toString())) {

                            OPV_2_Penta_2_Rota_2List.put(id, ((TextInputEditText) dateOPV_2_Penta_2_Rota_2.findViewById(id)).getText().toString());
                            changedInformation.put("OPV_2_Penta_2_Rota_2", OPV_2_Penta_2_Rota_2List);
//                            System.out.println("Changes " + changedInformation.get("OPV_2_Penta_2_Rota_2"));

                            if (saveChildInformation.getVisibility() == View.GONE) {
                                saveChildInformation.setVisibility(View.VISIBLE);
                            }

                        } else {
                            if(OPV_2_Penta_2_Rota_2List.isEmpty()){
                                changedInformation.remove("OPV_2_Penta_2_Rota_2");
                            } else {
                                OPV_2_Penta_2_Rota_2List.remove(id);
                                if(OPV_2_Penta_2_Rota_2List.isEmpty()) {
                                    changedInformation.remove("OPV_2_Penta_2_Rota_2");
                                } else {
                                    changedInformation.put("OPV_2_Penta_2_Rota_2", OPV_2_Penta_2_Rota_2List);
                                }
                            }

                            if (changedInformation.isEmpty() && addDateClicked == 0) {
                                saveChildInformation.setVisibility(View.GONE);
                            }

                        }

                    }
//                    System.out.println("Changes OPV_2_Penta_2_Rota_2" + changedInformation.get("OPV_2_Penta_2_Rota_2"));
                }

                for(int id = 0; id < children.get(selectedChildIndex).getVaccination().getOPV1IPV1Penta1Rota1().size(); id++) {
                    if (s.hashCode() == ((TextInputEditText) dateOPV1_IPV1_Penta1_Rota1.findViewById(id)).getText().hashCode()) {

                        if (!s.toString().equals(children.get(selectedChildIndex).getVaccination().getOPV1IPV1Penta1Rota1().get(id).toString())) {

                            OPV1_IPV1_Penta1_Rota1List.put(id, ((TextInputEditText) dateOPV1_IPV1_Penta1_Rota1.findViewById(id)).getText().toString());
                            changedInformation.put("OPV1_IPV1_Penta1_Rota1", OPV1_IPV1_Penta1_Rota1List);
//                            System.out.println("Changes " + changedInformation.get("OPV1_IPV1_Penta1_Rota1"));

                            if (saveChildInformation.getVisibility() == View.GONE) {
                                saveChildInformation.setVisibility(View.VISIBLE);
                            }

                        } else {
                            if(OPV1_IPV1_Penta1_Rota1List.isEmpty()){
                                changedInformation.remove("OPV1_IPV1_Penta1_Rota1");
                            } else {
                                OPV1_IPV1_Penta1_Rota1List.remove(id);
                                if(OPV1_IPV1_Penta1_Rota1List.isEmpty()) {
                                    changedInformation.remove("OPV1_IPV1_Penta1_Rota1");
                                } else {
                                    changedInformation.put("OPV1_IPV1_Penta1_Rota1", OPV1_IPV1_Penta1_Rota1List);
                                }
                            }

                            if (changedInformation.isEmpty() && addDateClicked == 0) {
                                saveChildInformation.setVisibility(View.GONE);
                            }

                        }

                    }
//                    System.out.println("Changes OPV1_IPV1_Penta1_Rota1" + changedInformation.get("OPV1_IPV1_Penta1_Rota1"));
                }

                for(int id = 0; id < children.get(selectedChildIndex).getVaccination().getBCG().size(); id++) {
                    if (s.hashCode() == ((TextInputEditText) dateBCG.findViewById(id)).getText().hashCode()) {

                        if (!s.toString().equals(children.get(selectedChildIndex).getVaccination().getBCG().get(id).toString())) {

                            BCGList.put(id, ((TextInputEditText) dateBCG.findViewById(id)).getText().toString());
                            changedInformation.put("BCG", BCGList);
//                            System.out.println("Changes " + changedInformation.get("BCG"));

                            if (saveChildInformation.getVisibility() == View.GONE) {
                                saveChildInformation.setVisibility(View.VISIBLE);
                            }

                        } else {
                            if(BCGList.isEmpty()){
                                changedInformation.remove("BCG");
                            } else {
                                BCGList.remove(id);
                                if(BCGList.isEmpty()) {
                                    changedInformation.remove("BCG");
                                } else {
                                    changedInformation.put("BCG", BCGList);
                                }
                            }

                            if (changedInformation.isEmpty() && addDateClicked == 0) {
                                saveChildInformation.setVisibility(View.GONE);
                            }

                        }

                    }
//                    System.out.println("Changes BCG" + changedInformation.get("BCG"));
                }

                for(int id = 0; id < children.get(selectedChildIndex).getVaccination().getB2VIT().size(); id++) {
                    if (s.hashCode() == ((TextInputEditText) dateB2VIT.findViewById(id)).getText().hashCode()) {

                        if (!s.toString().equals(children.get(selectedChildIndex).getVaccination().getB2VIT().get(id).toString())) {

                            B2VITList.put(id, ((TextInputEditText) dateB2VIT.findViewById(id)).getText().toString());
                            changedInformation.put("B2VIT", B2VITList);
//                            System.out.println("Changes " + changedInformation.get("B2VIT"));

                            if (saveChildInformation.getVisibility() == View.GONE) {
                                saveChildInformation.setVisibility(View.VISIBLE);
                            }

                        } else {
                            if(B2VITList.isEmpty()){
                                changedInformation.remove("B2VIT");
                            } else {
                                B2VITList.remove(id);
                                if(B2VITList.isEmpty()) {
                                    changedInformation.remove("B2VIT");
                                } else {
                                    changedInformation.put("B2VIT", B2VITList);
                                }
                            }

                            if (changedInformation.isEmpty() && addDateClicked == 0) {
                                saveChildInformation.setVisibility(View.GONE);
                            }

                        }

                    }
//                    System.out.println("Changes B2VIT" + changedInformation.get("B2VIT"));
                }

                for(int id = 0; id < children.get(selectedChildIndex).getVaccination().getOPV().size(); id++) {
                    if (s.hashCode() == ((TextInputEditText) dateOPV.findViewById(id)).getText().hashCode()) {

                        if (!s.toString().equals(children.get(selectedChildIndex).getVaccination().getOPV().get(id).toString())) {

                            OPVList.put(id, ((TextInputEditText) dateOPV.findViewById(id)).getText().toString());
                            changedInformation.put("OPV", OPVList);
//                            System.out.println("Changes " + changedInformation.get("OPV"));

                            if (saveChildInformation.getVisibility() == View.GONE) {
                                saveChildInformation.setVisibility(View.VISIBLE);
                            }

                        } else {
                            if(OPVList.isEmpty()){
                                changedInformation.remove("OPV");
                            } else {
                                OPVList.remove(id);
                                if(OPVList.isEmpty()) {
                                    changedInformation.remove("OPV");
                                } else {
                                    changedInformation.put("OPV", OPVList);
                                }
                            }

                            if (changedInformation.isEmpty() && addDateClicked == 0) {
                                saveChildInformation.setVisibility(View.GONE);
                            }

                        }

                    }
//                    System.out.println("Changes OPV" + changedInformation.get("OPV"));
                }

            }
        };

        for(int id = 0; id < children.get(selectedChildIndex).getVaccination().getDPTBOPVBMR2VitA2().size(); id++) {
            ((TextInputEditText) dateDPTB_OPVB_MR2_VitA2.findViewById(id)).addTextChangedListener(vaccinationDateTextWatcher);
            final int finalId = id;
            ((TextInputEditText) dateDPTB_OPVB_MR2_VitA2.findViewById(id)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                            String date = dayOfMonth + "/" + (month + 1) + "/" + year;

                            ((TextInputEditText) dateDPTB_OPVB_MR2_VitA2.findViewById(finalId)).setText(date);

                        }
                    };

                    DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), R.style.Theme_MaterialComponents_Light_Dialog_MinWidth, onDateSetListener, year, month, day);

                    datePickerDialog.getWindow();
                    datePickerDialog.show();
                }
            });
        }

        for(int id = 0; id < children.get(selectedChildIndex).getVaccination().getMR1VitA1().size(); id++) {
            ((TextInputEditText) dateMR1_VitA1.findViewById(id)).addTextChangedListener(vaccinationDateTextWatcher);

            final int finalId = id;
            ((TextInputEditText) dateMR1_VitA1.findViewById(id)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                            String date = dayOfMonth + "/" + (month + 1) + "/" + year;

                            ((TextInputEditText) dateMR1_VitA1.findViewById(finalId)).setText(date);

                        }
                    };

                    DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), R.style.Theme_MaterialComponents_Light_Dialog_MinWidth, onDateSetListener, year, month, day);

                    datePickerDialog.getWindow();
                    datePickerDialog.show();
                }
            });

        }

        for(int id = 0; id < children.get(selectedChildIndex).getVaccination().getOPV3IPV2Penta3Rota3().size(); id++) {
            ((TextInputEditText) dateOPV_3_IPV2_Penta3_Rota3.findViewById(id)).addTextChangedListener(vaccinationDateTextWatcher);

            final int finalId = id;
            ((TextInputEditText) dateOPV_3_IPV2_Penta3_Rota3.findViewById(id)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                            String date = dayOfMonth + "/" + (month + 1) + "/" + year;

                            ((TextInputEditText) dateOPV_3_IPV2_Penta3_Rota3.findViewById(finalId)).setText(date);

                        }
                    };

                    DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), R.style.Theme_MaterialComponents_Light_Dialog_MinWidth, onDateSetListener, year, month, day);

                    datePickerDialog.getWindow();
                    datePickerDialog.show();
                }
            });

        }

        for(int id = 0; id < children.get(selectedChildIndex).getVaccination().getOPV2Penta2Rota2().size(); id++) {
            ((TextInputEditText) dateOPV_2_Penta_2_Rota_2.findViewById(id)).addTextChangedListener(vaccinationDateTextWatcher);

            final int finalId = id;
            ((TextInputEditText) dateOPV_2_Penta_2_Rota_2.findViewById(id)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                            String date = dayOfMonth + "/" + (month + 1) + "/" + year;

                            ((TextInputEditText) dateOPV_2_Penta_2_Rota_2.findViewById(finalId)).setText(date);

                        }
                    };

                    DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), R.style.Theme_MaterialComponents_Light_Dialog_MinWidth, onDateSetListener, year, month, day);

                    datePickerDialog.getWindow();
                    datePickerDialog.show();
                }
            });

        }

        for(int id = 0; id < children.get(selectedChildIndex).getVaccination().getOPV1IPV1Penta1Rota1().size(); id++) {
            ((TextInputEditText) dateOPV1_IPV1_Penta1_Rota1.findViewById(id)).addTextChangedListener(vaccinationDateTextWatcher);

            final int finalId = id;
            ((TextInputEditText) dateOPV1_IPV1_Penta1_Rota1.findViewById(id)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                            String date = dayOfMonth + "/" + (month + 1) + "/" + year;

                            ((TextInputEditText) dateOPV1_IPV1_Penta1_Rota1.findViewById(finalId)).setText(date);

                        }
                    };

                    DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), R.style.Theme_MaterialComponents_Light_Dialog_MinWidth, onDateSetListener, year, month, day);

                    datePickerDialog.getWindow();
                    datePickerDialog.show();
                }
            });

        }

        for(int id = 0; id < children.get(selectedChildIndex).getVaccination().getBCG().size(); id++) {
            ((TextInputEditText) dateBCG.findViewById(id)).addTextChangedListener(vaccinationDateTextWatcher);

            final int finalId = id;
            ((TextInputEditText) dateBCG.findViewById(id)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                            String date = dayOfMonth + "/" + (month + 1) + "/" + year;

                            ((TextInputEditText) dateBCG.findViewById(finalId)).setText(date);

                        }
                    };

                    DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), R.style.Theme_MaterialComponents_Light_Dialog_MinWidth, onDateSetListener, year, month, day);

                    datePickerDialog.getWindow();
                    datePickerDialog.show();
                }
            });

        }

        for(int id = 0; id < children.get(selectedChildIndex).getVaccination().getB2VIT().size(); id++) {
            ((TextInputEditText) dateB2VIT.findViewById(id)).addTextChangedListener(vaccinationDateTextWatcher);

            final int finalId = id;
            ((TextInputEditText) dateB2VIT.findViewById(id)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                            String date = dayOfMonth + "/" + (month + 1) + "/" + year;

                            ((TextInputEditText) dateB2VIT.findViewById(finalId)).setText(date);

                        }
                    };

                    DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), R.style.Theme_MaterialComponents_Light_Dialog_MinWidth, onDateSetListener, year, month, day);

                    datePickerDialog.getWindow();
                    datePickerDialog.show();
                }
            });

        }

        for(int id = 0; id < children.get(selectedChildIndex).getVaccination().getOPV().size(); id++) {
            ((TextInputEditText) dateOPV.findViewById(id)).addTextChangedListener(vaccinationDateTextWatcher);

            final int finalId = id;
            ((TextInputEditText) dateOPV.findViewById(id)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                            String date = dayOfMonth + "/" + (month + 1) + "/" + year;

                            ((TextInputEditText) dateOPV.findViewById(finalId)).setText(date);

                        }
                    };

                    DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), R.style.Theme_MaterialComponents_Light_Dialog_MinWidth, onDateSetListener, year, month, day);

                    datePickerDialog.getWindow();
                    datePickerDialog.show();
                }
            });

        }

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

    private void clearDataFromLayout() {

//        editTextMotherName.getText().clear();
//        editTextMotherName.setEnabled(true);
//        editTextFatherName.getText().clear();
//        editTextFatherName.setEnabled(true);
//        editTextChildName.getText().clear();
        editTextChildRchId.getText().clear();

        newFatherNameMultiSpinner.setSelectedIndex(0);
        newMotherNameMultiSpinner.setSelectedIndex(0);

        dateDPTB_OPVB_MR2_VitA2.removeAllViews();
        DPTB_OPVB_MR2_VitA2Id = 0;
        dateMR1_VitA1.removeAllViews();
        MR1_VitA1Id = 0;
        dateOPV_3_IPV2_Penta3_Rota3.removeAllViews();
        OPV_3_IPV2_Penta3_Rota3Id = 0;
        dateOPV_2_Penta_2_Rota_2.removeAllViews();
        OPV_2_Penta_2_Rota_2Id = 0;
        dateOPV1_IPV1_Penta1_Rota1.removeAllViews();
        OPV1_IPV1_Penta1_Rota1Id = 0;
        dateBCG.removeAllViews();
        BCGId = 0;
        dateB2VIT.removeAllViews();
        B2VITId = 0;
        dateOPV.removeAllViews();
        OPVId = 0;

    }

//    private void removeListenersFromComponents() {
//
////        editTextChildName.removeTextChangedListener(textWatcher);
//        editTextChildRchId.removeTextChangedListener(textWatcher);
//
//        childNameSpinner.setOnItemSelectedListener(null);
//
//    }

    private void addValidation() {
        awesomeValidation.addValidation(editTextChildRchId, RegexTemplate.NOT_EMPTY, "Please enter child RCH" +
                " Id.");
    }

}