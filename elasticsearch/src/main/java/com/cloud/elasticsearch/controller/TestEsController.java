package com.cloud.elasticsearch.controller;

import com.cloud.elasticsearch.entity.Item;
import com.cloud.elasticsearch.service.ItemRepository;
import com.google.common.collect.Lists;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import result.Result;
import result.ResultData;

import java.util.ArrayList;

import static result.ResultData.SUCCESS;
import static result.Result.SUCCESS;

/**
 * 这里只展示es简单的增删改查操作
 * 其实es的强大之处在于聚合
 * 相较于数据库聚合函数, es速度更快, 接近于实时搜索
 */
@RestController
public class TestEsController {

    @Autowired
    ItemRepository itemRepository;

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
}
