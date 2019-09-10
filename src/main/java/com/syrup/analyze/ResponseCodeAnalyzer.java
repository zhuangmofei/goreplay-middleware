package com.syrup.analyze;

import com.syrup.util.MessageTuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author syrup
 * @date 2019/9/10
 */
public class ResponseCodeAnalyzer implements Runnable{

    private static final Logger unMatch = LoggerFactory.getLogger("UNMATCH");
    private static final Logger match = LoggerFactory.getLogger("MATCH");
    private BlockingQueue<MessageTuple> blockingQueue;
    private Map<String,Map<String,MessageTuple>> compareMap = new ConcurrentHashMap<String, Map<String, MessageTuple>>();

    public ResponseCodeAnalyzer(BlockingQueue<MessageTuple> blockingQueue) {
        this.blockingQueue = blockingQueue;
    }

    /**
     * Middleware program should accept the fact that all communication with Gor is asynchronous,
     * there is no guarantee that original request and response messages will come one after each other.
     */
    public void run() {
        while(true) {
            try{
                MessageTuple messageTuple = blockingQueue.take();
                if(messageTuple.getType().equals("1")) {
                    createAndPutIfAbsent(messageTuple,"request");
                }else if(messageTuple.getType().equals("2")) {
                    MessageTuple replay = getIfExists(messageTuple,"replay");
                    if(replay == null) {
                        createAndPutIfAbsent(messageTuple,"response");
                    }else {
                        if(!replay.getResponseCode().equals(messageTuple.getResponseCode())) {
                            unMatch.info("ResponseCodeAnalyzer-not match---->id:{},replay:{},response:{}",messageTuple.getId(),replay.getResponseCode(),messageTuple.getResponseCode());
                            unMatch.info("ResponseCodeAnalyzer-not match--1-->request:{}",getIfExists(messageTuple,"request"));
                        }else{
                            match.info("ResponseCodeAnalyzer-match-------->id:{},replay:{},response:{}",messageTuple.getId(),replay.getResponseCode(),messageTuple.getResponseCode());
                        }
                        compareMap.remove(messageTuple.getId());
                    }
                }else if(messageTuple.getType().equals("3")) {
                    MessageTuple response = getIfExists(messageTuple,"response");
                    if(response == null) {
                        createAndPutIfAbsent(messageTuple,"replay");
                    }else {
                        if(!response.getResponseCode().equals(messageTuple.getResponseCode())) {
                            unMatch.info("ResponseCodeAnalyzer-not match---->id:{},replay:{},response:{}",messageTuple.getId(),messageTuple.getResponseCode(),response.getResponseCode());
                            unMatch.info("ResponseCodeAnalyzer-not match--2-->request:{}",getIfExists(messageTuple,"request"));
                        }else {
                            match.info("ResponseCodeAnalyzer-match-------->id:{},replay:{},response:{}",messageTuple.getId(),messageTuple.getResponseCode(),response.getResponseCode());
                        }
                        compareMap.remove(messageTuple.getId());
                    }
                }
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void createAndPutIfAbsent(MessageTuple messageTuple,String key) {
        Map<String,MessageTuple> subMap = compareMap.get(messageTuple.getId());
        if(null == subMap || subMap.size() == 0) {
            subMap = new HashMap<String,MessageTuple>();
        }
        subMap.put(key,messageTuple);
        compareMap.put(messageTuple.getId(),subMap);
    }

    private MessageTuple getIfExists(MessageTuple messageTuple,String key) {
        Map<String,MessageTuple> subMap = compareMap.get(messageTuple.getId());
        if(null != subMap) {
            return subMap.get(key);
        }
        return null;
    }
}
