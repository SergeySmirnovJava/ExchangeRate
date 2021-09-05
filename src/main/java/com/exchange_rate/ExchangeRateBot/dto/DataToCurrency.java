package com.exchange_rate.ExchangeRateBot.dto;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
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
    }

    private String calculateCurrency(JsonNode jsonNodeExchange){
        JsonNode jsonNode = jsonNodeExchange.path("structure").path("dimensions").path("series");
        for(JsonNode nodeTypes : jsonNode){
            if(nodeTypes.get("name").asText().equals("Currency")){
                for(JsonNode nodeValue : nodeTypes.path("values")){
                    return nodeValue.path("name").asText();
                }
            }
        }
        return "Error getting current currency";
    }

    private String calculateOppositeCurrency(JsonNode jsonNodeExchange){
        JsonNode jsonNode = jsonNodeExchange.path("structure").path("dimensions").path("series");
        System.out.println(jsonNode.getNodeType());
        System.out.println(jsonNode.isArray());
        for(JsonNode nodeTypes : jsonNode){
            if(nodeTypes.get("name").asText().equals("Currency denominator")){
                for(JsonNode nodeValue : nodeTypes.path("values")){
                    System.out.println(nodeValue.asText());
                    return nodeValue.path("name").asText();
                }
            }
        }
        return "Error getting opposite currency";
    }

    private Double[] calculateExchangeCourse(JsonNode jsonNodeExchange){
        int exchangeCounter = 0;
        List<Double> tempExchangeData = new ArrayList<>();
        JsonNode jsonNode = jsonNodeExchange.path("dataSets");
        for(JsonNode nodeDimension : jsonNode){
            JsonNode nodeExchangeNode = nodeDimension.path("series").path("0:0:0:0:0").path("observations").path(Integer.toString(exchangeCounter));
            System.out.println(nodeExchangeNode.isArray());
            System.out.println(nodeExchangeNode.getNodeType());
           // for(JsonNode node : nodeExchangeNode){
                tempExchangeData.add(nodeExchangeNode.elements().next().doubleValue());

           // }
            exchangeCounter++;
        }
        return tempExchangeData.toArray(new Double[0]);
    }

    private void setDate(){
        currencyDto.setDate(null);
    }

    public CurrencyDto getExchangeResult(){
        return currencyDto;
    }
}
