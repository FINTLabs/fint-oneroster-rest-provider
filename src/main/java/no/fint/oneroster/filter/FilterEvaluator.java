package no.fint.oneroster.filter;

import lombok.extern.slf4j.Slf4j;
import no.fint.oneroster.antlr.FilterBaseVisitor;
import no.fint.oneroster.antlr.FilterParser;
import no.fint.oneroster.filter.operand.Operand;
import no.fint.oneroster.filter.operation.*;

import java.time.LocalDate;

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

                case FilterParser.CO:
                    return currentOperation.co(leftOperand, rightOperand);

                default:
                    throw new IllegalStateException("Unsupported operator detected.");
            }

        } catch (Exception ex) {
            throw new RuntimeException("Unable to execute the query.", ex);
        }
    }

    @Override
    public Boolean visitLogical(FilterParser.LogicalContext ctx) {
        return super.visitLogical(ctx);
    }

    @Override
    public Boolean visitAttrPath(FilterParser.AttrPathContext ctx) {
        leftOperand.setValue(givenItem, ctx.getText());

        return true;
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

        if (text.length() > 2) {
            LocalDate value = LocalDate.parse(text.substring(1, text.length() - 1));
            rightOperand.setDateValue(value);

        } else {
            rightOperand.setStringValue("");
        }

        return super.visitDate(ctx);
    }

    @Override
    public Boolean visitInteger(FilterParser.IntegerContext ctx) {
        currentOperation = new IntegerOperation();

        String text = ctx.getText();

        if (text.length() > 2) {
            Integer value = Integer.parseInt(text.substring(1, text.length() - 1));
            rightOperand.setIntValue(value);

        } else {
            rightOperand.setStringValue("");
        }

        return super.visitInteger(ctx);
    }

    @Override
    public Boolean visitString(FilterParser.StringContext ctx) {
        currentOperation = new StringOperation();

        String text = ctx.getText();

        if (text.length() > 2) {
            rightOperand.setStringValue(text.substring(1, text.length() - 1));

        } else {
            rightOperand.setStringValue("");
        }

        return true;
    }
}
