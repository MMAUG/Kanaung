package org.mmaug.kanaung;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

/**
 * Created by yemyatthu on 4/7/15.
 */
public class AboutActivity extends ActionBarActivity {
  public static Intent getOpenFacebookIntent(Context context) {
    try {
      context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
      return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://group/1438954713030179"));
    } catch (Exception e) {
      return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/groups/MyanmarAndroidUserGroup/"));
    }
  }
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.about);
    Toolbar toolbar = (Toolbar) findViewById(R.id.about_toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setTitle("About Kanaung");
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    TextView opensources = (TextView) findViewById(R.id.open_source);
    TextView maug = (TextView) findViewById(R.id.maug);
    TextView version = (TextView) findViewById(R.id.version);
    version.setText("Kanaung Version "+BuildConfig.VERSION_NAME);
    maug.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        Intent fbIntent = getOpenFacebookIntent(AboutActivity.this);
        startActivity(fbIntent);
      }
    });
    opensources.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        WebView webView = new WebView(AboutActivity.this);

        webView.loadUrl("file:///android_asset/licenses.html");

        new AlertDialog.Builder(AboutActivity.this).setTitle(R.string.about_licenses)
            .setView(webView)
            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
              }
            })
            .create()
            .show();
      }
    });


  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu,menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override public boolean onPrepareOptionsMenu(Menu menu) {
    menu.findItem(R.id.menu).setVisible(false);
    return super.onPrepareOptionsMenu(menu);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    if(item.getItemId()==android.R.id.home){
      finish();
    }
    return super.onOptionsItemSelected(item);
  }
}
