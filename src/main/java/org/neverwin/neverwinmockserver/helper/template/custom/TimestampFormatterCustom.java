package org.neverwin.neverwinmockserver.helper.template.custom;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TimestampFormatterCustom implements TemplateMethodModelEx {
    @Override
    public Object exec(List arguments) throws TemplateModelException {
        if (arguments.size() != 1) {
            return "";
        }

        String formatPattern = arguments.getFirst().toString();

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatPattern)
                    .withZone(ZoneId.systemDefault());

            return formatter.format(Instant.now());
        } catch (Exception e) {
            return "";
        }
    }
}
