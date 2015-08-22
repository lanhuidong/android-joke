package com.nexusy.gemini.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.nexusy.gemini.model.Joke;

import java.util.Collections;
import java.util.List;

/**
 * @author lanhuidong
 * @since 2015-08-16
 */
public class DataParser {

    public List<Joke> parseHttpResponse(String json) {
        if (!"".equals(json)) {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            return gson.fromJson(json, new TypeToken<List<Joke>>() {
            }.getType());
        } else {
            return Collections.emptyList();
        }
    }

}
