package com.hospitalmanagement.mainapp.retrofitService.apiInterface;

import com.hospitalmanagement.mainapp.pojo.CreateQuery;
import com.hospitalmanagement.mainapp.pojo.Data;
import com.hospitalmanagement.mainapp.pojo.Family;
import com.hospitalmanagement.mainapp.pojo.MutationQuery;
import com.hospitalmanagement.mainapp.pojo.MutationQueryFamilyDetail;
import com.hospitalmanagement.mainapp.pojo.UpdateResponse;

import retrofit2.Call;
import retrofit2.http.Body;

import retrofit2.http.GET;

import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("/graphql")
    Call<Family> getInformation(@Query("query") String query);

    @POST("/graphql")
    @Headers( "Content-Type: application/json" )
    Call<Family> updateInformation(@Body MutationQueryFamilyDetail mutationQuery);

    @POST("/graphql")
    @Headers( "Content-Type: application/json" )
    Call<Family> addInformation(@Body MutationQueryFamilyDetail mutationQuery);

    @POST("/graphql")
    @Headers("Content-Type: application/json")
    Call<Family> createFamily(@Body CreateQuery createQuery);

}
