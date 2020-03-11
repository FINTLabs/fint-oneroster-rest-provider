package no.fint.oneroster.filter.operand;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Slf4j
@Data
public class Operand {
    private Object objectValue;
    private String stringValue;
    private Integer integerValue;
    private Boolean booleanValue;
    private LocalDate dateValue;
}
