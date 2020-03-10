// Generated from /Users/torleif/Documents/GitHub/fint-oneroster-rest-provider/src/main/resources/Filter.g4 by ANTLR 4.8
package no.fint.oneroster.antlr;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class FilterLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.8", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, LOGICAL_OPERATOR=2, ATTRNAME=3, EQ=4, NE=5, GT=6, LT=7, GE=8, 
		LE=9, CO=10, BOOLEAN=11, DATE=12, INTEGER=13, STRING=14, WS=15, SQ=16;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "LOGICAL_OPERATOR", "ATTRNAME", "ATTR_NAME_CHAR", "DIGIT", "ALPHA", 
			"EQ", "NE", "GT", "LT", "GE", "LE", "CO", "BOOLEAN", "DATE", "INTEGER", 
			"STRING", "WS", "SQ"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'.'", null, null, "'='", "'!='", "'>'", "'<'", "'>='", "'<='", 
			"'~'", null, null, null, null, "' '", "'''"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, "LOGICAL_OPERATOR", "ATTRNAME", "EQ", "NE", "GT", "LT", "GE", 
			"LE", "CO", "BOOLEAN", "DATE", "INTEGER", "STRING", "WS", "SQ"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public FilterLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Filter.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\22\u0088\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\3\2\3\2\3\3\3\3\3\3\3\3\3\3\5\3\61\n\3\3\4\3"+
		"\4\7\4\65\n\4\f\4\16\48\13\4\3\5\3\5\3\5\5\5=\n\5\3\6\3\6\3\7\3\7\3\b"+
		"\3\b\3\t\3\t\3\t\3\n\3\n\3\13\3\13\3\f\3\f\3\f\3\r\3\r\3\r\3\16\3\16\3"+
		"\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\6\17^\n\17\r\17\16\17"+
		"_\3\17\3\17\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\6\20"+
		"o\n\20\r\20\16\20p\3\20\3\20\3\21\3\21\6\21w\n\21\r\21\16\21x\3\21\3\21"+
		"\3\22\3\22\6\22\177\n\22\r\22\16\22\u0080\3\22\3\22\3\23\3\23\3\24\3\24"+
		"\3\u0080\2\25\3\3\5\4\7\5\t\2\13\2\r\2\17\6\21\7\23\b\25\t\27\n\31\13"+
		"\33\f\35\r\37\16!\17#\20%\21\'\22\3\2\5\5\2//<<aa\4\2C\\c|\3\2\62;\2\u008d"+
		"\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3"+
		"\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2"+
		"\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\3)\3\2\2"+
		"\2\5\60\3\2\2\2\7\62\3\2\2\2\t<\3\2\2\2\13>\3\2\2\2\r@\3\2\2\2\17B\3\2"+
		"\2\2\21D\3\2\2\2\23G\3\2\2\2\25I\3\2\2\2\27K\3\2\2\2\31N\3\2\2\2\33Q\3"+
		"\2\2\2\35S\3\2\2\2\37c\3\2\2\2!t\3\2\2\2#|\3\2\2\2%\u0084\3\2\2\2\'\u0086"+
		"\3\2\2\2)*\7\60\2\2*\4\3\2\2\2+,\7C\2\2,-\7P\2\2-\61\7F\2\2./\7Q\2\2/"+
		"\61\7T\2\2\60+\3\2\2\2\60.\3\2\2\2\61\6\3\2\2\2\62\66\5\r\7\2\63\65\5"+
		"\t\5\2\64\63\3\2\2\2\658\3\2\2\2\66\64\3\2\2\2\66\67\3\2\2\2\67\b\3\2"+
		"\2\28\66\3\2\2\29=\t\2\2\2:=\5\13\6\2;=\5\r\7\2<9\3\2\2\2<:\3\2\2\2<;"+
		"\3\2\2\2=\n\3\2\2\2>?\4\62;\2?\f\3\2\2\2@A\t\3\2\2A\16\3\2\2\2BC\7?\2"+
		"\2C\20\3\2\2\2DE\7#\2\2EF\7?\2\2F\22\3\2\2\2GH\7@\2\2H\24\3\2\2\2IJ\7"+
		">\2\2J\26\3\2\2\2KL\7@\2\2LM\7?\2\2M\30\3\2\2\2NO\7>\2\2OP\7?\2\2P\32"+
		"\3\2\2\2QR\7\u0080\2\2R\34\3\2\2\2S]\5\'\24\2TU\7v\2\2UV\7t\2\2VW\7w\2"+
		"\2W^\7g\2\2XY\7h\2\2YZ\7c\2\2Z[\7n\2\2[\\\7u\2\2\\^\7g\2\2]T\3\2\2\2]"+
		"X\3\2\2\2^_\3\2\2\2_]\3\2\2\2_`\3\2\2\2`a\3\2\2\2ab\5\'\24\2b\36\3\2\2"+
		"\2cn\5\'\24\2de\t\4\2\2ef\t\4\2\2fg\t\4\2\2gh\t\4\2\2hi\7/\2\2ij\t\4\2"+
		"\2jk\t\4\2\2kl\7/\2\2lm\t\4\2\2mo\t\4\2\2nd\3\2\2\2op\3\2\2\2pn\3\2\2"+
		"\2pq\3\2\2\2qr\3\2\2\2rs\5\'\24\2s \3\2\2\2tv\5\'\24\2uw\t\4\2\2vu\3\2"+
		"\2\2wx\3\2\2\2xv\3\2\2\2xy\3\2\2\2yz\3\2\2\2z{\5\'\24\2{\"\3\2\2\2|~\5"+
		"\'\24\2}\177\13\2\2\2~}\3\2\2\2\177\u0080\3\2\2\2\u0080\u0081\3\2\2\2"+
		"\u0080~\3\2\2\2\u0081\u0082\3\2\2\2\u0082\u0083\5\'\24\2\u0083$\3\2\2"+
		"\2\u0084\u0085\7\"\2\2\u0085&\3\2\2\2\u0086\u0087\7)\2\2\u0087(\3\2\2"+
		"\2\13\2\60\66<]_px\u0080\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}