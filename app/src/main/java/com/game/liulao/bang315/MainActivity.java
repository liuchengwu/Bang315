package com.game.liulao.bang315;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.waps.AppConnect;
import cn.waps.AppListener;


public class MainActivity extends Activity implements View.OnTouchListener, GestureDetector.OnGestureListener {


    private static final int FLING_MIN_DISTANCE = 100;// 移动最小距离
    private static final int FLING_MIN_VELOCITY = 200;// 移动最大速度
    //构建手势探测器
    GestureDetector  detector;


     int[][]  sz=new int[4][4];

    LinearLayout  layout_top;
    MyLayout  layout_outer;
   
//    private LinearLayout layout1;
//    private LinearLayout layout2;
//    private LinearLayout layout3;
//    private LinearLayout layout4;

    LinearLayout[]  layouts=new LinearLayout[4];
    private int sum=0;
    private int round=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout_top= (LinearLayout) findViewById(R.id.id_layout_top);
        layout_outer=(MyLayout) findViewById(R.id.id_layout_outer);
        layouts[0]= (LinearLayout) findViewById(R.id.id_layout_1);
        layouts[1]= (LinearLayout) findViewById(R.id.id_layout_2);
        layouts[2]= (LinearLayout) findViewById(R.id.id_layout_3);
        layouts[3]= (LinearLayout) findViewById(R.id.id_layout_4);

        detector= new GestureDetector(this);

        layout_outer.setOnTouchListener(this);
        layout_outer. setLongClickable(true);

        initData();
        initView();

        //AID= cb384f3c768b0713ec15909b08726d54
        // 初始化统计器，并通过代码设置APP_ID, APP_PID
        AppConnect.getInstance(this);

