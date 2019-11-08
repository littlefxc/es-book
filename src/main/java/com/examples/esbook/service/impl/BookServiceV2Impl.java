package com.examples.esbook.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.awifi.capacity.common.elasticsearch.starter.component.ElasticsearchTemplate;
import com.examples.esbook.domain.common.Page;
import com.examples.esbook.domain.model.BookModel;
import com.examples.esbook.domain.vo.BookRequestVo;
import com.examples.esbook.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author fengxuechao
 * @version 0.1
 * @date 2019/8/5
 */
@Slf4j
@Service("bookServiceV2")
public class BookServiceV2Impl implements BookService {

    private static final String INDEX_NAME = "book";
    private static final String INDEX_TYPE = "_doc";

    private final ElasticsearchTemplate template;

    public BookServiceV2Impl(ElasticsearchTemplate template) {
        this.template = template;
    }

    /**
     * 分页查询
     *
     * @param bookRequestVo
     * @return
     */
    @Override
    public Page<BookModel> list(BookRequestVo bookRequestVo) {
        int pageNo = bookRequestVo.getPageNo();
        int pageSize = bookRequestVo.getPageSize();

        // 构建查询条件
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        if (StringUtils.isNotBlank(bookRequestVo.getName())) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("name", bookRequestVo.getName()));
        }
        if (StringUtils.isNotBlank(bookRequestVo.getAuthor())) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("author", bookRequestVo.getAuthor()));
        }
        if (null != bookRequestVo.getStatus()) {
            boolQueryBuilder.must(QueryBuilders.termQuery("status", bookRequestVo.getStatus()));
        }
        if (StringUtils.isNotBlank(bookRequestVo.getSellTime())) {
            boolQueryBuilder.must(QueryBuilders.termQuery("sellTime", bookRequestVo.getSellTime()));
        }
        if (StringUtils.isNotBlank(bookRequestVo.getCategories())) {
            String[] categoryArr = bookRequestVo.getCategories().split(",");
            List<Integer> categoryList = Arrays.stream(categoryArr).map(Integer::valueOf).collect(Collectors.toList());
            BoolQueryBuilder categoryBoolQueryBuilder = QueryBuilders.boolQuery();
            for (Integer category : categoryList) {
                categoryBoolQueryBuilder.should(QueryBuilders.termQuery("category", category));
            }
            boolQueryBuilder.must(categoryBoolQueryBuilder);
        }

        try {

            List<Map<String, Object>> maps = template.queryByPage(INDEX_NAME, bookRequestVo.getPageSize(), bookRequestVo.getPageNo(), "id", boolQueryBuilder, null);
            TypeReference<List<BookModel>> typeReference = new TypeReference<List<BookModel>>(){};
            return new Page<>(pageNo, pageSize, maps.size(), JSON.parseObject(JSON.toJSONString(maps), typeReference));
        } catch (Exception e) {
            log.error("查询失败！原因: {}", e.getMessage(), e);
        }
        return null;
    }

    @Override
    public BookModel detail(Integer id) {
        try {
            Map map = template.get(INDEX_NAME, null, id.toString());
            return JSON.parseObject(JSON.toJSONString(map), BookModel.class);
        } catch (Exception e) {
            log.error("查询失败！原因: {}", e.getMessage(), e);
        }
        return null;
    }

    @Override
    public void save(BookModel bookModel) {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("id", bookModel.getId());
        jsonMap.put("name", bookModel.getName());
        jsonMap.put("author", bookModel.getAuthor());
        jsonMap.put("category", bookModel.getCategory());
        jsonMap.put("price", bookModel.getPrice());
        jsonMap.put("sellTime", bookModel.getSellTime());
        jsonMap.put("sellReason", bookModel.getSellReason());
        jsonMap.put("status", bookModel.getStatus());
        try {
            template.createAsync(INDEX_NAME, null, jsonMap);
        } catch (Exception e) {
            log.error("添加失败！原因: {}", e.getMessage(), e);
        }
    }

    @Override
    public void update(BookModel bookModel) {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("sellReason", bookModel.getSellReason());
        try {
            template.update(INDEX_NAME, null, bookModel.getId().toString(), jsonMap);
        } catch (Exception e) {
            log.error("更新失败！原因: {}", e.getMessage(), e);
        }
    }

    @Override
    public void delete(Integer id) {
        try {
            template.delete(INDEX_NAME, null, id.toString());
        } catch (Exception e) {
            log.error("删除失败！原因: {}", e.getMessage(), e);
        }
    }
}
