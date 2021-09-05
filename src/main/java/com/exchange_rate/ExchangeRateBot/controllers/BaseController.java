package com.exchange_rate.ExchangeRateBot.controllers;

import com.exchange_rate.ExchangeRateBot.dto.CurrencyDto;
import com.exchange_rate.ExchangeRateBot.dto.DataToCurrency;
import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;


import java.util.Collections;
import java.util.List;


@RestController
public class BaseController {

    @GetMapping(value = "/api/exchange/{target}/{base}",
                produces = MediaType.APPLICATION_JSON_VALUE)
    public CurrencyDto getExchange(@PathVariable("target") String target,
                                   @PathVariable("base") String base,
                                   @RequestParam(value = "day", defaultValue = "day") String day,
                                   @RequestParam(value = "amount", defaultValue = "amount") String amount) throws JsonProcessingException {

        String typeQuery = String.format("/M.%s.%s.SP00.A",target, base);
        UriComponentsBuilder uriComponents = UriComponentsBuilder.newInstance();
        uriComponents.scheme("https")
                .host("sdw-wsrest.ecb.europa.eu")
                .path("/service/data/EXR")
                .path(typeQuery)
                .build();

        System.out.println(uriComponents.toUriString());
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(uriComponents.toUriString(), HttpMethod.GET, entity, String.class);
        DataToCurrency dataToCurrency = new DataToCurrency(responseEntity.getBody());
        System.out.println(dataToCurrency.getExchangeResult());
         return dataToCurrency.getExchangeResult();
    }





    @GetMapping("/test")
        public CurrencyDto test(@RequestParam(value = "name", defaultValue = "World") String name){
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange("https://sdw-wsrest.ecb.europa.eu/service/data/EXR/M.USD.EUR.SP00.A",
                                                            HttpMethod.GET, entity,String.class);
            //CurrencyDto currencyDto;
            //currencyDto = (responseEntity.);
            System.out.println(responseEntity.getBody());
                    //restTemplate.getForObject("https://graph.facebook.com/pivotalsoftware",entity ,CurrencyDto.class);
            //System.out.println(currencyDto.getHeader());
            //System.out.println(currencyDto.getError());
            return null;
        }

        @GetMapping("/hello")
        public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
            return String.format("Hello %s!", name);
        }
}
