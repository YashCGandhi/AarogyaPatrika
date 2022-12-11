package com.hospitalmanagement.mainapp.Fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
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
import com.hospitalmanagement.mainapp.pojo.EligibleCoupleName;
import com.hospitalmanagement.mainapp.pojo.Family;
import com.hospitalmanagement.mainapp.pojo.Member;
import com.hospitalmanagement.mainapp.pojo.MutationQueryFamilyDetail;
import com.hospitalmanagement.mainapp.pojo.Variables;
import com.hospitalmanagement.mainapp.retrofitService.apiClient.ApiClient;
import com.hospitalmanagement.mainapp.retrofitService.apiInterface.ApiInterface;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentFamilyDetailCoupleInformation extends Fragment {

    private MaterialSpinner husbandNameSpinner, newHusbandNameSpinner;
    private MaterialSpinner wifeNameSpinner, newWifeNameSpinner;
    private MaterialButton saveCoupleInformation, btnAddCutAntaraOralPillsDate;
    private RelativeLayout coupleLayout;
    private RadioGroup radioGroupFamilyPlanningMethod, radioGroupFutureMethod;
    private TextInputEditText editTextTotalMaleChildren, editCoupleId;
    private TextInputEditText editTextTotalFemaleChildren;
    private TextInputEditText editTextAgeLastChild;
    private RadioGroup radioGroupGenderLastChild;
    private LinearLayout ifNoMethodLayout, newCoupleSpinnerLinearLayout, cutAntaraOralPillsDateLinearLayout, dateIfNoMethodParentLinearLayout;
//    private TextInputEditText editTextFutureMethod;
    private TextInputEditText editTextDateOfGroupMeeting;
    private TextInputEditText editTextDateOfVisit;
    private MaterialButton addCoupleButton;
    private TextView textViewHusband, newTextViewHusband;
    private TextView textViewWife, newTextViewWife;
//    private TextInputEditText editTextHusbandName;
//    private TextInputEditText editTextWifeName;
    private NestedScrollView nestedScrollViewCouple;

    private int selectedCoupleIndex, selectedMemberWifeIndex = -1, selectedMemberHusbandIndex = -1;
    private boolean addCouple = false;

    //Validation
    private AwesomeValidation awesomeValidation;

    private TextWatcher textWatcher, cutAntaraOralPillsDateTextWatcher;
    private LayoutInflater layoutInflater;
    private List<EligibleCoupleName> eligibleCoupleNames;
    private List<Member> members;
    private List<String> maleMemberNamesList, femaleMemberNamesList;
    private List<String> husbandNames;
    private List<String> wifeNames;
    private HashMap<String, Object> changedInformation;
    private HashMap<Integer, Object> cutAntaraOralPillsDateList;
    private String objectId;
    private ProgressDialog progressDialog;
    private Handler handler;
    private AlertDialog.Builder alertDialogBuilder;
    private ApiInterface apiInterface;
    private DatePickerDialog datePickerDialog;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private String date;
    private Calendar calendar;
    private int day, month, year;
    private int dateFlag = -1;
    private int cutAntaraOralPillsDateId = 0, addDateClicked = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();

        if(bundle != null) {
            objectId = bundle.getString("objectId");
        }

        inflater = LayoutInflater.from(new ContextThemeWrapper(getContext(), R.style.AppTheme));

        return inflater.inflate(R.layout.familydetail_coupleinformation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        husbandNames = new ArrayList<>();
        wifeNames = new ArrayList<>();
        maleMemberNamesList = new ArrayList<>();
        femaleMemberNamesList = new ArrayList<>();
        changedInformation = new HashMap<>();
        handler = new Handler();
        cutAntaraOralPillsDateList = new HashMap<>();


        progressDialog = new ProgressDialog(getContext());
        alertDialogBuilder = new AlertDialog.Builder(getContext());
        layoutInflater = LayoutInflater.from(new ContextThemeWrapper(getContext(), R.style.AppTheme));
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

        //Validation
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);


        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                date = year + "/" + (month + 1) + "/" + dayOfMonth;

                if(dateFlag == 0) {
                    editTextDateOfVisit.setText(date);
                } else if(dateFlag == 1) {
                    editTextDateOfGroupMeeting.setText(date);
                }

            }
        };

        datePickerDialog = new DatePickerDialog(getContext(), R.style.Theme_MaterialComponents_Light_Dialog_MinWidth, onDateSetListener, year, month, day);

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

                    if(s.hashCode()  == editTextTotalMaleChildren.getText().hashCode()) {

                        if(eligibleCoupleNames.get(selectedCoupleIndex).getTotalChildren() != null && eligibleCoupleNames.get(selectedCoupleIndex).getTotalChildren().getMale() != null) {
                            if (!s.toString().equals(String.valueOf(eligibleCoupleNames.get(selectedCoupleIndex).getTotalChildren().getMale()))) {

                                if(editTextTotalMaleChildren.getText().toString().trim().length() != 0 && Integer.parseInt(editTextTotalMaleChildren.getText().toString().trim()) > 0) {

                                    awesomeValidation.clear();
                                    awesomeValidation.addValidation(editCoupleId, RegexTemplate.NOT_EMPTY, "कृपया " +
                                            "couple id प्रविष्ट करा");
//                                    awesomeValidation.addValidation(editTextTotalMaleChildren, RegexTemplate.NOT_EMPTY,
//                                            "कृपया एकूण पुरुष मुले प्रविष्ट करा");
//                                    awesomeValidation.addValidation(editTextTotalFemaleChildren, RegexTemplate.NOT_EMPTY,
//                                            "कृपया एकूण महिला मुले प्रविष्ट करा");
                                    awesomeValidation.addValidation(editTextAgeLastChild, RegexTemplate.NOT_EMPTY, "कृपया शेवटच्या मुलाचे वय प्रविष्ट करा");

                                    changedInformation.put("male", editTextTotalMaleChildren.getText().toString());
//                                System.out.println("Changes " + changedInformation.get("male"));

                                    if (saveCoupleInformation.getVisibility() == View.GONE) {
                                        saveCoupleInformation.setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    awesomeValidation.clear();
                                    awesomeValidation.addValidation(editCoupleId, RegexTemplate.NOT_EMPTY, "कृपया " +
                                            "couple id प्रविष्ट करा");
                                }
                            } else {

                                changedInformation.remove("male");
                                if (changedInformation.isEmpty() && addDateClicked == 0) {
                                    saveCoupleInformation.setVisibility(View.GONE);
                                }

                            }
                        } else {
                                if (editTextTotalMaleChildren.getText().toString().trim().length() != 0 && Integer.parseInt(editTextTotalMaleChildren.getText().toString().trim()) > 0) {

                                    awesomeValidation.clear();
                                    awesomeValidation.addValidation(editCoupleId, RegexTemplate.NOT_EMPTY, "कृपया " +
                                            "couple id प्रविष्ट करा");
//                                    awesomeValidation.addValidation(editTextTotalMaleChildren, RegexTemplate.NOT_EMPTY,
//                                            "कृपया एकूण पुरुष मुले प्रविष्ट करा");
//                                    awesomeValidation.addValidation(editTextTotalFemaleChildren, RegexTemplate.NOT_EMPTY,
//                                            "कृपया एकूण महिला मुले प्रविष्ट करा");
                                    awesomeValidation.addValidation(editTextAgeLastChild, RegexTemplate.NOT_EMPTY, "कृपया शेवटच्या मुलाचे वय प्रविष्ट करा");

                                    changedInformation.put("male", editTextTotalMaleChildren.getText().toString());
//                                System.out.println("Changes " + changedInformation.get("male"));

                                    if (saveCoupleInformation.getVisibility() == View.GONE) {
                                        saveCoupleInformation.setVisibility(View.VISIBLE);
                                    }


                                } else {
                                    awesomeValidation.clear();
                                    awesomeValidation.addValidation(editCoupleId, RegexTemplate.NOT_EMPTY, "कृपया " +
                                            "couple id प्रविष्ट करा");
                                }

                        }

                    } else if(s.hashCode() == editTextTotalFemaleChildren.getText().hashCode()) {

                        if(eligibleCoupleNames.get(selectedCoupleIndex).getTotalChildren() != null && eligibleCoupleNames.get(selectedCoupleIndex).getTotalChildren().getFemale() != null) {
                            if (!s.toString().equals(String.valueOf(eligibleCoupleNames.get(selectedCoupleIndex).getTotalChildren().getFemale()))) {

                                if(editTextTotalFemaleChildren.getText().toString().trim().length() != 0 && Integer.parseInt(editTextTotalFemaleChildren.getText().toString().trim()) > 0) {

                                    awesomeValidation.clear();
                                    awesomeValidation.addValidation(editCoupleId, RegexTemplate.NOT_EMPTY, "कृपया " +
                                            "couple id प्रविष्ट करा");
//                                    awesomeValidation.addValidation(editTextTotalMaleChildren, RegexTemplate.NOT_EMPTY,
//                                            "कृपया एकूण पुरुष मुले प्रविष्ट करा");
//                                    awesomeValidation.addValidation(editTextTotalFemaleChildren, RegexTemplate.NOT_EMPTY,
//                                            "कृपया एकूण महिला मुले प्रविष्ट करा");
                                    awesomeValidation.addValidation(editTextAgeLastChild, RegexTemplate.NOT_EMPTY, "कृपया शेवटच्या मुलाचे वय प्रविष्ट करा");
                                    changedInformation.put("female", editTextTotalFemaleChildren.getText().toString());
    //                                System.out.println("Changes " + changedInformation.get("female"));

                                    if (saveCoupleInformation.getVisibility() == View.GONE) {
                                        saveCoupleInformation.setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    awesomeValidation.clear();
                                    awesomeValidation.addValidation(editCoupleId, RegexTemplate.NOT_EMPTY, "कृपया " +
                                            "couple id प्रविष्ट करा");
                                }

                            } else {

                                changedInformation.remove("female");
                                if (changedInformation.isEmpty() && addDateClicked == 0) {
                                    saveCoupleInformation.setVisibility(View.GONE);
                                }

                            }
                        } else {
                            if (editTextTotalFemaleChildren.getText().toString().trim().length() != 0 && Integer.parseInt(editTextTotalFemaleChildren.getText().toString().trim()) > 0) {

                                awesomeValidation.clear();
                                awesomeValidation.addValidation(editCoupleId, RegexTemplate.NOT_EMPTY, "कृपया " +
                                        "couple id प्रविष्ट करा");
//                                    awesomeValidation.addValidation(editTextTotalMaleChildren, RegexTemplate.NOT_EMPTY,
//                                            "कृपया एकूण पुरुष मुले प्रविष्ट करा");
//                                    awesomeValidation.addValidation(editTextTotalFemaleChildren, RegexTemplate.NOT_EMPTY,
//                                            "कृपया एकूण महिला मुले प्रविष्ट करा");
                                awesomeValidation.addValidation(editTextAgeLastChild, RegexTemplate.NOT_EMPTY, "कृपया शेवटच्या मुलाचे वय प्रविष्ट करा");
                                if(editTextTotalFemaleChildren.getText().toString().trim().length() != 0) {
                                    changedInformation.put("female", editTextTotalFemaleChildren.getText().toString());
    //                                System.out.println("Changes " + changedInformation.get("female"));

                                    if (saveCoupleInformation.getVisibility() == View.GONE) {
                                        saveCoupleInformation.setVisibility(View.VISIBLE);
                                    }
                                }
                            } else {
                                awesomeValidation.clear();
                                awesomeValidation.addValidation(editCoupleId, RegexTemplate.NOT_EMPTY, "कृपया " +
                                        "couple id प्रविष्ट करा");
                            }

                        }


                    } else if(s.hashCode() == editTextAgeLastChild.getText().hashCode()) {

                        if(eligibleCoupleNames.get(selectedCoupleIndex).getTotalChildren() != null && eligibleCoupleNames.get(selectedCoupleIndex).getTotalChildren().getAgeLastChild() != null) {
                            if (!s.toString().equals(String.valueOf(eligibleCoupleNames.get(selectedCoupleIndex).getTotalChildren().getAgeLastChild()))) {

                                changedInformation.put("ageLastChild", editTextAgeLastChild.getText().toString());
//                                System.out.println("Changes mobile " + changedInformation.get("ageLastChild"));

                                if (saveCoupleInformation.getVisibility() == View.GONE) {
                                    saveCoupleInformation.setVisibility(View.VISIBLE);
                                }

                            } else {

                                changedInformation.remove("ageLastChild");
                                if (changedInformation.isEmpty() && addDateClicked == 0) {
                                    saveCoupleInformation.setVisibility(View.GONE);
                                }

                            }
                        } else {
                            if(editTextAgeLastChild.getText().toString().trim().length() != 0) {
                                changedInformation.put("ageLastChild", editTextAgeLastChild.getText().toString());
//                                System.out.println("Changes mobile " + changedInformation.get("ageLastChild"));

                                if (saveCoupleInformation.getVisibility() == View.GONE) {
                                    saveCoupleInformation.setVisibility(View.VISIBLE);
                                }
                            }
                        }

                    }
//                    else if(s.hashCode() == editTextFutureMethod.getText().hashCode()) {
//
//                        if(eligibleCoupleNames.get(selectedCoupleIndex).getIfNoOption().getFutureMethod() != null) {
//                            if (!s.toString().equals(eligibleCoupleNames.get(selectedCoupleIndex).getIfNoOption().getFutureMethod())) {
//
//                                changedInformation.put("futureMethod", editTextFutureMethod.getText().toString());
////                                System.out.println("Changes mobile " + changedInformation.get("futureMethod"));
//
//                                if (saveCoupleInformation.getVisibility() == View.GONE) {
//                                    saveCoupleInformation.setVisibility(View.VISIBLE);
//                                }
//
//                            } else {
//
//                                changedInformation.remove("futureMethod");
//                                if (changedInformation.isEmpty()) {
//                                    saveCoupleInformation.setVisibility(View.GONE);
//                                }
//
//                            }
//                        } else {
//                            if(editTextFutureMethod.getText().toString().trim().length() != 0) {
//                                changedInformation.put("futureMethod", editTextFutureMethod.getText().toString());
////                                System.out.println("Changes mobile " + changedInformation.get("futureMethod"));
//
//                                if (saveCoupleInformation.getVisibility() == View.GONE) {
//                                    saveCoupleInformation.setVisibility(View.VISIBLE);
//                                }
//                            }
//                        }
//
//                    }
                    else if(s.hashCode() == editTextDateOfVisit.getText().hashCode()) {

                        if(eligibleCoupleNames.get(selectedCoupleIndex).getIfNoOption() != null && eligibleCoupleNames.get(selectedCoupleIndex).getIfNoOption().getDateOfVisit() != null) {
                            if (!s.toString().equals(eligibleCoupleNames.get(selectedCoupleIndex).getIfNoOption().getDateOfVisit())) {

                                changedInformation.put("dateOfVisit", editTextDateOfVisit.getText().toString());
//                                System.out.println("Changes mobile " + changedInformation.get("dateOfVisit"));

                                if (saveCoupleInformation.getVisibility() == View.GONE) {
                                    saveCoupleInformation.setVisibility(View.VISIBLE);
                                }

                            } else {

                                changedInformation.remove("dateOfVisit");
                                if (changedInformation.isEmpty()) {
                                    saveCoupleInformation.setVisibility(View.GONE);
                                }

                            }
                        } else {
                            if(editTextDateOfVisit.getText().toString().trim().length() != 0) {
                                changedInformation.put("dateOfVisit", editTextDateOfVisit.getText().toString());
//                                System.out.println("Changes mobile " + changedInformation.get("dateOfVisit"));

                                if (saveCoupleInformation.getVisibility() == View.GONE) {
                                    saveCoupleInformation.setVisibility(View.VISIBLE);
                                }
                            }
                        }

                    } else if(s.hashCode() == editTextDateOfGroupMeeting.getText().hashCode()) {

                        if(eligibleCoupleNames.get(selectedCoupleIndex).getIfNoOption() != null && eligibleCoupleNames.get(selectedCoupleIndex).getIfNoOption().getDateOfGroupMeeting() != null) {
                            if (!s.toString().equals(eligibleCoupleNames.get(selectedCoupleIndex).getIfNoOption().getDateOfGroupMeeting())) {

                                changedInformation.put("dateOfGroupMeeting", editTextDateOfGroupMeeting.getText().toString());
//                                System.out.println("Changes mobile " + changedInformation.get("dateOfGroupMeeting"));

                                if (saveCoupleInformation.getVisibility() == View.GONE) {
                                    saveCoupleInformation.setVisibility(View.VISIBLE);
                                }

                            } else {

                                changedInformation.remove("dateOfGroupMeeting");
                                if (changedInformation.isEmpty()) {
                                    saveCoupleInformation.setVisibility(View.GONE);
                                }

                            }
                        } else {
                            if(editTextDateOfGroupMeeting.getText().toString().trim().length() != 0) {
                                changedInformation.put("dateOfGroupMeeting", editTextDateOfGroupMeeting.getText().toString());
//                                System.out.println("Changes mobile " + changedInformation.get("dateOfGroupMeeting"));

                                if (saveCoupleInformation.getVisibility() == View.GONE) {
                                    saveCoupleInformation.setVisibility(View.VISIBLE);
                                }
                            }
                        }

                    } else if(s.hashCode() == editCoupleId.getText().hashCode()) {

                        if(eligibleCoupleNames.get(selectedCoupleIndex).getCoupleid() != null) {
                            if (!s.toString().equals(String.valueOf(eligibleCoupleNames.get(selectedCoupleIndex).getCoupleid()))) {

                                changedInformation.put("coupleid", editCoupleId.getText().toString());
//                                System.out.println("Changes mobile " + changedInformation.get("coupleid"));

                                if (saveCoupleInformation.getVisibility() == View.GONE) {
                                    saveCoupleInformation.setVisibility(View.VISIBLE);
                                }

                            } else {

                                changedInformation.remove("coupleid");
                                if (changedInformation.isEmpty()) {
                                    saveCoupleInformation.setVisibility(View.GONE);
                                }

                            }
                        } else {
                            if(editCoupleId.getText().toString().trim().length() != 0) {
                                changedInformation.put("coupleid", editCoupleId.getText().toString());
//                                System.out.println("Changes mobile " + changedInformation.get("coupleid"));

                                if (saveCoupleInformation.getVisibility() == View.GONE) {
                                    saveCoupleInformation.setVisibility(View.VISIBLE);
                                }
                            }
                        }

                    }
//                    else if(s.hashCode() == editTextHusbandName.getText().hashCode()) {
//
//                        if(!s.toString().equals(eligibleCoupleNames.get(selectedCoupleIndex).getHusband())) {
//
//                            changedInformation.put("husband", editTextHusbandName.getText().toString());
////                            System.out.println("Changes mobile " + changedInformation.get("husband"));
//
//                            if(saveCoupleInformation.getVisibility() == View.GONE) {
//                                saveCoupleInformation.setVisibility(View.VISIBLE);
//                            }
//
//                        } else {
//
//                            changedInformation.remove("husband");
//                            if(changedInformation.isEmpty()) {
//                                saveCoupleInformation.setVisibility(View.GONE);
//                            }
//
//                        }
//
//                    } else if(s.hashCode() == editTextWifeName.getText().hashCode()) {
//
//                        if(!s.toString().equals(eligibleCoupleNames.get(selectedCoupleIndex).getWife())) {
//
//                            changedInformation.put("wife", editTextWifeName.getText().toString());
////                            System.out.println("Changes mobile " + changedInformation.get("wife"));
//
//                            if(saveCoupleInformation.getVisibility() == View.GONE) {
//                                saveCoupleInformation.setVisibility(View.VISIBLE);
//                            }
//
//                        } else {
//
//                            changedInformation.remove("wife");
//                            if(changedInformation.isEmpty()) {
//                                saveCoupleInformation.setVisibility(View.GONE);
//                            }
//
//                        }
//
//                    }

                }

            }
        };

        husbandNameSpinner = view.findViewById(R.id.husbandNameMultiSpinner);
        wifeNameSpinner = view.findViewById(R.id.wifeNameMultiSpinner);
        coupleLayout = view.findViewById(R.id.coupleLayout);
        radioGroupFamilyPlanningMethod = view.findViewById(R.id.radioGroupFamilyPlanningMethod);
        editTextTotalMaleChildren = view.findViewById(R.id.editTotalMaleChildren);
        editTextTotalFemaleChildren = view.findViewById(R.id.editTotalFemaleChildren);
        editTextAgeLastChild = view.findViewById(R.id.editAgeLastChild);
        radioGroupGenderLastChild = view.findViewById(R.id.radioGroupGenderLastChild);
        ifNoMethodLayout = view.findViewById(R.id.ifNoMethodLayout);
