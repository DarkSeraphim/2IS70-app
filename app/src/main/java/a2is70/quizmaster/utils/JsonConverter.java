package a2is70.quizmaster.utils;

import com.google.gson.GsonBuilder;
import com.google.gson.Gson;
import com.google.gson.FieldNamingPolicy;

public class JsonConverter {
  
    private static Gson gson;

    private static Gson getGson() {
      if (gson == null) {
        gson = new GsonBuilder()
                        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                        .create()
      }
      return gson;
    }

    public static String toJson(Object object) {
      return getGson().toJson(object);
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        return getGson().fromJson(json, clazz);
    }
}