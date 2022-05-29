package com.nguyencuong.librarytlu.retrofit;

import com.nguyencuong.librarytlu.config.Config;

public class APIService {
    private static String baseUrl = (Config.domain);
    public static DataService getService() {
        return APIRetrofitClient.getClient(baseUrl).create(DataService.class);
    }
}
