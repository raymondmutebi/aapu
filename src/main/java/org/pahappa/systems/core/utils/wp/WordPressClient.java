/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pahappa.systems.core.utils.wp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.pahappa.systems.core.services.SystemSettingService;
import org.pahappa.systems.core.utils.SSLUtilities;
import org.pahappa.systems.models.SystemSetting;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;
import org.sers.webutils.server.shared.CustomLogger;

/**
 *
 * @author RayGdhrt
 */
public class WordPressClient {

    public static final String FLUTTERWAVE_PUBLIC_KEY = "FLWPUBK_TEST-f43bfbb4d1e42b1452641aa8585f49ab-X";
    public static final String FLUTTERWAVE_ENCRYPTION_KEY = "FLWSECK_TESTe172564b6676";
    public static final String FLUTTER_SECRET_KEY = "FLWSECK-433eeec81d5f1e5bdb811111db9484dd-X";

    public static SystemSetting systemSetting;
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    public void initSettings() {

        systemSetting = ApplicationContextProvider.getBean(SystemSettingService.class).getAppSetting();

        CustomLogger.log(WordPressClient.class, systemSetting.toString());

    }

    public List<WordPressPost> fetchPosts(String transactionId) throws IOException {
        //  initSettings();
        SSLUtilities.trustAllHostnames();
        SSLUtilities.trustAllHttpsCertificates();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });

        OkHttpClient client = builder.build();
        Request request = new Request.Builder()
                //   .url(systemSetting.getPostEndpoint())
                .url("http://aapug.org/wp-json/wp/v2/posts")
                .get()
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .build();

        Response response = client.newCall(request).execute();
        CustomLogger.log(WordPressClient.class,
                "Recieved Response from wp site ..\n" + response.body());
        Gson gSon = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        Type collectionType = new TypeToken<List<WordPressPost>>() {}.getType();
        List<WordPressPost> posts = gSon.fromJson(response.body().string(), collectionType);
       
        return posts;
    }

    public static void main(String[] args) {
        try {
            new WordPressClient().fetchPosts("oooo");
        } catch (IOException ex) {
            Logger.getLogger(WordPressClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
