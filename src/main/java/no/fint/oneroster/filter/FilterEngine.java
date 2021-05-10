package no.fint.oneroster.filter;

import no.fint.oneroster.antlr.FilterLexer;
import no.fint.oneroster.antlr.FilterParser;
import no.fint.oneroster.exception.InvalidSyntaxException;
import no.fint.oneroster.exception.NoSuchFieldException;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ConsoleErrorListener;
import org.antlr.v4.runtime.tree.ParseTree;

public class FilterEngine {

    public static boolean execute(String expression, Object object) throws InvalidSyntaxException, NoSuchFieldException {
        FilterLexer lexer = new FilterLexer(CharStreams.fromString(expression.trim()));
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        FilterParser parser = new FilterParser(tokens);
        parser.removeErrorListener(ConsoleErrorListener.INSTANCE);
        parser.addErrorListener(FilterErrorListener.INSTANCE);

        ParseTree parseTree = parser.logical();

        return new FilterEvaluator(object).visit(parseTree);
    }
}