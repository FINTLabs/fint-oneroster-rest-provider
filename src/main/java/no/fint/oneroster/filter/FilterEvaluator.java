package no.fint.oneroster.filter;

import lombok.extern.slf4j.Slf4j;
import no.fint.oneroster.antlr.FilterBaseVisitor;
import no.fint.oneroster.antlr.FilterParser;
import no.fint.oneroster.exception.InvalidFilterException;
import org.apache.commons.beanutils.NestedNullException;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Slf4j
public class FilterEvaluator extends FilterBaseVisitor<Boolean> {
    private Object object;

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

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new InvalidFilterException(ex.getMessage());
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
                return leftExp;
            } else {
                return visit(ctx.query(1));
            }

        } else {
            if (ctx.LOGICAL_OPERATOR().getText().equals("AND")) {
                return leftExp;
            } else {
                return visit(ctx.query(1));
            }
        }
    }

    @Override
    public Boolean visitAttrPath(FilterParser.AttrPathContext ctx) {
        Class<?> propertyType;

        try {
            propertyType = PropertyUtils.getPropertyType(object, ctx.ATTRNAME().getText());

            if (propertyType == null) {
                throw new InvalidFilterException("Unknown field: " + ctx.getText());
            }

            if (propertyType.equals(List.class)) {
                /*
                Do something...
                 */
            } else {
                leftOperand = PropertyUtils.getProperty(object, ctx.getText());
            }

            return true;

        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new InvalidFilterException("Unknown field: {}" + ctx.getText());
        } catch (NestedNullException e) {
            throw new InvalidFilterException("Null fields: {}" + ctx.getText());
        }
    }

    @Override
    public Boolean visitSubAttr(FilterParser.SubAttrContext ctx) {
        return super.visitSubAttr(ctx);
    }

    @Override
    public Boolean visitValue(FilterParser.ValueContext ctx) {
        rightOperand = StringUtils.substringBetween(ctx.ANY_CHAR().getText(), "'");

        return true;
    }
}
