package org.mmaug.kanaung;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by yemyatthu on 4/8/15.
 */
public class UnicodeEdit extends EditText{

  public UnicodeEdit(Context context,AttributeSet attrs) {
    super(context,attrs);
    setVerticalScrollBarEnabled(true);
    setMovementMethod(new ScrollingMovementMethod());
  }

}
