package screen.from.text.read.com.readtextfromscreen;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SharedPreference {

    public static final String PREFS_NAME = "NKDROID_APP";
    public static final String FAVORITES = "Favorite";

    public SharedPreference() {
        super();
    }


    public void storeFavorites(Context context, List<Shortcuts> favorites) {
        SharedPreferences settings;
        Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);

        editor.putString(FAVORITES, jsonFavorites);

        editor.apply();
    }

    public ArrayList<Shortcuts> loadFavorites(Context context) {
        SharedPreferences settings;
        List<Shortcuts> favorites;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        if (settings.contains(FAVORITES)) {
            String jsonFavorites = settings.getString(FAVORITES, null);
            Gson gson = new Gson();
            Shortcuts[] favoriteItems = gson.fromJson(jsonFavorites, Shortcuts[].class);
            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList<>(favorites);
        } else
            return null;

        return (ArrayList<Shortcuts>) favorites;
    }


    public void addFavorite(Context context, Shortcuts beanSampleList) {
        List<Shortcuts> favorites = loadFavorites(context);
        if (favorites == null)
            favorites = new ArrayList<>();
        favorites.add(beanSampleList);
        storeFavorites(context, favorites);
    }

    public void removeFavorite(Context context, Shortcuts beanSampleList) {
        ArrayList<Shortcuts> favorites = loadFavorites(context);
        if (favorites != null) {
            favorites.remove(beanSampleList);
            storeFavorites(context, favorites);
        }
    }
}