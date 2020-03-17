package no.fint.oneroster.filter;

import lombok.extern.slf4j.Slf4j;
import no.fint.oneroster.antlr.FilterLexer;
import no.fint.oneroster.antlr.FilterParser;
import no.fint.oneroster.exception.BadRequestException;
import no.fint.oneroster.exception.NoSuchFieldException;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ConsoleErrorListener;
import org.antlr.v4.runtime.tree.ParseTree;

@Slf4j
public class FilterEngine {

    public static boolean execute(String expression, Object object) {
        CharStream stream = CharStreams.fromString(expression.trim());
        FilterLexer lexer = new FilterLexer(stream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        FilterParser parser = new FilterParser(tokens);
        parser.removeErrorListener(ConsoleErrorListener.INSTANCE);
        parser.addErrorListener(FilterErrorListener.INSTANCE);

        ParseTree parseTree = parser.logical();
        FilterEvaluator evaluator = new FilterEvaluator(object);

        try {
            return evaluator.visit(parseTree);
        } catch (NoSuchFieldException e) {
            throw new BadRequestException();
        }
    }
}
