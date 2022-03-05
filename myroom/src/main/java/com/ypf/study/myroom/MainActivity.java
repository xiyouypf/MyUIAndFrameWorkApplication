package com.ypf.study.myroom;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
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

//import com.ypf.study.myroom.bean.User;
//import com.ypf.study.myroom.dao.UserDao;
//import com.ypf.study.myroom.database.AppDatabase;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    public static Pattern LABEL_PATTERN = Pattern.compile("#\\s?[a-z0-9A-Z\u4e00-\u9fa5]+");
    public static Pattern LABEL_PATTERN_REQ = Pattern.compile("\\s#\\s[a-z0-9A-Z\u4e00-\u9fa5]+\\s");
    private static final String TAG = "MainActivity-------------";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate:" + "   " + "www");

        textView = findViewById(R.id.test_span);
        String text = " # 影综创作者激励计划   # 我的快乐源泉   # 追剧   # 聚会开心时刻 #韩国#好多个   # 青春不毕业  闺蜜模范，谁身边不想要一个这样的姐妹   # 青春不毕业 ";
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
//        for (int i = 0; i < builder.length() - 3; i++) {
//            if ((builder.charAt(i) == ' ') && (builder.charAt(i + 1) == ' ') && (builder.charAt(i + 2) == ' ')) {
//                builder.replace(i + 2, i + 3, "");
//            }
//        }
//        for (int i = 1; i < builder.length() - 1; i++) {
//            if (builder.charAt(i) == '#' && builder.charAt(i - 1) != ' ') {
//                builder.replace(i - 1, i, builder.charAt(i - 1) + " ");
//            }
//        }
        Matcher w = LABEL_PATTERN_REQ.matcher(builder);
        while (w.find()) {
            int start = w.start();
            int end = w.end();
            if (end - start <= 1) {
                continue;
            }
            builder.setSpan(new AbsoluteSizeSpan(dp2px(4)), start,
                    start + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            builder.setSpan(new AbsoluteSizeSpan(dp2px(4)),
                    start + 2, start + 3, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            builder.setSpan(new AbsoluteSizeSpan(dp2px(4)), end - 1, end,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        Matcher m = LABEL_PATTERN.matcher(builder);
        while (m.find()) {
            int start = m.start();
            int end = m.end();
            if (end - start <= 1) {
                continue;
            }
            Log.d(TAG, "onCreate: " + "start=" + start + ",end=" + end + ",subStr=" + builder.subSequence(start, end));
            builder.setSpan(new RoundBackgroundColorSpan(Color.GRAY, Color.BLACK), start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        }
        Log.d(TAG, "onCreate: "+builder);
        textView.setText(builder);
    }

    private int dp2px(int dp) {
        return 5 * dp;
    }
/*

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
*/

}
