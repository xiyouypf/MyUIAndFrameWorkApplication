package com.ypf.study.myroom;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.ypf.study.myroom.bean.User;
import com.ypf.study.myroom.dao.UserDao;
import com.ypf.study.myroom.database.AppDatabase;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.test_span);
        String text = "#美妆f1 #ypf2 #ypf3 #ypf1 #ypf2 #ypf3 #ypf1 #ypf2 #ypf3 #ypf1 #ypf2 #ypf3 #ypf1 #ypf2 #ypf3 #ypf1 #ypf2 #ypf3";
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        int start = 0;
        int end = 5;
        for (int i = 0; i < text.split(" ").length; i++) {
            //稍微设置标签文字小一点
            builder.setSpan(new RelativeSizeSpan(1f), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            //设置圆角背景
//        builder.setSpan(new RoundBackgroundColorSpan(Color.parseColor(/*"#" + goodsTags.get(i).getTags_color()*/Color.BLUE), Color.WHITE), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(new RoundBackgroundColorSpan(Color.GRAY, Color.BLACK), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(new BackgroundColorSpan(Color.GRAY), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            start += 6;
            end += 6;
        }
//        textView.setLineSpacing(90f, 0);
//        textView.setLineHeight(90);
        textView.setText(builder);
    }

    public void updataData(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //1）.创建内存数据库,也就是说这种数据库当中存储的数据，只会存留在内存当中，进程被杀死之后，数据随之丢失
                Room.inMemoryDatabaseBuilder(getApplicationContext(), AppDatabase.class);
                //2）.创建本地持久化的数据库
                AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "database-name")
//                         //是否允许在主线程上操作数据库，默认false。
//                         //相比sqlite法无明文禁止即可为来说，Room给出了规范
//                         .allowMainThreadQueries()
//                        //数据库创建和打开的事件会回调到这里，可以再次操作数据库
//                         .addCallback(new CallBack())
//                         //指定数据查询数据时候的线程池,
//                         .setQueryExecutor(cacheThreadPool)
//                         //它是用来创建supportsqliteopenhelper
//                         //可以利用它实现自定义的sqliteOpenHelper，来实现数据库的加密存储，默认是不加密的
//                         .openHelperFactory(new SupportSQLiteOpenHelper.Factory() {
//                             @Override
//                             public SupportSQLiteOpenHelper create(SupportSQLiteOpenHelper.Configuration configuration) {
//                                 return null;
//                             }
//                         })
//                         //数据库升级 1---2
//                         .addMigrations(new Migration(1,2){
//                             @Override
//                             public void migrate(@NonNull SupportSQLiteDatabase database) {
//
//                             }
//                         })
                        .build();
                UserDao userDao = db.userDao();
                userDao.insertAll(new User(0, "李三", "赵六"));
            }
        }).start();
    }

    public void queryData(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //1）.创建内存数据库,也就是说这种数据库当中存储的数据，只会存留在内存当中，进程被杀死之后，数据随之丢失
                Room.inMemoryDatabaseBuilder(getApplicationContext(), AppDatabase.class);
                //2）.创建本地持久化的数据库
                //获取到AppDatabase_Impl
                AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "database-name")
                        .build();
                //获取到UserDao_impl
                UserDao userDao = db.userDao();
                List<User> all = userDao.getAll();
                for (User user : all) {
                    Log.e("ypf-------------->", user.uid + user.firstName + user.lastName);
                }
            }
        }).start();
    }


    class CallBack extends RoomDatabase.Callback {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
        }
    }

}
