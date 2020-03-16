package no.fint.oneroster.filter;

import lombok.extern.slf4j.Slf4j;
import no.fint.oneroster.antlr.FilterBaseVisitor;
import no.fint.oneroster.antlr.FilterParser;
import no.fint.oneroster.exception.NoSuchFieldException;
import org.apache.commons.beanutils.NestedNullException;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;

@Slf4j
public class FilterEvaluator extends FilterBaseVisitor<Boolean> {
    private final Object object;

    private Object leftOperand;
    private Object rightOperand;

    public FilterEvaluator(Object object) {
        this.object = object;
    }

    @Override
    public Boolean visitQuery(FilterParser.QueryContext ctx) {
        visit(ctx.attrPath());
        visit(ctx.value());

        try {
            switch (ctx.op.getType()) {
                case FilterParser.EQ:
                    return Operation.eq(leftOperand, rightOperand);

                case FilterParser.NE:
                    return Operation.ne(leftOperand, rightOperand);

                case FilterParser.GT:
                    return Operation.gt(leftOperand, rightOperand);

                case FilterParser.LT:
                    return Operation.lt(leftOperand, rightOperand);

                case FilterParser.GE:
                    return Operation.ge(leftOperand, rightOperand);

                case FilterParser.LE:
                    return Operation.le(leftOperand, rightOperand);

                default:
                    return Operation.co(leftOperand, rightOperand);
            }

        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Boolean visitLogical(FilterParser.LogicalContext ctx) {
        Boolean leftExp = visit(ctx.query(0));

        if (ctx.LOGICAL_OPERATOR() == null) {
            return leftExp;
        }

        if (leftExp) {
            if (ctx.LOGICAL_OPERATOR().getText().equals("OR")) {
                return true;
            } else {
                return visit(ctx.query(1));
            }

        } else {
            if (ctx.LOGICAL_OPERATOR().getText().equals("AND")) {
                return false;
            } else {
                return visit(ctx.query(1));
            }
        }
    }

    @Override
    public Boolean visitAttrPath(FilterParser.AttrPathContext ctx) {
        try {
            leftOperand = PropertyUtils.getProperty(object, ctx.getText());

            return leftOperand != null;

        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new NoSuchFieldException();
        } catch (NestedNullException e) {
            return false;
        }
    }

    @Override
    public Boolean visitSubAttr(FilterParser.SubAttrContext ctx) {
        return super.visitSubAttr(ctx);
    }

    @Override
    public Boolean visitValue(FilterParser.ValueContext ctx) {
        rightOperand = StringUtils.substringBetween(ctx.getText(), "'");

        return true;
    }
}
