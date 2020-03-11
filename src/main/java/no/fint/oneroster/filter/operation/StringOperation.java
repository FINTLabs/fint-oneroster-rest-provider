package no.fint.oneroster.filter.operation;

import lombok.extern.slf4j.Slf4j;
import no.fint.oneroster.filter.operand.Operand;

@Slf4j
public class StringOperation implements Operation {

    public Boolean eq(Operand leftOperand, Operand rightOperand)  {
        return ((String) leftOperand.getObjectValue()).equalsIgnoreCase(rightOperand.getStringValue());
    }

    public Boolean ne(Operand leftOperand, Operand rightOperand) {
        return !((String) leftOperand.getObjectValue()).equalsIgnoreCase(rightOperand.getStringValue());
    }

    public Boolean gt(Operand leftOperand, Operand rightOperand) {
        return ((String) leftOperand.getObjectValue()).compareToIgnoreCase(rightOperand.getStringValue()) > 0;
    }

    public Boolean lt(Operand leftOperand, Operand rightOperand) {
        return ((String) leftOperand.getObjectValue()).compareToIgnoreCase(rightOperand.getStringValue()) < 0;
    }

    public Boolean ge(Operand leftOperand, Operand rightOperand) {
        return ((String) leftOperand.getObjectValue()).compareToIgnoreCase(rightOperand.getStringValue()) >= 0;
    }

    public Boolean le(Operand leftOperand, Operand rightOperand) {
        return ((String) leftOperand.getObjectValue()).compareToIgnoreCase(rightOperand.getStringValue()) <= 0;
    }

    public Boolean co(Operand leftOperand, Operand rightOperand) {
        return ((String) leftOperand.getObjectValue()).contains(rightOperand.getStringValue());
    }
}
