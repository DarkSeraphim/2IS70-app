package a2is70.quizmaster.utils;

import android.text.TextUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import a2is70.quizmaster.utils.function.Predicate;

/**
 * Created by s129977 on 26-3-2017.
 */

public enum Validator implements Predicate<String>{
    PASSWORD {
        private final Set<String> forbidden = new HashSet<String>() {{
            add("password");
            add("1234");
        }};

        @Override
        public boolean test(String password) {
            if (TextUtils.isEmpty(password) || forbidden.contains(password)) {
                return false;
            }
            return password.length() > 4;
        }
    },
    EMAIL {
        private final Pattern SIMPLE_EMAIL = Pattern.compile("\\S+@\\S+\\.\\w{2,10}");

        @Override
        public boolean test(String email) {
            return SIMPLE_EMAIL.matcher(email).matches();
        }
    };
    public abstract boolean test(String s);

    public static <T> T notNull(T value, String name) {
        if (value == null) {
            throw new NullPointerException(String.format("%s was null", name));
        }
        return value;
    }
}
