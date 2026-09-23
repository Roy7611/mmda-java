// Generated from D:/2026/java/mmda-core/mmda-core-sql/src/main/resources/SqlParser.g4 by ANTLR 4.13.2
package cloud.mmda.core.sql.antlr4;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class SqlParserLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.13.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, T__20=21, T__21=22, T__22=23, T__23=24, 
		T__24=25, T__25=26, T__26=27, T__27=28, T__28=29;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
			"T__9", "T__10", "T__11", "T__12", "T__13", "T__14", "T__15", "T__16", 
			"T__17", "T__18", "T__19", "T__20", "T__21", "T__22", "T__23", "T__24", 
			"T__25", "T__26", "T__27", "T__28"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "';'", "','", "'('", "')'", "'='", "'.'", "'+'", "'::'", "'*'", 
			"'/'", "'%'", "'-'", "'&'", "'^'", "'|'", "'||'", "'~'", "'$'", "'>'", 
			"'<'", "'!'", "'+='", "'-='", "'*='", "'/='", "'%='", "'&='", "'^='", 
			"'|='"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
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


	public SqlParserLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "SqlParser.g4"; }

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
		"\u0004\u0000\u001d\u007f\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002"+
		"\u0001\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002"+
		"\u0004\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002"+
		"\u0007\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002"+
		"\u000b\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e"+
		"\u0002\u000f\u0007\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011"+
		"\u0002\u0012\u0007\u0012\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014"+
		"\u0002\u0015\u0007\u0015\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017"+
		"\u0002\u0018\u0007\u0018\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a"+
		"\u0002\u001b\u0007\u001b\u0002\u001c\u0007\u001c\u0001\u0000\u0001\u0000"+
		"\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003"+
		"\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006"+
		"\u0001\u0007\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001\t\u0001\t\u0001"+
		"\n\u0001\n\u0001\u000b\u0001\u000b\u0001\f\u0001\f\u0001\r\u0001\r\u0001"+
		"\u000e\u0001\u000e\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u0010\u0001"+
		"\u0010\u0001\u0011\u0001\u0011\u0001\u0012\u0001\u0012\u0001\u0013\u0001"+
		"\u0013\u0001\u0014\u0001\u0014\u0001\u0015\u0001\u0015\u0001\u0015\u0001"+
		"\u0016\u0001\u0016\u0001\u0016\u0001\u0017\u0001\u0017\u0001\u0017\u0001"+
		"\u0018\u0001\u0018\u0001\u0018\u0001\u0019\u0001\u0019\u0001\u0019\u0001"+
		"\u001a\u0001\u001a\u0001\u001a\u0001\u001b\u0001\u001b\u0001\u001b\u0001"+
		"\u001c\u0001\u001c\u0001\u001c\u0000\u0000\u001d\u0001\u0001\u0003\u0002"+
		"\u0005\u0003\u0007\u0004\t\u0005\u000b\u0006\r\u0007\u000f\b\u0011\t\u0013"+
		"\n\u0015\u000b\u0017\f\u0019\r\u001b\u000e\u001d\u000f\u001f\u0010!\u0011"+
		"#\u0012%\u0013\'\u0014)\u0015+\u0016-\u0017/\u00181\u00193\u001a5\u001b"+
		"7\u001c9\u001d\u0001\u0000\u0000~\u0000\u0001\u0001\u0000\u0000\u0000"+
		"\u0000\u0003\u0001\u0000\u0000\u0000\u0000\u0005\u0001\u0000\u0000\u0000"+
		"\u0000\u0007\u0001\u0000\u0000\u0000\u0000\t\u0001\u0000\u0000\u0000\u0000"+
		"\u000b\u0001\u0000\u0000\u0000\u0000\r\u0001\u0000\u0000\u0000\u0000\u000f"+
		"\u0001\u0000\u0000\u0000\u0000\u0011\u0001\u0000\u0000\u0000\u0000\u0013"+
		"\u0001\u0000\u0000\u0000\u0000\u0015\u0001\u0000\u0000\u0000\u0000\u0017"+
		"\u0001\u0000\u0000\u0000\u0000\u0019\u0001\u0000\u0000\u0000\u0000\u001b"+
		"\u0001\u0000\u0000\u0000\u0000\u001d\u0001\u0000\u0000\u0000\u0000\u001f"+
		"\u0001\u0000\u0000\u0000\u0000!\u0001\u0000\u0000\u0000\u0000#\u0001\u0000"+
		"\u0000\u0000\u0000%\u0001\u0000\u0000\u0000\u0000\'\u0001\u0000\u0000"+
		"\u0000\u0000)\u0001\u0000\u0000\u0000\u0000+\u0001\u0000\u0000\u0000\u0000"+
		"-\u0001\u0000\u0000\u0000\u0000/\u0001\u0000\u0000\u0000\u00001\u0001"+
		"\u0000\u0000\u0000\u00003\u0001\u0000\u0000\u0000\u00005\u0001\u0000\u0000"+
		"\u0000\u00007\u0001\u0000\u0000\u0000\u00009\u0001\u0000\u0000\u0000\u0001"+
		";\u0001\u0000\u0000\u0000\u0003=\u0001\u0000\u0000\u0000\u0005?\u0001"+
		"\u0000\u0000\u0000\u0007A\u0001\u0000\u0000\u0000\tC\u0001\u0000\u0000"+
		"\u0000\u000bE\u0001\u0000\u0000\u0000\rG\u0001\u0000\u0000\u0000\u000f"+
		"I\u0001\u0000\u0000\u0000\u0011L\u0001\u0000\u0000\u0000\u0013N\u0001"+
		"\u0000\u0000\u0000\u0015P\u0001\u0000\u0000\u0000\u0017R\u0001\u0000\u0000"+
		"\u0000\u0019T\u0001\u0000\u0000\u0000\u001bV\u0001\u0000\u0000\u0000\u001d"+
		"X\u0001\u0000\u0000\u0000\u001fZ\u0001\u0000\u0000\u0000!]\u0001\u0000"+
		"\u0000\u0000#_\u0001\u0000\u0000\u0000%a\u0001\u0000\u0000\u0000\'c\u0001"+
		"\u0000\u0000\u0000)e\u0001\u0000\u0000\u0000+g\u0001\u0000\u0000\u0000"+
		"-j\u0001\u0000\u0000\u0000/m\u0001\u0000\u0000\u00001p\u0001\u0000\u0000"+
		"\u00003s\u0001\u0000\u0000\u00005v\u0001\u0000\u0000\u00007y\u0001\u0000"+
		"\u0000\u00009|\u0001\u0000\u0000\u0000;<\u0005;\u0000\u0000<\u0002\u0001"+
		"\u0000\u0000\u0000=>\u0005,\u0000\u0000>\u0004\u0001\u0000\u0000\u0000"+
		"?@\u0005(\u0000\u0000@\u0006\u0001\u0000\u0000\u0000AB\u0005)\u0000\u0000"+
		"B\b\u0001\u0000\u0000\u0000CD\u0005=\u0000\u0000D\n\u0001\u0000\u0000"+
		"\u0000EF\u0005.\u0000\u0000F\f\u0001\u0000\u0000\u0000GH\u0005+\u0000"+
		"\u0000H\u000e\u0001\u0000\u0000\u0000IJ\u0005:\u0000\u0000JK\u0005:\u0000"+
		"\u0000K\u0010\u0001\u0000\u0000\u0000LM\u0005*\u0000\u0000M\u0012\u0001"+
		"\u0000\u0000\u0000NO\u0005/\u0000\u0000O\u0014\u0001\u0000\u0000\u0000"+
		"PQ\u0005%\u0000\u0000Q\u0016\u0001\u0000\u0000\u0000RS\u0005-\u0000\u0000"+
		"S\u0018\u0001\u0000\u0000\u0000TU\u0005&\u0000\u0000U\u001a\u0001\u0000"+
		"\u0000\u0000VW\u0005^\u0000\u0000W\u001c\u0001\u0000\u0000\u0000XY\u0005"+
		"|\u0000\u0000Y\u001e\u0001\u0000\u0000\u0000Z[\u0005|\u0000\u0000[\\\u0005"+
		"|\u0000\u0000\\ \u0001\u0000\u0000\u0000]^\u0005~\u0000\u0000^\"\u0001"+
		"\u0000\u0000\u0000_`\u0005$\u0000\u0000`$\u0001\u0000\u0000\u0000ab\u0005"+
		">\u0000\u0000b&\u0001\u0000\u0000\u0000cd\u0005<\u0000\u0000d(\u0001\u0000"+
		"\u0000\u0000ef\u0005!\u0000\u0000f*\u0001\u0000\u0000\u0000gh\u0005+\u0000"+
		"\u0000hi\u0005=\u0000\u0000i,\u0001\u0000\u0000\u0000jk\u0005-\u0000\u0000"+
		"kl\u0005=\u0000\u0000l.\u0001\u0000\u0000\u0000mn\u0005*\u0000\u0000n"+
		"o\u0005=\u0000\u0000o0\u0001\u0000\u0000\u0000pq\u0005/\u0000\u0000qr"+
		"\u0005=\u0000\u0000r2\u0001\u0000\u0000\u0000st\u0005%\u0000\u0000tu\u0005"+
		"=\u0000\u0000u4\u0001\u0000\u0000\u0000vw\u0005&\u0000\u0000wx\u0005="+
		"\u0000\u0000x6\u0001\u0000\u0000\u0000yz\u0005^\u0000\u0000z{\u0005=\u0000"+
		"\u0000{8\u0001\u0000\u0000\u0000|}\u0005|\u0000\u0000}~\u0005=\u0000\u0000"+
		"~:\u0001\u0000\u0000\u0000\u0001\u0000\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}