package no.fint.oneroster.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.Year;
import java.time.ZonedDateTime;
import java.util.List;

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
            return ZonedDateTime.parse(leftOperand.toString()).isEqual(ZonedDateTime.parse(rightOperand.toString()));

        } else if (leftOperand instanceof Year) {
            return Year.parse(leftOperand.toString()).equals(Year.parse(rightOperand.toString()));

        } else if (leftOperand instanceof Enum) {
            return rightOperand.toString().equalsIgnoreCase(((Enum<?>) leftOperand).name());

        } else if (leftOperand instanceof List) {
            return ((List<?>) leftOperand).containsAll(StringUtils.commaDelimitedListToSet(rightOperand.toString()));

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
            return !ZonedDateTime.parse(leftOperand.toString()).isEqual(ZonedDateTime.parse(rightOperand.toString()));

        } else if (leftOperand instanceof Year) {
            return !Year.parse(leftOperand.toString()).equals(Year.parse(rightOperand.toString()));

        } else if (leftOperand instanceof Enum) {
            return !rightOperand.toString().equalsIgnoreCase(((Enum<?>) leftOperand).name());

        } else if (leftOperand instanceof List) {
            return !((List<?>) leftOperand).containsAll(StringUtils.commaDelimitedListToSet(rightOperand.toString()));

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
            return ZonedDateTime.parse(leftOperand.toString()).compareTo(ZonedDateTime.parse(rightOperand.toString())) > 0;

        } else if (leftOperand instanceof Year) {
            return Year.parse(leftOperand.toString()).compareTo(Year.parse(rightOperand.toString())) > 0;

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
            return ZonedDateTime.parse(leftOperand.toString()).compareTo(ZonedDateTime.parse(rightOperand.toString())) < 0;

        } else if (leftOperand instanceof Year) {
            return Year.parse(leftOperand.toString()).compareTo(Year.parse(rightOperand.toString())) < 0;

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
            return ZonedDateTime.parse(leftOperand.toString()).compareTo(ZonedDateTime.parse(rightOperand.toString())) >= 0;

        } else if (leftOperand instanceof Year) {
            return Year.parse(leftOperand.toString()).compareTo(Year.parse(rightOperand.toString())) >= 0;

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
            return ZonedDateTime.parse(leftOperand.toString()).compareTo(ZonedDateTime.parse(rightOperand.toString())) <= 0;

        } else if (leftOperand instanceof Year) {
            return Year.parse(leftOperand.toString()).compareTo(Year.parse(rightOperand.toString())) <= 0;

        } else {
            return false;
        }
    }

    public static Boolean co(Object leftOperand, Object rightOperand) {
        if (leftOperand instanceof String) {
            return leftOperand.toString().toLowerCase().contains(rightOperand.toString().toLowerCase());

        } else if (leftOperand instanceof List) {
            return ((List<?>) leftOperand).stream()
                    .anyMatch(item -> StringUtils.commaDelimitedListToSet(rightOperand.toString()).contains(item.toString()));

        } else {
            return false;
        }
    }
}
