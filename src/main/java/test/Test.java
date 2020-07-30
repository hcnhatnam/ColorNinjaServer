/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

/**
 *
 * @author namhcn
 */
public class Test {

    public static RestHighLevelClient client = new RestHighLevelClient(
            RestClient.builder(
                    new HttpHost("localhost", 9200, "http"),
                    new HttpHost("localhost", 9201, "http")));

    public static IndexResponse index(Map<String, Object> jsonMap) {
        try {

//            Map<String, Object> jsonMap = new HashMap<>();
//            jsonMap.put("user", "kimchy");
//            jsonMap.put("postDate", new Date());
//            jsonMap.put("message", "trying out Elasticsearch");
            IndexRequest indexRequest = new IndexRequest("posts")
                    .id(System.currentTimeMillis() + "").source(jsonMap);
            IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);

            client.close();
            return indexResponse;

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        return null;
    }

    public static void main(String[] args) {
        try {

            Map<String, Object> jsonMap = new HashMap<>();
            jsonMap.put("user", "kimchy");
            jsonMap.put("postDate", new Date());
            jsonMap.put("message", "trying out Elasticsearch");
            index(jsonMap);
            client.close();

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }
}
