package io.determann.shadow.api.annotation_processing.generate;

import io.determann.shadow.api.annotation_processing.Ap;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/// A Typesafe [Ap.AnnotationUsage] will be generated for annotated Annotations.
/// For external Annotations see [GenerateTypesafeUsageFor]
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.ANNOTATION_TYPE)
public @interface GenerateTypesafeUsage {}