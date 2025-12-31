package com.codestepfish.vlineex.api;

import com.codestepfish.vlineex.config.PushSuccessCondition;
import com.codestepfish.vlineex.model.DataItem;
import com.dtflys.forest.annotation.Address;
import com.dtflys.forest.annotation.JSONBody;
import com.dtflys.forest.annotation.Post;
import com.dtflys.forest.annotation.Success;
import com.dtflys.forest.callback.OnError;
import com.dtflys.forest.callback.OnSuccess;

@Address(scheme = "http", host = "127.0.0.1", port = "10086", basePath = "/api")
public interface ApiClient {

    @Post(url = "/test", async = true)
    @Success(condition = PushSuccessCondition.class)
    void uploadTrade(@JSONBody DataItem item, OnSuccess<String> onSuccess, OnError onError);
}