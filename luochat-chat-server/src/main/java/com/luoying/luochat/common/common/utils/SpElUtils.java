package com.luoying.luochat.common.common.utils;

import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * @Author 落樱的悔恨
 * @Date 2024/1/7 18:10
 */
public class SpElUtils {

    private static final ExpressionParser PARSER = new SpelExpressionParser();
    private static final DefaultParameterNameDiscoverer PARAMETER_NAME_DISCOVERER = new DefaultParameterNameDiscoverer();

    public static String getPrefixByMethod(Method method) {
        // 获取方法的全限定名
        return method.getDeclaringClass() + "." + method.getName();
    }

    public static String getKeyByParseSpEl(Method method, Object[] args, String key) {
        // 获取方法的参数名列表
        String[] paramNames = Optional.ofNullable(PARAMETER_NAME_DISCOVERER.getParameterNames(method)).orElse(new String[0]);
        // 创建一个评估上下文对象（context），用于存储参数名和参数值的映射关系。
        EvaluationContext context = new StandardEvaluationContext();//el解析需要的上下文对象
        // 遍历参数名列表，将每个参数名与对应的参数值关联起来，并将它们添加到评估上下文中。
        for (int i = 0; i < paramNames.length; i++) {
            context.setVariable(paramNames[i], args[i]);//所有参数都作为原材料扔进去
        }
        // 使用SpEL解析器（PARSER）解析给定的SpEL表达式
        Expression expression = PARSER.parseExpression(key);
        // 使用解析得到的表达式对象（expression）在评估上下文中计算结果，并返回该结果的字符串表示形式。
        return expression.getValue(context, String.class);
    }
}
