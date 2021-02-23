package com.pintu.neostore.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pintu.neostore.APIMsg;
import com.pintu.neostore.R;
import com.pintu.neostore.register.Register;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends AppCompatActivity {

    private LoginAPI loginAPI;
    private APIMsg message;

    EditText UserName,Password;
    TextView Forgot;
    Button Login;
    FloatingActionButton Fab;

    String UserNames,Passwords;
    String namePattern = "[a-zA-Z]+";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);
        getSupportActionBar().hide();

        UserName = (EditText)findViewById(R.id.ed_username);
        Password = (EditText)findViewById(R.id.ed_password);
        Login =(Button)findViewById(R.id.btn_login);
        Fab = (FloatingActionButton)findViewById(R.id.fab);
        Forgot = (TextView)findViewById(R.id.tv_forgot_pas_link);

        Forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(com.pintu.neostore.login.Login.this, com.pintu.neostore.forgot.Forgot.class);
                startActivity(intent);
            }
        });


        Fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserNames = UserName.getText().toString().trim();
                Passwords = Password.getText().toString().trim();

                if(UserNames.length()==0 || Passwords.length()==0){


                    if(UserNames.length()==0){
                       UserName.requestFocus();
                        UserName.setError("FIELD CANNOT BE EMPTY");
                    }else if(UserNames.length()!=0 && !UserNames.matches(namePattern)){
                        UserName.requestFocus();
                        UserName.setError("ENTER ONLY ALPHABETICAL CHARACTER");
                    }
                    if(Passwords.length()==0){
                        Password.requestFocus();
                        Password.setError("FIELD CANNOT BE EMPTY");
                    }
                }else{

//                    Intent intent = new Intent(com.pintu.neostore.login.Login.this, com.pintu.neostore.home.Home.class);
//                    startActivity(intent);

                    Gson gson = new GsonBuilder().serializeNulls().create();
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("http://staging.php-dev.in:8844/trainingapp/api/users/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    loginAPI = retrofit.create(LoginAPI.class);

                    createLogin();
                }

            }
        });
    }

    private void createLogin(){
//        int n = Integer.parseInt(Phones);
//        System.out.println(n);

       LoginModel loginModel = new LoginModel(UserNames,Passwords);
        System.out.println("-------------------------------------------------------");
        System.out.println(loginModel.getEmail());

        Call<APIMsg> call = loginAPI.createLogin(UserNames,Passwords);

        call.enqueue(new Callback<APIMsg>() {
            @Override
            public void onResponse(Call<APIMsg> call, Response<APIMsg> response) {
                if(response.isSuccessful()){

                 //   message=new Gson().fromJson(response.errorBody().charStream(),APIMsg.class);
                   // Toast.makeText(Login.this,""+message.getUser_msg(),Toast.LENGTH_LONG).show();
//                    System.out.println("------------------UnSucessful------------------");
//                    System.out.println(response.code());
//                    System.out.println(response.message());
                    APIMsg postResponse = response.body();
//
                    String content = "";
                    content += "Code: " + response.code()+ "\n";
                    content += "Email : " + postResponse.getMessage() + "\n";
                    System.out.println("--------------------------------------------------------------------------------------------------");
//                System.out.println(response.code());
//                System.out.println(postResponse.getMessage());
//                System.out.println(response.body());
                    Toast.makeText(Login.this, response.body().getMessage(), Toast.LENGTH_LONG).show();

                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(
                                Login.this,
                                jObjError.getString("user_msg"),
                                Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

            }
            @Override
            public void onFailure(Call<APIMsg> call, Throwable t) {

                Toast.makeText(Login.this,"Check Internet Connection",Toast.LENGTH_LONG).show();
                System.out.println("-------------------------------------------------------");
                System.out.println(t.getMessage());
                System.out.println("------------ff------UnSucessful------------------");
            }
        });
    }
}
