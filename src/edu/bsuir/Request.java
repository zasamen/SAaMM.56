package edu.bsuir;

class Request {

    private double timeInQueue;
    private double timeOfOfferIntoQueue;

    void offerIntoQueue(double currentTime) {
        timeOfOfferIntoQueue = currentTime;
    }

    void pollFromQueue(double currentTime) {
        timeInQueue = currentTime - timeOfOfferIntoQueue;
    }

    double getTimeInQueue() {
        return timeInQueue;
    }
}
