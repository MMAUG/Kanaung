package org.mmaug.kanaung;

import android.content.ClipboardManager;
import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by trhura on 10/22/14.
 */
public class UnicodeView extends TextView {
    private static final String TAG = "Kanaung";

    public UnicodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setVerticalScrollBarEnabled(true);
        setMovementMethod(new ScrollingMovementMethod());
    }

    @Override
    protected void onWindowVisibilityChanged (int visibility) {
        ClipboardManager manager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        CharSequence copiedText = manager.getText();
        if (copiedText == null)      {
          setText(R.string.nothing_copied);}
              Log.d("dada", String.valueOf(Converter.detector(copiedText.toString())));
              switch (Converter.detector(copiedText.toString())){

                case 0:
                  setText(copiedText.toString());
                  break;
                case 1:
                  setText(copiedText.toString());
                  break;
                case 2:
                  setText(Converter.zg12uni51(copiedText.toString()));
                  break;
                default:
                  setText(Converter.zg12uni51(copiedText.toString()));


        }
    }

}
