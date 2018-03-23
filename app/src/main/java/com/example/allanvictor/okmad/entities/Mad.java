package com.example.allanvictor.okmad.entities;

import com.example.allanvictor.okmad.config.Response;

import org.apache.commons.io.IOUtils;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Arrays.asList;

public class Mad implements Response {

    private final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Mad.class);
    private final String SEPARATOR = ";";
    private ArrayList<String> excuses;
    private final String SENTENCE_NAME = "OKMAD";
    private final String SENTENCE = "okay mad";

    public Mad(InputStream inputStream) {
        try {
            this.excuses = parseExcusesAsList(inputStream);
        } catch (IOException e) {
            LOGGER.error("IO Exception: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    private ArrayList<String> parseExcusesAsList(final InputStream inputStream) throws IOException {
        String excuses = IOUtils.toString(inputStream, UTF_8);
        return new ArrayList(asList(excuses.split(SEPARATOR)));
    }

    @Override
    public String getRandomSentence() {
        int randomIndex = new Random().nextInt(excuses.size() - 1);
        return excuses.get(randomIndex);
    }

    @Override
    public String getSentenceName() {
        return SENTENCE_NAME;
    }

    @Override
    public String getSentence() {
        return SENTENCE;
    }
}
