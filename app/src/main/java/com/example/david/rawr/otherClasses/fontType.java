package com.example.david.rawr.otherClasses;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by david on 11/02/2015.
 */
public class fontType {

    public static Typeface font(Context c , String ruta){
        Typeface face = Typeface.createFromAsset(c.getAssets(),ruta);
        return face;
    }

}
