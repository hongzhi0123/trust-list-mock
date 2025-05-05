package coba.api.psd2.signapp.lotl.model;

import lombok.Data;
import java.util.List;

@Data
public class TL {
    private String countryCode;
    private LoTL lotl;
    private List<TSP> tspList;

    @Data
    public class LoTL {
        private String certificate;
        private String location;
        private String issueDateTime;
        private String updateDateTime;
    }

    @Data
    public class TSP {
        private String id;
        private List<TSPService> tspServiceList;

        @Data public class TSPService {
            private String id;
            private String certificate;
            private String status;
            private String statusStartingTime;
        }
    }
}
