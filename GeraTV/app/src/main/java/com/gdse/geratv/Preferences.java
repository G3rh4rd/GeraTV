package com.gdse.geratv;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

// singleton example from https://www.geeksforgeeks.org/singleton-class-in-android/
public class Preferences {
    private static volatile Preferences INSTANCE = null;
    private static SharedPreferences sharedPreferences = null;

    private Preferences() { }

    // public static method to retrieve the singleton instance
    public static Preferences getInstance(Context context) {
        // Check if the instance is already created
        if(INSTANCE == null) {
            // synchronize the block to ensure only one thread can execute at a time
            synchronized (Preferences.class) {
                // check again if the instance is already created
                if (INSTANCE == null) {
                    // create the singleton instance
                    INSTANCE = new Preferences();
                }
            }
        }

        sharedPreferences = context.getSharedPreferences("GeraTV", MODE_PRIVATE);

        // return the singleton instance
        return INSTANCE;
    }

    public void store(String tag, String value) {
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString(tag, value);
        myEdit.commit();
    }

    public void store(String tag, int value) {
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putInt(tag, value);
        myEdit.commit();
    }

    public String readString(String tag) {
        return sharedPreferences.getString(tag, "");
    }

    public int readInt(String tag) {
        return sharedPreferences.getInt(tag, 0);
    }
}
