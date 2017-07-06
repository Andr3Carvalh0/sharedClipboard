package andre.pt.projectoeseminario.Fragments;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import andre.pt.projectoeseminario.Adapters.Clipboard.ClipboardCategoriesAdapter;
import andre.pt.projectoeseminario.Activities.Interfaces.HistoryActions;
import andre.pt.projectoeseminario.Adapters.Clipboard.ClipboardCategoryDetailedAdapter;
import andre.pt.projectoeseminario.Classifiers.Classifiers;
import andre.pt.projectoeseminario.ContentProvider.ResourcesContentProviderContent;
import andre.pt.projectoeseminario.Fragments.Interfaces.IHistory;
import andre.pt.projectoeseminario.Fragments.Interfaces.ParentFragment;
import andre.pt.projectoeseminario.R;

import static andre.pt.projectoeseminario.R.id.content_text;
import static andre.pt.projectoeseminario.R.id.recyclerView;

public class HistoryFragment extends ParentFragment implements IHistory {
    private static final int PERMISSION_ID = 1;
    private View mView;
    private boolean isInCategoriesView;

    private FloatingActionButton fab;
    private HistoryActions activity;
    private RecyclerView mRecyclerView;
    private RelativeLayout no_content;

    private static final HashMap<String, Uri> router;

    static {
        router = new HashMap<>();
        router.put("Text", ResourcesContentProviderContent.Text.CONTENT_URI);
        router.put("Links", ResourcesContentProviderContent.Links.CONTENT_URI);
        router.put("Contacts", ResourcesContentProviderContent.Contacts.CONTENT_URI);
        router.put("Recent", ResourcesContentProviderContent.Recent.CONTENT_URI);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_history, container, false);
        activity = ((HistoryActions) getActivity());

        no_content = (RelativeLayout) mView.findViewById(R.id.no_Content);
        no_content.setVisibility(View.GONE);

        fab = (FloatingActionButton) mView.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(v -> {
            final String[] recents = getTableContents(router.get("Recent"));

            if(recents.length > 0) {
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


    @Override
    public void switchToCategoriesView() {
        this.isInCategoriesView = true;
        switchToContentView();
        ClipboardCategoriesAdapter adapter = new ClipboardCategoriesAdapter(getContext(), this::switchToDetailCategoryView);
        mRecyclerView.setAdapter(adapter);

    }

    private void switchToNoContentView() {
        no_content.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }

    private void switchToContentView() {
        no_content.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }


    private void switchToDetailCategoryView(String category) {
        this.isInCategoriesView = false;

        Uri table = router.get(category);

        String[] content = getTableContents(table);

        if (content.length == 0) {
            switchToNoContentView();
            return;
        }

        switchToContentView();
        ClipboardCategoryDetailedAdapter adapter = new ClipboardCategoryDetailedAdapter(getContext(), content, this::handleOnClickEvent);
        mRecyclerView.setAdapter(adapter);
    }

    private void handleOnClickEvent(String text) {
        if(Classifiers.isPhoneNumber(text)){
            new AlertDialog.Builder(getContext())
                    .setTitle(R.string.TITLE_PHONE_ITEM)
                    .setMessage("\n")
                    .setCancelable(false)
                    .setNegativeButton(R.string.ITEM_OPTION_CALL, (dialog, which) -> handleACall(text))
                    .setPositiveButton(R.string.ITEM_OPTION_COPY, (dialog, which) -> handleCopyingTextToClipboard(text))
                    .show();
            return;
        }

        handleCopyingTextToClipboard(text);
    }

    private String[] getTableContents(Uri table) {
        List<String> tmp = new LinkedList<>();

        Cursor cursor = getActivity().getContentResolver().query(table, null, null, null, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            tmp.add(cursor.getString(1));

            cursor.moveToNext();
        }

        return tmp.stream().toArray(String[]::new);
    }

    @Override
    public boolean isInCategoriesView() {
        return isInCategoriesView;
    }


    private void handleCopyingTextToClipboard(String text){
        Snackbar.make(getView(), getString(R.string.COPIED_BEGINNIG) + " " + text + " " + getString(R.string.COPIED_END), Snackbar.LENGTH_SHORT).show();
    }

    private void handleACall(String text){
        Intent intent = new Intent(Intent.ACTION_CALL);

        intent.setData(Uri.parse("tel:" + text));

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, PERMISSION_ID);
        }else{
            getContext().startActivity(intent);
        }

    }
}
