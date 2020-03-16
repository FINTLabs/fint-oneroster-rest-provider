package no.fint.oneroster.filter;

import lombok.extern.slf4j.Slf4j;
import no.fint.oneroster.exception.InvalidSyntaxException;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

@Slf4j
public class FilterErrorListener extends BaseErrorListener {

    public static final FilterErrorListener INSTANCE = new FilterErrorListener();

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        throw new InvalidSyntaxException("line " + line + ":" + charPositionInLine + " " + msg);
    }
}
