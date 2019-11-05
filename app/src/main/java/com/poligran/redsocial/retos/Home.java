package com.poligran.redsocial.retos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class Home extends AppCompatActivity {


    FloatingActionButton fab1;
    FloatingActionButton fab2;
    FloatingActionButton fab3;

    boolean isFABOpen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.appbar);
        fab1 = (FloatingActionButton) findViewById(R.id.appbar2);
        fab2 = (FloatingActionButton) findViewById(R.id.appbar3);
        fab3 = (FloatingActionButton) findViewById(R.id.appbar4);

        fab1.hide();
        fab2.hide();
        fab3.hide();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isFABOpen){
                    showFABMenu();
                }else{
                    closeFABMenu();
                }
            }
        });
    }

    private void showFABMenu(){
        isFABOpen=true;
        fab1.show();
        fab2.show();
        fab3.show();

    }

    private void closeFABMenu(){
        isFABOpen=false;
        fab1.hide();
        fab2.hide();
        fab3.hide();
    }


}
