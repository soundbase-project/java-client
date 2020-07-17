package com.soundhive.core.response;


import com.soundhive.core.upload.FileUploadError;
import kong.unirest.*;
import kong.unirest.json.JSONException;
import org.javatuples.Pair;


import java.io.File;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Map;

public class Response<T> {

    public enum Status {
        SUCCESS,
        UNAUTHENTICATED,
        CONNECTION_FAILED,
        INTERNAL_ERROR,
    }

    private final Status status;
    private final String message;
    private final Throwable exception;
    private final T content;

    private Response(final T content,final  Status status, final String message) {
        this.content = content;
        this.status = status;
        this.message = message;
        this.exception = null;
    }

    public Response(final Status status, final String message) {
        this.status = status;
        this.message = message;
        this.content = null;
        this.exception = null;

    }

    public Response(final Status status, final String message, final Throwable exception) {
        this.status = status;
        this.exception = exception;
        this.content = null;
        this.message = message;
    }

//    public Response(Response<?> response) {
//        this.status = response.getStatus();
//        this.message = response.getMessage();
//        switch (response.getStatus()) {
//            case SUCCESS:
//                this.content = (T) response.getContent();
//                this.exception = null;
//                break;
//
//            case CONNECTION_FAILED:
//                this.exception = response.getException();
//                this.content = null;
//            case INTERNAL_ERROR:
//                this.exception = response.getException();
//            case UNAUTHENTICATED:
//                this.
//        }
//        this.status = response.getStatus();
//        this.message = response.getMessage();
//        this.exception = response.getException();
//        if (response.getStatus() == Status.SUCCESS) {
//            this.content = (T) response.getContent();
//        }
//        else {
//            this.content = null;
//        }
//    }

    public Status getStatus() {
        return status;
    }

    public Throwable getException() {
        return exception;
    }

    public T getContent() {

        if (this.status == Status.SUCCESS) {
            return content;
        } else {
            throw new NoContentError("If the status of the response is different than Success, there is no content.");
        }
    }

    public String getMessage() {
        return message;
    }

    public static <T> Response<T> queryResponse(String route, String token, JsonInterpretingFunction<T> cast) {
        HttpResponse<JsonNode> res;
        try {
                res = Unirest.get(route)
                    .header("accept", "application/json")
                    .header("authorization", "Bearer " + token)
                    .asJson();
        }
        catch (Exception e) {
            return new Response<>(Response.Status.CONNECTION_FAILED, "No query response from the server.",e);
        }


        switch (res.getStatus()) {
            case 200:
                try {
                    T responseObject = cast.apply(res.getBody());
                    return new Response<>(responseObject , Response.Status.SUCCESS, res.getStatusText());
                }
                catch (JSONException  e) {
                    return new Response<>(Response.Status.INTERNAL_ERROR,res.getStatusText(), e);
                }
                catch (InternalRequestError e) {
                    Response<?> innerRes = e.getResponse();
                    return new Response<>(innerRes.getStatus(), innerRes.getMessage(), e);
                }
            case 401:
                return new Response<>(Status.UNAUTHENTICATED, res.getStatusText());

            default:
                if (res.getBody().getObject().has("message"))
                    return new Response<>(Response.Status.INTERNAL_ERROR,res.getBody().getObject().getString("message"));
                else
                    return new Response<>(Response.Status.INTERNAL_ERROR, res.getStatusText());
        }

    }

    public static <T> Response<T> postResponse(String route, String token, Map<String, Object> textFields, Pair<String, File> file, JsonConsumerInterface onResult) {


        HttpResponse<JsonNode> res;
        try {

            MultipartBody req = Unirest.post(route)
                    .header("accept", "application/json")
                    .header("authorization", "Bearer " + token)
                    .fields(textFields);

            if (file != null) {
                ContentType mime = ContentType.create(Files.probeContentType(file.getValue1().toPath()));
                InputStream stream = new FileInputStream(file.getValue1());
                req = req.field(file.getValue0(), stream, mime, "file");
            }



            res = req.asJson();

        } catch (FileUploadError e) {
            return new Response<>(Status.INTERNAL_ERROR, e.getMessage(), e);
        }
        catch (IOException e) {
            throw new FileUploadError(String.format("There was a problem reading the file \" %s \".", file.getValue1().getName()),e);
        }
        catch (Exception e) {
            return new Response<>(Response.Status.CONNECTION_FAILED, e.getMessage(), e);
        }


        switch (res.getStatus()) {
            case 201:
                try {

                    if (onResult != null) {
                        onResult.accept(res.getBody());
                    }

                    return new Response<>(Response.Status.SUCCESS, res.getStatusText());
                } catch (JSONException e) {
                    return new Response<>(Response.Status.INTERNAL_ERROR, e.getMessage());
                }
                catch (InternalRequestError e) {
                    Response<?> innerRes = e.getResponse();
                    return new Response<>(innerRes.getStatus(), innerRes.getMessage(), e);
                }
            case 401:
                return new Response<>(Status.UNAUTHENTICATED, res.getStatusText());

            default:
                if (res.getBody().getObject().has("message"))
                    return new Response<>(Response.Status.INTERNAL_ERROR,res.getBody().getObject().getString("message"));
                else
                    return new Response<>(Response.Status.INTERNAL_ERROR, res.getStatusText());
        }
    }
}
