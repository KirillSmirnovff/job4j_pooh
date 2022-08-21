package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicService implements Service {

    private final ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>> map = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        Resp result = null;
        if ("POST".equals(req.httpRequestType())) {
            for (ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> client : map.values()) {
                client.putIfAbsent(req.getSourceName(), new ConcurrentLinkedQueue<>());
                client.get(req.getSourceName()).add(req.getParam());
            }
        }
        if ("GET".equals(req.httpRequestType())) {
            result = new Resp("", Resp.NODATA);
            String client = req.getParam();
            String topic = req.getSourceName();
            if (map.putIfAbsent(client, new ConcurrentHashMap<>()) != null) {
                ConcurrentLinkedQueue<String> queue = map.get(client).putIfAbsent(topic, new ConcurrentLinkedQueue<>());
                if (queue != null) {
                    String param = queue.poll();
                    if (param != null) {
                        result = new Resp(param, Resp.SUCCESS);
                    }
                }
            }
        }
        return result;
    }
}