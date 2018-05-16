package com.yafuquen.pagersocket.view.ui.util;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DisplayUtils {

    private DisplayUtils() {
        // No instances
    }

    public static String getLanguages(List<String> languages) {
        List<String> languagesToShow = new ArrayList<>();
        for (String language : languages) {
            languagesToShow.add(new Locale(language).getDisplayLanguage(Locale.US));
        }
        return TextUtils.join(", ", languagesToShow);
    }

    public static String getCountry(String location) {
        Locale locale = new Locale(Locale.US.getLanguage(), location);
        return locale.getDisplayCountry(Locale.US);
    }
}
