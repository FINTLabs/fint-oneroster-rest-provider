package no.fint.oneroster.filter.operation;

import no.fint.oneroster.filter.operand.Operand;

import java.time.LocalDate;

public class DateOperation implements Operation {

    @Override
    public Boolean eq(Operand leftOperand, Operand rightOperand) {
        return LocalDate.parse(leftOperand.getObjectValue().toString()).isEqual(rightOperand.getDateValue());
    }

    @Override
    public Boolean ne(Operand leftOperand, Operand rightOperand) {
        return !LocalDate.parse(leftOperand.getObjectValue().toString()).isEqual(rightOperand.getDateValue());

    }

    @Override
    public Boolean gt(Operand leftOperand, Operand rightOperand) {
        return LocalDate.parse(leftOperand.getObjectValue().toString()).compareTo(rightOperand.getDateValue()) > 0;
    }

    @Override
    public Boolean lt(Operand leftOperand, Operand rightOperand) {
        return LocalDate.parse(leftOperand.getObjectValue().toString()).compareTo(rightOperand.getDateValue()) < 0;
    }

    @Override
    public Boolean ge(Operand leftOperand, Operand rightOperand) {
        return LocalDate.parse(leftOperand.getObjectValue().toString()).compareTo(rightOperand.getDateValue()) >= 0;
    }

    @Override
    public Boolean le(Operand leftOperand, Operand rightOperand) {
        return LocalDate.parse(leftOperand.getObjectValue().toString()).compareTo(rightOperand.getDateValue()) <= 0;
    }

    @Override
    public Boolean co(Operand leftOperand, Operand rightOperand) {
        throw new UnsupportedOperationException("co is not a supported operator on dates.");
    }
}
