package andre.pt.projectoeseminario.Services;

import android.app.IntentService;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import andre.pt.projectoeseminario.ClipboardControllerFactory;
import andre.pt.projectoeseminario.Data.APIRequest;
import andre.pt.projectoeseminario.State.ClipboardController;

import static android.support.v4.content.FileProvider.getUriForFile;

/*
* Handles what to do when, the firebase service receives an notification
*/
public class ClipboardEventHandler extends IntentService {

    private static final String TAG = "Portugal:ClipHandler";

    public ClipboardEventHandler() {
        this("ClipboardEventHandler");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public ClipboardEventHandler(String name) {
        super(name);
    }

    /*
    *   Stores the text into the device clipboard
    */
    private void handleTextContent(Context context, String content) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(content, content);
        clipboard.setPrimaryClip(clip);
    }

    /*
    *   Stores the image into the device clipboard
    */
    private void handleMultimediaContent(Context context, String content) {
        File imagePath = new File(context.getFilesDir()+ File.separator +"1.jpg");
        Uri contentUri = getUriForFile(context, "andre.pt.projectoeseminario.fileprovider", imagePath);

        ClipboardManager cm = (ClipboardManager)context.getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);

        Intent a = new Intent();
        a.setData(contentUri);

        a.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        ClipData data = ClipData.newUri(new ContentResolver(context) {
            @Nullable
            @Override
            public String[] getStreamTypes(@NonNull Uri url, @NonNull String mimeTypeFilter) {
                return new String[]{"image/jpeg"};
            }





        }, "la", contentUri);



        //ClipData.newUri(context.getContentResolver(), "la", contentUri);

        cm.setPrimaryClip(data);

        //imageDownload(context, content);

    }

    public void imageDownload(Context ctx, String url) {

        Handler uiHandler = new Handler(Looper.getMainLooper());
        uiHandler.post(() -> Picasso.with(ctx.getApplicationContext())
                .load(url)
                .into(getTarget(ctx, url)));
    }

    //target to save
    private Target getTarget(Context ctx, final String url) {
        Target target = new Target() {

            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(() -> {
                    File file = new File(ctx.getFilesDir() + File.separator + "1.jpg");
                    try {
                        file.createNewFile();
                        FileOutputStream ostream = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, ostream);
                        ostream.flush();
                        ostream.close();
                    } catch (IOException e) {
                        Log.e("IOException", e.getLocalizedMessage());
                    }

                    File imagePath = new File(ctx.getFilesDir()+ File.separator +"1.jpg");
                    Uri contentUri = getUriForFile(ctx, "andre.pt.projectoeseminario.fileprovider", imagePath);

                    ClipboardManager cm = (ClipboardManager)ctx.getApplicationContext()
                            .getSystemService(Context.CLIPBOARD_SERVICE);

                    ClipData data = new ClipData("image", new String[]{"images/jpeg"}, new ClipData.Item(contentUri));

                    cm.setPrimaryClip(data);
                }).start();

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        return target;
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        final ClipboardController clipboardController = ClipboardControllerFactory.getSingleton();
        final Context context = this.getApplicationContext();

        assert intent != null;
        final String content = intent.getStringExtra("content");
        final boolean isMIME = intent.getBooleanExtra("isMIME", false);
        final boolean upload = intent.getBooleanExtra("upload", false);
        final int token = intent.getIntExtra("token", 0);
        APIRequest mApi = new APIRequest(null, context);

        try {
            clipboardController.acquireWork();
            if (clipboardController.switchClipboardValue(content)) {
                Log.v(TAG, "onHandleIntent");

                if (upload) {

                    mApi.pushTextInformation(token, content);
                } else {
                    if (isMIME) {
                        handleMultimediaContent(context, content);
                        return;
                    }
                    handleMultimediaContent(context, content);
                    //handleTextContent(context, content);
                }
            }
        } finally {
            clipboardController.releaseWork();
        }

    }
}
