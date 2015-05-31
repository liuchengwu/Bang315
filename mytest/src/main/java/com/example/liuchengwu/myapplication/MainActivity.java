package com.example.liuchengwu.myapplication;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity implements GestureDetector.OnGestureListener,View.OnTouchListener {
    private static final int FLING_MIN_DISTANCE = 100;
    private static final int FLING_MIN_VELOCITY = 200;
    LinearLayout layout_top;
    LinearLayout  layout_outer;

    GestureDetector  gestureDetector;

    int[][]  sz=new int[4][4];

    LinearLayout[]  layouts=new LinearLayout[4];
    private int sum=0;
    private int round=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gestureDetector=new GestureDetector(this);

        layout_top= (LinearLayout) findViewById(R.id.id_layout_top);
        layout_outer=(LinearLayout) findViewById(R.id.id_layout_outer);
        layouts[0]= (LinearLayout) findViewById(R.id.id_layout_1);
        layouts[1]= (LinearLayout) findViewById(R.id.id_layout_2);
        layouts[2]= (LinearLayout) findViewById(R.id.id_layout_3);
        layouts[3]= (LinearLayout) findViewById(R.id.id_layout_4);




         

//        layout_outer.setOnTouchListener(this);
//        layout_outer. setLongClickable(true);
        layout_top.setOnTouchListener(this);
        layout_top. setLongClickable(true);
        initData();
        initView();
    }

    void initData(){
//        isGameOver=false;
//        isMove=0;
        round=0;
        sum=0;

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



    void  initView(){




        for (int r=0;r<4;r++){

            for (int c=0;c<4;c++){
                TextView tv= (TextView) layouts[r].getChildAt(c);
                if(sz[r][c]==0){
                    tv.setText("");
                    tv.setBackgroundColor(Color.parseColor("#ffa2a49f"));
                }
                else {
                    int v=sz[r][c];
                    tv.setText(String.valueOf(v));



                    if(v<4)
                        tv.setBackgroundColor(Color.parseColor("#ffffff"));
                    else if(v<8) {
                        tv.setBackgroundColor(Color.parseColor("#FFFFCD"));
                    }
                    else if(v<16) {
                        tv.setBackgroundColor(Color.parseColor("#FFDEAD"));
                    }
                    else if(v<32)
                        tv.setBackgroundColor(Color.parseColor("#FA8072"));
                    else if(v<64)
                        tv.setBackgroundColor(Color.parseColor("#FF6100"));
                    else if(v<128)
                        tv.setBackgroundColor(Color.parseColor("#FFD700"));
                    else if(v<256)
                        tv.setBackgroundColor(Color.parseColor("#7CFC00"));
                    else if(v<512)
                        tv.setBackgroundColor(Color.parseColor("#40E0D0"));
                    else if(v<1024)
                        tv.setBackgroundColor(Color.parseColor("#00FFFF"));
                    else if(v<2048)
                        tv.setBackgroundColor(Color.parseColor("#1E90FF"));
                    else
                        tv.setBackgroundColor(Color.parseColor("#8A2BE2"));

                }
            }
        }

        TextView  tv_score= (TextView)findViewById(R.id.tv_score);
        tv_score.setText("SCORE " + String.valueOf(sum));

        TextView  tv_round= (TextView)findViewById(R.id.tv_round);
        tv_round.setText("    ROUND  " + String.valueOf(round));
    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
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
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float v, float v1) {

        if (e1.getY() - e2.getY() > FLING_MIN_DISTANCE){
//                     && Math.abs(velocityX) > FLING_MIN_VELOCITY) {

            Toast.makeText(this,"up......",Toast.LENGTH_LONG).show();
            doup();
            mergeUp();
            doup();

        }

            if (e2.getY() - e1.getY() > FLING_MIN_DISTANCE){
                Toast.makeText(this,"down......",Toast.LENGTH_LONG).show();

                dodown();
                mergeDown();
                dodown();

            }

        return false;
    }

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



    int isMove=0;

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
    private void  dodown() {


        for (int r=0;r<4;r++){
            for (int c=3;c>0;c--){
                if (sz[c][r] == 0) {
                    int k=c-1;
                    while(k>-1 && sz[k][r]==0) {
                        k--;

                    }
                    if(k==-1)break;
                    sz[c][r] = sz[k][r];
                    sz[k][r] = 0;
                    isMove++;
                }

            }
        }

    }
    private void doup() {


        for (int r=0;r<4;r++){
            for (int c=0;c<3;c++){
                if (sz[c][r] == 0) {
                    int k=c+1;
                    while(k<4&&sz[k][r]==0) {
                        k++;
                    }

                    if(k==4)break;
                    sz[c][r] = sz[k][r];
                    sz[k][r] = 0;
                    isMove++;

                }

            }
        }

    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        return gestureDetector.onTouchEvent(motionEvent);
    }
}
