// Generated from /Users/torleif/Documents/GitHub/fint-oneroster-rest-provider/src/main/resources/Filter.g4 by ANTLR 4.8
package no.fint.oneroster.antlr;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link FilterParser}.
 */
public interface FilterListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link FilterParser#query}.
	 * @param ctx the parse tree
	 */
	void enterQuery(FilterParser.QueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link FilterParser#query}.
	 * @param ctx the parse tree
	 */
	void exitQuery(FilterParser.QueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link FilterParser#logical}.
	 * @param ctx the parse tree
	 */
	void enterLogical(FilterParser.LogicalContext ctx);
	/**
	 * Exit a parse tree produced by {@link FilterParser#logical}.
	 * @param ctx the parse tree
	 */
	void exitLogical(FilterParser.LogicalContext ctx);
	/**
	 * Enter a parse tree produced by {@link FilterParser#attrPath}.
	 * @param ctx the parse tree
	 */
	void enterAttrPath(FilterParser.AttrPathContext ctx);
	/**
	 * Exit a parse tree produced by {@link FilterParser#attrPath}.
	 * @param ctx the parse tree
	 */
	void exitAttrPath(FilterParser.AttrPathContext ctx);
	/**
	 * Enter a parse tree produced by {@link FilterParser#subAttr}.
	 * @param ctx the parse tree
	 */
	void enterSubAttr(FilterParser.SubAttrContext ctx);
	/**
	 * Exit a parse tree produced by {@link FilterParser#subAttr}.
	 * @param ctx the parse tree
	 */
	void exitSubAttr(FilterParser.SubAttrContext ctx);
	/**
	 * Enter a parse tree produced by the {@code boolean}
	 * labeled alternative in {@link FilterParser#value}.
	 * @param ctx the parse tree
	 */
	void enterBoolean(FilterParser.BooleanContext ctx);
	/**
	 * Exit a parse tree produced by the {@code boolean}
	 * labeled alternative in {@link FilterParser#value}.
	 * @param ctx the parse tree
	 */
	void exitBoolean(FilterParser.BooleanContext ctx);
	/**
	 * Enter a parse tree produced by the {@code date}
	 * labeled alternative in {@link FilterParser#value}.
	 * @param ctx the parse tree
	 */
	void enterDate(FilterParser.DateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code date}
	 * labeled alternative in {@link FilterParser#value}.
	 * @param ctx the parse tree
	 */
	void exitDate(FilterParser.DateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code integer}
	 * labeled alternative in {@link FilterParser#value}.
	 * @param ctx the parse tree
	 */
	void enterInteger(FilterParser.IntegerContext ctx);
	/**
	 * Exit a parse tree produced by the {@code integer}
	 * labeled alternative in {@link FilterParser#value}.
	 * @param ctx the parse tree
	 */
	void exitInteger(FilterParser.IntegerContext ctx);
	/**
	 * Enter a parse tree produced by the {@code string}
	 * labeled alternative in {@link FilterParser#value}.
	 * @param ctx the parse tree
	 */
	void enterString(FilterParser.StringContext ctx);
	/**
	 * Exit a parse tree produced by the {@code string}
	 * labeled alternative in {@link FilterParser#value}.
	 * @param ctx the parse tree
	 */
	void exitString(FilterParser.StringContext ctx);
}