//        editTextFutureMethod = view.findViewById(R.id.editFutureMethod);
        editTextDateOfGroupMeeting = view.findViewById(R.id.editDateOfGroupMeeting);
        editTextDateOfVisit = view.findViewById(R.id.editDateOfVisit);
        saveCoupleInformation = view.findViewById(R.id.btnSaveCoupleInformation);
        addCoupleButton = view.findViewById(R.id.btnAddCouple);
        textViewHusband = view.findViewById(R.id.textHusband);
        textViewWife = view.findViewById(R.id.textWife);
//        editTextHusbandName = view.findViewById(R.id.editTextHusbandName);
//        editTextWifeName = view.findViewById(R.id.editTextWifeName);
        nestedScrollViewCouple = view.findViewById(R.id.coupleInformationNestedScrollView);
        newCoupleSpinnerLinearLayout = view.findViewById(R.id.newCoupleSpinners);
        newHusbandNameSpinner = view.findViewById(R.id.newHusbandNameMultiSpinner);
        newWifeNameSpinner = view.findViewById(R.id.newWifeNameMultiSpinner);
        radioGroupFutureMethod = view.findViewById(R.id.radioGroupFutureMethod);
        cutAntaraOralPillsDateLinearLayout = view.findViewById(R.id.cutAntaraOralPillsDateLinearLayout);
        btnAddCutAntaraOralPillsDate = view.findViewById(R.id.btnAddCutAntaraOralPillsDate);
        dateIfNoMethodParentLinearLayout = view.findViewById(R.id.dateIfNoMethodParentLinearLayout);
        editCoupleId = view.findViewById(R.id.editCoupleId);

        editTextDateOfVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dateFlag = 0;
                datePickerDialog.getWindow();
                datePickerDialog.show();

            }
        });

        editTextDateOfGroupMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dateFlag = 1;
                datePickerDialog.getWindow();
                datePickerDialog.show();

            }
        });

        btnAddCutAntaraOralPillsDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                layoutInflater.inflate(R.layout.familydetail_vaccinationdate, cutAntaraOralPillsDateLinearLayout, true).findViewById(R.id.editVaccinationDate).setId(cutAntaraOralPillsDateId);

                final TextInputEditText textInputEditText = ((TextInputEditText) cutAntaraOralPillsDateLinearLayout.findViewById(cutAntaraOralPillsDateId));

                ((TextInputEditText) cutAntaraOralPillsDateLinearLayout.findViewById(cutAntaraOralPillsDateId)).setOnClickListener(new View.OnClickListener() {
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

                saveCoupleInformation.setVisibility(View.VISIBLE);
                cutAntaraOralPillsDateId++;

            }
        });

        addCoupleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addCouple = true;
                newHusbandNameSpinner.setSelectedIndex(0);
                newWifeNameSpinner.setSelectedIndex(0);
                textViewHusband.setVisibility(View.GONE);
                textViewWife.setVisibility(View.GONE);
                husbandNameSpinner.setVisibility(View.GONE);
                wifeNameSpinner.setVisibility(View.GONE);
                coupleLayout.setVisibility(View.GONE);
