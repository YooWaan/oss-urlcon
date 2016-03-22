/**
 * @(#)Command.java		2016/03/23
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

import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collections;
import java.util.function.BiFunction;

import org.json.JSONObject;

public class Command {

    public static void main(String[] args) {
        Command cmd = new Command();
        if (args.length <= 0 || cmd.has(args, "-h") || cmd.has(args, "-help")) {
            cmd.usage();
            return;
        }
        String method = cmd.parseMethod(args[0]);
        String url = method == null ? args[0] : args[1];
        if (method == null) {
            method = "GET";
        }
        String param = cmd.findParam(args, "-param");
        if (cmd.has(args, "-json")) {
            BiFunction<String,JSONObject,JSONObject> jsonFunc
                = new JsonCon().urlcon(method, new JsonCallback());
            System.out.println(jsonFunc.apply(url, cmd.toJson(param)));

        } else {
            BiFunction<String,Map<String,String>,String> httpFunc
                = new UrlCon().urlcon(method, new StringCallback());
            System.out.println(httpFunc.apply(url, cmd.toMap(param)));
        }
    }

    String parseMethod(String mtd) {
        switch (mtd) {
        case "GET": return "GET";
        case "PUT": return "PUT";
        case "POST": return "POST";
        case "DELETE": return "DELETE";
        }
        return null;
    }

    boolean has(String[] args, String opt) {
        for (String arg : args) {
            if (arg.equals(opt)) {
                return true;
            }
        }
        return false;
    }

    JSONObject toJson(String param) {
        if (param == null || param.isEmpty()) {
            return null;
        }
        return new JSONObject(param);
    }

    String findParam(String[] args, String opt) {
        for (int i = 0; i < args.length ;i++) {
            if (args[i].equals(opt)) {
                return args[i+1];
            }
        }
        return null;
    }

    Map<String,String> toMap(String param) {
        if (param == null || param.isEmpty()) {
            return Collections.<String,String>emptyMap();
        }
        Map<String,String> map = new LinkedHashMap<>();
        for (String query : param.split("&")) {
            String[] values = query.split("=");
            map.put(values[0], values[1]);
        }
        return map;
    }

    void usage() {
        String help =
            "Usage\n"
            + "    java -jar urlcon.jar [method] url [-json] [-param xxx]\n\n"
            + "method : GET,POST,PUT,DELETE\n"
            + "-json  : application/json request/response\n"
            + "-param : GET,DELETE => query parameters\n"
            + "         POST,PUT   => body\n"
            ;
        System.out.println(help);
    }

}
