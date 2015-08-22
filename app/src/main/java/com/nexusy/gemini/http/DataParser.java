package com.nexusy.gemini.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.nexusy.gemini.model.Joke;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * @author lanhuidong
 * @since 2015-08-16
 */
public class DataParser {

    public List<Joke> parseHttpResponse(HttpResponse response) {
        if (response != null && response.getStatusLine().getStatusCode() == 200) {
            try {
                String jsonString = EntityUtils.toString(response.getEntity());
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
                List<Joke> jokes = gson.fromJson(jsonString, new TypeToken<List<Joke>>() {
                }.getType());
                return jokes;
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Collections.emptyList();
    }

}
