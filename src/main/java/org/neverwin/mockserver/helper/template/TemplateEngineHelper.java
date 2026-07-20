package org.neverwin.mockserver.helper.template;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.*;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.neverwin.mockserver.helper.template.custom.ConcatFormatterCustom;
import org.neverwin.mockserver.helper.template.custom.TimestampFormatterCustom;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@Slf4j
public class TemplateEngineHelper {

    private Configuration configuration;
    private StringTemplateLoader stringLoader;

    @PostConstruct
    private void init() {
        this.configuration = new Configuration(Configuration.VERSION_2_3_34);
        this.stringLoader = new StringTemplateLoader();

        this.configuration.setTemplateLoader(stringLoader);
        this.configuration.setDefaultEncoding("UTF-8");

        this.configuration.setSharedVariable("formatTime", new TimestampFormatterCustom());
        this.configuration.setSharedVariable("concat", new ConcatFormatterCustom());
        this.configuration.setSharedVariable("uuid", new TemplateMethodModelEx() {
            @Override
            public Object exec(List arguments) throws TemplateModelException {
                return UUID.randomUUID().toString();
            }
        });

    }

    public String render(String templateContent, Map<String, Object> data) {
        try {
            if (data == null) data = new HashMap<>();

            String templateName = UUID.randomUUID().toString();
            stringLoader.putTemplate(templateName, templateContent);
            Template template = configuration.getTemplate(templateName);

            StringWriter out = new StringWriter();
            template.process(data, out);
            return out.toString();
        } catch (IOException | TemplateException e) {
            throw new RuntimeException("Gagal memproses template", e);
        }
    }

}
