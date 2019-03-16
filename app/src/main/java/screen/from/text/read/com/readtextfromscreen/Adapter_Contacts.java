package screen.from.text.read.com.readtextfromscreen;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Adapter_Contacts extends RecyclerView.Adapter<Adapter_Contacts.PizzaViewHolder> {

    MyAccessibilityService myaccessibilityserivice;

    public static class PizzaViewHolder extends RecyclerView.ViewHolder {

        //TextViews
        TextView phone;
        TextView name;
        RelativeLayout container;
        Button copyToClipboard;

        //Initialise the ViewHolder
        PizzaViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            phone = (TextView) itemView.findViewById(R.id.phone);
            container = (RelativeLayout) itemView.findViewById(R.id.container);
            copyToClipboard = (Button) itemView.findViewById(R.id.copy);
        }
    }

    //Adapter parameters
    List<HashMap<String,String>> list;

    //Adapter initialization
    Adapter_Contacts(List<HashMap<String,String>> list) {
        this.list = list;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public PizzaViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);
        return new PizzaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final PizzaViewHolder personViewHolder, final int position) {
        HashSet<HashMap<String,String>> hashSet = new HashSet<>();
        hashSet.addAll(list);
        list.clear();
        list.addAll(hashSet);

        Collections.sort(list, new Comparator<HashMap<String,String>>() {
            public int compare(HashMap<String, String> v1, HashMap<String, String> v2) {
                return v1.get("name").compareTo(v2.get("name"));
            }
        });

        personViewHolder.name.setText(list.get(position).get("name"));
        personViewHolder.phone.setText(list.get(position).get("number"));


        myaccessibilityserivice = new MyAccessibilityService();

        personViewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent serviceIntent = new Intent(personViewHolder.name.getContext(),MyAccessibilityService.class);
                serviceIntent.putExtra("number", list.get(position).get("number"));
                serviceIntent.putExtra("action", "replace");
                personViewHolder.name.getContext().startService(serviceIntent);
            }
        });

        personViewHolder.copyToClipboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent serviceIntent = new Intent(personViewHolder.name.getContext(),MyAccessibilityService.class);
                serviceIntent.putExtra("number", list.get(position).get("number"));
                serviceIntent.putExtra("action", "copy");
                personViewHolder.name.getContext().startService(serviceIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

