package com.codestepfish.vline.http.client;

import com.dtflys.forest.http.ForestRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VlineForestClient {
    private ForestRequest request;
    private ForestHandler handler;
}
