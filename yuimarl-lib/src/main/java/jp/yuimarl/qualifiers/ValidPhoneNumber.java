/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 BitFarm Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package jp.yuimarl.qualifiers;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.regex.Matcher;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import jp.yuimarl.qualifiers.ValidPhoneNumber.PhoneValidator;

/**
 *
 * @author Masahiro Nitta
 */
@Constraint(validatedBy = {PhoneValidator.class})
@Documented
@Target({ElementType.METHOD,
    ElementType.FIELD,
    ElementType.ANNOTATION_TYPE,
    ElementType.CONSTRUCTOR,
    ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPhoneNumber {

    /**
     *
     * @return
     */
    String message() default "{party.phoneNo}";

    /**
     *
     * @return
     */
    Class<?>[] groups() default {};

    /**
     *
     * @return
     */
    Class<? extends Payload>[] payload() default {};

    /**
     *
     */
    @Target({ElementType.METHOD,
        ElementType.FIELD,
        ElementType.ANNOTATION_TYPE,
        ElementType.CONSTRUCTOR,
        ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        /**
         *
         * @return
         */
        ValidPhoneNumber[] value();
    }

    /**
     *
     */
    class PhoneValidator
            implements ConstraintValidator<ValidPhoneNumber, String> {

        /**
         *
         * @param date 日付
         */
        @Override
        public void initialize(final ValidPhoneNumber date) {
        }

        /**
         *
         * @param email メールアドレス
         * @param context ConstraintValidatorContext
         * @return 有効
         */
        @Override
        public boolean isValid(final String email,
                final ConstraintValidatorContext context) {
            if (email == null || email.equals("")) {
                return true;
            }
            java.util.regex.Pattern p = java.util.regex.Pattern
                    .compile("^\\d{2,4}-\\d{2,4}-\\d{4}$");
            Matcher m = p.matcher(email);
            return m.find();
        }
    }

}
