package com.syrup.util;

/**
 * @author syrup
 * @date 2019/9/10
 */
public class MessageTuple {
    /**
     * 1-request
     * 2-original response
     * 3-replay response
     */
    String type = "";
    /**
     * unique id
     */
    String id = "";
    /**
     * orignial response code and replay response code
     */
    String responseCode;
    /**
     * 1-GET
     * 2-POST
     */
    String requestType;
    /**
     * request path
     */
    String path;
    /**
     * request body
     */
    String body;

    public MessageTuple(String inputRaw) {
        if(null == inputRaw || inputRaw.length() < 0) {
            throw new IllegalArgumentException("input raw error");
        }
        String[] inputSting = inputRaw.split("\n");
        if(inputSting.length < 2) {
            throw new IllegalArgumentException("input raw error");
        }
        this.type = inputSting[0].split(" ")[0];
        this.id = inputSting[0].split(" ")[1];
        if(type.equals("1")) {
            this.requestType = inputSting[1].split(" ")[0];
            this.path = inputSting[1].split(" ")[1];
            this.body = new String(inputSting[inputSting.length - 1]);
        }else if(type.equals("2") || type.equals("3")){
            responseCode = inputSting[1];
        }else {
            throw new IllegalStateException("can't handle message type " + type);
        }
    }

    @Override
    public String toString() {
        return "MessageTuple{" +
                "type='" + type + '\'' +
                ", id='" + id + '\'' +
                ", responseCode='" + responseCode + '\'' +
                ", requestType='" + requestType + '\'' +
                ", path='" + path + '\'' +
                ", body='" + body + '\'' +
                '}';
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public String getRequestType() {
        return requestType;
    }

    public String getPath() {
        return path;
    }

    public String getBody() {
        return body;
    }
}
