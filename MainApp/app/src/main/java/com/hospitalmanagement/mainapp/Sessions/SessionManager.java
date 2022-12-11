package com.hospitalmanagement.mainapp.Sessions;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hospitalmanagement.mainapp.GetterSetter.CoupleDetails;
import com.hospitalmanagement.mainapp.GetterSetter.FamilyMemberList;
import com.hospitalmanagement.mainapp.GetterSetter.PregnantDetails;

import org.bson.types.ObjectId;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class SessionManager {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;

    public static final String MYPREFERENCES = "HOSPITAL_PREF";
    public static final String TOILET = "TOILET";
    public static final String AREANAME = "AREANAME";
    public static final String BPL = "BPL";
    public static final String HOUSE = "HOUSE";
    public static final String WATER = "WATER";
    public static final String CASTE = "CASTE";
    public static final String FAMCOUNT = "FAMCOUNT";
    public static final String MOBILE = "MOBILE";
    public static final String ISGENINFOSTORED = "ISGENINFOSTORED";
    public static final String ISMEMDETAILSSTORED = "ISMEMDETAILSSTORED";
    public static final String MEMBERDETAILS = "MEMBERDETAILS";
    public static final String COUPLEDETAILS = "COUPLEDETAILS";
    public static final String PREGNANTDETAILS = "PREGNANTDETAILS";
    public static final String ISPREGINFOSTORED = "ISPREGINFOSTORED";

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(MYPREFERENCES,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setGeneralInfo(String areaName, boolean toilet, boolean bpl, boolean house,
                               String water, String caste, int famCount, String mobile,
                               boolean isGenInfoStored) {
        editor.putString(AREANAME, areaName);
        editor.putBoolean(TOILET, toilet);
        editor.putBoolean(BPL, bpl);
        editor.putBoolean(HOUSE, house);
        editor.putString(WATER, water);
        editor.putString(CASTE, caste);
        editor.putInt(FAMCOUNT, famCount);
        editor.putString(MOBILE, mobile);
        editor.putBoolean(ISGENINFOSTORED, isGenInfoStored);
        editor.putBoolean(ISMEMDETAILSSTORED, false);
        editor.putBoolean(ISPREGINFOSTORED, false);
        editor.commit();
    }

    public boolean isGenInfoStored() {
        return sharedPreferences.getBoolean(ISGENINFOSTORED, false);
    }
    public String getAreaName() {
        return sharedPreferences.getString(AREANAME, null);
    }
    public boolean getToilet() {
        return sharedPreferences.getBoolean(TOILET, true);
    }
    public boolean getBPL() {
        return sharedPreferences.getBoolean(BPL, true);
    }
    public boolean getHouse() {
        return sharedPreferences.getBoolean(HOUSE, true);
    }
    public String getWater() {
        return sharedPreferences.getString(WATER, null);
    }
    public String getCaste() {
        return sharedPreferences.getString(CASTE, null);
    }
    public String getMobile() {
        return sharedPreferences.getString(MOBILE, null);
    }
    public int getFamCount() {
        return sharedPreferences.getInt(FAMCOUNT, 0);
    }

    public void clearAllInformation() {
        editor.clear();
        editor.commit();
    }
    public void saveMemberInformation (ArrayList<FamilyMemberList> list, boolean isMemInfo) {
//        editor.putBoolean(ISMEMDETAILSSTORED, isMemInfo);
//        Gson gson = new Gson();
//        String json = gson.toJson(list);
//        editor.putString(MEMBERDETAILS, json);
//        editor.apply();
//        editor.commit();

        editor.putBoolean(ISMEMDETAILSSTORED, isMemInfo);
        try {
            editor.putString(MEMBERDETAILS, ObjectSerializable.serialize(list));
            editor.apply();
            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("SESSION", "saveMemberInformation: "+e.getLocalizedMessage());
        }
    }
    public ArrayList<FamilyMemberList> getMemberList() {
//        Gson gson = new Gson();
//        String json = sharedPreferences.getString(MEMBERDETAILS, null);
//        Type type = new TypeToken<ArrayList<FamilyMemberList>>(){}.getType();
//        return gson.fromJson(json, type);
        ArrayList<FamilyMemberList> familyMemberLists = null;
        try {
            familyMemberLists =
                    (ArrayList<FamilyMemberList>) ObjectSerializable.deserialize(sharedPreferences.getString(MEMBERDETAILS, null));
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("SESSION", "saveMemberInformation: "+e.getLocalizedMessage());
        }
        return familyMemberLists;
    }
    public boolean isMemInfoStored() {
        return sharedPreferences.getBoolean(ISMEMDETAILSSTORED, false);
    }
    public void saveCoupleInformation (ArrayList<CoupleDetails> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(COUPLEDETAILS, json);
        editor.apply();
    }
    public ArrayList<CoupleDetails> getCoupleList() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(COUPLEDETAILS, null);
        Type type = new TypeToken<ArrayList<CoupleDetails>>(){}.getType();
        return gson.fromJson(json, type);
    }
    public void savePregnantInformation (ArrayList<PregnantDetails> list, boolean isPregInfo) {
        editor.putBoolean(ISPREGINFOSTORED, isPregInfo);
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(PREGNANTDETAILS, json);
        editor.apply();
    }
    public ArrayList<PregnantDetails> getPregnantInformation() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(PREGNANTDETAILS, null);
        Type type = new TypeToken<ArrayList<PregnantDetails>>(){}.getType();
        return gson.fromJson(json, type);
    }
    public boolean isPregInfoStored() {
        return sharedPreferences.getBoolean(ISPREGINFOSTORED, false);
    }
    public static String getMongoId () {
        try {
            ObjectId memberId = new ObjectId();
            System.out.println(memberId.toString());
            return memberId.toString();
        } catch (Exception e) {
            System.out.println(e.getCause());
            return "false";
        }
    }
}
