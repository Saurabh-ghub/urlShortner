package com.encoder.urlshortner.services;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;

import com.encoder.urlshortner.model.Url;
import com.encoder.urlshortner.model.UrlDto;
import com.encoder.urlshortner.repository.UrlShortnerRepository;
import com.google.common.hash.Hashing;

import io.micrometer.common.util.StringUtils;

public class UrlShortnerServiceImpl implements UrlShortnerService{

    @Autowired
    private UrlShortnerRepository urlRepository;

    private String encodeUrl(String url){
        String encodedUrl = "";
        LocalDateTime time = LocalDateTime.now();
        encodedUrl = Hashing.murmur3_128()
                        .hashString(encodedUrl.concat(time.toString()), StandardCharsets.UTF_8)
                        .toString()
                        .substring(0,16);
        return encodedUrl;
    }

    private LocalDateTime getExpirationDateTime(LocalDateTime creationDateTime,UrlDto urlDto){
        if(urlDto.getExpirationDate()==null || StringUtils.isBlank(urlDto.getExpirationDate())){
            return creationDateTime.plus(30, null);
        }
        else{
            return LocalDateTime.parse(urlDto.getExpirationDate());
        }
    }

    @Override
    public Url generateShortUrl(UrlDto urlDto) {
        if(urlDto!=null && (urlDto.getUrl()!=null || !urlDto.getUrl().isEmpty() )){
            String encodedUrl = encodeUrl(urlDto.getUrl());
            Url urlToSave = new Url();
            urlToSave.setOriginalUrl(urlDto.getUrl());
            urlToSave.setCreationDate(LocalDateTime.now());
            urlToSave.setShortUrl(encodedUrl);
            urlToSave.setExpirationDate(getExpirationDateTime(urlToSave.getCreationDate(),urlDto));
            Url urlSaved =  saveShortUrl(urlToSave);

            if(urlSaved!=null){
                return urlSaved;
            }
            return null;
        }
        return null;
    }

    @Override
    public Url saveShortUrl(Url url) {
        return urlRepository.save(url);
    }

    @Override
    public Url getShortUrl(String url) {
        return urlRepository.findByShortUrl(url);
    }

    @Override
    public void deleteShortUrl(Url url) {
        urlRepository.delete(url);
    }

}
