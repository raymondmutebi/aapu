/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pahappa.systems.core.utils;

import com.flutterwave.rave.java.entry.cardPayment;
import com.flutterwave.rave.java.entry.mobileMoney;
import com.flutterwave.rave.java.entry.transValidation;
import com.flutterwave.rave.java.entry.validateCardCharge;
import com.flutterwave.rave.java.payload.cardLoad;
import com.flutterwave.rave.java.payload.mobilemoneyPayload;
import com.flutterwave.rave.java.payload.transverifyPayload;
import com.flutterwave.rave.java.payload.validateCardPayload;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.net.UnknownHostException;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;
import org.pahappa.systems.core.services.SystemSettingService;
import org.pahappa.systems.models.SystemSetting;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;
import org.sers.webutils.server.shared.CustomLogger;

/**
 *
 * @author RayGdhrt
 */
public class FlutterwaveClient {

    public static final String FLUTTERWAVE_PUBLIC_KEY = "FLWPUBK_TEST-f43bfbb4d1e42b1452641aa8585f49ab-X";
    public static final String FLUTTERWAVE_ENCRYPTION_KEY = "FLWSECK_TESTe172564b6676";
    public static final String FLUTTER_SECRET_KEY = "FLWSECK-433eeec81d5f1e5bdb811111db9484dd-X";

    public static SystemSetting systemSetting;
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    public void initSettings() {

        systemSetting = ApplicationContextProvider.getBean(SystemSettingService.class).getAppSetting();

        CustomLogger.log(FlutterwaveClient.class, systemSetting.toString());

    }

    public String makeMMPayment(mobilemoneyPayload mobilemoneyPayload) throws UnknownHostException {
        initSettings();
        mobileMoney mobileMoney = new mobileMoney();

        mobilemoneyPayload.setEncryption_key(systemSetting.getFlutterwaveEncryptionKey());
        mobilemoneyPayload.setPublic_key(systemSetting.getFlutterwavePublicKey());

        String response = mobileMoney.domobilemoney(mobilemoneyPayload);

        return response;

    }

    public static String makePayment(cardLoad cardload) throws JSONException, UnknownHostException {
        cardPayment cardPayment = new cardPayment();
        cardload.setMetaname("");
        cardload.setMetavalue(systemSetting.getFlutterwavePublicKey());

        cardload.setRedirect_url("");
        cardload.setDevice_fingerprint("");
        cardload.setEncryption_key(systemSetting.getFlutterwaveEncryptionKey());
        cardload.setPublic_key(systemSetting.getFlutterwavePublicKey());

        String response = cardPayment.doflwcardpayment(cardload);

        JSONObject myObject = new JSONObject(response);

        if (myObject.optString("suggested_auth").equals("PIN")) {
            //get PIN fom customer
            cardload.setPin("Enter pin");
            cardload.setSuggested_auth("PIN");
            String response_one = cardPayment.doflwcardpayment(cardload);

            JSONObject iObject = new JSONObject(response_one);
            JSONObject Object = iObject.optJSONObject("data");

            String transaction_reference = Object.optString("flwRef");

            validateCardCharge validatecardcharge = new validateCardCharge();
            validateCardPayload validatecardpayload = new validateCardPayload();
            validatecardpayload.setPBFPubKey("Eneter pubkey");
            validatecardpayload.setTransaction_reference(transaction_reference);
            validatecardpayload.setOtp("Enter otp");

            response = validatecardcharge.doflwcardvalidate(validatecardpayload);
        } else if (myObject.optString("suggested_auth").equals("NOAUTH_INTERNATIONAL")) {
            //billing info - billingzip, billingcity, billingaddress, billingstate, billingcountry
//            cardload.setBillingaddress(Billingaddress);
//            cardload.setBillingcity(Billingcity);
//            cardload.setBillingcountry(Billingcountry);
//            cardload.setBillingstate(Billingstate);
//            cardload.setBillingzip(Billingzip);
            cardload.setSuggested_auth("NOAUTH_INTERNATIONAL");
            String response_one = cardPayment.doflwcardpayment(cardload);

            JSONObject iObject = new JSONObject(response_one);
            JSONObject Object = iObject.optJSONObject("data");

            String transaction_reference = Object.optString("flwRef");

            validateCardCharge validatecardcharge = new validateCardCharge();
            validateCardPayload validatecardpayload = new validateCardPayload();
            validatecardpayload.setPBFPubKey("Enter otp");
            validatecardpayload.setTransaction_reference(transaction_reference);

            response = validatecardcharge.doflwcardvalidate(validatecardpayload);
        } else if (!"N/A".equals(myObject.optString("authurl"))) {
            //load the url in an IFRAME
        }

        return response;

    }

