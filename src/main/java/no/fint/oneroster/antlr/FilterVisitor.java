// Generated from /Users/torleif/Documents/GitHub/fint-oneroster-rest-provider/src/main/resources/Filter.g4 by ANTLR 4.8
package no.fint.oneroster.antlr;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link FilterParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface FilterVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link FilterParser#query}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuery(FilterParser.QueryContext ctx);
	/**
	 * Visit a parse tree produced by {@link FilterParser#logical}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogical(FilterParser.LogicalContext ctx);
	/**
	 * Visit a parse tree produced by {@link FilterParser#attrPath}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttrPath(FilterParser.AttrPathContext ctx);
	/**
	 * Visit a parse tree produced by {@link FilterParser#subAttr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubAttr(FilterParser.SubAttrContext ctx);
	/**
	 * Visit a parse tree produced by the {@code boolean}
	 * labeled alternative in {@link FilterParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoolean(FilterParser.BooleanContext ctx);
	/**
	 * Visit a parse tree produced by the {@code date}
	 * labeled alternative in {@link FilterParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDate(FilterParser.DateContext ctx);
	/**
	 * Visit a parse tree produced by the {@code integer}
	 * labeled alternative in {@link FilterParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInteger(FilterParser.IntegerContext ctx);
	/**
	 * Visit a parse tree produced by the {@code string}
	 * labeled alternative in {@link FilterParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitString(FilterParser.StringContext ctx);
}