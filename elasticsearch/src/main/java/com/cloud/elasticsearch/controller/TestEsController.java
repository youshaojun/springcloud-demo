package com.cloud.elasticsearch.controller;

import com.cloud.elasticsearch.entity.FilmEntity;
import com.cloud.elasticsearch.entity.Item;
import com.cloud.elasticsearch.service.FilmEntityRepository;
import com.cloud.elasticsearch.service.ItemRepository;
import com.google.common.collect.Lists;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import result.Result;
import result.ResultData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static result.ResultData.SUCCESS;
import static result.Result.SUCCESS;

@RestController
public class TestEsController {

    @Autowired
    ItemRepository itemRepository;
    @Autowired
    FilmEntityRepository filmEntityRepository;
    @Autowired
    TransportClient transportClient;

    @RequestMapping("/insert")
    public Result testEs() {
        itemRepository.save(new Item()
                .setId(1234L)
                .setTitle("森马牛仔裤")
                .setCategory("服装")
                .setBrand("森马")
                .setPrice(199.0)
                .setImages("http://xxx"));
        return SUCCESS();
    }

    @RequestMapping("/delete/{id}")
    public Result delete(@PathVariable Long id) {
        itemRepository.deleteById(id);
        return SUCCESS();
    }

    @RequestMapping("/modify")
    public Result modify() {
        itemRepository.save(new Item()
                .setId(123L)
                .setTitle("森马牛仔裤")
                .setCategory("服装")
                .setBrand("森马")
                .setPrice(198.0)
                .setImages("http://xxx"));
        return SUCCESS();
    }

    @RequestMapping("/findById/{id}")
    public ResultData findById(@PathVariable Long id) {
        return SUCCESS(itemRepository.findById(id).get());
    }

    @RequestMapping("/findAll")
    public ResultData findAll() {
        Iterable<Item> all = itemRepository.findAll();
        ArrayList<Item> items = Lists.newArrayList(all);
        return SUCCESS(items);
    }

    /**
     * 测试ik分词
     * <p>
     * 森马牛仔裤
     * <p>
     * {
     * "tokens": [
     * {
     * "token": "森",
     * "start_offset": 0,
     * "end_offset": 1,
     * "type": "CN_CHAR",
     * "position": 0
     * }
     * ,
     * {
     * "token": "马",
     * "start_offset": 1,
     * "end_offset": 2,
     * "type": "CN_CHAR",
     * "position": 1
     * }
     * ,
     * {
     * "token": "牛仔裤",
     * "start_offset": 2,
     * "end_offset": 5,
     * "type": "CN_WORD",
     * "position": 2
     * }
     * ,
     * {
     * "token": "牛仔",
     * "start_offset": 2,
     * "end_offset": 4,
     * "type": "CN_WORD",
     * "position": 3
     * }
     * ,
     * {
     * "token": "裤",
     * "start_offset": 4,
     * "end_offset": 5,
     * "type": "CN_CHAR",
     * "position": 4
     * }
     * ]
     * }
     *
     * @param title    标题
     * @param pageable 分页
     * @return
     */
    @RequestMapping("/testIk/{title}")
    public ResultData testIk(@PathVariable String title, @PageableDefault(value = 2) Pageable pageable) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        MatchQueryBuilder matchQuery = QueryBuilders.matchQuery("title", title);
        boolQuery.must(matchQuery);
        Iterable<Item> all = itemRepository.search(boolQuery, pageable);
        ArrayList<Item> items = Lists.newArrayList(all);
        return SUCCESS(items);
    }

    @RequestMapping("/insertFilm")
    public Result insertFilm() {
        filmEntityRepository.save(new FilmEntity().setId(123456L).setName("我爱你中国"));
        return SUCCESS();
    }

    @RequestMapping("/search/{query}")
    public ResultData search(@PathVariable String query) {
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        //高亮显示规则
        highlightBuilder.preTags("<span style='color:green'>");
        highlightBuilder.postTags("</span>");
        //指定高亮字段
        highlightBuilder.field("name");
        highlightBuilder.field("name.pinyin");
        String[] fileds = {"name", "name.pinyin"};
        QueryBuilder matchQuery = QueryBuilders.multiMatchQuery(query, fileds);
        //QueryBuilder matchQuery = QueryBuilders.matchPhraseQuery("name.pinyin", query);
        //搜索数据
        SearchResponse response = transportClient.prepareSearch("film-entity")
                .setQuery(matchQuery)
                .highlighter(highlightBuilder)
                .execute().actionGet();
        SearchHits searchHits = response.getHits();
        System.out.println("记录数-->" + searchHits.getTotalHits());
        List<FilmEntity> list = new ArrayList<>();
        for (SearchHit hit : searchHits) {
            FilmEntity entity = new FilmEntity();
            Map<String, Object> entityMap = hit.getSourceAsMap();
            System.out.println(hit.getHighlightFields());
            //高亮字段
            if (!StringUtils.isEmpty(hit.getHighlightFields().get("name.pinyin"))) {
                Text[] text = hit.getHighlightFields().get("name.pinyin").getFragments();
                entity.setName(text[0].toString());
            }
            if (!StringUtils.isEmpty(hit.getHighlightFields().get("name"))) {
                Text[] text = hit.getHighlightFields().get("name").getFragments();
                entity.setName(text[0].toString());
            }
            if (!CollectionUtils.isEmpty(entityMap)) {
                if (!StringUtils.isEmpty(entityMap.get("id"))) {
                    entity.setId(Long.valueOf(String.valueOf(entityMap.get("id"))));
                }
            }
            list.add(entity);
        }
        return SUCCESS(list);
    }


}
