package screen.from.text.read.com.readtextfromscreen;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

import static android.R.id.list;


public class Adapter_Saved_Phrases extends RecyclerView.Adapter<Adapter_Saved_Phrases.PizzaViewHolder> {

    public static class PizzaViewHolder extends RecyclerView.ViewHolder {

        //TextViews
        TextView shortcut;
        TextView phrase;

        ImageView deleteButton;

        //Initialise the ViewHolder
        PizzaViewHolder(View itemView) {
            super(itemView);
            shortcut = (TextView) itemView.findViewById(R.id.Shortcut);
            phrase = (TextView) itemView.findViewById(R.id.Phrase);
            deleteButton = (ImageView) itemView.findViewById(R.id.delete_button);
        }
    }

    //Adapter parameters
    ArrayList<Shortcuts> shortcuts;
    SharedPreference sharedPreference;

    //Adapter initialization
    Adapter_Saved_Phrases(ArrayList<Shortcuts> shortcuts, SharedPreference sharedPreference) {
        this.shortcuts = shortcuts;
        this.sharedPreference = sharedPreference;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public PizzaViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shortcut_item, viewGroup, false);
        return new PizzaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final PizzaViewHolder personViewHolder, final int position) {
        personViewHolder.shortcut.setText(shortcuts.get(position).shortcut);
        personViewHolder.phrase.setText(shortcuts.get(position).phrase);

        personViewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shortcuts.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(0, shortcuts.size());
                sharedPreference.storeFavorites(personViewHolder.phrase.getContext(),shortcuts);
            }
        });

        //personViewHolder.deleteButton.getContext().startService(new Intent(personViewHolder.deleteButton.getContext(),MyAccessibilityService.class));

    }

    @Override
    public int getItemCount() {
        return shortcuts.size();
    }
}

