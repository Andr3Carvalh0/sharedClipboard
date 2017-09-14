package andre.pt.projectoeseminario.View.Activities;


import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

import andre.pt.projectoeseminario.R;
import andre.pt.projectoeseminario.View.Activities.Interfaces.ParentActivity;
import andre.pt.projectoeseminario.View.Fragments.HistoryFragment;

/**
 * Handles the history floating window
 * Based on: https://github.com/klinker24/FloatingWindowDemo
 */
public class ClipboardContentChooser extends ParentActivity {
    private Toolbar mToolbar;
    private HistoryFragment historyFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void binding() {
        openFloatingWindow();

        setContentView(R.layout.activity_clipboard_content_chooser);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(getString(R.string.History));

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        historyFragment = new HistoryFragment();
        fragmentTransaction.add(R.id.fragment_container, historyFragment, "history");
        fragmentTransaction.commit();
    }

    @Override
    protected void afterBinding() {

    }

    public void openFloatingWindow() {
        // Creates the layout for the window and the look of it
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND,
                WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        // Params for the window.
        // You can easily set the alpha and the dim behind the window from here
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 1.0f;
        params.dimAmount = 0.6f;
        getWindow().setAttributes(params);

        // Gets the display size so that you can set the window to a percent of that
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        if (height > width) {
            getWindow().setLayout((int) (width * .9), (int) (height * .7));
        } else {
            getWindow().setLayout((int) (width * .7), (int) (height * .8));
        }
    }

    @Override
    public void onBackPressed() {
        if (!historyFragment.isInCategoriesView()){
            historyFragment.switchToCategoriesView();
            return;
        }

        super.onBackPressed();
        finish();
    }

}
