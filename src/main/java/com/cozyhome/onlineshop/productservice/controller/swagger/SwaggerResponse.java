package com.cozyhome.onlineshop.productservice.controller.swagger;

public final class SwaggerResponse {

    public static class Code{
        public static final String CODE_200 = "200";
        public static final String CODE_201 = "201";
        public static final String CODE_400 = "400";
        public static final String CODE_401 = "401";
        public static final String CODE_404 = "404";
        public static final String CODE_500 = "500";
    }

    public static class Message{
        public static final String CODE_200 = "OK";
        public static final String CODE_200_FOUND_DESCRIPTION = "Successfully retrieved";
        public static final String CODE_200_UPDATED_DESCRIPTION = "Model was updated";
        public static final String CODE_200_DELETED_DESCRIPTION = "Model was deleted";
        public static final String CODE_201_CREATED_DESCRIPTION = "Model was added to the database";
        public static final String CODE_400 = "Bad Request. The request parameters are invalid or missing.";
        public static final String CODE_401 = "Unauthorized. The user is not authenticated to access this resource.";
        public static final String CODE_404 = "The resource you were trying to reach is not found";
        public static final String CODE_500 = "Internal Server Error. An unexpected error occurred on the server.";
    }
}
