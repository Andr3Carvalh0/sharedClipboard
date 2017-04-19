package andre.pt.projectoeseminario.Events;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 *
 * Watch the input fields on the login.
 * Used to show or hide the clear button.
 */
public class CustomFocusListener implements View.OnFocusChangeListener {
    private ImageView mClearButton;
    private TextView mTextView;

    public CustomFocusListener(ImageView mClearButton, TextView mTextView){
        this.mClearButton = mClearButton;
        this.mTextView = mTextView;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(!hasFocus) {
            mClearButton.setVisibility(View.GONE);
        }else{
            if(mTextView.getText().length() > 0)
                mClearButton.setVisibility(View.VISIBLE);
        }
    }
}
