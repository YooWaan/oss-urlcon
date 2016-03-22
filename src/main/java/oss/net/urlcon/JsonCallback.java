/**
 * @(#)JsonCallback.java		2016/03/15
 *
 *
 * Copyright 2016 YooWaan. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package oss.net.urlcon;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.IOException;
import java.util.Map;
import java.net.HttpURLConnection;

import org.json.JSONObject;
import org.json.JSONTokener;

public class JsonCallback implements Callback<JSONObject, JSONObject> {

    @Override
    public JSONObject call(HttpURLConnection con) {
        try (
            InputStreamReader reader = new InputStreamReader(handle(con), charset())
        ) {
            return new JSONObject(new JSONTokener(reader));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String,String> applyHeader(String method) {
        Map<String,String> header = Callback.super.applyHeader(method);
        header.put("Content-Type", "application/json");
        return header;
    }

    @Override
    public int writeBody(HttpURLConnection con, JSONObject request) {
        if (request == null || request.length() == 0) {
            return 0;
        }
        try (OutputStream out = con.getOutputStream()) {
            byte[] data = request.toString().getBytes(charset());
            out.write(data);
            out.flush();
            return data.length;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
