package com.bdstar.taxi.net.apis;

import com.bdstar.taxi.net.bean.Ads;
import com.bdstar.taxi.net.bean.DefaultData;
import com.bdstar.taxi.net.bean.Destinations;
import com.bdstar.taxi.net.bean.DriverInfoResponse;
import com.bdstar.taxi.net.bean.FPUrl;
import com.bdstar.taxi.net.bean.InsertRecordRequest;
import com.bdstar.taxi.net.bean.Notification;
import com.bdstar.taxi.net.bean.OrderData;
import com.bdstar.taxi.net.bean.OrderSaveRequest;
import com.bdstar.taxi.net.bean.OrderSaveResponse;
import com.bdstar.taxi.net.bean.Response;
import com.bdstar.taxi.net.bean.UpdateInfo;
import com.bdstar.taxi.net.bean.UpdateRecordRequest;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface BaseApi {

    @POST("Taxi/appRecord/insertRecord")
    Observable<Response<OrderData>> createOrder(@Body InsertRecordRequest request);

    @GET("Taxi/appDriver/selectDriverCardByCardNo")
    Observable<DriverInfoResponse> getDriverInfo(@Query("code") String cardNo);

    @GET("Taxi/appProDes/selectProDestination")
    Observable<Response<Destinations>> getDestinations();

    @POST("Taxi/appRecord/updateRecord")
    Observable<Response<DefaultData>> updateRecord(@Body UpdateRecordRequest recordRequest);

    @GET("Taxi/led/select")
    Observable<Response<Ads>> getAds();

    @GET("Taxi/appDriver/selectMsg")
    Observable<Response<Notification>> getNotice();

    @GET("Taxi/appDriver/selectApkVersion")
    Observable<Response<UpdateInfo>> getUpdateInfo();

    @GET("Taxi/appDriver/insertDevice")
    Observable<Response<DefaultData>> uploadDeviceUUID(@Query("name") String uuid);

    @GET(" ")
    Observable<Response<FPUrl>> getFPUrl(@Query("id") String orderId);
}
