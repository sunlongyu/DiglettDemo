package com.example.diglettdemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.ref.WeakReference;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    public static final int DIGLETT_CODE = 123;
    public static final int TOTAL_DIGLETT_NUMBER = 10;
    private TextView scoreTextView;
    private Button beginButton;
    private ImageView diglettImageView;
    private int totalCount = 0;
    private int score = 0;
    private DiglettHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化视图
        intView();

        handler = new DiglettHandler(this);
    }

    /**
     * 初始化视图的方法
     */
    private void intView() {
        scoreTextView = findViewById(R.id.scoreView);
        beginButton = findViewById(R.id.beginButton);
        diglettImageView = findViewById(R.id.diglettImageView);

        beginButton.setOnClickListener(this);
        diglettImageView.setOnTouchListener(this);
    }

    /**
     * 点击开始按钮开始游戏方法
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.beginButton:
                startGame();
                break;
        }
    }

    /**
     * 点击地鼠触发的事件
     *
     * @param v
     * @param event
     * @return
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.diglettImageView: {
                score++;
                diglettImageView.setVisibility(View.GONE);
                scoreTextView.setText("您打到了" + score + "只地鼠,共" + TOTAL_DIGLETT_NUMBER + "只地鼠");
            }
        }
        return false;
    }

    /**
     * 开始游戏的方法
     */
    private void startGame() {
        scoreTextView.setText("您打到了" + score + "只地鼠,共" + TOTAL_DIGLETT_NUMBER + "只地鼠");
        //点击开始按钮后文本显示游戏中
        beginButton.setText(R.string.gaming);
        //并将按钮设置为不可用
        beginButton.setEnabled(false);
        next(0);
    }

    /**
     * 游戏开始后下一个地鼠出现的方法
     *
     * @param delayTime
     */
    public void next(int delayTime) {
        Message message = Message.obtain();
        message.what = DIGLETT_CODE;
        handler.sendMessageDelayed(message, delayTime);
        totalCount++;
    }

    public static class DiglettHandler extends Handler {

        final WeakReference<MainActivity> weakReference;
        private MainActivity mainActivity;

        public DiglettHandler(MainActivity activity) {
            this.weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            mainActivity = weakReference.get();
            switch (msg.what) {
                case DIGLETT_CODE:
                    if (mainActivity.totalCount > 10) {
                        Toast.makeText(mainActivity, "地鼠打完了，您的得分是" + mainActivity.score + "分", Toast.LENGTH_SHORT).show();
                        finishGame();
                        return;
                    }
                    Random random = new Random();
                    int x = random.nextInt(800) + 1;
                    int y = random.nextInt(800) + 1;
                    mainActivity.diglettImageView.setX(x);
                    mainActivity.diglettImageView.setY(y);
                    mainActivity.diglettImageView.setVisibility(View.VISIBLE);
                    int delayTime = random.nextInt(500) + 500;
                    mainActivity.next(delayTime);
                    break;
            }
        }

        private void finishGame() {
            mainActivity.totalCount = 0;
            mainActivity.score = 0;
            mainActivity.beginButton.setText(R.string.begin_game);
            mainActivity.beginButton.setEnabled(true);
        }
    }
}
