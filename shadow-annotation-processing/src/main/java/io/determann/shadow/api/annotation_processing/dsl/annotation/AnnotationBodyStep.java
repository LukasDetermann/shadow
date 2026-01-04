package io.determann.shadow.api.annotation_processing.dsl.annotation;

import io.determann.shadow.api.annotation_processing.dsl.annotation_value.AnnotationValueRenderable;
import io.determann.shadow.api.annotation_processing.dsl.declared.DeclaredRenderable;
import io.determann.shadow.api.annotation_processing.dsl.field.FieldRenderable;
import io.determann.shadow.api.annotation_processing.dsl.method.MethodRenderable;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.List;

public interface AnnotationBodyStep
      extends AnnotationRenderable
{
   @Contract(value = "_ -> new", pure = true)
   AnnotationRenderable body(String body);

   @Contract(value = "_ -> new", pure = true)
   AnnotationBodyStep field(String... fields);

   @Contract(value = "_ -> new", pure = true)
   default AnnotationBodyStep field(FieldRenderable... fields)
   {
      return field(Arrays.asList(fields));
   }

   @Contract(value = "_ -> new", pure = true)
   AnnotationBodyStep field(List<? extends FieldRenderable> fields);

   @Contract(value = "_ -> new", pure = true)
   AnnotationBodyStep method(String... methods);

   @Contract(value = "_ -> new", pure = true)
   default AnnotationBodyStep method(MethodRenderable... methods)
   {
      return method(Arrays.asList(methods));
   }

   @Contract(value = "_ -> new", pure = true)
   AnnotationBodyStep method(List<? extends MethodRenderable> methods);

   AnnotationBodyStep method(MethodRenderable method, AnnotationValueRenderable defaultValue);

   @Contract(value = "_ -> new", pure = true)
   AnnotationBodyStep inner(String... inner);

   default AnnotationBodyStep inner(DeclaredRenderable... inner)
   {
      return inner(Arrays.asList(inner));
   }

   @Contract(value = "_ -> new", pure = true)
   AnnotationBodyStep inner(List<? extends DeclaredRenderable> inner);
}
