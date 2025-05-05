package com.test.eidas.trustlist;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.test.eidas.trustlist.lotl.LoTLGenerator;
import com.test.eidas.trustlist.lotl.model.LoTL;
import com.test.eidas.trustlist.lotl.model.TL;

import freemarker.core.ParseException;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
import lombok.experimental.var;

public class CreateLoTL {
    public static void main(String[] args) throws Exception {
        System.out.println(generateTL());
        System.out.println(generateLoTL());
    }

    private static String generateLoTL() throws Exception {

        var lotl = new LoTL();
        lotl.setIssueDateTime(ZonedDateTime.now().withZoneSameInstant(java.time.ZoneOffset.UTC)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX")));
        lotl.setUpdateDateTime(ZonedDateTime.now().withZoneSameInstant(java.time.ZoneOffset.UTC)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX")));
        
        var tslList = new ArrayList<LoTL.TSL>();
        var tsl1 = lotl.new TSL();
        tsl1.setCountryCode("EU");
        tsl1.setCertificate("");
        tsl1.setLocation("https://www.example.com");
        var tsl2 = lotl.new TSL();
        tsl2.setCountryCode("LU");
        tsl2.setCertificate("");
        tsl2.setLocation("https://www.example.com");
        var tsl3 = lotl.new TSL();
        tsl3.setCountryCode("FR");
        tsl3.setCertificate("");
        tsl3.setLocation("https://www.example.com");
        var tsl4 = lotl.new TSL();
        tsl4.setCountryCode("BE");
        tsl4.setCertificate("");
        tsl4.setLocation("https://www.example.com");
        tslList.add(tsl1);
        tslList.add(tsl2);
        tslList.add(tsl3);
        tslList.add(tsl4);
        lotl.setTslList(tslList);

        return LoTLGenerator.generateLoTL(lotl);
    }

    private static String generateTL() throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException, TemplateException {
        var tl = new TL();

        tl.setCountryCode("LU");

        var lotl = tl.new LoTL();
        tl.setLotl(lotl);
        tl.getLotl().setCertificate("");
        tl.getLotl().setLocation("https://www.example.com");
        tl.getLotl().setIssueDateTime(ZonedDateTime.now().withZoneSameInstant(java.time.ZoneOffset.UTC)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX")));
        tl.getLotl().setUpdateDateTime(ZonedDateTime.now().withZoneSameInstant(java.time.ZoneOffset.UTC)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX")));

        List<TL.TSP> tspList = new ArrayList<TL.TSP>();
        tl.setTspList(tspList);
        var tsp1 = tl.new TSP();
        tsp1.setId("3.1");
        tspList.add(tsp1);

        List<TL.TSP.TSPService> tspServiceList1 = new ArrayList<TL.TSP.TSPService>();
        tsp1.setTspServiceList(tspServiceList1);

        var tspService1 = tsp1.new TSPService();
        tspService1.setId("3.1.1");
        tspService1.setCertificate("");
        tspService1.setStatus("granted");
        tspService1.setStatusStartingTime(ZonedDateTime.now().withZoneSameInstant(java.time.ZoneOffset.UTC)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX")));
        var tspService2 = tsp1.new TSPService();
        tspService2.setId("3.1.2");
        tspService2.setCertificate("");
        tspService2.setStatus("granted");
        tspService2.setStatusStartingTime(ZonedDateTime.now().withZoneSameInstant(java.time.ZoneOffset.UTC)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX")));
        var tspService3 = tsp1.new TSPService();
        tspService3.setId("3.1.3");
        tspService3.setCertificate("");
        tspService3.setStatus("granted");
        tspService3.setStatusStartingTime(ZonedDateTime.now().withZoneSameInstant(java.time.ZoneOffset.UTC)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX")));
        var tspService4 = tsp1.new TSPService();
        tspService4.setId("3.1.4");
        tspService4.setCertificate(""); 
        tspService4.setStatus("withdrawn");
        tspService4.setStatusStartingTime(ZonedDateTime.now().withZoneSameInstant(java.time.ZoneOffset.UTC)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX")));
        var tspService5 = tsp1.new TSPService();
        tspService5.setId("3.1.5");
        tspService5.setCertificate("");
        tspService5.setStatus("withdrawn");
        tspService5.setStatusStartingTime(ZonedDateTime.now().withZoneSameInstant(java.time.ZoneOffset.UTC)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX")));
        var tspService6 = tsp1.new TSPService();
        tspService6.setId("3.1.6");
        tspService6.setCertificate("");
        tspService6.setStatus("withdrawn");
        tspService6.setStatusStartingTime(ZonedDateTime.now().withZoneSameInstant(java.time.ZoneOffset.UTC)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX")));
        tspServiceList1.add(tspService1);
        tspServiceList1.add(tspService2);
        tspServiceList1.add(tspService3);
        tspServiceList1.add(tspService4);
        tspServiceList1.add(tspService5);
        tspServiceList1.add(tspService6);

        var tsp2 = tl.new TSP();
        tspList.add(tsp2);        
        tsp2.setId("3.2");
        List<TL.TSP.TSPService> tspServiceList2 = new ArrayList<TL.TSP.TSPService>();
        tsp2.setTspServiceList(tspServiceList2);

        var tspService7 = tsp2.new TSPService();
        tspService7.setId("3.2.1");
        tspService7.setCertificate("");
        tspService7.setStatus("granted");
        tspService7.setStatusStartingTime(ZonedDateTime.now().withZoneSameInstant(java.time.ZoneOffset.UTC)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX")));
        
        var tspService8 = tsp2.new TSPService();
        tspService8.setId("3.2.2");
        tspService8.setCertificate("");
        tspService8.setStatus("granted");
        tspService8.setStatusStartingTime(ZonedDateTime.now().withZoneSameInstant(java.time.ZoneOffset.UTC)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX")));
        var tspService9 = tsp2.new TSPService();
        tspService9.setId("3.2.3");
        tspService9.setCertificate("");
        tspService9.setStatus("granted");
        tspService9.setStatusStartingTime(ZonedDateTime.now().withZoneSameInstant(java.time.ZoneOffset.UTC)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX")));
        tspServiceList2.add(tspService7);
        tspServiceList2.add(tspService8);
        tspServiceList2.add(tspService9);

        var tsp3 = tl.new TSP();
        tspList.add(tsp3);
        tsp3.setId("3.3");
        List<TL.TSP.TSPService> tspServiceList3 = new ArrayList<TL.TSP.TSPService>();
        tsp3.setTspServiceList(tspServiceList3);
        var tspService10 = tsp3.new TSPService();
        tspService10.setId("3.3.1");
        tspService10.setCertificate("");
        tspService10.setStatus("granted");
        tspService10.setStatusStartingTime(ZonedDateTime.now().withZoneSameInstant(java.time.ZoneOffset.UTC)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX")));
        var tspService11 = tsp3.new TSPService();
        tspService11.setId("3.3.2");
        tspService11.setCertificate("");
        tspService11.setStatus("granted");
        tspService11.setStatusStartingTime(ZonedDateTime.now().withZoneSameInstant(java.time.ZoneOffset.UTC)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX")));
        tspServiceList3.add(tspService10);
        tspServiceList3.add(tspService11);
        
        return LoTLGenerator.generateTL(tl);
    }
}