        // 设置互动广告无数据时的回调监听（该方法必须在showBannerAd之前调用）
        AppConnect.getInstance(this).setBannerAdNoDataListener(new AppListener(){

            @Override
            public void onBannerNoData() {
                Log.i("debug", "banner广告暂无可用数据");
            }

        });
        LinearLayout adlayout =(LinearLayout)findViewById(R.id.AdLinearLayout);
        AppConnect.getInstance(this).showBannerAd(this, adlayout);
    }


    @Override
    protected void onDestroy() {
        // 释放资源，原finalize()方法名修改为close()
        AppConnect.getInstance(this).close();
        super.onDestroy();
    }



    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // land do nothing is ok
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            // port do nothing is ok
        }
    }




    //First 开局添加2个值是2的方块
    void initData(){
        isGameOver=false;
        isMove=0;
        round=0;
        sum=0;
        //清零
        for (int r=0;r<4;r++){
            for (int c=0;c<4;c++){
                 sz[r][c]=0;
            }
        }

      int n1= (int) (Math.random()*4);
      int n2= (int) (Math.random()*4);
      int n3= (int) (Math.random()*4);
      int n4;
       while (true){
            n4 = (int) (Math.random()*4);
            if (n4 != n1 && n4 != n2 && n4 != n3)break;
        }

        sz[n1][n2]=2;
        sz[n3][n4]=2;
    }


    //每次显示界面
   void  initView(){
       //#ffa2a49f 灰   //#ffffffff白  ffbaff80绿



       for (int r=0;r<4;r++){

           for (int c=0;c<4;c++){
               TextView  tv= (TextView) layouts[r].getChildAt(c);
               if(sz[r][c]==0){
                   tv.setText("");
                   tv.setBackgroundColor(Color.parseColor("#ffa2a49f"));
               }
               else {
                   int v=sz[r][c];
                   tv.setText(String.valueOf(v));

                   //不同的分值 由不同的颜色区分

                   if(v<4)
                       tv.setBackgroundColor(Color.parseColor("#ffffff"));
                   else if(v<8) {
                       tv.setBackgroundColor(Color.parseColor("#FFFFCD")); //白杏仁
                   }
                   else if(v<16) {
                       tv.setBackgroundColor(Color.parseColor("#FFDEAD")); //navajoWhite
                   }
                   else if(v<32)
                       tv.setBackgroundColor(Color.parseColor("#FA8072"));   //橙红色
                   else if(v<64)
                       tv.setBackgroundColor(Color.parseColor("#FF6100"));   //橙色orange
                   else if(v<128)
                       tv.setBackgroundColor(Color.parseColor("#FFD700"));  //金黄色gold
                   else if(v<256)
                       tv.setBackgroundColor(Color.parseColor("#7CFC00"));  //草地绿green
                   else if(v<512)
                       tv.setBackgroundColor(Color.parseColor("#40E0D0"));  //青绿色
                   else if(v<1024)
                       tv.setBackgroundColor(Color.parseColor("#00FFFF"));  //青
                   else if(v<2048)
                       tv.setBackgroundColor(Color.parseColor("#1E90FF"));  // dodger blue
                   else
                       tv.setBackgroundColor(Color.parseColor("#8A2BE2"));  //紫罗蓝色purple

               }
           }
       }

       TextView  tv_score= (TextView)findViewById(R.id.tv_score);
       tv_score.setText("SCORE "+String.valueOf(sum));

       TextView  tv_round= (TextView)findViewById(R.id.tv_round);
       tv_round.setText("    ROUND  "+String.valueOf(round));
   }









    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    boolean isShow=false;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.action_new){ //新建游戏
            initData();
            initView();
        }


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            TextView tv= (TextView) findViewById(R.id.tv_help);

            if(!isShow) {
                tv.setVisibility(View.VISIBLE);
                isShow=true;
            }
            else{
                tv.setVisibility(View.INVISIBLE);
                isShow=false;
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float x, float y) {

           return false;

    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float v, float v2) {

        if(isGameOver)return false;

        //向上up
        if (e1.getY() - e2.getY() > FLING_MIN_DISTANCE){
//                     && Math.abs(velocityX) > FLING_MIN_VELOCITY) {

                doup();
                mergeUp();
                doup();

        }
        else  //向down
        if (e2.getY() - e1.getY() > FLING_MIN_DISTANCE){


                dodown();
                mergeDown();
                dodown();

        }
        else        // left
        if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE) {

                doleft();
                mergeHL();
                doleft();


        }
        else    //right
        if (e1.getX() - e2.getX() < -FLING_MIN_DISTANCE) {

                 doright();
                mergeHR();
                doright();


        }


        if(isMove>0)round++;

        initView();
        randomLocation();



        return false;
    }

    Boolean isGameOver=false;
    //每回合前添加一个方块, 如果没有移动 不添加
    private void randomLocation(){
        if(isMove==0 &&hasEmpty())return;  //如果没有移动，就不添加
        isMove=0;

        int n1,n2;

        while (true) {
            n1 = (int) (Math.random() * 4);
            n2 = (int) (Math.random() * 4);
            if(sz[n1][n2]==0){
                sz[n1][n2]=2;

                drawElement(n1,n2);
                return;
            }
            if(!hasEmpty()){  //没有空位  先看是否有可合并项。
                if(checkH()||checkV()){    //有合并项，返回游戏让用户进行合并
                      return;
                }
                else {         //没有合并项，游戏结束

                    Toast.makeText(this, "Game over", Toast.LENGTH_LONG).show();
                    isGameOver = true;
                        return;
                }
            }
        }
    }

    //检查是否有空位  [值是零的项]，在界面上添加一个新块
    private boolean hasEmpty(){

        for (int r=0;r<4;r++){
            for (int c=0;c<4;c++){
                if (sz[r][c]==0) {

                    return true;  //有空位 可以添加一个2的方块
                }
            }
        }
        return false;  //没有空位
    }

    //销毁合并block
    private void disappearElement(int n1, int n2) {

        TextView  tv= (TextView) layouts[n1].getChildAt(n2);

       // tv.setText("");
       // tv.setBackgroundColor(Color.parseColor("#ffa2a49f"));

//        ObjectAnimator animX = ObjectAnimator.ofFloat(tv, "scale", 0f, 2f);//
//        animX.setInterpolator(new AccelerateDecelerateInterpolator());
//        animX.setDuration(2000);
//        animX.start();

        AnimationSet animationSet = new AnimationSet(true);
        //透明动画
        AlphaAnimation alphaAnimation = new AlphaAnimation(1f,0);
              // (float fromAlpha, float toAlpha)中1代表全不透明，0为透明
         alphaAnimation.setDuration(500);// 设置持续事件
          animationSet.addAnimation(alphaAnimation);
        tv.startAnimation(animationSet);


    }

    //画出每回合新添加的方块
    private void drawElement(int n1, int n2) {

        TextView  tv= (TextView) layouts[n1].getChildAt(n2);

        tv.setText("2");
        tv.setBackgroundColor(Color.parseColor("#ffffffff"));

//        ObjectAnimator animX = ObjectAnimator.ofFloat(tv, "scale", 0f, 2f);//
//        animX.setInterpolator(new AccelerateDecelerateInterpolator());
//        animX.setDuration(2000);
//        animX.start();

        AnimationSet animationSet = new AnimationSet(true);
        //透明动画
//        AlphaAnimation alphaAnimation = new AlphaAnimation(0.5f,1);
//              // (float fromAlpha, float toAlpha)中1代表全不透明，0为透明
//         alphaAnimation.setDuration(500);// 设置持续事件
//          animationSet.addAnimation(alphaAnimation);
//        tv.startAnimation(animationSet);

        //缩放动画
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.7f, 1f, 0.7f,  1f,
                Animation.RELATIVE_TO_SELF, 0.5f,
             Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(300);
       animationSet.addAnimation(scaleAnimation);
        tv.startAnimation(animationSet);


        //tv.animate().scaleX(1f).scaleY(1f).setDuration(2000); rotation
    }

    //每回合left right比对相等的值合并
    private void mergeHL(){
        for (int r=0;r<4;r++){
            for (int c=0;c<3;c++){
                if (sz[r][c] == sz[r][c+1]) {
                    sz[r][c] =2* sz[r][c];
                    sz[r][c+1]=0;

                  //  disappearElement(r,c+1);
                    sum=sum+sz[r][c];
                    c++;


                }
            }
        }
    }
    //每回合left right比对相等的值合并
    private void mergeHR(){
        for (int r=0;r<4;r++){
            for (int c=3;c>0;c--){
                if (sz[r][c] == sz[r][c-1]) {
                    sz[r][c] =2* sz[r][c];

                 //   disappearElement(r,c-1);
                    sz[r][c-1]=0;
                    sum=sum+sz[r][c];

                    c--;
                }
            }
        }
    }

    //检查horizontal是否有合并项
    private boolean checkH(){
        for (int r=0;r<4;r++){
            for (int c=0;c<3;c++){
                if (sz[r][c] == sz[r][c+1]) {
                    return true;
                }
            }
        }
        return false;
    }
    //每回合上下 对比值相等合并
    private void mergeUp(){
        for (int r=0;r<4;r++){
            for (int c=0;c<3;c++){
                if (sz[c][r] == sz[c+1][r]) {
                    sz[c][r] =2* sz[c+1][r];
                    sz[c+1][r]=0;

                    sum+=sz[c][r];
                    c++;



                }
            }
        }
    }
    private void mergeDown(){
        for (int r=0;r<4;r++){
            for (int c=3;c>0;c--){
                if (sz[c][r] == sz[c-1][r]) {
                    sz[c][r] =2* sz[c][r];
                    sz[c-1][r]=0;
                    sum+=sz[c][r];
                    c--;


                }
            }
        }
    }
    //检查vertical是否有合并项
    private boolean checkV(){
        for (int r=0;r<4;r++){
            for (int c=0;c<3;c++){
                if (sz[c][r] == sz[c+1][r]) {
                    return true;
                }
            }
        }
        return false;
    }


    //判断是否移动 ，true用于添加一个方块
    int isMove=0;
    //操作方块left移位
    private void doleft() {

        for (int r=0;r<4;r++){
            for (int c=0;c<3;c++){
                if (sz[r][c] == 0) {
                    int k=c+1;
                    while(k<4 && sz[r][k]==0) {
                        k++;
                    }

                    if(k==4)break;
                    sz[r][c] = sz[r][k];
                    sz[r][k] = 0;
                    isMove++;
                }

            }
        }

    }

    //操作方块right移位
    private void doright() {


        for (int r=0;r<4;r++){
            for (int c=3;c>0;c--){
                if (sz[r][c] == 0) {
                    int k=c-1;
                    while(k>=0 && sz[r][k]==0) {
                        k--;
                    }
                    if(k>=0) {
                        sz[r][c] = sz[r][k];
                        sz[r][k]=0;
                        isMove++;
                    }
                }

            }
        }

    }

    //操作方块down移动
    private void  dodown() {


        for (int r=0;r<4;r++){
            for (int c=3;c>0;c--){
                if (sz[c][r] == 0) {  //如果第4行r列的值=0，上一列向下移。
                    int k=c-1;
                    while(k>-1 && sz[k][r]==0) {   //如果上一列还是0，就上上列。
                        k--;

                    }
                    if(k==-1)break;
                    sz[c][r] = sz[k][r];      //直到不是0 把该元素值赋给当前[c，r]的元素
                    sz[k][r] = 0;               //移走的元素，值归0
                    isMove++;
                }

            }
        }

    }

    //操作方块up移动
    private void doup() {


        for (int r=0;r<4;r++){
            for (int c=0;c<3;c++){
                if (sz[c][r] == 0) {  //如果第一行r列的值=0，下一列向上移。
                    int k=c+1;
                    while(k<4&&sz[k][r]==0) {   //如果下一列还是0，就下下列。
                        k++;
                    }

                    if(k==4)break;
                    sz[c][r] = sz[k][r];      //直到不是0 把该元素值赋给当前[c，r]的元素
                    sz[k][r] = 0;               //移走的元素，值归0
                    isMove++;

                }

            }
        }

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

          return detector.onTouchEvent(motionEvent);

    }


    public void MyClick(View view) {
        Toast.makeText(this, view.getContentDescription(), Toast.LENGTH_LONG).show();
    }
}
















