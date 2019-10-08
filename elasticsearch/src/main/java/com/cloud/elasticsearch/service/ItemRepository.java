package com.cloud.elasticsearch.service;

import com.cloud.elasticsearch.entity.Item;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Service;

@Service
public interface ItemRepository extends ElasticsearchRepository<Item,Long> {

}
