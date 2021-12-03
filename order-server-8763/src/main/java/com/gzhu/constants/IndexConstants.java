package com.gzhu.constants;

public class IndexConstants {
    public static final String MAPPING_STRING= "{\n" +
            "  \"mappings\": {\n" +
            "    \"properties\": {\n" +
            "      \"id\":{\n" +
            "        \"type\": \"keyword\"\n" +
            "      },\n" +
            "      \"userId\":{\n" +
            "        \"type\": \"keyword\"\n" +
            "      },\n" +
            "      \"name\":{\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"ik_max_word\"\n" +
            "      },\n" +
            "      \"price\":{\n" +
            "        \"type\": \"long\"\n" +
            "      },\n" +
            "      \"num\":{\n" +
            "        \"type\": \"integer\"\n" +
            "      },\n" +
            "      \"user\":{\n" +
            "        \"type\": \"object\"\n" +
            "      }\n" +
            "  }\n" +
            "}\n" +
            "}";
}
