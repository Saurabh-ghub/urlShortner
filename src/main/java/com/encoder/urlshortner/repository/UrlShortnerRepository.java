package com.encoder.urlshortner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.encoder.urlshortner.model.Url;

@Repository
public interface UrlShortnerRepository extends JpaRepository<Url,Long>{
    public Url findByShortUrl(String shortUrl);
}
