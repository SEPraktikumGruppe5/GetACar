package org.grp5.getacar.persistence.validation;

import org.grp5.getacar.persistence.BaseEntity;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static org.grp5.getacar.persistence.validation.DateBeforeOtherDate.Accuracy;

/**
 *
 */
public class DateBeforeOtherDateValidator implements ConstraintValidator<DateBeforeOtherDate, BaseEntity> {

    private String dateFieldName;
    private String otherDateFieldName;
    private Accuracy accuracy;
    private String message;

    @Override
    public void initialize(DateBeforeOtherDate constraintAnnotation) {
        dateFieldName = constraintAnnotation.date();
        otherDateFieldName = constraintAnnotation.otherDate();
        accuracy = constraintAnnotation.accuracy();
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(BaseEntity value, ConstraintValidatorContext context) {
        // TODO: Implement with joda-time!
//        try {
//            final Calendar calendar = DateTimeHelper.getGermanCalendar();
//
//            Date date = (Date) PropertyUtils.getProperty(value, dateFieldName);
//            if (date != null) {
//                if (Accuracy.DATE.equals(accuracy)) {
//                    calendar.setTime(date);
//                    DateTimeHelper.normalizeMin(calendar);
//                    date = calendar.getTime();
//                }
//            }
//            Date otherDate = (Date) PropertyUtils.getProperty(value, otherDateFieldName);
//            if (otherDate != null) {
//                if (Accuracy.DATE.equals(accuracy)) {
//                    calendar.setTime(otherDate);
//                    DateTimeHelper.normalizeMin(calendar);
//                    otherDate = calendar.getTime();
//                }
//            }
//
//            if (date != null && otherDate != null) {
//                if (!date.before(otherDate)) {
//                    context.buildConstraintViolationWithTemplate(message)
//                            .addNode(dateFieldName)
//                            .addConstraintViolation();
//                    context.buildConstraintViolationWithTemplate(message)
//                            .addNode(otherDateFieldName)
//                            .addConstraintViolation();
//                    return false;
//                }
//            }
//        } catch (Exception e) {
//            // ignore
//        }
        return true;
    }
}