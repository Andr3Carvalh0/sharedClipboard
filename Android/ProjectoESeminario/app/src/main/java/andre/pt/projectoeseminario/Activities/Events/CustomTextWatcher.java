package andre.pt.projectoeseminario.Activities.Events;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import andre.pt.projectoeseminario.Activities.Interfaces.LoginMethods;

/**
 *
 * Used to show or hide the proceed button, that is displayed on the login screen.
 */
public class CustomTextWatcher implements TextWatcher {
    private TextView other;
    private LoginMethods loginMethods;
    private ImageView clearButton;

    public CustomTextWatcher(TextView other, LoginMethods lm, ImageView clearButton){
        this.other = other;
        this.loginMethods = lm;
        this.clearButton = clearButton;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        //shows or hides the clear field text
        clearButton.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);

        //If the password field and email field are field, we show the proceed button
        if(other.getText().length() > 0 && s.length() > 0){
            loginMethods.showProceedButton();
        }

        if(s.length() == 0){
            loginMethods.hideProceedButton();
        }
    }
}
