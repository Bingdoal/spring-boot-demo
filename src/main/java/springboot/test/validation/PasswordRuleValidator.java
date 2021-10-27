package springboot.test.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordRuleValidator implements ConstraintValidator<PasswordRule, String> {
    private PasswordRule passwordRule;

    @Override
    public void initialize(PasswordRule constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.passwordRule = constraintAnnotation;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        int numberCount = 0;
        int lowerCount = 0;
        int upperCount = 0;
        int nonAlphaNumCount = 0;
        for (int i = 0; i < value.length(); i++) {
            char ch = value.charAt(i);
            if (Character.isDigit(ch)) {
                numberCount++;
            } else if (Character.isUpperCase(ch)) {
                upperCount++;
            } else if (Character.isLowerCase(ch)) {
                lowerCount++;
            } else {
                nonAlphaNumCount++;
            }
        }
        return numberCount >= passwordRule.number() &&
                lowerCount >= passwordRule.lowerLetter() &&
                upperCount >= passwordRule.upperLetter() &&
                nonAlphaNumCount >= passwordRule.nonAlphaNum();
    }
}
