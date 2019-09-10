package com.syrup;

import com.syrup.analyze.ResponseCodeAnalyzer;
import com.syrup.util.MessageTuple;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author syrup
 * @date 2019/9/10
 */
public class SyrupEcho {

    public static void main(String[] args) {
        BlockingQueue<MessageTuple> blockingQueue = new LinkedBlockingQueue<MessageTuple>();
        ResponseCodeAnalyzer analyzer = new ResponseCodeAnalyzer(blockingQueue);
        new Thread(analyzer).start();
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        String line;
        try {
            while ((line = stdin.readLine()) != null) {
                String anaStr = line;
                blockingQueue.offer(new MessageTuple(decodeHexString(anaStr)));
                System.out.println(line);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static String decodeHexString(String s) throws DecoderException {
        return new String(Hex.decodeHex(s.toCharArray()));
    }
}
