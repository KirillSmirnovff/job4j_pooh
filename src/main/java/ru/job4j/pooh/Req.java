package ru.job4j.pooh;

public class Req {

    private final String httpRequestType;
    private final String poohMode;
    private final String sourceName;
    private final String param;

    public Req(String httpRequestType, String poohMode, String sourceName, String param) {
        this.httpRequestType = httpRequestType;
        this.poohMode = poohMode;
        this.sourceName = sourceName;
        this.param = param;
    }

    public static Req of(String content) {
        String[] req = content.split(" ");
        String httpRequestType = req[0];
        String[] modeAndName = req[1].split("/");
        String poohMode = modeAndName[1];
        String sourceName = modeAndName[2];
        String[] params = req[req.length - 1].split(System.lineSeparator());
        String param = params[params.length - 1];
        if ("GET".equals(httpRequestType)) {
            if ("queue".equals(poohMode)) {
                param = "";
            }
            if ("topic".equals(poohMode)) {
                param = modeAndName[modeAndName.length - 1];
            }
        }
        return new Req(httpRequestType, poohMode, sourceName, param);
    }

    public String httpRequestType() {
        return httpRequestType;
    }

    public String getPoohMode() {
        return poohMode;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getParam() {
        return param;
    }
}