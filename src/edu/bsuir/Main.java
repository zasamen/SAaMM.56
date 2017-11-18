package edu.bsuir;

public class Main {

    public static void main(String[] args) {
        Taxi taxi = new Taxi(15,12,1_000_000);
        taxi.process();
        taxi.printStats();
    }
}
