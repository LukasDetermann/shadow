package com.derivandi.api.generate;

import com.derivandi.api.Ap;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/// A Typesafe [Ap.AnnotationUsage] will be generated for annotated Annotations.
/// For external Annotations see [GenerateTypesafeUsageFor]
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.ANNOTATION_TYPE)
public @interface GenerateTypesafeUsage {}