// new GestureDetector.SimpleOnGestureListener() {
//                    /**
//                     * 手指在屏幕上滑动的时候 调用的方法
//                     */
//
// e1：第1个ACTION_DOWN MotionEvent
// e2：最后一个ACTION_MOVE MotionEvent
// velocityX：X轴上的移动速度（像素/秒）
// velocityY：Y轴上的移动速度（像素/秒
// X轴的坐标位移大于FLING_MIN_DISTANCE，且移动速度大于FLING_MIN_VELOCITY个像素/秒
//                    @Override
//                    public boolean onFling(MotionEvent e1, MotionEvent e2,
//                                           float v, float v2) {
//
//                        //向上
//                        if (e1.getY() - e2.getY() > FLING_MIN_DISTANCE){
//
//                            Toast.makeText(MainActivity.this, "onFling 向上", Toast.LENGTH_LONG).show();
//                            return  true;
//                        }
//                        //向down
//                        if (e2.getY() - e1.getY() > FLING_MIN_DISTANCE){
//                              //  && Math.abs(v) > FLING_MIN_VELOCITY) {
//                            Toast.makeText(MainActivity.this, "onFling 向下", Toast.LENGTH_LONG).show();
//                            return true;
//
//                        }
//                        // 左划
//                        if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE) {
//                            Toast.makeText(MainActivity.this, "onFling 左划", Toast.LENGTH_LONG).show();
//                            return false;
//                        }
//                        if (e1.getX() - e2.getX() < -FLING_MIN_DISTANCE) {
//                            Toast.makeText(MainActivity.this, "onFling 右划", Toast.LENGTH_LONG).show();
//                            return false;
//                        }
//
//                        return super.onFling(e1, e2, v, v2);
//                    }
//                });