//                editTextHusbandName.setVisibility(View.GONE);
//                editTextWifeName.setVisibility(View.GONE);

                newCoupleSpinnerLinearLayout.setVisibility(View.VISIBLE);
                changedInformation.clear();
                saveCoupleInformation.setVisibility(View.VISIBLE);
                clearDataFromLayout();
                removeListenersFromComponents();

                apiCallToGetMemberNames();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        coupleLayout.setVisibility(View.VISIBLE);
                    }
                }, 500);
                dateIfNoMethodParentLinearLayout.setVisibility(View.GONE);
                ifNoMethodLayout.setVisibility(View.GONE);
                cutAntaraOralPillsDateLinearLayout.setVisibility(View.GONE);
                btnAddCutAntaraOralPillsDate.setVisibility(View.GONE);
                radioGroupFamilyPlanningMethod.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if(checkedId == R.id.radioMethodNoMethod) {
                            dateIfNoMethodParentLinearLayout.setVisibility(View.VISIBLE);
                            cutAntaraOralPillsDateLinearLayout.setVisibility(View.GONE);
                            btnAddCutAntaraOralPillsDate.setVisibility(View.GONE);
                            ifNoMethodLayout.setVisibility(View.VISIBLE);
                        } else if(checkedId == R.id.radioMethodAntara || checkedId == R.id.radioMethodOralPills || checkedId == R.id.radioMethodCut) {
                            dateIfNoMethodParentLinearLayout.setVisibility(View.VISIBLE);
                            ifNoMethodLayout.setVisibility(View.GONE);
                            cutAntaraOralPillsDateLinearLayout.setVisibility(View.VISIBLE);
                            btnAddCutAntaraOralPillsDate.setVisibility(View.VISIBLE);
                        }
                        else {
                            dateIfNoMethodParentLinearLayout.setVisibility(View.GONE);
                            ifNoMethodLayout.setVisibility(View.GONE);
                            cutAntaraOralPillsDateLinearLayout.setVisibility(View.GONE);
                            btnAddCutAntaraOralPillsDate.setVisibility(View.GONE);
                        }
                    }
                });

                nestedScrollViewCouple.scrollTo(0, 0);

            }
        });


        saveCoupleInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Variables variables = new Variables();
                MutationQueryFamilyDetail mutationQuery = new MutationQueryFamilyDetail();

                boolean check = false;
                List<String> fpmdatesList;

                if(awesomeValidation.validate()) {
                    if (!addCouple) {
                        if (eligibleCoupleNames.get(selectedCoupleIndex).getFpmdates() != null) {
                            fpmdatesList = new ArrayList<>(eligibleCoupleNames.get(selectedCoupleIndex).getFpmdates());
                        } else {
                            fpmdatesList = new ArrayList<>();
                        }
                        HashMap<String, Object> ifNoOption = new HashMap<>();
                        HashMap<String, Object> totalChildren = new HashMap<>();

                        if (changedInformation.get("fpmdates") != null) {
                            for (Map.Entry date : (Set<Map.Entry>) ((HashMap) changedInformation.get("fpmdates")).entrySet()) {

                                if (date.getValue().toString().trim().length() != 0) {
                                    fpmdatesList.set((Integer) date.getKey(), date.getValue().toString());
                                }
                            }
                        }
                        if (cutAntaraOralPillsDateId > fpmdatesList.size()) {
                            check = true;
                            for (int i = fpmdatesList.size(); i < cutAntaraOralPillsDateId; i++) {
                                fpmdatesList.add(((TextInputEditText) cutAntaraOralPillsDateLinearLayout.findViewById(i)).getText().toString());
                            }
                        }
                        if (changedInformation.get("fpmdates") != null || check) {
                            check = false;
                            changedInformation.remove("fpmdates");
                            changedInformation.put("fpmdates", fpmdatesList);
                        }

                        if (changedInformation.get("dateOfVisit") != null) {
                            changedInformation.put("dateOfVisit", changedInformation.get("dateOfVisit").toString());
                        }
                        if (changedInformation.get("dateOfGroupMeeting") != null) {
                            changedInformation.put("dateOfGroupMeeting", changedInformation.get("dateOfGroupMeeting").toString());
                        }
                        if (changedInformation.get("coupleid") != null) {
                            changedInformation.put("coupleid", Integer.parseInt(String.valueOf(changedInformation.get("coupleid"))));
                        }
                        if (changedInformation.get("male") != null) {
                            changedInformation.put("male", Integer.parseInt(String.valueOf(changedInformation.get("male"))));
                        }
                        if (changedInformation.get("female") != null) {
                            changedInformation.put("female", Integer.parseInt(String.valueOf(changedInformation.get("female"))));
                        }
                        if (changedInformation.get("ageLastChild") != null) {
                            changedInformation.put("ageLastChild", Integer.parseInt(String.valueOf(changedInformation.get("ageLastChild"))));
                        }


                        if (radioGroupFamilyPlanningMethod.getCheckedRadioButtonId() == R.id.radioMethodNoMethod) {
                            if (changedInformation.get("futureMethod") != null || changedInformation.get("dateOfVisit") != null || changedInformation.get("dateOfGroupMeeting") != null) {

                                if (changedInformation.get("futureMethod") != null) {
                                    ifNoOption.put("futureMethod", changedInformation.get("futureMethod"));
                                    changedInformation.remove("futureMethod");
                                }
                                if (changedInformation.get("dateOfVisit") != null) {
                                    ifNoOption.put("dateOfVisit", changedInformation.get("dateOfVisit"));
                                    changedInformation.remove("dateOfVisit");
                                }
                                if (changedInformation.get("dateOfGroupMeeting") != null) {
                                    ifNoOption.put("dateOfGroupMeeting", changedInformation.get("dateOfGroupMeeting"));
                                    changedInformation.remove("dateOfGroupMeeting");
                                }
                                changedInformation.put("ifNoOption", ifNoOption);

                            }
                        } else {
                            changedInformation.remove("futureMethod");
                            changedInformation.remove("dateOfVisit");
                            changedInformation.remove("dateOfGroupMeeting");
                            changedInformation.remove("ifNoOption");
                        }


                        if (changedInformation.get("male") != null || changedInformation.get("female") != null || changedInformation.get("ageLastChild") != null || changedInformation.get("genderLastChild") != null) {

                            if (changedInformation.get("male") != null) {
                                totalChildren.put("male", changedInformation.get("male"));
                                changedInformation.remove("male");
                            }
                            if (changedInformation.get("female") != null) {
                                totalChildren.put("female", changedInformation.get("female"));
                                changedInformation.remove("female");
                            }
                            if (changedInformation.get("ageLastChild") != null) {
                                totalChildren.put("ageLastChild", changedInformation.get("ageLastChild"));
                                changedInformation.remove("ageLastChild");
                            }
                            if (changedInformation.get("genderLastChild") != null) {
                                totalChildren.put("genderLastChild", changedInformation.get("genderLastChild"));
                                changedInformation.remove("genderLastChild");
                            }
                            changedInformation.put("totalChildren", totalChildren);

                        }

                        variables.setInput(changedInformation);
                        mutationQuery.setVariables(variables);
                        apiCallToUpdateCoupleInformation(mutationQuery);

                    } else {

                        // add new couple

                        List<HashMap<String, Object>> input = new ArrayList<>();
                        HashMap<String, Object> information = new HashMap<>();
                        HashMap<String, Object> ifNoOption = new HashMap<>();
                        HashMap<String, Object> totalChildren = new HashMap<>();
                        List<String> cutAntaraOralPillsL = new ArrayList<>();


                        if (selectedMemberHusbandIndex != -1) {
                            information.put("husband", maleMemberNamesList.get(selectedMemberHusbandIndex));
                        }

                        if (selectedMemberWifeIndex != -1) {
                            information.put("wife", femaleMemberNamesList.get(selectedMemberWifeIndex));
                        }

//                    if(editTextWifeName.getText().toString().trim().length() != 0) {
//                        information.put("wife", editTextWifeName.getText().toString());
//                    }
//                    if(editTextHusbandName.getText().toString().trim().length() != 0) {
//                        information.put("husband", editTextHusbandName.getText().toString());
//                    }


                        if (radioGroupFamilyPlanningMethod.getCheckedRadioButtonId() == R.id.radioMethodOralPills) {
                            information.put("familyPlanningMethod", "गर्भनिरोधक गोळ्या");
                        } else if (radioGroupFamilyPlanningMethod.getCheckedRadioButtonId() == R.id.radioMethodCut) {
                            information.put("familyPlanningMethod", "कॉपर टी");
                        } else if (radioGroupFamilyPlanningMethod.getCheckedRadioButtonId() == R.id.radioMethodNoMethod) {
                            information.put("familyPlanningMethod", "काहीही नाही");
                        } else if (radioGroupFamilyPlanningMethod.getCheckedRadioButtonId() == R.id.radioMethodAntara) {
                            information.put("familyPlanningMethod", "अंतरा");
                        } else if (radioGroupFamilyPlanningMethod.getCheckedRadioButtonId() == R.id.radioMethodNirodh) {
                            information.put("familyPlanningMethod", "निरोध");
                        } else if (radioGroupFamilyPlanningMethod.getCheckedRadioButtonId() == R.id.radioMethodSterilization) {
                            information.put("familyPlanningMethod", "नसबंदी");
                        }

                        if (radioGroupFamilyPlanningMethod.getCheckedRadioButtonId() == R.id.radioMethodAntara || radioGroupFamilyPlanningMethod.getCheckedRadioButtonId() == R.id.radioMethodCut || radioGroupFamilyPlanningMethod.getCheckedRadioButtonId() == R.id.radioMethodOralPills) {
                            for (int id = 0; id < cutAntaraOralPillsDateId; id++) {
                                if (((TextInputEditText) cutAntaraOralPillsDateLinearLayout.findViewById(id)).getText().toString().trim().length() != 0) {
                                    cutAntaraOralPillsL.add(((TextInputEditText) cutAntaraOralPillsDateLinearLayout.findViewById(id)).getText().toString());
                                }
                            }
                            if (!cutAntaraOralPillsL.isEmpty()) {
                                information.put("fpmdates", cutAntaraOralPillsL);
                            }
                        }

                        if (radioGroupFamilyPlanningMethod.getCheckedRadioButtonId() == R.id.radioMethodNoMethod) {


                            if (radioGroupFutureMethod.getCheckedRadioButtonId() == R.id.radioFutureMethodOralPills) {
                                ifNoOption.put("futureMethod", "गर्भनिरोधक गोळ्या");
                            } else if (radioGroupFutureMethod.getCheckedRadioButtonId() == R.id.radioFutureMethodCut) {
                                ifNoOption.put("futureMethod", "कॉपर टी");
                            }
//                        else if(radioGroupFutureMethod.getCheckedRadioButtonId() == R.id.radioFutureMethodNoMethod) {
//                            ifNoOption.put("futureMethod", "काहीही नाही");
//                        }
                            else if (radioGroupFutureMethod.getCheckedRadioButtonId() == R.id.radioFutureMethodAntara) {
                                ifNoOption.put("futureMethod", "अंतरा");
                            } else if (radioGroupFutureMethod.getCheckedRadioButtonId() == R.id.radioFutureMethodNirodh) {
                                ifNoOption.put("futureMethod", "निरोध");
                            } else if (radioGroupFutureMethod.getCheckedRadioButtonId() == R.id.radioFutureMethodSterilization) {
                                ifNoOption.put("futureMethod", "नसबंदी");
                            }

                            information.put("ifNoOption", ifNoOption);

//                        if (editTextFutureMethod.getText().toString().trim().length() != 0 || editTextDateOfVisit.getText().toString().trim().length() != 0 || editTextDateOfGroupMeeting.getText().toString().trim().length() != 0) {
//
//                            if (editTextFutureMethod.getText().toString().trim().length() != 0) {
//                                ifNoOption.put("futureMethod", editTextFutureMethod.getText().toString());
//                            }
//                            if (editTextDateOfVisit.getText().toString().trim().length() != 0) {
//                                ifNoOption.put("dateOfVisit", editTextDateOfVisit.getText().toString());
//                            }
//                            if (editTextDateOfGroupMeeting.getText().toString().trim().length() != 0) {
//                                ifNoOption.put("dateOfGroupMeeting", editTextDateOfGroupMeeting.getText().toString());
//                            }
//                            information.put("ifNoOption", ifNoOption);
//
//                        }
                        }


                        if (editTextTotalMaleChildren.getText().toString().trim().length() != 0 || editTextTotalFemaleChildren.getText().toString().trim().length() != 0 || editTextAgeLastChild.getText().toString().trim().length() != 0 || radioGroupGenderLastChild.getCheckedRadioButtonId() != -1) {

                            if (editTextTotalMaleChildren.getText().toString().trim().length() != 0) {
                                totalChildren.put("male", Integer.parseInt(editTextTotalMaleChildren.getText().toString()));

                            }
                            if (editTextTotalFemaleChildren.getText().toString().trim().length() != 0) {
                                totalChildren.put("female", Integer.parseInt(editTextTotalFemaleChildren.getText().toString()));

                            }
                            if (editTextAgeLastChild.getText().toString().trim().length() != 0) {
                                totalChildren.put("ageLastChild", Integer.parseInt(editTextAgeLastChild.getText().toString()));

                            }
                            if (radioGroupGenderLastChild.getCheckedRadioButtonId() != -1) {
                                if (radioGroupGenderLastChild.getCheckedRadioButtonId() == R.id.radioGenderMale) {
                                    totalChildren.put("genderLastChild", "पुरुष");
                                } else if (radioGroupGenderLastChild.getCheckedRadioButtonId() == R.id.radioGenderFemale) {
                                    totalChildren.put("genderLastChild", "स्त्री");
                                } else if (radioGroupGenderLastChild.getCheckedRadioButtonId() == R.id.radioGenderOther) {
                                    totalChildren.put("genderLastChild", "इतर");
                                }


                            }
                            information.put("totalChildren", totalChildren);

                        }


                        information.put("eligibleCoupleNameId", new ObjectId().toString());
                        input.add(information);

                        variables.setInput(input);
                        mutationQuery.setVariables(variables);
//                        System.out.println("nnew couple " + input);
                        apiCallToAddCoupleInformation(mutationQuery);

                    }
                }


            }
        });


        addValidation();
        apiCallToGetCoupleInformation();
        addListenerToComponents();

    }


    private void addListenerToComponents() {


        addCoupleButton.setVisibility(View.VISIBLE);
        textViewHusband.setVisibility(View.VISIBLE);
        husbandNameSpinner.setVisibility(View.VISIBLE);
        textViewWife.setVisibility(View.VISIBLE);
        wifeNameSpinner.setVisibility(View.VISIBLE);



        editTextTotalMaleChildren.addTextChangedListener(textWatcher);
        editTextTotalFemaleChildren.addTextChangedListener(textWatcher);
        editTextAgeLastChild.addTextChangedListener(textWatcher);
//        editTextFutureMethod.addTextChangedListener(textWatcher);
        editTextDateOfGroupMeeting.addTextChangedListener(textWatcher);
        editTextDateOfVisit.addTextChangedListener(textWatcher);
//        editTextWifeName.addTextChangedListener(textWatcher);
//        editTextHusbandName.addTextChangedListener(textWatcher);
        editCoupleId.addTextChangedListener(textWatcher);


        newHusbandNameSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {

                if(position != 0) {

                    selectedMemberHusbandIndex = position;

                } else {
                    selectedMemberHusbandIndex = -1;
                }

            }
        });

        newWifeNameSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {

                if(position != 0) {

                    selectedMemberWifeIndex = position;

                } else {
                    selectedMemberWifeIndex = -1;
                }

            }
        });


        husbandNameSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {

                wifeNameSpinner.setSelectedIndex(position);

                coupleLayout.setVisibility(View.GONE);

                if(position != 0) {

                    selectedCoupleIndex = position -1;

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            coupleLayout.setVisibility(View.VISIBLE);
                            nestedScrollViewCouple.scrollTo(0, 0);
                        }
                    }, 500);

                    clearDataFromLayout();
                    setDataToLayout(eligibleCoupleNames.get(position-1));

                    changedInformation.clear();
                    saveCoupleInformation.setVisibility(View.GONE);

                } else {
                    coupleLayout.setVisibility(View.GONE);
                }

            }
        });

        wifeNameSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {

                husbandNameSpinner.setSelectedIndex(position);

                coupleLayout.setVisibility(View.INVISIBLE);

                if(position != 0) {

                    selectedCoupleIndex = position -1;

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            coupleLayout.setVisibility(View.VISIBLE);
                            nestedScrollViewCouple.scrollTo(0, 0);
                        }
                    }, 500);

                    clearDataFromLayout();
                    setDataToLayout(eligibleCoupleNames.get(position-1));

                } else {
                    coupleLayout.setVisibility(View.GONE);
                }

            }
        });


        radioGroupFamilyPlanningMethod.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                int responseFpmId = -1;

                if ("गर्भनिरोधक गोळ्या".equals(eligibleCoupleNames.get(selectedCoupleIndex).getFamilyPlanningMethod())) {
                    responseFpmId = R.id.radioMethodOralPills;
                } else if ("कॉपर टी".equals(eligibleCoupleNames.get(selectedCoupleIndex).getFamilyPlanningMethod())) {
                    responseFpmId = R.id.radioMethodCut;
                } else if ("काहीही नाही".equals(eligibleCoupleNames.get(selectedCoupleIndex).getFamilyPlanningMethod())) {
                    responseFpmId = R.id.radioMethodNoMethod;
                } else if ("अंतरा".equals(eligibleCoupleNames.get(selectedCoupleIndex).getFamilyPlanningMethod())) {
                    responseFpmId = R.id.radioMethodAntara;
                } else if ("निरोध".equals(eligibleCoupleNames.get(selectedCoupleIndex).getFamilyPlanningMethod())) {
                    responseFpmId = R.id.radioMethodNirodh;
                } else if ("नसबंदी".equals(eligibleCoupleNames.get(selectedCoupleIndex).getFamilyPlanningMethod())) {
                    responseFpmId = R.id.radioMethodSterilization;
                }


                switch(checkedId) {

                    case R.id.radioMethodOralPills:
                        if(responseFpmId == R.id.radioMethodOralPills) {
                            changedInformation.remove("familyPlanningMethod");
                            dateIfNoMethodParentLinearLayout.setVisibility(View.VISIBLE);
                            ifNoMethodLayout.setVisibility(View.GONE);
                            cutAntaraOralPillsDateLinearLayout.setVisibility(View.VISIBLE);
                            btnAddCutAntaraOralPillsDate.setVisibility(View.VISIBLE);
                            if(changedInformation.isEmpty()) {
                                saveCoupleInformation.setVisibility(View.GONE);
                            }

                        } else {

                            dateIfNoMethodParentLinearLayout.setVisibility(View.VISIBLE);
                            ifNoMethodLayout.setVisibility(View.GONE);
                            cutAntaraOralPillsDateLinearLayout.setVisibility(View.VISIBLE);
                            btnAddCutAntaraOralPillsDate.setVisibility(View.VISIBLE);
                            if(saveCoupleInformation.getVisibility() == View.GONE) {
                                saveCoupleInformation.setVisibility(View.VISIBLE);
                            }

                            changedInformation.put("familyPlanningMethod", "गर्भनिरोधक गोळ्या");
                        }
                        break;

                    case R.id.radioMethodCut:
                        if(responseFpmId == R.id.radioMethodCut) {
                            changedInformation.remove("familyPlanningMethod");
                            dateIfNoMethodParentLinearLayout.setVisibility(View.VISIBLE);
                            ifNoMethodLayout.setVisibility(View.GONE);
                            cutAntaraOralPillsDateLinearLayout.setVisibility(View.VISIBLE);
                            btnAddCutAntaraOralPillsDate.setVisibility(View.VISIBLE);
                            if(changedInformation.isEmpty()) {
                                saveCoupleInformation.setVisibility(View.GONE);
                            }

                        } else {

                            dateIfNoMethodParentLinearLayout.setVisibility(View.VISIBLE);
                            ifNoMethodLayout.setVisibility(View.GONE);
                            cutAntaraOralPillsDateLinearLayout.setVisibility(View.VISIBLE);
                            btnAddCutAntaraOralPillsDate.setVisibility(View.VISIBLE);
                            if(saveCoupleInformation.getVisibility() == View.GONE) {
                                saveCoupleInformation.setVisibility(View.VISIBLE);
                            }

                            changedInformation.put("familyPlanningMethod", "कॉपर टी");
                        }
                        break;

                    case R.id.radioMethodNoMethod:
                        if(responseFpmId == R.id.radioMethodNoMethod) {
                            changedInformation.remove("familyPlanningMethod");
                            dateIfNoMethodParentLinearLayout.setVisibility(View.VISIBLE);
                            ifNoMethodLayout.setVisibility(View.VISIBLE);
                            cutAntaraOralPillsDateLinearLayout.setVisibility(View.GONE);
                            btnAddCutAntaraOralPillsDate.setVisibility(View.GONE);
                            if(changedInformation.isEmpty()) {
                                saveCoupleInformation.setVisibility(View.GONE);
                            }

                        } else {
                            dateIfNoMethodParentLinearLayout.setVisibility(View.VISIBLE);
                            ifNoMethodLayout.setVisibility(View.VISIBLE);
                            cutAntaraOralPillsDateLinearLayout.setVisibility(View.GONE);
                            btnAddCutAntaraOralPillsDate.setVisibility(View.GONE);
                            if(saveCoupleInformation.getVisibility() == View.GONE) {
                                saveCoupleInformation.setVisibility(View.VISIBLE);
                            }

                            changedInformation.put("familyPlanningMethod", "काहीही नाही");
                        }
                        break;

                    case R.id.radioMethodAntara:
                        if(responseFpmId == R.id.radioMethodAntara) {
                            changedInformation.remove("familyPlanningMethod");
                            dateIfNoMethodParentLinearLayout.setVisibility(View.VISIBLE);
                            ifNoMethodLayout.setVisibility(View.GONE);
                            cutAntaraOralPillsDateLinearLayout.setVisibility(View.VISIBLE);
                            btnAddCutAntaraOralPillsDate.setVisibility(View.VISIBLE);
                            if(changedInformation.isEmpty()) {
                                saveCoupleInformation.setVisibility(View.GONE);
                            }

                        } else {

                            dateIfNoMethodParentLinearLayout.setVisibility(View.VISIBLE);
                            ifNoMethodLayout.setVisibility(View.GONE);
                            cutAntaraOralPillsDateLinearLayout.setVisibility(View.VISIBLE);
                            btnAddCutAntaraOralPillsDate.setVisibility(View.VISIBLE);
                            if(saveCoupleInformation.getVisibility() == View.GONE) {
                                saveCoupleInformation.setVisibility(View.VISIBLE);
                            }

                            changedInformation.put("familyPlanningMethod", "अंतरा");
                        }
                        break;

                    case R.id.radioMethodNirodh:
                        if(responseFpmId == R.id.radioMethodNirodh) {
                            changedInformation.remove("familyPlanningMethod");
                            dateIfNoMethodParentLinearLayout.setVisibility(View.GONE);
                            ifNoMethodLayout.setVisibility(View.GONE);
                            cutAntaraOralPillsDateLinearLayout.setVisibility(View.GONE);
                            btnAddCutAntaraOralPillsDate.setVisibility(View.GONE);
                            if(changedInformation.isEmpty()) {
                                saveCoupleInformation.setVisibility(View.GONE);
                            }

                        } else {

                            dateIfNoMethodParentLinearLayout.setVisibility(View.GONE);
                            ifNoMethodLayout.setVisibility(View.GONE);
                            cutAntaraOralPillsDateLinearLayout.setVisibility(View.GONE);
                            btnAddCutAntaraOralPillsDate.setVisibility(View.GONE);
                            if(saveCoupleInformation.getVisibility() == View.GONE) {
                                saveCoupleInformation.setVisibility(View.VISIBLE);
                            }

                            changedInformation.put("familyPlanningMethod", "निरोध");
                        }
                        break;

                    case R.id.radioMethodSterilization:
                        if(responseFpmId == R.id.radioMethodSterilization) {
                            changedInformation.remove("familyPlanningMethod");
                            dateIfNoMethodParentLinearLayout.setVisibility(View.GONE);
                            ifNoMethodLayout.setVisibility(View.GONE);
                            cutAntaraOralPillsDateLinearLayout.setVisibility(View.GONE);
                            btnAddCutAntaraOralPillsDate.setVisibility(View.GONE);
                            if(changedInformation.isEmpty()) {
                                saveCoupleInformation.setVisibility(View.GONE);
                            }

                        } else {

                            dateIfNoMethodParentLinearLayout.setVisibility(View.GONE);
                            ifNoMethodLayout.setVisibility(View.GONE);
                            cutAntaraOralPillsDateLinearLayout.setVisibility(View.GONE);
                            btnAddCutAntaraOralPillsDate.setVisibility(View.GONE);
                            if(saveCoupleInformation.getVisibility() == View.GONE) {
                                saveCoupleInformation.setVisibility(View.VISIBLE);
                            }

                            changedInformation.put("familyPlanningMethod", "नसबंदी");
                        }
                        break;

                }

