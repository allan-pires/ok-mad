package com.example.allanvictor.okmad;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.Assert.*;

public class ExcuseTest {

    private Excuse excuse;
    private final String fileContents = "excuse one;excuse two;excuse three";


    @Before
    public void setup() {
        InputStream inputStream = new ByteArrayInputStream(fileContents.getBytes());
        excuse = new Excuse(inputStream);
    }

    @Test
    public void getExcuse() throws Exception {
        String randomExcuse = excuse.getRandom();

        System.out.println(randomExcuse);
        assertTrue("excuse one".equals(randomExcuse)
                || "excuse two".equals(randomExcuse)
                || "excuse three".equals(randomExcuse));
    }

}