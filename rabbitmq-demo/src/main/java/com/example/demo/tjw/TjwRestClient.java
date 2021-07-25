package com.example.demo.tjw;

import com.example.demo.config.DingDingProperties;
import com.example.demo.config.TjwRestClientProperties;
import com.example.demo.entity.ToDo;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.ParameterizedType;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Reference;

@Service
public class TjwRestClient {
    private RestTemplate restTemplate;
    private TjwRestClientProperties properties;
    private DingDingProperties dingDingProperties;
    public TjwRestClient(
            TjwRestClientProperties properties,DingDingProperties dingDingProperties){
        this.restTemplate = new RestTemplate();
        this.restTemplate.setErrorHandler(
                new TjwErrorHandler());
        this.properties = properties;
        this.dingDingProperties = dingDingProperties;
    }

    public TjwResponse get() {
        RequestEntity<TjwResponse> requestEntity = null;
        try {
            requestEntity = new RequestEntity
                    <TjwResponse>(HttpMethod.GET,new URI(properties.getUrl() +
                    properties.getBasePath()));
            ResponseEntity<TjwResponse> response =
                    restTemplate.exchange(requestEntity,new ParameterizedTypeReference<TjwResponse>(){});
            if(response.getStatusCode() == HttpStatus.OK){
                return response.getBody();
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return null;
    }
    public String getFarm() {
        RequestEntity<String> requestEntity = null;
        try {
            requestEntity = new RequestEntity
                    <String>(HttpMethod.GET,new URI(properties.getFarmUrl() ));
            ResponseEntity<String> response =
                    restTemplate.exchange(requestEntity,String.class);
            if(response.getStatusCode() == HttpStatus.OK){
                return response.getBody();
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return null;
    }
    public DingResponse sendWarn(String phone,String message) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String content = "{ \"msgtype\": \"text\", \"text\": {\"content\": \"[报警]:"+message+"\"}, \"at\": {\"atMobiles\": ['"+phone+"'], \"isAtAll\": false}}";

        HttpEntity<String> request = new HttpEntity<>(content, headers);
        ResponseEntity<DingResponse> response =
                null;
        try {
            response = restTemplate.postForEntity(new URI(dingDingProperties.getUrl()),request, DingResponse.class);
            if(response.getStatusCode() == HttpStatus.OK){
                return response.getBody();
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return null;
    }
}