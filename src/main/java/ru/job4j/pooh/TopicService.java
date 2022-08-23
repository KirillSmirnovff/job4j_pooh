package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicService implements Service {

    private final ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>> map = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        Resp result = new Resp("", Resp.ERROR);
        if ("POST".equals(req.httpRequestType())) {
            ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> topicMap = map.get(req.getSourceName());
            if (topicMap != null) {
                for (ConcurrentLinkedQueue<String> client : topicMap.values()) {
                    client.add(req.getParam());
                }
                result = new Resp("", Resp.SUCCESS);
            }
        } else if ("GET".equals(req.httpRequestType())) {
            result = new Resp("", Resp.NODATA);
            String client = req.getParam();
            String topic = req.getSourceName();
            map.putIfAbsent(topic, new ConcurrentHashMap<>());
            map.get(topic).putIfAbsent(client, new ConcurrentLinkedQueue<>());
            ConcurrentLinkedQueue<String> clientQueue = map.get(topic).get(client);
            String param = clientQueue.poll();
            if (param != null) {
                result = new Resp(param, Resp.SUCCESS);
            }
        }
        return result;
    }
}