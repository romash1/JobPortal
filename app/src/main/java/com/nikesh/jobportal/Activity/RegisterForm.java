package com.nikesh.jobportal.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.nikesh.jobportal.Model.User;
import com.nikesh.jobportal.R;
import com.nikesh.jobportal.common.Common;

import java.util.ArrayList;
import java.util.List;

public class RegisterForm extends AppCompatActivity {
    EditText password,fullname,phoneNumber, email,work,bio;
    Button btnRegister;
    TextView LoginButton;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;
    LinearLayout programmingList;
    AutoCompleteTextView username;
    CheckBox chkDesign, chkData,chkContent,chkWebsite,chkMobile,chkMarketing;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_form);
//        getSupportActionBar().setTitle("Register user");
        programmingList= findViewById(R.id.programmingList);
        Intent intent = getIntent();
        String as = intent.getStringExtra ("As");
//        Bundle bundle=getIntent().getExtras();
//        String as=bundle.getString("As");
        if (as.equals("JobProvider"))
        {
            programmingList.setVisibility(View.GONE);
        }
        else {
            programmingList.setVisibility(View.VISIBLE);
        }
        firebaseAuth = FirebaseAuth.getInstance();
        username = findViewById(R.id.country);
        progressBar= findViewById(R.id.progressbar);
        password = findViewById(R.id.password);
        email = findViewById(R.id.email);
        fullname = findViewById(R.id.name);
        work = findViewById(R.id.workPlace);
        phoneNumber = findViewById(R.id.phonenumber);
        btnRegister = findViewById(R.id.register);
        bio = findViewById(R.id.Bio);
        LoginButton=findViewById(R.id.login);
        chkContent= findViewById(R.id.chkContentDev);
        chkData= findViewById(R.id.chkDataDev);
        chkMobile= findViewById(R.id.chkMobileDev);
        chkMarketing= findViewById(R.id.chkMarketingDev);
        chkWebsite= findViewById(R.id.chkWebsiteDev);
        chkDesign= findViewById(R.id.chkDesignDev);
        ArrayAdapter<String> stringArrayAdapter=new ArrayAdapter<>(this,android.R.layout.select_dialog_item, Common.countryName);
        username.setAdapter(stringArrayAdapter);
        username.setThreshold(1);

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(RegisterForm.this, LoginForm.class);
                startActivity(intent);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                final String userName = username.getText().toString();
                final String pass = password.getText().toString();
                final String name = fullname.getText().toString();
                final String workP = work.getText().toString();
                final String Bio = bio.getText().toString();
                final String emailAddress = email.getText().toString();
                final String  number= phoneNumber.getText().toString();
                final String image = "Default";

                if(TextUtils.isEmpty(name)){
                    fullname.setError("You have to enter full name!");
                    fullname.requestFocus();
                }

                else if(TextUtils.isEmpty(userName)){
                    username.setError("Please select country!");
                    username.requestFocus();
                }


                else if(TextUtils.isEmpty(number)){
                    phoneNumber.setError("Please enter phone number!");
                    phoneNumber.requestFocus();
                }

                else if(number.length()<10){
                    phoneNumber.setError("Phone number has to be 10 digits!");
                    phoneNumber.requestFocus();
                }

                else if(TextUtils.isEmpty(workP)){
                    work.setError("Please enter work place!");
                    work.requestFocus();
                }
                else if(TextUtils.isEmpty(Bio)){
                    bio.setError("Please write something about yourself!");
                    bio.requestFocus();
                }

                else if(!emailAddress.matches(emailPattern)){
                    email.setError("Please correct your email!");
                    email.requestFocus();
                }

                else if(TextUtils.isEmpty(emailAddress)){
                    email.setError("Please enter email address!");
                    email.requestFocus();
                }

                else if(TextUtils.isEmpty(pass)){
                    password.setError("Please enter password!");
                    password.requestFocus();
                }

                else if(pass.length()<8){
                    password.setError("Password needs to have at least 8 characters!");
                    password.requestFocus();
                }

                else {
                    final List<String> list=new ArrayList<String>();
                    if(chkContent.isChecked())
                    {
                        list.add("Content");
                    }
                    if(chkData.isChecked())
                    {
                        list.add("Data");
                    }
                    if(chkDesign.isChecked())
                    {
                        list.add("Design");
                    }
                    if(chkMobile.isChecked())
                    {
                        list.add("Mobile");
                    }
                    if(chkWebsite.isChecked())
                    {
                        list.add("Website");
                    }
                    if(chkMarketing.isChecked())
                    {
                        list.add("Marketing");
                    }
                    if(list.size()==0)
                    {
                        list.add("Null");
                    }
                    firebaseAuth.createUserWithEmailAndPassword(emailAddress, pass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        User user = new User(FirebaseAuth.getInstance().getCurrentUser().getUid(), name, number, workP, userName,Bio, emailAddress, pass, image,"online",list);
                                        FirebaseDatabase.getInstance().getReference("User")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(user);
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(getApplicationContext(), "Account registered successfully!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), LoginForm.class);
                                        startActivity(intent);
                                    } else {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                }


                            });
                }


            }
        });
    }

}