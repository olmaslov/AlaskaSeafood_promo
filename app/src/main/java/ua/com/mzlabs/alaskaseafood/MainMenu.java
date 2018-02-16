package ua.com.mzlabs.alaskaseafood;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by maslo on 16.02.2018.
 */

public class MainMenu extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main_menu);
    }

    public void AlaskaInfo(View view) {
        Intent intent = new Intent(MainMenu.this, AlaskaInfo.class);
        startActivity(intent);
    }
}
