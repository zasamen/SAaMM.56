package edu.bsuir;

import static java.lang.Math.abs;
import static java.lang.Math.min;

class Taxi {

    private static final int MAX = 1_000_000;
    private static final double EPS = 1e-6;
    private int n;
    private Queue personQueue;
    private Queue carQueue;
    private Source personSource;
    private Source carSource;
    private int ticks;
    private double carCount;
    private double personCount;
    private int currentState;
    private double[] stateList;
    private double carQueueTime;
    private double personQueueTime;
    private double carQueueLength;
    private double personQueueLength;
    private int successCars;
    private int successPeople;


    Taxi(double mu, double lambda, int ticks) {
        this.n = 10;
        personQueue = new Queue(0);
        carQueue = new Queue(n);
        personSource = new Source(lambda);
        carSource = new Source(mu);
        this.ticks = ticks;
        carCount = 0;
        personCount = 0;
        stateList = new double[n+1+MAX];
    }

    void process() {
        successCars = 0;
        successPeople = 0;
        personCount = 0;
        carCount = 0;
        carQueueTime = 0;
        personQueueTime = 0;
        carQueueLength = 0;
        personQueueLength = 0;
        double timeLeftBeforePerson;
        double timeLeftBeforeCar;
        double timeLeftBeforeEvent;
        double timeLeft = ticks;
        double timePassed = 0;
        do {
            timeLeftBeforePerson = personSource.getTimeLeft();
            timeLeftBeforeCar = carSource.getTimeLeft();

            timeLeftBeforeEvent = min(timeLeftBeforePerson, timeLeftBeforeCar);

            updateStateCounter(timeLeftBeforeEvent);
            updateQueueTime(timeLeftBeforeEvent);

            timeLeft -= timeLeftBeforeEvent;
            timePassed += timeLeftBeforeEvent;

            personSource.setTimeToGenerate(timeLeftBeforePerson - timeLeftBeforeEvent);
            carSource.setTimeToGenerate(timeLeftBeforeCar - timeLeftBeforeEvent);

            if (abs(personSource.getTimeLeft()) - EPS < 0) {
                personSource.calculateTimeLeft();
                personCount++;
                if (personQueue.isEmpty()) {
                    if (!carQueue.isEmpty()) {
                        carQueueTime += carQueue.get(timePassed).getTimeInQueue();
                        successCars++;
                    } else {
                        personQueue.put(new Request(), timePassed);
                    }
                } else {
                    personQueue.put(new Request(), timePassed);
                }
                currentState++;
            } else if (abs(carSource.getTimeLeft()) - EPS < 0) {
                boolean carAccepted;
                carSource.calculateTimeLeft();
                carCount++;
                if (carQueue.isEmpty()) {
                    if (!personQueue.isEmpty()) {
                        personQueueTime += personQueue.get(timePassed).getTimeInQueue();
                        successPeople++;
                        carAccepted = true;
                    } else {
                        carAccepted = carQueue.put(new Request(), timePassed);
                    }
                } else {
                    carAccepted = carQueue.put(new Request(), timePassed);
                }
                if (carAccepted) currentState--;
            } else throw new RuntimeException("FATAL");
        } while (timeLeft >= 0);
    }

    private void updateQueueLength(double timeLeftBeforeEvent) {

    }

    private void updateQueueTime(double delta) {
        if (currentState > 0) {
            personQueueLength += delta * currentState;
        } else if (currentState < 0) {
            carQueueLength += delta * abs(currentState);
        }
    }

    private void updateStateCounter(double delta) {
        stateList[currentState + n] += delta;
    }

    void printStats() {
        System.out.println("p = " + stateList[n]/ticks);
        System.out.println("Loc " + carQueueLength/ticks);
        System.out.println("Lop " + personQueueLength/ticks);
        System.out.println("Woc " + carQueueTime/carCount);
        System.out.println("Wop " + personQueueTime/personCount);
    }
}
