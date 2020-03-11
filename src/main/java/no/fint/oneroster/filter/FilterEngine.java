package no.fint.oneroster.filter;

import no.fint.oneroster.antlr.FilterLexer;
import no.fint.oneroster.antlr.FilterParser;
import no.fint.oneroster.exception.InvalidFilterException;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ConsoleErrorListener;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.commons.lang3.StringUtils;

public class FilterEngine {

    public static boolean execute(String expression, Object object) throws InvalidFilterException {
        CharStream stream = CharStreams.fromString(expression.trim());
        FilterLexer lexer = new FilterLexer(stream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        FilterParser parser = new FilterParser(tokens);
        parser.removeErrorListener(ConsoleErrorListener.INSTANCE);
        parser.addErrorListener(FilterErrorListener.INSTANCE);
        ParseTree parseTree = parser.logical();

        FilterEvaluator evaluator = new FilterEvaluator(object);
        return evaluator.visit(parseTree);
    }
}
