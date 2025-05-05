package com.test.eidas.trustlist.lotl;

import java.io.IOException;
import java.io.StringWriter;

import com.test.eidas.trustlist.lotl.model.LoTL;
import com.test.eidas.trustlist.lotl.model.TL;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

public class LoTLGenerator {
    public static String generateLoTL(LoTL lotl)
            throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException,
            TemplateException {
        // Create and configure the FreeMarker configuration
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_34);

        // Set the directory for loading templates from the classpath
        cfg.setClassLoaderForTemplateLoading(
                LoTLGenerator.class.getClassLoader(), "templates");

        Template template = cfg.getTemplate("lotlTemplate.ftl");

        // Generate the output
        StringWriter writer = new StringWriter();
        template.process(lotl, writer);

        return writer.toString();
    }

    public static String generateTL(TL tl)
            throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException,
            TemplateException {
        // Create and configure the FreeMarker configuration
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_34);

        // Set the directory for loading templates from the classpath
        cfg.setClassLoaderForTemplateLoading(
                LoTLGenerator.class.getClassLoader(), "templates");

        Template template = cfg.getTemplate("tlTemplate.ftl");

        // Generate the output
        StringWriter writer = new StringWriter();
        template.process(tl, writer);

        return writer.toString();
    }
}
