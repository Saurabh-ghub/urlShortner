package com.encoder.urlshortner.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.encoder.urlshortner.model.Url;
import com.encoder.urlshortner.model.UrlDto;
import com.encoder.urlshortner.model.UrlErrorResponseDto;
import com.encoder.urlshortner.model.UrlResponseDto;
import com.encoder.urlshortner.services.UrlShortnerService;
import com.encoder.urlshortner.services.UrlShortnerServiceImpl;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.websocket.server.PathParam;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
public class UrlShortnerController {

    @Autowired
    private UrlShortnerService urlService;

    @PostMapping("")
    public ResponseEntity<?> createShortUrl(@RequestBody UrlDto urlDto) {
        Url url = urlService.generateShortUrl(urlDto);
        if(url != null){
            UrlResponseDto urlResponseDto = new UrlResponseDto();
            urlResponseDto.setOriginalUrl(url.getOriginalUrl());
            try{

                urlResponseDto.setExpirationDate(url.getExpirationDate());
            }
             catch (Exception e) {
                System.out.println("Expiration date is not mentioned");
            } 
            urlResponseDto.setShortUrl(url.getShortUrl());
            return new ResponseEntity<UrlResponseDto>(urlResponseDto, HttpStatus.OK);

        }
        UrlErrorResponseDto urlErrorResponseDto  = new UrlErrorResponseDto();
        urlErrorResponseDto.setError("Failed to create short url");
        urlErrorResponseDto.setStatus("404");
        return new ResponseEntity<UrlErrorResponseDto>(urlErrorResponseDto,HttpStatus.OK);
    }

    @GetMapping("/{shorturl}")
    public ResponseEntity<?> redirectToOriginalUrl(@PathVariable String shorturl, HttpServletResponse response) {
        if(shorturl == null || shorturl.isEmpty()){
            UrlErrorResponseDto urlErrorResponseDto  = new UrlErrorResponseDto();
            urlErrorResponseDto.setError("Invalid short url");
            urlErrorResponseDto.setStatus("404");
            return new ResponseEntity<UrlErrorResponseDto>(urlErrorResponseDto,HttpStatus.OK);
        }
        Url url = urlService.getUrlFromShortUrl(shorturl);
        System.out.println(shorturl);
        if(url == null){
            UrlErrorResponseDto urlErrorResponseDto  = new UrlErrorResponseDto();
            urlErrorResponseDto.setError("Url not found");
            urlErrorResponseDto.setStatus("404");
            return new ResponseEntity<UrlErrorResponseDto>(urlErrorResponseDto,HttpStatus.OK);
        }
        
        LocalDateTime expirationDateTime = url.getExpirationDate();
        if(expirationDateTime.isBefore(LocalDateTime.now())){
            urlService.deleteShortUrl(url);
            UrlErrorResponseDto urlErrorResponseDto  = new UrlErrorResponseDto();
            urlErrorResponseDto.setError("Url has been expired");
            urlErrorResponseDto.setStatus("404");
            return new ResponseEntity<UrlErrorResponseDto>(urlErrorResponseDto,HttpStatus.OK);
        }
        else{
            
                System.out.println("Original url "+url.getOriginalUrl());
                try {
                    response.sendRedirect(url.getOriginalUrl());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                
                return null;
           
            }
        
    }
    
    
}
