package org.sbcoba.springboot.multidatasource.autoconfigure.aop;

import org.springframework.aop.support.AopUtils;
import org.springframework.aop.support.annotation.AnnotationMethodMatcher;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Annotation Matcher
 *
 * @author sbcoba
 */
public class AnnotationDeepMethodMatcher extends AnnotationMethodMatcher {
    private final Class<? extends Annotation> annotationType;

    public AnnotationDeepMethodMatcher(Class<? extends Annotation> annotationType) {
        super(annotationType);
        this.annotationType = annotationType;
    }

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        Annotation annotation = AnnotationUtils.findAnnotation(method, annotationType);
        if (annotation != null) {
            return true;
        }

        Method specificMethod = AopUtils.getMostSpecificMethod(method, targetClass);
        annotation = AnnotationUtils.findAnnotation(specificMethod, annotationType);
        if (annotation != null) {
            return true;
        }

        annotation = AnnotationUtils.findAnnotation(targetClass, annotationType);
        if (annotation != null) {
            return true;
        }

        annotation = AnnotationUtils.findAnnotation(targetClass.getPackage(), annotationType);
        if (annotation != null) {
            return true;
        }

        return false;
    }

}