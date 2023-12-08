package com.example.formuser_example_04;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    TextInputLayout tiName,tiEmail,tiPassword;

    Button register;

    String name,email,password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tiName=findViewById(R.id.tiName);
        tiEmail= findViewById(R.id.tiEmail);
        tiPassword= findViewById(R.id.tiPassword);

        register= findViewById(R.id.buttonRegister);

        tiEmail.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!validateEmail(tiEmail.getEditText().getText().toString())){
                    tiEmail.setError("Email invalido");
                }else{
                    tiEmail.setError(null);
                    tiEmail.setBoxStrokeColor(Color.GREEN);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name= tiName.getEditText().getText().toString();
                email=tiEmail.getEditText().getText().toString();
                password=tiPassword.getEditText().getText().toString();
                if(name.isEmpty() || email.isEmpty() || password.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Todos los campos deben diligenciarse",Toast.LENGTH_LONG).show();
                }else{
                    if (validateUser(email)){
                        tiEmail.setError("Este correo ya esta registrado");
                    }else{
                        UserModel user= new UserModel(name,email,password);
                        saveUser(user);
                        Toast.makeText(getApplicationContext(),"Usuario Registrado",Toast.LENGTH_LONG).show();
                        tiName.getEditText().setText("");
                        tiEmail.getEditText().setText("");
                        tiPassword.getEditText().setText("");
                        tiEmail.setError(null);
                        tiEmail.setBoxStrokeColor(Color.BLACK);
                    }
                }
            }
        });



    }

    public boolean validateEmail(String email){
        // Patron(Pattern) de un email --> texto@texto.texto
        Pattern pattern= Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    public void saveUser(UserModel user){

        //crear un archivo
        File user_file= new File(getFilesDir(),"user.txt");

        try {
            FileWriter writer= new FileWriter(user_file, true);
            BufferedWriter bufferedWriter= new BufferedWriter(writer);

            bufferedWriter.write(
                    user.getName()+","+
                       user.getEmail()+","+
                       user.getPassword()
            );
            bufferedWriter.newLine();
            bufferedWriter.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public boolean validateUser(String email){

        File file= new File(getFilesDir(),"user.txt");
        boolean exist= false;
        try {
            FileReader reader= new FileReader(file);
            BufferedReader bufferedReader= new BufferedReader(reader);
            String line;

            while ((line=bufferedReader.readLine())!=null){
                String []data= line.split(",");
                if(email.equalsIgnoreCase(data[1])){
                    exist=true;
                    break;
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

       return exist;

    }



}