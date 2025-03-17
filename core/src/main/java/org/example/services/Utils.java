package org.example.services;

import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class Utils {

    final private static Pattern weblinkPattern =
            Pattern.compile("^https://+$");

    public static boolean verifyWebLink(String link) {
        return link.matches(link);
    }

}
