package andre.pt.projectoeseminario.Fragments;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import andre.pt.projectoeseminario.Adapters.Clipboard.ClipboardCategoriesAdapter;
import andre.pt.projectoeseminario.Interfaces.HistoryActions;
import andre.pt.projectoeseminario.R;

import static andre.pt.projectoeseminario.R.id.recyclerView;

public class HistoryFragment extends ParentFragment {
    private View mView;

    private FloatingActionButton fab;
    private HistoryActions activity;
    private RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_history, container, false);
        activity = ((HistoryActions)getActivity());

        fab = (FloatingActionButton) mView.findViewById(R.id.floatingActionButton);
        mRecyclerView = (RecyclerView) mView.findViewById(recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        ClipboardCategoriesAdapter adapter = new ClipboardCategoriesAdapter(getContext());

        mRecyclerView.setAdapter(adapter);

        return mView;
    }

}
