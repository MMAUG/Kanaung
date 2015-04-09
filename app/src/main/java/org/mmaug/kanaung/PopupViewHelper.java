package org.mmaug.kanaung;

import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import org.gdgyangon.kanaung.R;

/**
 * Created by yemyatthu on 4/8/15.
 */
public abstract class PopupViewHelper implements View.OnClickListener {
  Context mContext;
  UnicodeEdit mUnicodeEdit;
  UnicodeView mUnicodeView;
  ImageButton mEditButton;
  ImageButton mFullScreenButton;
  Button mMagic;
  boolean inEditMode;
  boolean inFullScreen;

  public PopupViewHelper(Context context) {
    mContext = context;
  }

  public View getPopUpView() {
    View view = View.inflate(mContext, R.layout.popup_view, null);
    mUnicodeEdit = (UnicodeEdit) view.findViewById(R.id.unicode_edit);
    mUnicodeView = (UnicodeView) view.findViewById(R.id.unicode_view);
    mEditButton = (ImageButton) view.findViewById(R.id.edit_button);
    mFullScreenButton = (ImageButton) view.findViewById(R.id.full_screen_button);
    mMagic = (Button) view.findViewById(R.id.magic_button);
    mMagic.setOnClickListener(this);
    mUnicodeEdit.setVisibility(View.INVISIBLE);
    inEditMode = false;
    mMagic.setVisibility(View.GONE);
    mEditButton.setOnClickListener(this);
    mUnicodeEdit.setOnLongClickListener(new View.OnLongClickListener() {
      @Override public boolean onLongClick(View view) {
        String[] strings = {"Paste"};
        TextView textView = new TextView(mContext);
        textView.setBackgroundColor(Color.WHITE);

        AlertDialog alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(mContext,R.style.popup_theme)).setItems(strings,new DialogInterface.OnClickListener() {
          @Override public void onClick(DialogInterface dialogInterface, int i) {
            ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
            mUnicodeEdit.setText(clipboard.getText());
            dialogInterface.dismiss();
          }
        }).create();
        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        alertDialog.show();
        return true;
      }
    });
    mFullScreenButton.setOnClickListener(this);
    return view;
  }

  @Override public void onClick(View view) {
    if (view.getId() == R.id.edit_button) {
      InputMethodManager imm =
          (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);

      if (!inEditMode) {
        mUnicodeEdit.setVisibility(View.VISIBLE);
        mUnicodeEdit.invalidate();
        mUnicodeView.setVisibility(View.GONE);
        mMagic.setVisibility(View.VISIBLE);
        mEditButton.setImageResource(R.drawable.ic_save_white_24dp);
        imm.showSoftInput(mUnicodeEdit, 0);
        inEditMode = true;
      } else {
        mUnicodeView.setVisibility(View.VISIBLE);
        mUnicodeEdit.setVisibility(View.GONE);
        mMagic.setVisibility(View.GONE);
        mEditButton.setImageResource(R.drawable.ic_mode_edit_white_24dp);
        if (mUnicodeEdit.getText().length() > 0) {
          ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
          clipboard.setText(mUnicodeEdit.getText());
          mUnicodeView.setText(clipboard.getText());
        }
        imm.hideSoftInputFromWindow(mUnicodeEdit.getWindowToken(), 0);
        inEditMode = false;
      }
    } else if (view.getId() == R.id.full_screen_button) {
      if (inFullScreen) {
        mFullScreenButton.setImageResource(R.drawable.ic_fullscreen_white_24dp);
        onClickNormalScreen();
        inFullScreen = false;
      } else {
        mFullScreenButton.setImageResource(R.drawable.ic_fullscreen_exit_white_24dp);
        onClickFullScreen();
        inFullScreen = true;
      }
    } else if (view.getId() == R.id.magic_button) {
      if (mUnicodeEdit.getText().toString().contains("<Unicode>") && mUnicodeEdit.getText()
          .toString()
          .contains("<Zawgyi>")) {
        return;
      }
      if (mUnicodeEdit.getText().toString().length() > 0) {
        switch (Converter.detector(mUnicodeEdit.getText().toString())) {
          case 0:
            Toast toast = Toast.makeText(mContext, "No Magic On English Text!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            break;
          case 2:
            mUnicodeEdit.setText("<Unicode>"
                + "\n"
                + Converter.zg12uni51(mUnicodeEdit.getText().toString())
                + "\n\n"
                + "<Zawgyi>"
                + "\n"
                + mUnicodeEdit.getText());
            break;
          case 1:
            mUnicodeEdit.setText("<Unicode>"
                + "\n"
                + mUnicodeEdit.getText().toString()
                + "\n\n"
                + "<Zawgyi>"
                + "\n"
                + Converter.uni512zg1(mUnicodeEdit.getText().toString()));
        }
      }else {
        Toast toast = Toast.makeText(mContext, "Can't Do Magic on Empty Text!", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
      }
    }
  }

  public abstract void onClickFullScreen();

  public abstract void onClickNormalScreen();

}
