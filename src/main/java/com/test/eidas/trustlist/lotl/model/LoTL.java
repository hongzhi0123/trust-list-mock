package com.test.eidas.trustlist.lotl.model;

import java.util.List;

import lombok.Data;

@Data
public class LoTL {
    private String issueDateTime;
    private String updateDateTime;
    private List<TSL> tslList;

    @Data
    public class TSL {
        private String countryCode;
        private String certificate;
        private String location;
    }
}
