package org.brownmun.util;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String>
{
    @Override
    public void initialize(PhoneNumber constraintAnnotation)
    {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context)
    {
        if (value == null)
        {
            return false;
        }

        try
        {
            Phonenumber.PhoneNumber number = PhoneNumberUtil.getInstance().parse(value, "US");
            return PhoneNumberUtil.getInstance().isValidNumber(number);
        }
        catch (NumberParseException e)
        {
            return false;
        }
    }
}
