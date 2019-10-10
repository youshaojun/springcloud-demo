package com.cloud.elasticsearch.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Mapping;
import org.springframework.data.elasticsearch.annotations.Setting;

@Document(indexName = "film-entity", type = "film", shards = 1, replicas = 0)
@Setting(settingPath = "/json/film-setting.json")
@Mapping(mappingPath = "/json/film-mapping.json")
@Data
@Accessors(chain = true)
public class FilmEntity {
    @Id
    private Long id;
    private String name;

}
