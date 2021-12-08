/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pahappa.systems.core.utils;

import com.flutterwave.rave.java.entry.cardPayment;
import com.flutterwave.rave.java.entry.mobileMoney;
import com.flutterwave.rave.java.entry.validateCardCharge;
import com.flutterwave.rave.java.payload.cardLoad;
import com.flutterwave.rave.java.payload.mobilemoneyPayload;
import com.flutterwave.rave.java.payload.validateCardPayload;
import java.net.UnknownHostException;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author RayGdhrt
 */
public class FlutterwaveClient {

    public static final String FLUTTERWAVE_PUBLIC_KEY = "FLWPUBK_TEST-f43bfbb4d1e42b1452641aa8585f49ab-X";
    public static final String FLUTTERWAVE_ENCRYPTION_KEY = "FLWSECK_TESTe172564b6676";

    public static String makeMMPayment(mobilemoneyPayload mobilemoneyPayload) throws UnknownHostException {
        mobileMoney mobileMoney = new mobileMoney();

        mobilemoneyPayload.setEncryption_key(FLUTTERWAVE_ENCRYPTION_KEY);
        mobilemoneyPayload.setPublic_key(FLUTTERWAVE_PUBLIC_KEY);

        String response = mobileMoney.domobilemoney(mobilemoneyPayload);

        return response;

    }

    public static String makeCardPayment(cardLoad cardload) throws JSONException, UnknownHostException {
        cardPayment cardPayment = new cardPayment();
        cardload.setMetaname("");
        cardload.setMetavalue(FLUTTERWAVE_PUBLIC_KEY);

        cardload.setRedirect_url("");
        cardload.setDevice_fingerprint("");
        cardload.setEncryption_key(FLUTTERWAVE_ENCRYPTION_KEY);
        cardload.setPublic_key(FLUTTERWAVE_PUBLIC_KEY);

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
}
