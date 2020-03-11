package no.fint.oneroster.resolver;

import lombok.extern.slf4j.Slf4j;
import no.fint.oneroster.antlr.FilterLexer;
import no.fint.oneroster.antlr.FilterParser;
import no.fint.oneroster.exception.InvalidFilterException;
import no.fint.oneroster.filter.FilterErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ConsoleErrorListener;
import org.antlr.v4.runtime.tree.ParseTree;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
public class FilterResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return ParseTree.class.equals(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String filter = webRequest.getParameter("filter");

        if (filter == null || filter.isEmpty()) {
            return null;
        }

        CharStream stream = CharStreams.fromString(filter.trim());
        FilterLexer lexer = new FilterLexer(stream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        FilterParser parser = new FilterParser(tokens);
        parser.removeErrorListener(ConsoleErrorListener.INSTANCE);
        parser.addErrorListener(FilterErrorListener.INSTANCE);

        try {
            return parser.logical();
        } catch (InvalidFilterException e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
