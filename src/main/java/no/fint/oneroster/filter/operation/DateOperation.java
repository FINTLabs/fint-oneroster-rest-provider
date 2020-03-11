package no.fint.oneroster.filter.operation;

import lombok.extern.slf4j.Slf4j;
import no.fint.oneroster.filter.operand.Operand;

import java.time.LocalDate;
import java.time.ZonedDateTime;

@Slf4j
public class DateOperation implements Operation {

    @Override
    public Boolean eq(Operand leftOperand, Operand rightOperand) {
        if (leftOperand.getObjectValue() instanceof ZonedDateTime) {
            return ZonedDateTime.parse(leftOperand.getObjectValue().toString()).toLocalDate().isEqual(rightOperand.getDateValue());
        }

        return LocalDate.parse(leftOperand.getObjectValue().toString()).isEqual(rightOperand.getDateValue());
    }

    @Override
    public Boolean ne(Operand leftOperand, Operand rightOperand) {
        if (leftOperand.getObjectValue() instanceof ZonedDateTime) {
            return !ZonedDateTime.parse(leftOperand.getObjectValue().toString()).toLocalDate().isEqual(rightOperand.getDateValue());
        }

        return !LocalDate.parse(leftOperand.getObjectValue().toString()).isEqual(rightOperand.getDateValue());
    }

    @Override
    public Boolean gt(Operand leftOperand, Operand rightOperand) {
        if (leftOperand.getObjectValue() instanceof ZonedDateTime) {
            return ZonedDateTime.parse(leftOperand.getObjectValue().toString()).toLocalDate().compareTo(rightOperand.getDateValue()) > 0;
        }

        return LocalDate.parse(leftOperand.getObjectValue().toString()).compareTo(rightOperand.getDateValue()) > 0;
    }

    @Override
    public Boolean lt(Operand leftOperand, Operand rightOperand) {
        if (leftOperand.getObjectValue() instanceof ZonedDateTime) {
            return ZonedDateTime.parse(leftOperand.getObjectValue().toString()).toLocalDate().compareTo(rightOperand.getDateValue()) < 0;
        }

        return LocalDate.parse(leftOperand.getObjectValue().toString()).compareTo(rightOperand.getDateValue()) < 0;
    }

    @Override
    public Boolean ge(Operand leftOperand, Operand rightOperand) {
        if (leftOperand.getObjectValue() instanceof ZonedDateTime) {
            return ZonedDateTime.parse(leftOperand.getObjectValue().toString()).toLocalDate().compareTo(rightOperand.getDateValue()) >= 0;
        }

        return LocalDate.parse(leftOperand.getObjectValue().toString()).compareTo(rightOperand.getDateValue()) >= 0;
    }

    @Override
    public Boolean le(Operand leftOperand, Operand rightOperand) {
        if (leftOperand.getObjectValue() instanceof ZonedDateTime) {
            return ZonedDateTime.parse(leftOperand.getObjectValue().toString()).toLocalDate().compareTo(rightOperand.getDateValue()) <= 0;
        }

        return LocalDate.parse(leftOperand.getObjectValue().toString()).compareTo(rightOperand.getDateValue()) <= 0;
    }

    @Override
    public Boolean co(Operand leftOperand, Operand rightOperand) {
        return false;
    }
}
