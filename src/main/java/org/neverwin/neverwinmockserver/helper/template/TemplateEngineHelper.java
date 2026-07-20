package org.neverwin.neverwinmockserver.helper.template;

import freemarker.template.*;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.neverwin.neverwinmockserver.helper.template.custom.ConcatFormatterCustom;
import org.neverwin.neverwinmockserver.helper.template.custom.TimestampFormatterCustom;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class TemplateEngineHelper {

    private Configuration configuration;
    private final Map<String, Template> templateCache = new ConcurrentHashMap<>();

    @PostConstruct
    private void init() {
        this.configuration = new Configuration(Configuration.VERSION_2_3_34);
        this.configuration.setSharedVariable("formatTime", new TimestampFormatterCustom());
        this.configuration.setSharedVariable("concat", new ConcatFormatterCustom());
        this.configuration.setSharedVariable("uuid", new TemplateMethodModelEx() {
            @Override
            public Object exec(List arguments) throws TemplateModelException {
                return UUID.randomUUID().toString();
            }
        });

    }

    public String render(String templateName, String templateContent, Map<String, Object> data) {
        try {
            if (data == null) data = new HashMap<>();

            Template template = templateCache.computeIfAbsent(templateName, key -> {
                try {
                    return new Template(key, new StringReader(templateContent), this.configuration);
                } catch (Exception e) {
                    throw new RuntimeException("Gagal parsing template: " + key, e);
                }
            });

            StringWriter writer = new StringWriter();
            template.process(data, writer);

            return writer.toString();
        } catch (IOException | TemplateException e) {
            throw new RuntimeException("Gagal memproses template", e);
        }
    }

    public void clearCache() {
        this.templateCache.clear();
    }

}
