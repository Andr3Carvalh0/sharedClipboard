package andre.pt.projectoeseminario.Activities;


import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import andre.pt.projectoeseminario.Activities.Interfaces.History;
import andre.pt.projectoeseminario.ContentProvider.ResourcesContentProviderContent;
import andre.pt.projectoeseminario.Fragments.HistoryFragment;
import andre.pt.projectoeseminario.R;

//Floating Activity: https://github.com/klinker24/FloatingWindowDemo
public class ClipboardContentChooser extends History {
    private Toolbar mToolbar;
    private static final HashMap<String,Uri> router;
    private HistoryFragment historyFragment;

    static {
        router = new HashMap<>();
        router.put(ResourcesContentProviderContent.Text.TABLE_NAME, ResourcesContentProviderContent.Text.CONTENT_URI);
        router.put(ResourcesContentProviderContent.Links.TABLE_NAME, ResourcesContentProviderContent.Links.CONTENT_URI);
        router.put(ResourcesContentProviderContent.Contacts.TABLE_NAME, ResourcesContentProviderContent.Contacts.CONTENT_URI);
        router.put(ResourcesContentProviderContent.Recent.TABLE_NAME, ResourcesContentProviderContent.Recent.CONTENT_URI);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void init() {
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
    protected void setupEvents() {

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
    public List<String> getCategoryElements(String category) {
        List<String> toReturn = new LinkedList<>();

        Cursor cursor = getContentResolver().query(router.get(category), null, null, null, null);

        try {
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                Log.v("Andre", cursor.getString(1));
                cursor.moveToNext();

            }

        }catch(NullPointerException e){

        }
        finally {
            if(cursor != null)
                cursor.close();
        }

        return toReturn;
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
