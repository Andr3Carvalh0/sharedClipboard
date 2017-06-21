package andre.pt.projectoeseminario.Activities;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

import andre.pt.projectoeseminario.Adapters.Clipboard.ClipboardAdapter;
import andre.pt.projectoeseminario.Adapters.Entities.Preference;
import andre.pt.projectoeseminario.R;


//Modified version of: https://github.com/klinker24/FloatingWindowDemo
public class ClipboardContentChooser extends Activity {

    private Toolbar mToolbar;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        openFloatingWindow();

        setContentView(R.layout.activity_clipboard_content_chooser);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(getString(R.string.app_name));

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //Placeholdet to test
        ClipboardAdapter adapter = new ClipboardAdapter(new Preference[]{new Preference(null, null, false)}, R.layout.clipboard_class_item);

        recyclerView.setAdapter(adapter);

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

        // You could also easily used an integer value from the shared preferences to set the percent
        if (height > width) {
            getWindow().setLayout((int) (width * .9), (int) (height * .7));
        } else {
            getWindow().setLayout((int) (width * .7), (int) (height * .8));
        }
    }
}
