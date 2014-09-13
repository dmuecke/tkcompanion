package com.muecke.tkcompanion;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Random;

/**
 * Just for testing Partition!
 * Created by smuecke on 12.09.2014.
 */
public class PartitionElement implements Evaluable {

    private int v;

    public PartitionElement() {
        Random r = new Random();
        this.v = r.nextInt(100) + 1;
    }

    public PartitionElement(int v) {
        this.v = v;
    }

    public String toString() {
        return "E_" + v;
    }

    @Override
    public int value() {
        return v;
    }

    /**
     * Creates ArrayList of PartitionElements from specially formatted file
     * @param path file path
     * @return ArrayList of PartitionElements
     */
    public static ArrayList<PartitionElement> fromFile(String path) {
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(path, "r");
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Can't open file with PartitionElements!");
        }

        // read first line
        String line = "";
        try {
            line = raf.readLine();

        } catch (IOException e) {
            throw new RuntimeException("Can't read line from file with PartitionElements!");
        }

        try { raf.close(); } catch (IOException e) {}

        String[] values = line.split(",");

        ArrayList<PartitionElement> elements = new ArrayList<PartitionElement>();
        int value;
        for (String s : values) {
            try {
                value = Integer.parseInt(s);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Can't parse int from element in file with PartitionElements!");
            }
            elements.add(new PartitionElement(value));
        }

        return elements;
    }
}