package com.encoder.urlshortner.services;

import com.encoder.urlshortner.model.Url;
import com.encoder.urlshortner.model.UrlDto;

public interface UrlShortnerService {
    public Url generateShortUrl(UrlDto urlDto);
    public Url saveShortUrl(Url url);
    public Url getShortUrl(String url);
    public void deleteShortUrl(Url url);
}
