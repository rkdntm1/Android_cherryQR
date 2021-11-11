package net.cherry.retrofit;

import net.cherry.retrofit.entities.DonateDataInServer;
import net.cherry.retrofit.entities.DonateHistoryDataInServer;
import net.cherry.retrofit.entities.JoinDataInServer;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitAPI {
    // =========== join ================
    @FormUrlEncoded
    @POST("/api/join")
    Call<JoinDataInServer> doJoin(
            @Field("uid") String uid,
            @Field("token") String token,
            @Field("trmnlUno") String trmnlUno,
            @Field("brthdy") String brthdy,
            @Field("sexSeCode") String sexSeCode,
            @Field("userCi") String userCi,
            @Field("moblphonNo") String moblphonNo
    );

    @POST("/api/withdrawal")
    Call<JoinDataInServer> doWithdrawal(
            @Header("Authorization") String authorization
    );

    @GET("/api/userCheck")
    Call<JoinDataInServer> doCheckUser(
            @Header("Authorization") String authorization
    );

    // =========== donate ================
    @GET("/api/donate/history")
    Call<DonateHistoryDataInServer> doHistory(
            @Header("Authorization") String authorization,
            @Query("pageRef") Integer pageRef,
            @Query("year") Integer year,
            @Query("month") Integer month
    );

    @FormUrlEncoded
    @POST("/api/donate")
    Call<DonateDataInServer> doDonate(
            @Header("Authorization") String authorization,
            @Field("mrhstId") String mrhstId,
            @Field("setleAmt") Long setleAmt
    );

    @FormUrlEncoded
    @GET("/api//donate/detail")
    Call<DonateHistoryDataInServer> doDetail(
            @Field("setleSn") Long setleSn
    );
}
