package com.cloud.elasticsearch.service;

import com.cloud.elasticsearch.entity.FilmEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Service;

@Service
public interface FilmEntityRepository extends ElasticsearchRepository<FilmEntity, Long> {

}
