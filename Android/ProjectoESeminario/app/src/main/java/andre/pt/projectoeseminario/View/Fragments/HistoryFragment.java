package andre.pt.projectoeseminario.View.Fragments;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import andre.pt.projectoeseminario.View.Adapters.Clipboard.ClipboardCategoriesAdapter;
import andre.pt.projectoeseminario.View.Adapters.Clipboard.ClipboardCategoryDetailedAdapter;
import andre.pt.projectoeseminario.View.Adapters.Clipboard.ClipboardLinkDetailedAdapter;
import andre.pt.projectoeseminario.View.Adapters.ParentAdapter;
import andre.pt.projectoeseminario.Controller.Classifiers.Classifiers;
import andre.pt.projectoeseminario.Controller.Data.ContentProvider.ResourcesContentProviderContent;
import andre.pt.projectoeseminario.View.Fragments.Interfaces.IHistory;
import andre.pt.projectoeseminario.View.Fragments.Interfaces.ParentFragment;
import andre.pt.projectoeseminario.Projecto;
import andre.pt.projectoeseminario.R;
import andre.pt.projectoeseminario.Services.ClipboardEventHandler;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

import static andre.pt.projectoeseminario.R.id.recyclerView;

/**
 * Fragment that is shown in the SettingsActivity/ClipboardContentChoosed
 */
@RuntimePermissions
public class HistoryFragment extends ParentFragment implements IHistory {
    private static final int PERMISSION_ID = 1;
    private View mView;
    private boolean isInCategoriesView;
    private FloatingActionButton fab;
    private RecyclerView mRecyclerView;
    private RelativeLayout no_content;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_history, container, false);

        no_content = (RelativeLayout) mView.findViewById(R.id.no_Content);
        no_content.setVisibility(View.GONE);

        fab = (FloatingActionButton) mView.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(v -> {
            final String[] recents = ((Projecto) getActivity().getApplication()).getContent(ResourcesContentProviderContent.Recent.TABLE_NAME);

            if (recents.length > 0) {
                handleOnClickEvent(recents[0]);
                return;
            }

            Snackbar.make(getView(), R.string.NO_RECENT_ITEM, Snackbar.LENGTH_SHORT).show();

        });

        mRecyclerView = (RecyclerView) mView.findViewById(recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        switchToCategoriesView();

        return mView;
    }

    /**
     * Shows all the categories possible(text/contacts/links)
     */
    @Override
    public void switchToCategoriesView() {
        this.isInCategoriesView = true;
        switchToContentView();
        ClipboardCategoriesAdapter adapter = new ClipboardCategoriesAdapter(getContext(), this::switchToDetailCategoryView);
        mRecyclerView.setAdapter(adapter);

    }

    /**
     * Shows all the content of the given category @category
     * @param category the category to show the content from.
     */
    private void switchToDetailCategoryView(String category) {
        this.isInCategoriesView = false;

        String[] content = ((Projecto) getActivity().getApplication()).getContent(category);

        if (content.length == 0) {
            switchToNoContentView();
            return;
        }

        switchToContentView();
        ParentAdapter adapter = category.equals(ResourcesContentProviderContent.Links.TABLE_NAME)
                ? new ClipboardLinkDetailedAdapter(getContext(), content, this::handleOnClickEvent)
                : new ClipboardCategoryDetailedAdapter(getContext(), content, this::handleOnClickEvent);

        mRecyclerView.setAdapter(adapter);
    }

    /**
     * Shows the empty view when we dont have content to show
     */
    private void switchToNoContentView() {
        no_content.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }

    /**
     * Hides the empty view
     */
    private void switchToContentView() {
        no_content.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * Click Event when the user clicks on a phone number
     * @param text
     */
    private void handleOnClickEvent(String text) {
        if (Classifiers.isPhoneNumber(text)) {
            new AlertDialog.Builder(getContext())
                    .setTitle(R.string.TITLE_PHONE_ITEM)
                    .setMessage("\n")
                    .setCancelable(false)
                    .setPositiveButton(R.string.ITEM_OPTION_CALL, (dialog, which) -> HistoryFragmentPermissionsDispatcher.handleACallWithCheck(this, text))
                    .setNegativeButton(R.string.ITEM_OPTION_COPY, (dialog, which) -> handleCopyingTextToClipboard(text))
                    .show();
            return;
        }

        handleCopyingTextToClipboard(text);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        HistoryFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    public boolean isInCategoriesView() {
        return isInCategoriesView;
    }

    /**
     * Helper method to handle a phone call
     * @param text the phone number
     */
    @NeedsPermission(Manifest.permission.CALL_PHONE)
    public void handleACall(String text) {
        Intent intent = new Intent(Intent.ACTION_CALL);

        intent.setData(Uri.parse("tel:" + text));

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            handleCopyingTextToClipboard(text);
            return;
        }

        getContext().startActivity(intent);

    }

    @OnShowRationale(Manifest.permission.CALL_PHONE)
    public void showRationaleForCamera(final PermissionRequest request) {
        new AlertDialog.Builder(getContext())
                .setMessage(R.string.permission_phone_rationale)
                .setPositiveButton(R.string.button_allow, (dialog, button) -> request.proceed())
                .setNegativeButton(R.string.button_deny, (dialog, button) -> request.cancel())
                .show();
    }

    @OnPermissionDenied(Manifest.permission.CALL_PHONE)
    public void showDeniedForCamera() {
        Toast.makeText(getContext(), R.string.permission_camera_neverask, Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain(Manifest.permission.CALL_PHONE)
    public void showNeverAskForCamera() {
        Toast.makeText(getContext(), R.string.permission_camera_neverask, Toast.LENGTH_SHORT).show();
    }

    /**
     * Helper method to handle a new value to copy to the device's clipboard
     * @param text the value to copy
     */
    private void handleCopyingTextToClipboard(String text){
        Intent intent = new Intent(getContext(), ClipboardEventHandler.class);
        intent.putExtra("action", "switch");
        intent.putExtra("content", text);

        getContext().startService(intent);

        Snackbar.make(getView(), getString(R.string.COPIED_BEGINNIG) + " " +"\""+ text +"\"" + " " + getString(R.string.COPIED_END), Snackbar.LENGTH_SHORT).show();
    }
}
