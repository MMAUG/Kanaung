package org.mmaug.kanaung;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import org.gdgyangon.kanaung.R;

public class MainActivity extends ActionBarActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    Bundle bundle = getIntent().getExtras();

    if (getIntent().hasExtra("LAUNCH") && bundle.getString("LAUNCH").equals("YES")) {
      startService(new Intent(MainActivity.this, FlyService.class));
    }

    Button launch = (Button) findViewById(R.id.btnStart);
    launch.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        startService(new Intent(MainActivity.this, FlyService.class));
      }
    });

    Button stop = (Button) findViewById(R.id.btnStop);
    stop.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        stopService(new Intent(MainActivity.this, FlyService.class));
      }
    });
  }

  @Override
  protected void onResume() {
    Bundle bundle = getIntent().getExtras();

    if (getIntent().hasExtra("LAUNCH") && bundle.getString("LAUNCH").equals("YES")) {
      startService(new Intent(MainActivity.this, FlyService.class));
    }
    super.onResume();
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.menu) {
      Intent i = new Intent(this, AboutActivity.class);
      startActivity(i);
    }
    return super.onOptionsItemSelected(item);
  }
}