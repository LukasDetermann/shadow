package io.determann.shadow.api.annotation_processing.model_gen;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/// Meta-Models will be generated for listed annotations
/// Alternative see [GenerateMetaModel]
///
/// A Meta-Model Provides Type safety when accessing AnnotationValues
/// @see GenerateMetaModel
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface GenerateMetaModelFor {

   /// Annotations a Meta-Model should be generated for
   /// @see GenerateMetaModel
   Class<?>[] value();
}