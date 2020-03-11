package no.fint.oneroster.filter.operation;

import no.fint.oneroster.filter.operand.Operand;

import java.time.LocalDate;
import java.time.ZonedDateTime;

public class DateOperation implements Operation {

    @Override
    public Boolean eq(Operand leftOperand, Operand rightOperand) {
        return ZonedDateTime.parse(leftOperand.getObjectValue().toString()).toLocalDate().isEqual(rightOperand.getDateValue());
    }

    @Override
    public Boolean ne(Operand leftOperand, Operand rightOperand) {
        return !ZonedDateTime.parse(leftOperand.getObjectValue().toString()).toLocalDate().isEqual(rightOperand.getDateValue());

    }

    @Override
    public Boolean gt(Operand leftOperand, Operand rightOperand) {
        return ZonedDateTime.parse(leftOperand.getObjectValue().toString()).toLocalDate().compareTo(rightOperand.getDateValue()) > 0;
    }

    @Override
    public Boolean lt(Operand leftOperand, Operand rightOperand) {
        return ZonedDateTime.parse(leftOperand.getObjectValue().toString()).toLocalDate().compareTo(rightOperand.getDateValue()) < 0;
    }

    @Override
    public Boolean ge(Operand leftOperand, Operand rightOperand) {
        return ZonedDateTime.parse(leftOperand.getObjectValue().toString()).toLocalDate().compareTo(rightOperand.getDateValue()) >= 0;
    }

    @Override
    public Boolean le(Operand leftOperand, Operand rightOperand) {
        return ZonedDateTime.parse(leftOperand.getObjectValue().toString()).toLocalDate().compareTo(rightOperand.getDateValue()) <= 0;
    }

    @Override
    public Boolean co(Operand leftOperand, Operand rightOperand) {
        throw new UnsupportedOperationException("co is not a supported operator on dates.");
    }
}
