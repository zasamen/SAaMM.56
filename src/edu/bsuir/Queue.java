package edu.bsuir;

import java.util.concurrent.ArrayBlockingQueue;

class Queue {

    private static final int MAX_Q_LENGTH = 10000;

    private final int limit;
    private int queue;
    private ArrayBlockingQueue<Request> abq;

    Queue(int limit) {
        this.limit = limit;
        abq = new ArrayBlockingQueue<>(limit > 0 ? limit : MAX_Q_LENGTH);
        queue = 0;
    }

    boolean put(Request request,double time) {
        if (isUnlimited() || queue < limit) {
            queue++;
            request.offerIntoQueue(time);
            if (!abq.offer(request)) throw new RuntimeException("SSS");
            return true;
        } else {
            return false;
        }
    }

    Request get(double time) {
        queue--;
        if (queue>=0) {
            Request request = abq.poll();
            request.pollFromQueue(time);
            return request;
        }
        throw new RuntimeException("AAA");
    }

    private boolean isUnlimited() {
        return limit == 0;
    }


    boolean isEmpty() {
        return queue == 0;
    }


}
