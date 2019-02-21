package com.booburbundle1.adforest.profile;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.booburbundle1.adforest.home.HomeActivity;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.booburbundle1.adforest.R;
import com.booburbundle1.adforest.ad_detail.Ad_detail_activity;
import com.booburbundle1.adforest.helper.MyAdsOnclicklinstener;
import com.booburbundle1.adforest.modelsList.myAdsModel;
import com.booburbundle1.adforest.profile.adapter.ItemMyAdsAdapter;
import com.booburbundle1.adforest.utills.AnalyticsTrackers;
import com.booburbundle1.adforest.utills.CustomBorderDrawable;
import com.booburbundle1.adforest.utills.NestedScroll;
import com.booburbundle1.adforest.utills.Network.RestService;
import com.booburbundle1.adforest.utills.SettingsMain;
import com.booburbundle1.adforest.utills.UrlController;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MyAds_Inactive extends Fragment {

    SettingsMain settingsMain;

    private TextView tv1;
    private EditText etName,etEmail,etSubject,etMessage;
    private Button btnSend;
    JsonObject creds;



    RecyclerView recyclerView;
    ItemMyAdsAdapter adapter;
    int nextPage = 1;
    boolean loading = true, hasNextPage = false;
    ProgressBar progressBar;
    NestedScrollView nestedScrollView;
    RestService restService;
    private ArrayList<myAdsModel> list = new ArrayList<>();

    public MyAds_Inactive() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.contact_us, container, false);
        getActivity().setTitle("Contact Us");
        settingsMain = new SettingsMain(getActivity());

        tv1             =(TextView)(view.findViewById(R.id.textView25));
        etName          =(EditText)view.findViewById(R.id.editText5);
        etEmail         =(EditText)(view.findViewById(R.id.editText6));
        etSubject       =(EditText)(view.findViewById(R.id.editText7));
        etMessage       =(EditText)(view.findViewById(R.id.editText8));
        btnSend         =(Button)  (view.findViewById(R.id.button2));

//        restService = UrlController.createService(RestService.class, settingsMain.getUserEmail(), settingsMain.getUserPassword(), getActivity());


        btnSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String uName    = etName.getText().toString().trim();
                String uEmail   = etEmail.getText().toString().trim();
                String uSubject = etSubject.getText().toString().trim();
                String uMesage  = etMessage.getText().toString().trim();

                if (TextUtils.isEmpty(uName) || TextUtils.isEmpty(uEmail) || TextUtils.isEmpty(uSubject) || TextUtils.isEmpty(uMesage)) {
                    Toast.makeText(getActivity(), "Fill the required field", Toast.LENGTH_LONG).show();

                    if (TextUtils.isEmpty(uMesage)) {
                        etMessage.setBackgroundColor(Color.parseColor("#f5e8a0"));
                        etMessage.requestFocus();
                    }else {
                        etMessage.setBackgroundColor(Color.parseColor("#ffffff"));
                    }

                    if (TextUtils.isEmpty(uSubject)) {
                        etSubject.setBackgroundColor(Color.parseColor("#f5e8a0"));
                        etSubject.requestFocus();
                    }else {
                        etSubject.setBackgroundColor(Color.parseColor("#ffffff"));
                    }

                    if (TextUtils.isEmpty(uEmail)) {
                        etEmail.setBackgroundColor(Color.parseColor("#f5e8a0"));
                        etEmail.requestFocus();
                    }else {
                        etEmail.setBackgroundColor(Color.parseColor("#ffffff"));
                    }

                    if (TextUtils.isEmpty(uName)) {
                        etName.setBackgroundColor(Color.parseColor("#f5e8a0"));
                        etName.requestFocus();
                    }else {
                        etName.setBackgroundColor(Color.parseColor("#ffffff"));
                    }



                }else if( !uEmail.matches("\\b[\\w.%-]+@[-.\\w]+\\.[A-Za-z]{2,4}\\b")) {
                    Toast.makeText(getActivity(), "Invalid Email", Toast.LENGTH_SHORT).show();
                    etEmail.requestFocus();
                    etEmail.setBackgroundColor(Color.parseColor("#f5e8a0"));
                    etEmail.setTextColor(Color.RED);
                    return;
                }else{
                    etEmail.setTextColor(Color.BLACK);
                    contact(uName, uEmail, uSubject, uMesage);



                }
            }
        });

        final LinearLayoutManager MyLayoutManager = new LinearLayoutManager(getActivity());
        MyLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        SwipeRefreshLayout swipeRefreshLayout = getActivity().findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setEnabled(true);

        return view;
    }

    private void contact(String uName, String uEmail, String uSubject, String uMessage) {
        String URL = "https://boobur.com/wp-json/adforest/v1/";
        JSONObject jasonObjectcontact = new JSONObject();

//        try {
//            jasonObjectcontact.put("name",uName);
//            jasonObjectcontact.put("email",uEmail);
//            jasonObjectcontact.put("subject",uSubject);
//            jasonObjectcontact.put("message",uMessage);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }


//        JsonObject params = new JsonObject();
//        params.addProperty("email",uEmail);
//        params.addProperty("name",uName);
//        params.addProperty("subject",uSubject);
//        params.addProperty("message",uMessage);



//        Log.d("jasondata", String.valueOf(jasonObjectcontact));
//        Log.d("paramsDAta", String.valueOf(params));



        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        restService = retrofit.create(RestService.class);

        HashMap<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("Content-Type", "application/jason");



        Call<ResponseBody> call = restService.contact_us(headerMap,uName,uEmail,uSubject,uMessage);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("response",  response.toString());

                try{
                    String json = response.body().string();
                    Log.d("data", json);


//                    JSONObject jsonObject4 = new JSONObject(String.valueOf(response));
//                    String success = jsonObject4.getString("success");
//                    String msg = jsonObject4.getString("message");
//                    Toast.makeText(getActivity(), "info"+msg+"||"+success, Toast.LENGTH_SHORT).show();
//                    Log.d("successstatus", success);
//                    Log.d("messagebody", msg);

                    Toast.makeText(getActivity(), "Message Sent", Toast.LENGTH_SHORT).show();
                    changeScreen();

                }
                catch (IOException e){
                    Log.e("err2", "onResponse: JSONException: " + e.getMessage() );
                }
//                catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });



    }

    private void changeScreen() {
        Intent intent;
        intent = new Intent(getActivity(), HomeActivity.class);
        startActivity(intent);
    }


    private void setAllViewsText(JSONObject jsonObject) {

    }


    public void replaceFragment(Fragment someFragment, String tag) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.right_enter, R.anim.left_out, R.anim.left_enter, R.anim.right_out);
        transaction.replace(R.id.frameContainer, someFragment, tag);
        transaction.addToBackStack(tag);
        transaction.commit();
    }


}
