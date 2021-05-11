package cn.com.aratek.leandemo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import cn.leancloud.AVObject;
import cn.leancloud.AVQuery;
import cn.leancloud.AVUser;
import cn.leancloud.types.AVNull;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {

    private TextView tv_text;
    private String mFirstObjectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_text = findViewById(R.id.tv_text);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
                showAddDialog();
                break;
            case R.id.btn_delete:
                delete();
                query();
                break;
            case R.id.btn_query:
                query();
                break;
            case R.id.btn_update:
                update();
                query();
                break;
        }
    }

    private void delete() {
        if (!TextUtils.isEmpty(mFirstObjectId)) {
            AVObject todo = AVObject.createWithoutData("Todo", mFirstObjectId);
            todo.deleteInBackground().subscribe(new Observer<AVNull>() {
                @Override
                public void onSubscribe(@NonNull Disposable d) {

                }

                @Override
                public void onNext(@NonNull AVNull avNull) {
                    query();
                }

                @Override
                public void onError(@NonNull Throwable e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onComplete() {

                }
            });
        }
    }

    private void update() {
        if (!TextUtils.isEmpty(mFirstObjectId)) {
            AVObject todo = AVObject.createWithoutData("Todo", mFirstObjectId);
            todo.put("title", "这条被修改了");
            todo.saveInBackground().subscribe(new Observer<AVObject>() {
                @Override
                public void onSubscribe(@NonNull Disposable d) {

                }

                @Override
                public void onNext(@NonNull AVObject avObject) {
                    query();
                }

                @Override
                public void onError(@NonNull Throwable e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onComplete() {

                }
            });
        }
    }

    private void showAddDialog() {
        EditText editText = new EditText(this);
        new AlertDialog.Builder(this)
                .setView(editText)
                .setTitle("添加todo")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String trim = editText.getText().toString().trim();
                        if (TextUtils.isEmpty(trim)) {
                            Toast.makeText(MainActivity.this, "内容不能为空！", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        AVObject todo = new AVObject("Todo");
                        todo.put("title", trim);
                        todo.put("user", AVUser.getCurrentUser());
                        todo.saveInBackground().subscribe(new Observer<AVObject>() {
                            public void onSubscribe(@NotNull Disposable disposable) {
                            }

                            public void onNext(@NotNull AVObject todo) {
                                Toast.makeText(MainActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                                query();
                            }

                            public void onError(@NotNull Throwable throwable) {
                                Toast.makeText(MainActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            public void onComplete() {
                            }
                        });
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

    private void query() {
        AVQuery<AVObject> query = new AVQuery<>("Todo");
        query.whereEqualTo("user", AVUser.getCurrentUser());
        query.findInBackground().subscribe(new Observer<List<AVObject>>() {
            public void onSubscribe(@NotNull Disposable disposable) {
            }

            public void onNext(@NotNull List<AVObject> students) {
                // students 是包含满足条件的 Student 对象的数组
                if (students.size() > 0) {
                    mFirstObjectId = students.get(0).getObjectId();
                }
                StringBuilder sb = new StringBuilder();
                for (AVObject object : students) {
                    sb.append(object.get("title"));
                    sb.append("\n");
                }
                tv_text.setText(sb.toString());
            }

            public void onError(@NotNull Throwable throwable) {
            }

            public void onComplete() {
            }
        });
    }
}