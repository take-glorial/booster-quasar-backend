package kr.co.takeit.web;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;

@SuppressWarnings("serial")
public class JsonLowerCase extends PropertyNamingStrategy.SnakeCaseStrategy {
    @Override
    public String translate(String input) {
        String lowerCase = super.translate(input);
        String transCase = lowerCase.toLowerCase();

        System.out.println(input + "+++" + lowerCase + ":::" + transCase + "++++");

        return transCase;
    }
}