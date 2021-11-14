package net.cherry.retrofit;

import net.cherry.R;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    public static String BASE_URL = "http://172.30.1.3:18080";

    private static Retrofit retrofit;
    public static Retrofit getClient() {
        if(retrofit == null) {
            retrofit = new Retrofit.Builder() //retrofit 객체생성
                    .baseUrl(BASE_URL) // 어떤서버로 네트워크 통신할지
                    .addConverterFactory(GsonConverterFactory.create()) //통신완료 후 어떤 converter로 데이터를 parsing할지
                    .build(); // 통신하여 데이터를 파싱한 retrofit 객체 생성완료.
        }
        return retrofit;
    }
}
