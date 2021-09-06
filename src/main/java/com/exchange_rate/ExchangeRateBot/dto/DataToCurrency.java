package com.exchange_rate.ExchangeRateBot.dto;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DataToCurrency {
    private final CurrencyDto currencyDto;
    private JsonNode jsonNodeExchange;

    public DataToCurrency(String exchangeContent) throws JsonProcessingException {
        currencyDto = new CurrencyDto();
        ObjectMapper currencyMapper = new ObjectMapper();
        jsonNodeExchange = currencyMapper.readTree(exchangeContent);
        currencyDto.setCurrentCurrency(this.calculateCurrency(jsonNodeExchange));
        currencyDto.setOppositeCurrency(this.calculateOppositeCurrency(jsonNodeExchange));
        currencyDto.setExchangeCourse(this.calculateExchangeCourse(jsonNodeExchange));
        currencyDto.setDate(this.calculateDates(jsonNodeExchange));
    }

    private String calculateCurrency(JsonNode jsonNodeExchange){
        Iterator<JsonNode> jsonNodeIterator = jsonNodeExchange.path("structure")
                                                              .path("dimensions")
                                                              .path("series")
                                                              .elements();
        jsonNodeIterator.next();
        JsonNode nodeCurrency = jsonNodeIterator.next().path("values");
        return nodeCurrency.elements().next().path("name").asText();
    }

    private String calculateOppositeCurrency(JsonNode jsonNodeExchange){
        Iterator<JsonNode> jsonNodeIterator = jsonNodeExchange.path("structure")
                                                              .path("dimensions")
                                                              .path("series")
                                                              .elements();
        jsonNodeIterator.next();
        jsonNodeIterator.next();
        JsonNode nodeCurrency = jsonNodeIterator.next().path("values");
        return nodeCurrency.elements().next().path("name").asText();
    }

    private Double[] calculateExchangeCourse(JsonNode jsonNodeExchange){
        int exchangeCounter = 0;
        List<Double> tempExchangeData = new ArrayList<>();
        JsonNode jsonNode = jsonNodeExchange.path("dataSets").elements().next()
                                                                        .path("series")
                                                                        .path("0:0:0:0:0")
                                                                        .path("observations");
        JsonNode nodeData = jsonNode.path(Integer.toString(exchangeCounter));
        while (!nodeData.isMissingNode()){
            tempExchangeData.add(nodeData.elements().next().doubleValue());
            exchangeCounter++;
            nodeData = jsonNode.path(Integer.toString(exchangeCounter));
        }
        return tempExchangeData.toArray(new Double[0]);
    }

    private String[] calculateDates(JsonNode jsonNodeObservations){
        List<String> tempDateArr = new ArrayList<>();
        JsonNode jsonNode = jsonNodeObservations.path("structure")
                                                .path("dimensions")
                                                .path("observation")
                                                .elements().next().path("values");
        for(JsonNode jsonNodeDate : jsonNode){
            tempDateArr.add(jsonNodeDate.path("name").asText());
        }
        return tempDateArr.toArray(new String[0]);
    }

    public CurrencyDto getExchangeResult(){
        return currencyDto;
    }
}
