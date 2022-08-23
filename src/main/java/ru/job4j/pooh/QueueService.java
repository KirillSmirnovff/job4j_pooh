package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueService implements Service {

    private final ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> map = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        Resp result = new Resp("", Resp.ERROR);
        if ("POST".equals(req.httpRequestType())) {
            map.putIfAbsent(req.getSourceName(), new ConcurrentLinkedQueue<>());
            map.get(req.getSourceName()).add(req.getParam());
            result = new Resp("", Resp.SUCCESS);
        } else if ("GET".equals(req.httpRequestType())) {
            result = new Resp("", Resp.NODATA);
            ConcurrentLinkedQueue<String> queue = map.getOrDefault(req.getSourceName(), new ConcurrentLinkedQueue<>());
            String param = queue.poll();
            if (param != null) {
                result = new Resp(param, Resp.SUCCESS);
            }
        }
        return result;
    }
}