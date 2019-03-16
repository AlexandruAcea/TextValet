package screen.from.text.read.com.readtextfromscreen;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class Fragment_Shortcuts extends Fragment {

    RecyclerView recyclerView;
    SharedPreference sharedPreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.second_fragment, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        sharedPreference = new SharedPreference();
        initialiseAdapter();

        return rootView;
    }

    public void initialiseAdapter() {
        ArrayList<Shortcuts> arrayList = sharedPreference.loadFavorites(getContext());
        if (arrayList != null)
            recyclerView.setAdapter(new Adapter_Saved_Phrases(arrayList, sharedPreference));
    }
}