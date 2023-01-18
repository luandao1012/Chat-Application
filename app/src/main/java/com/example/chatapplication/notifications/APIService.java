package com.example.chatapplication.notifications;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers({
            "Content-Type:application/json",
            "Authotization:key=AAAAEbE-vbQ:APA91bFBc46wnEuDcmc5RMeBportMkUEIH_0toUd40cFkGYRb3kx89xpso3ihDqeNB7TOOlTxaxOHYqP_aWMTNDnGY_UvyvkSlge0CGLPFLSzynwPx74GVwMm1G99UVQVPCS4m5ouIuu"
    })

    @POST("fcm/send")
    Call<Response> sendNotification(@Body Sender body);
}
