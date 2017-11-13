package edu.bsuir;

import java.util.Random;

import static java.lang.Math.log;

class Source {

    private final Random random;
    private double lambda;
    private double timeToGenerate;

    Source(double lambda) {
        random = new Random();
        this.lambda = lambda;
        calculateTimeLeft();
    }

    void setTimeToGenerate(double ticks) {
        this.timeToGenerate = ticks;
    }

    private double getParam() {
        return lambda;
    }

    double getTimeLeft() {
        return timeToGenerate;
    }

    void calculateTimeLeft() {
        setTimeToGenerate(-1 / getParam() * log(random.nextDouble()));
    }

}
