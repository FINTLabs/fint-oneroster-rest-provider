package no.fint.oneroster.filter.operand;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.ClassUtils;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Data
public class Operand {
    private Object value;
    private String stringValue;
    private Integer intValue;
    private Boolean booleanValue;
    private LocalDate dateValue;

    public Operand() {
    }

    public void setValue(Object object, String name) {
        Class<?> propertyType;

        try {
            String[] split = name.split("\\.");

            propertyType = PropertyUtils.getPropertyType(object, split[0]);

            if (propertyType.equals(List.class)) {
                /*
                Do something
                 */
            } else {
                if (propertyType.isEnum()) {
                    /*
                    Do something
                     */
                }

                this.value = PropertyUtils.getProperty(object, name);
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
