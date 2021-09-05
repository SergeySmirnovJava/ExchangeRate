package com.exchange_rate.ExchangeRateBot.dto;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;


//@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrencyDto {
   private String currentCurrency;
   private String oppositeCurrency;
   private Double[] exchangeCourse;
   private String[] date;

    public String getCurrentCurrency() {
        return currentCurrency;
    }

    public void setCurrentCurrency(String currentCurrency) {
        this.currentCurrency = currentCurrency;
    }

    public String getOppositeCurrency() {
        return oppositeCurrency;
    }

    public void setOppositeCurrency(String oppositeCurrency) {
        this.oppositeCurrency = oppositeCurrency;
    }

    public Double[] getExchangeCourse() {
        return exchangeCourse;
    }

    public void setExchangeCourse(Double[] exchangeCourse) {
        this.exchangeCourse = exchangeCourse;
    }

    public String[] getDate() {
        return date;
    }

    public void setDate(String[] date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "CurrencyDto{" +
                "currentCurrency='" + currentCurrency + '\'' +
                ", oppositeCurrency='" + oppositeCurrency + '\'' +
                ", exchangeCourse=" + Arrays.toString(exchangeCourse) +
                ", date=" + Arrays.toString(date) +
                '}';
    }

    // ObjectMapper objectMapper = new ObjectMapper();

 /*   public CurrencyDto(String jsonFile) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(jsonFile);
        JsonNode nodes = jsonNode.path("dataSets");
        int iterators = 0;
        if(nodes.isArray()){
            for (JsonNode node : nodes){
                JsonNode nod = (node.path("series").path("0:0:0:0:0").path("attributes"));
                if(nod.isArray()){
                    System.out.println("here");
                    for(JsonNode testNode : nod){
                        System.out.println(testNode.asText());
                    }
                }
            }
            System.out.println("array");
            //System.out.println(nodes.path("name").asText());
        }
        else{
            System.out.println(nodes.asText());
        }
    }*/
}
