package no.fint.oneroster.filter.operation;

import lombok.extern.slf4j.Slf4j;
import no.fint.oneroster.filter.operand.Operand;

@Slf4j
public class IntegerOperation implements Operation {

    @Override
    public Boolean eq(Operand leftOperand, Operand rightOperand) {
        if (leftOperand.getValue() == null) return false;

        return ((Integer) leftOperand.getValue()).compareTo(rightOperand.getIntValue()) == 0;
    }

    @Override
    public Boolean ne(Operand leftOperand, Operand rightOperand) {
        if (leftOperand.getValue() == null) return false;

        return ((Integer) leftOperand.getValue()).compareTo(rightOperand.getIntValue()) != 0;
    }

    @Override
    public Boolean gt(Operand leftOperand, Operand rightOperand) {
        if (leftOperand.getValue() == null) return false;

        return ((Integer) leftOperand.getValue()).compareTo(rightOperand.getIntValue()) > 0;
    }

    @Override
    public Boolean lt(Operand leftOperand, Operand rightOperand) {
        if (leftOperand.getValue() == null) return false;

        return ((Integer) leftOperand.getValue()).compareTo(rightOperand.getIntValue()) < 0;
    }

    @Override
    public Boolean ge(Operand leftOperand, Operand rightOperand) {
        if (leftOperand.getValue() == null) return false;

        return ((Integer) leftOperand.getValue()).compareTo(rightOperand.getIntValue()) >= 0;
    }

    @Override
    public Boolean le(Operand leftOperand, Operand rightOperand) {
        if (leftOperand.getValue() == null) return false;

        return ((Integer) leftOperand.getValue()).compareTo(rightOperand.getIntValue()) <= 0;
    }

    @Override
    public Boolean co(Operand leftOperand, Operand rightOperand) {
        if (leftOperand.getValue() == null) return false;

        return null;
    }
}
