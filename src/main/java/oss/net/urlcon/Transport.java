/**
 * @(#)Transport.java		2016/02/13
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

import java.net.URL;
import java.net.HttpURLConnection;
import java.util.function.BiFunction;

@FunctionalInterface
public interface Transport<D,R> {

    BiFunction<String,D,R> urlcon(String method, Callback<D,R> callback);

    default HttpURLConnection
        prepare(String method, String urlstr, D request, Callback<D,R> callback) {
        URL url = callback.url(method, urlstr, request);
        HttpURLConnection con = callback.open(url, method);
        callback.onReadyOpened(con, method);
        callback.onReadyRequest(con, method, request);
        return con;
    }

    default R transport(String method, String url, D request, Callback<D,R> callback) {
        return callback.call(prepare(method, url, request, callback));
    }


    default R get(String url, Callback<D,R> callback) {
        return transport("GET", url, null, callback);
    }

    default R get(String url, D request, Callback<D,R> callback) {
        return transport("GET", url, request, callback);
    }


    default R post(String url, Callback<D,R> callback) {
        return transport("POST", url, null, callback);
    }

    default R post(String url, D request, Callback<D,R> callback) {
        return transport("POST", url, request, callback);
    }


    default R put(String url, Callback<D,R> callback) {
        return transport("PUT", url, null, callback);
    }

    default R put(String url, D request, Callback<D,R> callback) {
        return transport("PUT", url, request, callback);
    }


    default R delete(String url, Callback<D,R> callback) {
        return transport("DELETE", url, null, callback);
    }

    default R delete(String url, D request, Callback<D,R> callback) {
        return transport("DELETE", url, request, callback);
    }


}
