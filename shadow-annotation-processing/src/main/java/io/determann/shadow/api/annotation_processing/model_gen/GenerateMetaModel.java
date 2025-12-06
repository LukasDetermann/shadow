package io.determann.shadow.api.annotation_processing.model_gen;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/// A Meta-Model will be generated for annotated Annotations.
/// For external Annotations see [GenerateMetaModelFor]
///
/// A Meta-Model Provides Type safety when accessing AnnotationValues
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.ANNOTATION_TYPE)
public @interface GenerateMetaModel {}