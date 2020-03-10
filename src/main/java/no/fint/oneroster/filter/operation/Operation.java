package no.fint.oneroster.filter.operation;

import no.fint.oneroster.filter.operand.Operand;

public interface Operation {

    Boolean eq(Operand leftOperand, Operand rightOperand);

    Boolean ne(Operand leftOperand, Operand rightOperand);

    Boolean gt(Operand leftOperand, Operand rightOperand);

    Boolean lt(Operand leftOperand, Operand rightOperand);

    Boolean ge(Operand leftOperand, Operand rightOperand);

    Boolean le(Operand leftOperand, Operand rightOperand);

    Boolean co(Operand leftOperand, Operand rightOperand);
}
