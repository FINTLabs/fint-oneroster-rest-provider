package no.fint.oneroster.filter.operation;

import no.fint.oneroster.filter.operand.Operand;

public interface Operation {

    Boolean eq(Operand leftOperand, Operand rightOperand) throws ClassCastException;

    Boolean ne(Operand leftOperand, Operand rightOperand) throws ClassCastException;

    Boolean gt(Operand leftOperand, Operand rightOperand) throws ClassCastException;

    Boolean lt(Operand leftOperand, Operand rightOperand) throws ClassCastException;

    Boolean ge(Operand leftOperand, Operand rightOperand) throws ClassCastException;

    Boolean le(Operand leftOperand, Operand rightOperand) throws ClassCastException;

    Boolean co(Operand leftOperand, Operand rightOperand) throws ClassCastException;
}
