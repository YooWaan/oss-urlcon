/**
 * @(#)StringCallback.java		2016/03/15
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
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.net.HttpURLConnection;

public class StringCallback implements Callback<Map<String,String>, String> {

    @Override
    public String call(HttpURLConnection con) {
        try (ByteArrayOutputStream o = new ByteArrayOutputStream();
            InputStream in = handle(con);
        ) {
            int len = 0;
            byte[] body = new byte[1024];
            while ((len = in.read(body)) !=  -1) {
                o.write(body, 0, len);
            }
            return o.toString(charset());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String,String> applyHeader(String method) {
        Map<String,String> header = Callback.super.applyHeader(method);
        header.put("Content-Type", "application/x-www-form-urlencoded");
        return header;
    }

    @Override
    public int writeBody(HttpURLConnection con, Map<String,String> request) {
        if (request == null || request.isEmpty()) {
            return 0;
        }
        try (OutputStream out = con.getOutputStream()) {
            StringBuilder body = new StringBuilder();
            request.forEach((k,v) -> {
                    body.append(k).append('=');
                    if (v != null && !v.isEmpty()) {
                        body.append(encode(v));
                    }
                    body.append('&');
                });
            body.deleteCharAt(body.length()-1);
            byte[] data = body.toString().getBytes(charset());
            out.write(data);
            out.flush();
            return data.length;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
