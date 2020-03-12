package no.fint.oneroster.filter;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.ZonedDateTime;

@Slf4j
public class Operation {

    public static Boolean eq(Object leftOperand, Object rightOperand) {
        if (leftOperand instanceof String) {
            return leftOperand.toString().equalsIgnoreCase(rightOperand.toString());

        } else if (leftOperand instanceof Boolean) {
            return Boolean.parseBoolean(leftOperand.toString()) == Boolean.parseBoolean(rightOperand.toString());

        } else if (leftOperand instanceof LocalDate) {
            return LocalDate.parse(leftOperand.toString()).isEqual(LocalDate.parse(rightOperand.toString()));

        } else if (leftOperand instanceof ZonedDateTime) {
            return ZonedDateTime.parse(leftOperand.toString()).toLocalDate().isEqual(LocalDate.parse(rightOperand.toString()));

        } else if (leftOperand instanceof Integer) {
            return Integer.parseInt(leftOperand.toString()) == Integer.parseInt(rightOperand.toString());

        } else if (leftOperand instanceof Enum) {
            /*
            Do something...
             */
            return false;

        } else {
            return false;
        }
    }

    public static Boolean ne(Object leftOperand, Object rightOperand) {
        if (leftOperand instanceof String) {
            return !leftOperand.toString().equalsIgnoreCase(rightOperand.toString());

        } else if (leftOperand instanceof Boolean) {
            return !Boolean.parseBoolean(leftOperand.toString()) == Boolean.parseBoolean(rightOperand.toString());

        } else if (leftOperand instanceof LocalDate) {
            return !LocalDate.parse(leftOperand.toString()).isEqual(LocalDate.parse(rightOperand.toString()));

        } else if (leftOperand instanceof ZonedDateTime) {
            return !ZonedDateTime.parse(leftOperand.toString()).toLocalDate().isEqual(LocalDate.parse(rightOperand.toString()));

        } else if (leftOperand instanceof Integer) {
            return Integer.parseInt(leftOperand.toString()) != Integer.parseInt(rightOperand.toString());

        } else if (leftOperand instanceof Enum) {
            /*
            Do something...
             */
            return false;

        } else {
            return false;
        }
    }

    public static Boolean gt(Object leftOperand, Object rightOperand) {
        if (leftOperand instanceof String) {
            return leftOperand.toString().compareToIgnoreCase(rightOperand.toString()) > 0;

        } else if (leftOperand instanceof LocalDate) {
            return LocalDate.parse(leftOperand.toString()).compareTo(LocalDate.parse(rightOperand.toString())) > 0;

        } else if (leftOperand instanceof ZonedDateTime) {
            return ZonedDateTime.parse(leftOperand.toString()).toLocalDate().compareTo(LocalDate.parse(rightOperand.toString())) > 0;

        } else if (leftOperand instanceof Integer) {
            return Integer.parseInt(leftOperand.toString()) > Integer.parseInt(rightOperand.toString());

        } else if (leftOperand instanceof Enum) {
            /*
            Do something...
             */
            return false;

        } else {
            return false;
        }
    }

    public static Boolean lt(Object leftOperand, Object rightOperand) {
        if (leftOperand instanceof String) {
            return leftOperand.toString().compareToIgnoreCase(rightOperand.toString()) < 0;

        } else if (leftOperand instanceof LocalDate) {
            return LocalDate.parse(leftOperand.toString()).compareTo(LocalDate.parse(rightOperand.toString())) < 0;

        } else if (leftOperand instanceof ZonedDateTime) {
            return ZonedDateTime.parse(leftOperand.toString()).toLocalDate().compareTo(LocalDate.parse(rightOperand.toString())) < 0;

        } else if (leftOperand instanceof Integer) {
            return Integer.parseInt(leftOperand.toString()) < Integer.parseInt(rightOperand.toString());

        } else if (leftOperand instanceof Enum) {
            /*
            Do something...
             */
            return false;

        } else {
            return false;
        }
    }

    public static Boolean ge(Object leftOperand, Object rightOperand) {
        if (leftOperand instanceof String) {
            return leftOperand.toString().compareToIgnoreCase(rightOperand.toString()) >= 0;

        } else if (leftOperand instanceof LocalDate) {
            return LocalDate.parse(leftOperand.toString()).compareTo(LocalDate.parse(rightOperand.toString())) >= 0;

        } else if (leftOperand instanceof ZonedDateTime) {
            return ZonedDateTime.parse(leftOperand.toString()).toLocalDate().compareTo(LocalDate.parse(rightOperand.toString())) >= 0;

        } else if (leftOperand instanceof Integer) {
            return Integer.parseInt(leftOperand.toString()) >= Integer.parseInt(rightOperand.toString());

        } else if (leftOperand instanceof Enum) {
            /*
            Do something...
             */
            return false;

        } else {
            return false;
        }
    }

    public static Boolean le(Object leftOperand, Object rightOperand) {
        if (leftOperand instanceof String) {
            return leftOperand.toString().compareToIgnoreCase(rightOperand.toString()) <= 0;

        } else if (leftOperand instanceof LocalDate) {
            return LocalDate.parse(leftOperand.toString()).compareTo(LocalDate.parse(rightOperand.toString())) <= 0;

        } else if (leftOperand instanceof ZonedDateTime) {
            return ZonedDateTime.parse(leftOperand.toString()).toLocalDate().compareTo(LocalDate.parse(rightOperand.toString())) <= 0;

        } else if (leftOperand instanceof Integer) {
            return Integer.parseInt(leftOperand.toString()) <= Integer.parseInt(rightOperand.toString());

        } else if (leftOperand instanceof Enum) {
            /*
            Do something...
             */
            return false;

        } else {
            return false;
        }
    }

    public static Boolean co(Object leftOperand, Object rightOperand) {
        if (leftOperand instanceof String) {
            return leftOperand.toString().contains(rightOperand.toString());

        } else {
            return false;
        }
    }
}