//                System.out.println("familyPlanningMethod " + changedInformation.get("familyPlanningMethod"));
            }
        });


        radioGroupFutureMethod.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                int responseFpmId = -1;

                if(eligibleCoupleNames.get(selectedCoupleIndex).getIfNoOption() != null && eligibleCoupleNames.get(selectedCoupleIndex).getIfNoOption().getFutureMethod() != null) {
                    if ("गर्भनिरोधक गोळ्या".equals(eligibleCoupleNames.get(selectedCoupleIndex).getIfNoOption().getFutureMethod())) {
                        responseFpmId = R.id.radioFutureMethodOralPills;
                    } else if ("कॉपर टी".equals(eligibleCoupleNames.get(selectedCoupleIndex).getIfNoOption().getFutureMethod())) {
                        responseFpmId = R.id.radioFutureMethodCut;
                    }
//                else if ("काहीही नाही".equals(eligibleCoupleNames.get(selectedCoupleIndex).getIfNoOption().getFutureMethod())) {
//                    responseFpmId = R.id.radioFutureMethodNoMethod;
//                }
                    else if ("अंतरा".equals(eligibleCoupleNames.get(selectedCoupleIndex).getIfNoOption().getFutureMethod())) {
                        responseFpmId = R.id.radioFutureMethodAntara;
                    } else if ("निरोध".equals(eligibleCoupleNames.get(selectedCoupleIndex).getIfNoOption().getFutureMethod())) {
                        responseFpmId = R.id.radioFutureMethodNirodh;
                    } else if ("नसबंदी".equals(eligibleCoupleNames.get(selectedCoupleIndex).getIfNoOption().getFutureMethod())) {
                        responseFpmId = R.id.radioFutureMethodSterilization;
                    }
                }


                switch(checkedId) {

                    case R.id.radioFutureMethodOralPills:
                        if(responseFpmId == R.id.radioFutureMethodOralPills) {
                            changedInformation.remove("futureMethod");
//                            ifNoMethodLayout.setVisibility(View.GONE);
                            if(changedInformation.isEmpty()) {
                                saveCoupleInformation.setVisibility(View.GONE);
                            }

                        } else {

//                            ifNoMethodLayout.setVisibility(View.GONE);
                            if(saveCoupleInformation.getVisibility() == View.GONE) {
                                saveCoupleInformation.setVisibility(View.VISIBLE);
                            }

                            changedInformation.put("futureMethod", "गर्भनिरोधक गोळ्या");
                        }
                        break;

                    case R.id.radioFutureMethodCut:
                        if(responseFpmId == R.id.radioFutureMethodCut) {
                            changedInformation.remove("futureMethod");
//                            ifNoMethodLayout.setVisibility(View.GONE);
                            if(changedInformation.isEmpty()) {
                                saveCoupleInformation.setVisibility(View.GONE);
                            }

                        } else {

//                            ifNoMethodLayout.setVisibility(View.GONE);
                            if(saveCoupleInformation.getVisibility() == View.GONE) {
                                saveCoupleInformation.setVisibility(View.VISIBLE);
                            }

                            changedInformation.put("futureMethod", "कॉपर टी");
                        }
                        break;

//                    case R.id.radioMethodNoMethod:
//                        if(responseFpmId == R.id.radioMethodNoMethod) {
//                            changedInformation.remove("futureMethod");
//
//                            if(changedInformation.isEmpty()) {
//                                saveCoupleInformation.setVisibility(View.GONE);
//                            }
//
//                        } else {
//                            ifNoMethodLayout.setVisibility(View.VISIBLE);
//                            if(saveCoupleInformation.getVisibility() == View.GONE) {
//                                saveCoupleInformation.setVisibility(View.VISIBLE);
//                            }
//
//                            changedInformation.put("futureMethod", "काहीही नाही");
//                        }
//                        break;

                    case R.id.radioFutureMethodAntara:
                        if(responseFpmId == R.id.radioFutureMethodAntara) {
                            changedInformation.remove("futureMethod");
//                            ifNoMethodLayout.setVisibility(View.GONE);
                            if(changedInformation.isEmpty()) {
                                saveCoupleInformation.setVisibility(View.GONE);
                            }

                        } else {

//                            ifNoMethodLayout.setVisibility(View.GONE);
                            if(saveCoupleInformation.getVisibility() == View.GONE) {
                                saveCoupleInformation.setVisibility(View.VISIBLE);
                            }

                            changedInformation.put("futureMethod", "अंतरा");
                        }
                        break;

                    case R.id.radioFutureMethodNirodh:
                        if(responseFpmId == R.id.radioFutureMethodNirodh) {
                            changedInformation.remove("futureMethod");
//                            ifNoMethodLayout.setVisibility(View.GONE);
                            if(changedInformation.isEmpty()) {
                                saveCoupleInformation.setVisibility(View.GONE);
                            }

                        } else {

//                            ifNoMethodLayout.setVisibility(View.GONE);
                            if(saveCoupleInformation.getVisibility() == View.GONE) {
                                saveCoupleInformation.setVisibility(View.VISIBLE);
                            }

                            changedInformation.put("futureMethod", "निरोध");
                        }
                        break;

                    case R.id.radioFutureMethodSterilization:
                        if(responseFpmId == R.id.radioFutureMethodSterilization) {
                            changedInformation.remove("futureMethod");
//                            ifNoMethodLayout.setVisibility(View.GONE);
                            if(changedInformation.isEmpty()) {
                                saveCoupleInformation.setVisibility(View.GONE);
                            }

                        } else {

//                            ifNoMethodLayout.setVisibility(View.GONE);
                            if(saveCoupleInformation.getVisibility() == View.GONE) {
                                saveCoupleInformation.setVisibility(View.VISIBLE);
                            }

                            changedInformation.put("futureMethod", "नसबंदी");
                        }
                        break;

                }

