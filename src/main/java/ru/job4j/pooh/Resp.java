package ru.job4j.pooh;

public class Resp {

    static final String SUCCESS = "200";
    static final String NODATA = "204";
    private final String text;
    private final String status;

    public Resp(String text, String status) {
        this.text = text;
        this.status = status;
    }

    public String text() {
        return text;
    }

    public String status() {
        return status;
    }
}