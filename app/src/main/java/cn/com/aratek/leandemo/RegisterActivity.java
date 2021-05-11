package cn.com.aratek.leandemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import cn.leancloud.AVUser;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

public class RegisterActivity extends AppCompatActivity {
    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String name = et_name.getText().toString().trim();
            String pwd = et_pwd.getText().toString().trim();
            if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(pwd)) {
                btn_register_login.setEnabled(true);
            } else {
                btn_register_login.setEnabled(false);
            }
        }
    };
    private EditText et_name;
    private EditText et_pwd;
    private Button btn_register_login;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        progressBar = findViewById(R.id.progressBar);
        et_name = findViewById(R.id.et_name);
        et_pwd = findViewById(R.id.et_pwd);
        et_name.addTextChangedListener(watcher);
        et_pwd.addTextChangedListener(watcher);
        btn_register_login = findViewById(R.id.btn_register_login);
        btn_register_login.setEnabled(false);
        btn_register_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String name = et_name.getText().toString().trim();
                String pwd = et_pwd.getText().toString().trim();
                AVUser user = new AVUser();
                user.setUsername(name);
                user.setPassword(pwd);
                user.signUpInBackground().subscribe(new Observer<AVUser>() {

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull AVUser avUser) {
                        Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();

                        AVUser.logIn(name, pwd).subscribe(new Observer<AVUser>() {
                            public void onSubscribe(@NonNull Disposable disposable) {
                            }

                            public void onNext(@NonNull AVUser user) {
                                Toast.makeText(RegisterActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.INVISIBLE);
                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }

                            public void onError(@NonNull Throwable throwable) {
                                Toast.makeText(RegisterActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.INVISIBLE);
                            }

                            public void onComplete() {
                            }
                        });
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
            }
        });
    }
}