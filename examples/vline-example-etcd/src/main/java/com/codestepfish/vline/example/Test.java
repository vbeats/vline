package com.codestepfish.vline.example;

import com.codestepfish.vline.etcd.EtcdClientHolder;
import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Client;
import io.etcd.jetcd.KV;
import io.etcd.jetcd.kv.GetResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequestMapping("/test")
public class Test {

    @RequestMapping("/kv")
    public String kv() {
        // key value test
        Client client = EtcdClientHolder.ETCD_CLIENTS.get("t1");
        KV kvClient = client.getKVClient();
        ByteSequence key = ByteSequence.from("test_key".getBytes());
        ByteSequence value = ByteSequence.from("test_value".getBytes());

        // put the key-value
        try {
            kvClient.put(key, value).get();

            // get the CompletableFuture
            CompletableFuture<GetResponse> getFuture = kvClient.get(key);

            // get the value from CompletableFuture
            GetResponse response = getFuture.get();

            log.info("etcd res: {}", response);
        } catch (Exception e) {
            log.error("etcd error", e);
        }

        return "success";
    }
}
