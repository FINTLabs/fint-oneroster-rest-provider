package no.fint.oneroster.filter.operation;

import no.fint.oneroster.filter.operand.Operand;

public class BooleanOperation implements Operation {

    public Boolean eq(Operand leftOperand, Operand rightOperand) {
        return leftOperand.getObjectValue() == rightOperand.getBooleanValue();
    }

    public Boolean ne(Operand leftOperand, Operand rightOperand) {
        return leftOperand.getObjectValue() != rightOperand.getBooleanValue();
    }

    public Boolean gt(Operand leftOperand, Operand rightOperand) {
        return false;
    }

    public Boolean lt(Operand leftOperand, Operand rightOperand) {
        return false;
    }

    public Boolean ge(Operand leftOperand, Operand rightOperand) {
        return false;
    }

    public Boolean le(Operand leftOperand, Operand rightOperand) {
        return false;
    }

    public Boolean co(Operand leftOperand, Operand rightOperand) {
        return false;
    }
}
