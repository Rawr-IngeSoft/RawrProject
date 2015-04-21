package com.example.david.rawr.db;

import org.apache.http.HttpEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Created by alfredo on 20/04/15.
 */
public class JsonParser {

    private InputStream inputStream = null;
    private JSONObject jObject;

    JsonParser(InputStream inputStream){
        this.inputStream = inputStream;
        parse();
    }

    void parse(){
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();

            String line = null;
            while ((line = br.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            String result = sb.toString();
            jObject = new JSONObject(result);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public JSONObject getjObject() {
        return jObject;
    }
}
