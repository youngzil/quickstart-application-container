package org.quickstart.smart.http;

import org.smartboot.http.client.HttpClient;

public class HttpGetDemo {
    public static void main(String[] args) {
        HttpClient httpClient = new HttpClient("www.baidu.com", 80);
        httpClient.connect();
        httpClient.get("/")//
            .onSuccess(response -> System.out.println(response.body()))//
            .onFailure(throwable -> throwable.printStackTrace())//
            .send();
    }
}
