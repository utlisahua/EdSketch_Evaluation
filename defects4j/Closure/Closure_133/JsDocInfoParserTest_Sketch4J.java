package com.google.javascript.jscomp.parsing;

import java.util.Set;

import com.google.javascript.jscomp.parsing.Config.LanguageMode;
import com.google.javascript.jscomp.testing.TestErrorReporter;
import com.google.javascript.rhino.InputId;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.head.Token.CommentType;
import com.google.javascript.rhino.head.ast.Comment;
import com.google.javascript.rhino.jstype.SimpleSourceFile;
import com.google.javascript.rhino.jstype.StaticSourceFile;
import com.google.javascript.rhino.testing.BaseJSTypeTestCase;

import sketch4j.generator.SketchRequest;

public class JsDocInfoParserTest_Sketch4J extends BaseJSTypeTestCase {
	private Set<String> extraAnnotations;
	private Set<String> extraSuppressions;
	private Node.FileLevelJsDocBuilder fileLevelJsDocBuilder = null;

	public void testTextExtents() {
		parse("@return {@code foo} bar \n *    baz. */", true,
				"Bad type annotation. type not recognized due to syntax error");
	}

	private JSDocInfo parse(String comment, boolean parseDocumentation, String... warnings) {
		return parse(comment, parseDocumentation, false, warnings);
	}

	private JsDocTokenStream stream(String source) {
		return new JsDocTokenStream(source, 0);
	}

	private JSDocInfo parse(String comment, boolean parseDocumentation, boolean parseFileOverview, String... warnings) {
		TestErrorReporter errorReporter = new TestErrorReporter(null, warnings);

		Config config = new Config(extraAnnotations, extraSuppressions, parseDocumentation, LanguageMode.ECMASCRIPT3,
				false);
		StaticSourceFile file = new SimpleSourceFile("testcode", false);
		Node associatedNode = new Node(Token.SCRIPT);
		associatedNode.setInputId(new InputId(file.getName()));
		associatedNode.setStaticSourceFile(file);
		JsDocInfoParser jsdocParser = new JsDocInfoParser(stream(comment),
				new Comment(0, 0, CommentType.JSDOC, comment), associatedNode, config, errorReporter);

		if (fileLevelJsDocBuilder != null) {
			jsdocParser.setFileLevelJsDocBuilder(fileLevelJsDocBuilder);
		}

		jsdocParser.parse();

		if(! errorReporter.hasEncounteredAllWarnings()) throw new RuntimeException("Test Failure");

		if (parseFileOverview) {
			return jsdocParser.getFileOverviewJSDocInfo();
		} else {
			return jsdocParser.retrieveAndResetParsedJSDocInfo();
		}
	}

	public static void main(String[] arg) {
		JsDocInfoParserTest_Sketch4J driver = new JsDocInfoParserTest_Sketch4J();
		try{
		driver.testTextExtents();
		}catch (NullPointerException npe) {
			npe.printStackTrace();
			System.out.println( "*****BACKTRACKING: null pointer exception");
			SketchRequest.backtrack();
		} catch (RuntimeException re) {
			SketchRequest.backtrack();
		} catch (Exception e) {
			SketchRequest.backtrack();
		}
		throw new RuntimeException("****FOUND FIRST SOLUTION");
	}
	
}
