/**
 * @(#)Callback.java		2016/02/13
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
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.LinkedHashMap;
import java.net.URL;
import java.net.HttpURLConnection;
import java.net.URLEncoder;

@FunctionalInterface
public interface Callback<D,R> {

    R call(HttpURLConnection con);

    default String charset() {
        return "utf-8";
    }

    default String encode(String s) {
        try {
            return URLEncoder.encode(s, charset());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    default InputStream handle(HttpURLConnection con) {
        try {
            return con.getResponseCode() == HttpURLConnection.HTTP_OK
                ? con.getInputStream() : con.getErrorStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    default URL url(String method, String url, D request) {
        try {
            return new URL(url);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    default HttpURLConnection open(URL url, String method) {
        try {
            return (HttpURLConnection)url.openConnection();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    default void onReadyOpened(HttpURLConnection con, String method) {
        try {
            con.setRequestMethod(method);
            Map<String,String> header = this.applyHeader(method);
            if (header != null && !header.isEmpty()) {
                header.forEach((k, v) -> {
                    con.setRequestProperty(k, v);
                });
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    default Map<String,String> applyHeader(String method) {
        Map<String,String> header = new LinkedHashMap<>();
        header.put("User-Agent", "jUrlCon/v0.0.1");
        return header;
    }

    default void onReadyRequest(HttpURLConnection con, String method, D request) {
        con.setUseCaches(false);
        if (isWriteBody(method)) {
            writeBody(con, request);
            con.setDoOutput(true);
        } else {
            con.setDoOutput(false);
        }
        con.setDoInput(true);
    }

    default boolean isWriteBody(String method) {
        return "PUT".equals(method) || "POST".equals(method);
    }

    default int writeBody(HttpURLConnection con, D request) {
        return 0;
    }

}