//                System.out.println("futureMethod " + changedInformation.get("futureMethod"));
            }
        });


        radioGroupGenderLastChild.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                int responseGenderId = -1;


                if(eligibleCoupleNames.get(selectedCoupleIndex).getTotalChildren() != null && eligibleCoupleNames.get(selectedCoupleIndex).getTotalChildren().getGenderLastChild() != null) {
                    if(eligibleCoupleNames.get(selectedCoupleIndex).getTotalChildren().getGenderLastChild().equalsIgnoreCase("पुरुष")) {
                        responseGenderId = R.id.radioGenderMale;
                    } else if(eligibleCoupleNames.get(selectedCoupleIndex).getTotalChildren().getGenderLastChild().equalsIgnoreCase("स्त्री")) {
                        responseGenderId = R.id.radioGenderFemale;
                    } else if(eligibleCoupleNames.get(selectedCoupleIndex).getTotalChildren().getGenderLastChild().equalsIgnoreCase("इतर")) {
                        responseGenderId = R.id.radioGenderOther;
                    }
                }




                switch(checkedId) {

                    case R.id.radioGenderMale:
                        if(responseGenderId == R.id.radioGenderMale) {

                            changedInformation.remove("genderLastChild");
//                            System.out.println(changedInformation.get("genderLastChild"));

                            if(changedInformation.isEmpty()) {
                                saveCoupleInformation.setVisibility(View.GONE);
                            }

                        } else {

                            if(saveCoupleInformation.getVisibility() == View.GONE) {
                                saveCoupleInformation.setVisibility(View.VISIBLE);
                            }

                            changedInformation.put("genderLastChild", "पुरुष");
                        }
                        break;

                    case R.id.radioGenderFemale:
                        if(responseGenderId == R.id.radioGenderFemale) {
                            changedInformation.remove("genderLastChild");

                            if(changedInformation.isEmpty()) {
                                saveCoupleInformation.setVisibility(View.GONE);
                            }

                        } else {

                            if(saveCoupleInformation.getVisibility() == View.GONE) {
                                saveCoupleInformation.setVisibility(View.VISIBLE);
                            }

                            changedInformation.put("genderLastChild", "स्त्री");
                        }
                        break;

                    case R.id.radioGenderOther:
                        if(responseGenderId == R.id.radioGenderOther) {
                            changedInformation.remove("genderLastChild");

                            if(changedInformation.isEmpty()) {
                                saveCoupleInformation.setVisibility(View.GONE);
                            }

                        } else {

                            if(saveCoupleInformation.getVisibility() == View.GONE) {
                                saveCoupleInformation.setVisibility(View.VISIBLE);
                            }

                            changedInformation.put("genderLastChild", "इतर");
                        }
                        break;

                }

