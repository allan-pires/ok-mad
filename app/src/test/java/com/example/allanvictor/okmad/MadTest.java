package com.example.allanvictor.okmad;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.Assert.*;

public class MadTest {

    private Mad mad;
    private final String fileContents = "mad one;mad two;mad three";


    @Before
    public void setup() {
        mad = new Mad();
    }

    @Test
    public void getExcuse() throws Exception {
        String randomExcuse = mad.getRandomSentence();

        System.out.println(randomExcuse);
        assertTrue("mad one".equals(randomExcuse)
                || "mad two".equals(randomExcuse)
                || "mad three".equals(randomExcuse));
    }

}