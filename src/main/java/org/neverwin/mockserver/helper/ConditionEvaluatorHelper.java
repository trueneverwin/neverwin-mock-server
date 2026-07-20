package org.neverwin.mockserver.helper;

import org.neverwin.mockserver.model.ScenarioRequest;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ConditionEvaluatorHelper {

    private final ExpressionParser spelParser = new SpelExpressionParser();

    public ScenarioRequest findMatchedRule(List<ScenarioRequest> rules, Map<String, String> headers, Map<String, String> queryParams, String body) {
        MockContextHelper mockContext = new MockContextHelper(headers, queryParams, body);
        StandardEvaluationContext evalContext = new StandardEvaluationContext(mockContext);

        return rules.stream()
                .filter(rule -> isRuleMatched(rule, evalContext))
                .findFirst()
                .orElse(null);
    }

    private boolean isRuleMatched(ScenarioRequest rule, StandardEvaluationContext context) {
        String condition = rule.getConditionRule();
        if (condition == null || condition.trim().isEmpty()) {
            return true;
        }

        try {
            return Boolean.TRUE.equals(spelParser.parseExpression(condition).getValue(context, Boolean.class));
        } catch (Exception ignored) {
            return false;
        }
    }

}
