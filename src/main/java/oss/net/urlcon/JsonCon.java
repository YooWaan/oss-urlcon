/**
 * @(#)JsonCon.java		2016/03/23
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

import java.util.function.BiFunction;

import org.json.JSONObject;

public class JsonCon implements Transport<JSONObject,JSONObject> {

    @Override
    public BiFunction<String,JSONObject,JSONObject> urlcon(String method, Callback<JSONObject,JSONObject> callback) {
        return (url, json) -> { return transport(method, url, json, callback);};
    }

}
