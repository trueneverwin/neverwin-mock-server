package org.neverwin.mockserver.helper.template.custom;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ConcatFormatterCustom implements TemplateMethodModelEx {
    @Override
    public Object exec(List arguments) throws TemplateModelException {
        if (arguments.size() != 2) {
            return "";
        }

        String value1 = arguments.getFirst().toString();
        String value2 = arguments.get(1).toString();

        try {
            return value1 + "-" + value2;
        } catch (Exception e) {
            return "";
        }
    }
}
