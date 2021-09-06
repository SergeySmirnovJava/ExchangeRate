package com.exchange_rate.ExchangeRateBot.controllers;

import com.exchange_rate.ExchangeRateBot.dto.CurrencyDto;
import com.exchange_rate.ExchangeRateBot.dto.DataToCurrency;
import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;


import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;


@RestController
public class BaseController {

    @GetMapping(value = "/api/exchange/{target}/{base}",
                produces = MediaType.APPLICATION_JSON_VALUE)
    public CurrencyDto getExchange(@PathVariable("target") String target,
                                   @PathVariable("base") String base,
                                   @RequestParam(value = "date") String date,
                                   @RequestParam(value = "amount", defaultValue = "amount") String amount) throws JsonProcessingException {
        new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        String typeQuery = String.format("/M.%s.%s.SP00.A",target, base);
        UriComponentsBuilder uriComponents = UriComponentsBuilder.newInstance();
        uriComponents.scheme("https")
                        .host("sdw-wsrest.ecb.europa.eu")
                        .path("/service/data/EXR")
                        .path(typeQuery)
                        .queryParam("startPeriod", date)
                        .build();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(uriComponents.toUriString(), HttpMethod.GET, entity, String.class);
        DataToCurrency dataToCurrency = new DataToCurrency(responseEntity.getBody());
        return dataToCurrency.getExchangeResult();
    }

}
