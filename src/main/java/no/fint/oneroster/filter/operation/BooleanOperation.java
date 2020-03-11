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
        throw new UnsupportedOperationException("gt is not a supported operator on booleans.");
    }

    public Boolean lt(Operand leftOperand, Operand rightOperand) {
        throw new UnsupportedOperationException("lt is not a supported operator on booleans.");
    }

    public Boolean ge(Operand leftOperand, Operand rightOperand) {
        throw new UnsupportedOperationException("ge is not a supported operator on booleans.");
    }

    public Boolean le(Operand leftOperand, Operand rightOperand) {
        throw new UnsupportedOperationException("le is not a supported operator on booleans.");
    }

    public Boolean co(Operand leftOperand, Operand rightOperand) {
        throw new UnsupportedOperationException("co is not a supported operator on booleans.");
    }
}
