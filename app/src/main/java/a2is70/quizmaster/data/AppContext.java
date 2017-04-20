package a2is70.quizmaster.data;


import java.io.IOException;

import a2is70.quizmaster.database.DBInterface;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppContext {
    private static final AppContext ourInstance = new AppContext();

    private Account myAccount;

    private DBInterface dbi;

    public static AppContext getInstance() {
        return ourInstance;
    }

    private String cookies;

    private AppContext() {
        myAccount = null;

        try {
            // hacky hacky session cookies
            OkHttpClient client = new OkHttpClient();
            client.interceptors().add(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    if (cookies != null) {
                        request = request.newBuilder().addHeader("Cookie", cookies).build();
                    }

                    Response response = chain.proceed(request);

                    if (response.isSuccessful()) {
                        String cookie = response.header("Set-Cookie", "");
                        if (cookie.contains("sessionid=")) {
                            cookies = cookie;
                        }
                    }

                    return response;
                }
            });
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(DBInterface.SERVER_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create());

            Retrofit retrofit = builder.client(new OkHttpClient.Builder().build()).build();

            dbi = retrofit.create(DBInterface.class);
        } catch (Exception e){

        }
    }

    public void setAccount(Account a){
        myAccount = a;
    }

    public Account getAccount(){
        return myAccount;
    }

    public DBInterface getDBI(){
        return dbi;
    }
}
