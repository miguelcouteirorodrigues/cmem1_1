package pt.ipvc.miguels.cmmtrabalho1;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitAdapter {

    private static Retrofit retrofit;
    private static RestAPIService service;

    private RetrofitAdapter () { }

    public static RestAPIService getInstance()
    {
        if (retrofit == null)
        {
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.37.129.3:3000")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            service = retrofit.create(RestAPIService.class);
        }

        return service;
    }
}