//                System.out.println("gender " + changedInformation.get("genderLastChild"));
            }
        });


    }

    private void apiCallToAddCoupleInformation(MutationQueryFamilyDetail mutationQuery) {

        showProgressBar("Adding New Couple ...");

        String query = "mutation($input: [EligibleCoupleNameInput]) { addEligibleCoupleName(id: \"" + objectId + "\", input: $input) { n nModified ok } }";

        mutationQuery.setQuery(query);

        Call<Family> addCoupleInformation = apiInterface.addInformation(mutationQuery);

        addCoupleInformation.enqueue(new Callback<Family>() {
            @Override
            public void onResponse(Call<Family> call, Response<Family> response) {

                if(response.isSuccessful() && response.body() != null) {

                    apiCallToGetCoupleInformation();
                    changedInformation.clear();

                    saveCoupleInformation.setVisibility(View.GONE);
                    coupleLayout.setVisibility(View.GONE);
                    newCoupleSpinnerLinearLayout.setVisibility(View.GONE);
//                    editTextHusbandName.setVisibility(View.VISIBLE);
//                    editTextWifeName.setVisibility(View.VISIBLE);
                    textViewHusband.setVisibility(View.VISIBLE);
                    husbandNameSpinner.setVisibility(View.VISIBLE);
                    textViewWife.setVisibility(View.VISIBLE);
                    wifeNameSpinner.setVisibility(View.VISIBLE);

                    clearDataFromLayout();
                    addListenerToComponents();

                    addCouple = false;
                    stopProgressBar();
                    showAlertDialog("Couple Added Successfully.");

                } else {

                    stopProgressBar();
                    showAlertDialog("Error Adding Couple.");

                }

            }

            @Override
            public void onFailure(Call<Family> call, Throwable t) {

            }
        });

    }

    private void apiCallToUpdateCoupleInformation(MutationQueryFamilyDetail mutationQuery) {

        showProgressBar("Updating Couple Data ...");

        String query = "mutation($input: EligibleCoupleNameInput){ updateEligibleCoupleName(eligibleCoupleNameId:\"" + eligibleCoupleNames.get(selectedCoupleIndex).getEligibleCoupleNameId() + "\",input:$input) { nModified n ok } }";

        mutationQuery.setQuery(query);

        Call<Family> updateCoupleInformation = apiInterface.updateInformation(mutationQuery);

        updateCoupleInformation.enqueue(new Callback<Family>() {
            @Override
            public void onResponse(Call<Family> call, Response<Family> response) {

                if(response.isSuccessful() && response.body() != null) {

                    stopProgressBar();
                    apiCallToGetCoupleInformation();
                    saveCoupleInformation.setVisibility(View.GONE);
                    changedInformation.clear();
                    showAlertDialog("Data Updated Successfully.");

                } else {
                    stopProgressBar();
                    showAlertDialog("Error Updating Data.");
                }

            }

            @Override
            public void onFailure(Call<Family> call, Throwable t) {
//                System.out.println(t.getMessage());
            }
        });


    }

    private void apiCallToGetCoupleInformation() {

        showProgressBar("Loading Please Wait ...");

        String query = "query{ getFamily(_id:\"" + objectId + "\") { eligibleCoupleName { husband wife coupleid fpmdates eligibleCoupleNameId familyPlanningMethod totalChildren { male female ageLastChild genderLastChild } ifNoOption { futureMethod dateOfVisit dateOfGroupMeeting }}}}";

        Call<Family> getCoupleInformation = apiInterface.getInformation(query);

        getCoupleInformation.enqueue(new Callback<Family>() {
            @Override
            public void onResponse(Call<Family> call, Response<Family> response) {

                if(response.isSuccessful() && response.body() != null) {

                    eligibleCoupleNames = response.body().getData().getGetFamily().getEligibleCoupleName();

//                    clearDataFromLayout();
                    husbandNames.clear();
                    wifeNames.clear();
                    husbandNames.add("---Select Husband---");
                    wifeNames.add("---Select Wife---");
                    for(EligibleCoupleName eligibleCoupleName : eligibleCoupleNames) {

                        husbandNames.add(eligibleCoupleName.getHusband());
                        wifeNames.add(eligibleCoupleName.getWife());

                    }
                    husbandNameSpinner.setItems(husbandNames);
                    wifeNameSpinner.setItems(wifeNames);

                    stopProgressBar();


                }
                else {
                    stopProgressBar();
                    showAlertDialog("No Couple Found.");
                }

            }

            @Override
            public void onFailure(Call<Family> call, Throwable t) {

            }
        });

    }


    private void apiCallToGetMemberNames() {

        showProgressBar("Loading Please Wait ...");

        String query = "query { getFamily(_id:\"" + objectId + "\") { members { memberid membername sex }}}";

        Call<Family> getMembers = apiInterface.getInformation(query);

        getMembers.enqueue(new Callback<Family>() {
            @Override
            public void onResponse(Call<Family> call, Response<Family> response) {

                if(response.isSuccessful() && response.body() != null) {

                    members = response.body().getData().getGetFamily().getMembers();


                    maleMemberNamesList.clear();
                    maleMemberNamesList.add("---Select Member---");
                    femaleMemberNamesList.clear();
                    femaleMemberNamesList.add("---Select Member---");

                    for(Member member : members) {
                        if(member.getMembername() != null) {
                            if (member.getSex() != null && member.getSex().equalsIgnoreCase("पुरुष")) {
                                maleMemberNamesList.add(member.getMembername());
                            } else if (member.getSex() != null && member.getSex().equalsIgnoreCase("स्त्री")) {
                                femaleMemberNamesList.add(member.getMembername());
                            }
                        }

                    }


                    newHusbandNameSpinner.setItems(maleMemberNamesList);
                    newWifeNameSpinner.setItems(femaleMemberNamesList);


                    stopProgressBar();


                } else {

                    stopProgressBar();
                    showAlertDialog("No Members Found");
                }

            }

            @Override
            public void onFailure(Call<Family> call, Throwable t) {

//                System.out.println(t.getMessage());

            }
        });

    }


    private void setDataToLayout(EligibleCoupleName couple) {

        cutAntaraOralPillsDateLinearLayout.removeAllViews();
        cutAntaraOralPillsDateId = 0;

        if(couple.getCoupleid() != null) {
            editCoupleId.setText(String.valueOf(couple.getCoupleid()));
        }
        if(couple.getTotalChildren() != null) {
            if (couple.getTotalChildren().getMale() != null) {
                editTextTotalMaleChildren.setText(String.valueOf(couple.getTotalChildren().getMale()));
            }
            if (couple.getTotalChildren().getFemale() != null) {
                editTextTotalFemaleChildren.setText(String.valueOf(couple.getTotalChildren().getFemale()));
            }
            if (couple.getTotalChildren().getAgeLastChild() != null) {
                editTextAgeLastChild.setText(String.valueOf(couple.getTotalChildren().getAgeLastChild()));
            }
            if(couple.getTotalChildren().getGenderLastChild() != null) {
                if(couple.getTotalChildren().getGenderLastChild().equalsIgnoreCase("पुरुष")) {
                    radioGroupGenderLastChild.check(R.id.radioGenderMale);
                } else if(couple.getTotalChildren().getGenderLastChild().equalsIgnoreCase("स्त्री")) {
                    radioGroupGenderLastChild.check(R.id.radioGenderFemale);
                } else if(couple.getTotalChildren().getGenderLastChild().equalsIgnoreCase("इतर")) {
                    radioGroupGenderLastChild.check(R.id.radioGenderOther);
                }
            }
        }
//        editTextHusbandName.setText(couple.getHusband());
//        editTextWifeName.setText(couple.getWife());


        if(couple.getFamilyPlanningMethod() != null) {
            switch (couple.getFamilyPlanningMethod()) {
                case "गर्भनिरोधक गोळ्या":
                    radioGroupFamilyPlanningMethod.check(R.id.radioMethodOralPills);
                    if(couple.getFpmdates() != null) {

                        dateIfNoMethodParentLinearLayout.setVisibility(View.VISIBLE);
                        ifNoMethodLayout.setVisibility(View.GONE);
                        cutAntaraOralPillsDateLinearLayout.setVisibility(View.VISIBLE);
                        btnAddCutAntaraOralPillsDate.setVisibility(View.VISIBLE);

                        for(String date : couple.getFpmdates()) {
                            layoutInflater.inflate(R.layout.familydetail_vaccinationdate, cutAntaraOralPillsDateLinearLayout, true).findViewById(R.id.editVaccinationDate).setId(cutAntaraOralPillsDateId);
                            ((TextInputEditText) cutAntaraOralPillsDateLinearLayout.findViewById(cutAntaraOralPillsDateId)).setText(String.valueOf(date));
                            cutAntaraOralPillsDateId++;
                        }

                    }
                    break;
                case "कॉपर टी":
                    radioGroupFamilyPlanningMethod.check(R.id.radioMethodCut);
//                    System.out.println(couple.getFpmdates());
                    if(couple.getFpmdates() != null) {

                        dateIfNoMethodParentLinearLayout.setVisibility(View.VISIBLE);
                        ifNoMethodLayout.setVisibility(View.GONE);
                        cutAntaraOralPillsDateLinearLayout.setVisibility(View.VISIBLE);
                        btnAddCutAntaraOralPillsDate.setVisibility(View.VISIBLE);

                        for(String date : couple.getFpmdates()) {
                            layoutInflater.inflate(R.layout.familydetail_vaccinationdate, cutAntaraOralPillsDateLinearLayout, true).findViewById(R.id.editVaccinationDate).setId(cutAntaraOralPillsDateId);
                            ((TextInputEditText) cutAntaraOralPillsDateLinearLayout.findViewById(cutAntaraOralPillsDateId)).setText(String.valueOf(date));
                            cutAntaraOralPillsDateId++;
                        }

                    }
                    break;
                case "काहीही नाही":

                    radioGroupFamilyPlanningMethod.check(R.id.radioMethodNoMethod);

                    dateIfNoMethodParentLinearLayout.setVisibility(View.VISIBLE);
                    cutAntaraOralPillsDateLinearLayout.setVisibility(View.GONE);
                    btnAddCutAntaraOralPillsDate.setVisibility(View.GONE);
                    ifNoMethodLayout.setVisibility(View.VISIBLE);
                    editTextDateOfVisit.setText(couple.getIfNoOption().getDateOfVisit().toString());
                    editTextDateOfGroupMeeting.setText(couple.getIfNoOption().getDateOfGroupMeeting().toString());
//                    editTextFutureMethod.setText(couple.getIfNoOption().getFutureMethod().toString());

                    if(couple.getIfNoOption().getFutureMethod() != null) {
                        switch (couple.getIfNoOption().getFutureMethod()) {
                            case "गर्भनिरोधक गोळ्या":
                                radioGroupFutureMethod.check(R.id.radioFutureMethodOralPills);
                                break;
                            case "कॉपर टी":
                                radioGroupFutureMethod.check(R.id.radioFutureMethodCut);
                                break;
                            case "अंतरा":
                                radioGroupFutureMethod.check(R.id.radioFutureMethodAntara);
                                break;
                            case "निरोध":
                                radioGroupFutureMethod.check(R.id.radioFutureMethodNirodh);
                                break;
                            case "नसबंदी":
                                radioGroupFutureMethod.check(R.id.radioFutureMethodSterilization);
                                break;
                        }
                    }


                    break;
                case "अंतरा":
                    radioGroupFamilyPlanningMethod.check(R.id.radioMethodAntara);
                    if(couple.getFpmdates() != null) {

                        dateIfNoMethodParentLinearLayout.setVisibility(View.VISIBLE);
                        ifNoMethodLayout.setVisibility(View.GONE);
                        cutAntaraOralPillsDateLinearLayout.setVisibility(View.VISIBLE);
                        btnAddCutAntaraOralPillsDate.setVisibility(View.VISIBLE);

                        for(String date : couple.getFpmdates()) {
                            layoutInflater.inflate(R.layout.familydetail_vaccinationdate, cutAntaraOralPillsDateLinearLayout, true).findViewById(R.id.editVaccinationDate).setId(cutAntaraOralPillsDateId);
                            ((TextInputEditText) cutAntaraOralPillsDateLinearLayout.findViewById(cutAntaraOralPillsDateId)).setText(String.valueOf(date));
                            cutAntaraOralPillsDateId++;
                        }

                    }
                    break;
                case "निरोध":
                    radioGroupFamilyPlanningMethod.check(R.id.radioMethodNirodh);
                    break;
                case "नसबंदी":
                    radioGroupFamilyPlanningMethod.check(R.id.radioMethodSterilization);
                    break;
                default:
                    ifNoMethodLayout.setVisibility(View.GONE);
                    cutAntaraOralPillsDateLinearLayout.setVisibility(View.GONE);
                    btnAddCutAntaraOralPillsDate.setVisibility(View.GONE);
            }
        }




        cutAntaraOralPillsDateTextWatcher();


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

    private void removeListenersFromComponents() {

        editTextTotalMaleChildren.removeTextChangedListener(textWatcher);
        editTextTotalFemaleChildren.removeTextChangedListener(textWatcher);
        editTextAgeLastChild.removeTextChangedListener(textWatcher);
//        editTextFutureMethod.removeTextChangedListener(textWatcher);
        editTextDateOfGroupMeeting.removeTextChangedListener(textWatcher);
        editTextDateOfVisit.removeTextChangedListener(textWatcher);
        editCoupleId.removeTextChangedListener(textWatcher);
//        editTextWifeName.removeTextChangedListener(textWatcher);
//        editTextHusbandName.removeTextChangedListener(textWatcher);


//        newHusbandNameSpinner.setOnItemSelectedListener(null);
//        newWifeNameSpinner.setOnItemSelectedListener(null);


        husbandNameSpinner.setOnItemSelectedListener(null);
        wifeNameSpinner.setOnItemSelectedListener(null);

        radioGroupFamilyPlanningMethod.setOnCheckedChangeListener(null);
        radioGroupGenderLastChild.setOnCheckedChangeListener(null);
        radioGroupFutureMethod.setOnCheckedChangeListener(null);

    }

    private void clearDataFromLayout() {

        cutAntaraOralPillsDateId = 0;
        cutAntaraOralPillsDateLinearLayout.removeAllViews();

//        editTextWifeName.getText().clear();
//        editTextHusbandName.getText().clear();
        radioGroupFamilyPlanningMethod.clearCheck();
//        editTextFutureMethod.getText().clear();
        editTextDateOfVisit.getText().clear();
        editTextDateOfGroupMeeting.getText().clear();
        editTextTotalMaleChildren.getText().clear();
        editTextTotalFemaleChildren.getText().clear();
        editTextAgeLastChild.getText().clear();
        radioGroupGenderLastChild.clearCheck();
        editTextDateOfVisit.getText().clear();
        editTextDateOfGroupMeeting.getText().clear();
        radioGroupFutureMethod.clearCheck();
        editCoupleId.getText().clear();
//        editTextFutureMethod.getText().clear();

    }

    private void cutAntaraOralPillsDateTextWatcher() {

        cutAntaraOralPillsDateTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(eligibleCoupleNames.get(selectedCoupleIndex).getFpmdates() != null) {
////                    System.out.println(eligibleCoupleNames.get(selectedCoupleIndex).getFpmdates());
                    for (int id = 0; id < eligibleCoupleNames.get(selectedCoupleIndex).getFpmdates().size(); id++) {
                        if (s.hashCode() == ((TextInputEditText) cutAntaraOralPillsDateLinearLayout.findViewById(id)).getText().hashCode()) {

                            if (!s.toString().equals(eligibleCoupleNames.get(selectedCoupleIndex).getFpmdates().get(id).toString())) {

                                cutAntaraOralPillsDateList.put(id, ((TextInputEditText) cutAntaraOralPillsDateLinearLayout.findViewById(id)).getText().toString());
                                changedInformation.put("fpmdates", cutAntaraOralPillsDateList);
//                                System.out.println("Changes " + changedInformation.get("fpmdates"));

                                if (saveCoupleInformation.getVisibility() == View.GONE) {
                                    saveCoupleInformation.setVisibility(View.VISIBLE);
                                }

                            } else {
                                if (cutAntaraOralPillsDateList.isEmpty()) {
                                    changedInformation.remove("DPTB_OPVB_MR2_VitA2");
                                } else {
                                    cutAntaraOralPillsDateList.remove(id);
                                    if (cutAntaraOralPillsDateList.isEmpty()) {
                                        changedInformation.remove("DPTB_OPVB_MR2_VitA2");
                                    } else {
                                        changedInformation.put("fpmdates", cutAntaraOralPillsDateList);
                                    }
                                }

                                if (changedInformation.isEmpty() && addDateClicked == 0) {
                                    saveCoupleInformation.setVisibility(View.GONE);
                                }

                            }

                        }
//                        System.out.println("Changes list" + changedInformation.get("fpmdates"));
                    }
                }

            }
        };


        if(eligibleCoupleNames.get(selectedCoupleIndex).getFpmdates() != null) {
            for (int id = 0; id < eligibleCoupleNames.get(selectedCoupleIndex).getFpmdates().size(); id++) {
                ((TextInputEditText) cutAntaraOralPillsDateLinearLayout.findViewById(id)).addTextChangedListener(cutAntaraOralPillsDateTextWatcher);

                final int finalId = id;
                ((TextInputEditText) cutAntaraOralPillsDateLinearLayout.findViewById(id)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                                String date = dayOfMonth + "/" + (month + 1) + "/" + year;

                                ((TextInputEditText) cutAntaraOralPillsDateLinearLayout.findViewById(finalId)).setText(date);

                            }
                        };

                        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), R.style.Theme_MaterialComponents_Light_Dialog_MinWidth, onDateSetListener, year, month, day);

                        datePickerDialog.getWindow();
                        datePickerDialog.show();
                    }
                });

            }
        }


    }

    private void addValidation() {
        awesomeValidation.addValidation(editCoupleId, RegexTemplate.NOT_EMPTY, "कृपया " +
                "couple id प्रविष्ट करा");
    }

}
