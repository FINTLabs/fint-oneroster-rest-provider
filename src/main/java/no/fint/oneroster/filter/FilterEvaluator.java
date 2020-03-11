package no.fint.oneroster.filter;

import lombok.extern.slf4j.Slf4j;
import no.fint.oneroster.antlr.FilterBaseVisitor;
import no.fint.oneroster.antlr.FilterParser;
import no.fint.oneroster.exception.InvalidFilterException;
import no.fint.oneroster.filter.operand.Operand;
import no.fint.oneroster.filter.operation.*;
import org.apache.commons.beanutils.NestedNullException;
import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.List;

@Slf4j
public class FilterEvaluator extends FilterBaseVisitor<Boolean> {
    private Object givenItem;

    private Operation currentOperation;
    private Operand leftOperand = new Operand();
    private Operand rightOperand = new Operand();

    public FilterEvaluator(Object object) {
        this.givenItem = object;
    }

    @Override
    public Boolean visitQuery(FilterParser.QueryContext ctx) {
        visit(ctx.attrPath());
        visit(ctx.value());

        try {
            switch (ctx.op.getType()) {
                case FilterParser.EQ:
                    return currentOperation.eq(leftOperand, rightOperand);

                case FilterParser.NE:
                    return currentOperation.ne(leftOperand, rightOperand);

                case FilterParser.GT:
                    return currentOperation.gt(leftOperand, rightOperand);

                case FilterParser.LT:
                    return currentOperation.lt(leftOperand, rightOperand);

                case FilterParser.GE:
                    return currentOperation.ge(leftOperand, rightOperand);

                case FilterParser.LE:
                    return currentOperation.le(leftOperand, rightOperand);

                default:
                    return currentOperation.co(leftOperand, rightOperand);
            }

        } catch (Exception ex) {
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
            String[] split = ctx.getText().split("\\.");

            propertyType = PropertyUtils.getPropertyType(givenItem, split[0]);

            if (propertyType == null) {
                throw new InvalidFilterException("Unknown field: " + ctx.getText());
            }

            if (propertyType.equals(List.class)) {
                /*
                Do something...
                 */
            } else {
                Object value = PropertyUtils.getProperty(givenItem, ctx.getText());
                leftOperand.setObjectValue(value);
            }

            return true;

        } catch (NestedNullException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new InvalidFilterException("Unknown field: {}" + ctx.getText());
        }
    }

    @Override
    public Boolean visitBoolean(FilterParser.BooleanContext ctx) {
        currentOperation = new BooleanOperation();

        String text = ctx.getText();
        Boolean value = Boolean.parseBoolean(text.substring(1, text.length() - 1));
        rightOperand.setBooleanValue(value);

        return true;
    }

    @Override
    public Boolean visitDate(FilterParser.DateContext ctx) {
        currentOperation = new DateOperation();

        String text = ctx.getText();
        LocalDate value = LocalDate.parse(text.substring(1, text.length() - 1));
        rightOperand.setDateValue(value);

        return true;
    }

    @Override
    public Boolean visitInteger(FilterParser.IntegerContext ctx) {
        currentOperation = new IntegerOperation();

        String text = ctx.getText();
        Integer value = Integer.parseInt(text.substring(1, text.length() - 1));
        rightOperand.setIntegerValue(value);

        return true;
    }

    @Override
    public Boolean visitString(FilterParser.StringContext ctx) {
        currentOperation = new StringOperation();

        String text = ctx.getText();
        String value = text.substring(1, text.length() - 1);
        rightOperand.setStringValue(value);

        return true;
    }
}