    public FlutterReponse requestPaymentInitiation(String transactionId, float amount, String email, String phoneNumber, String customerName, String redirectUrl) throws IOException {
        FluterwaveRequest fluterwaveRequest = new FluterwaveRequest()
                .addAmount(String.valueOf(amount))
                .addCurrency("UGX")
                .addTxRef(transactionId)
                .addPaymentOptions("card,mobilemoney")
                .addRedirectUrl(redirectUrl)
                .addCustomer(
                        new Customer()
                                .addEmail(email)
                                .addPhonenumber(phoneNumber)
                                .addName(customerName))
                .addCustomizations(new Customizations()
                        .addLogo("https://aapug.org/wp-content/uploads/2021/10/cropped-image014-1.png")
                        .addTitle("Payment for AAPU membership")
                        .addDescription("Registration fee for AAPU"));

        initSettings();
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
        RequestBody body = RequestBody.create(JSON, new Gson().toJson(fluterwaveRequest));
        Request request = new Request.Builder()
                .url(systemSetting.getFlutterwaveUrl() + "payments/")
                .get()
                .post(body)
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer " + systemSetting.getFlutterwaveSecretKey())
                .addHeader("Content-Type", "application/json")
                .build();

        Response response = client.newCall(request).execute();

        FlutterReponse flutterReponse = new Gson().fromJson(response.body().string(), FlutterReponse.class);
        CustomLogger.log(FlutterwaveClient.class,
                "String Response for payment status add trans ID" + transactionId + "..\n" + flutterReponse);

        return flutterReponse;
    }

    public void buildPaymentJsonObject() {

    }

    public String checkPaymentStatus(String transactionId) {
        initSettings();
        transValidation transValidation = new transValidation();
        transverifyPayload transverifyPayload = new transverifyPayload();
        transverifyPayload.setSECKEY(systemSetting.getFlutterwaveSecretKey());
        transverifyPayload.setTxref(transactionId);
        transverifyPayload.setTest("true");

        String response = transValidation.bvnvalidate(transverifyPayload);
        CustomLogger.log(FlutterwaveClient.class,
                "String Response for payment ststus add trans ID" + transactionId + "..\n" + response);

        return response;

    }

    public FlutterReponse checkPaymentStatusV2(String transactionId) throws IOException {
        initSettings();
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
                .url(systemSetting.getFlutterwaveUrl() + "transactions/" + transactionId + "/verify")
                .get()
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer " + systemSetting.getFlutterwaveSecretKey())
                .addHeader("Content-Type", "application/json")
                .build();

        Response response = client.newCall(request).execute();
        CustomLogger.log(FlutterwaveClient.class,
                "String Response for payment ststus add trans ID" + transactionId + "..\n" + response.body().toString());
        Gson gSon = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
       
        FlutterReponse flutterReponse = gSon.fromJson(response.body().string(), FlutterReponse.class);

        return flutterReponse;
    }

    public void main(String[] args) {
        //new FlutterwaveClient().requestPaymentInitiation("shdbcsdhbchjscd", 0, "WYSBUHISIXIXS", FLUTTER_SECRET_KEY, FLUTTER_SECRET_KEY)
    }
}
