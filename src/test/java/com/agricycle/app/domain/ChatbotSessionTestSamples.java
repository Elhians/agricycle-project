package com.agricycle.app.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class ChatbotSessionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ChatbotSession getChatbotSessionSample1() {
        return new ChatbotSession().id(1L);
    }

    public static ChatbotSession getChatbotSessionSample2() {
        return new ChatbotSession().id(2L);
    }

    public static ChatbotSession getChatbotSessionRandomSampleGenerator() {
        return new ChatbotSession().id(longCount.incrementAndGet());
    }
}
