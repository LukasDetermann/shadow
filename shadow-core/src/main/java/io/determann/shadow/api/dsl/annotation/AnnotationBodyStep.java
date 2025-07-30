package io.determann.shadow.api.dsl.annotation;

import io.determann.shadow.api.dsl.annotation_value.AnnotationValueRenderable;
import io.determann.shadow.api.dsl.declared.DeclaredRenderable;
import io.determann.shadow.api.dsl.field.FieldRenderable;
import io.determann.shadow.api.dsl.method.MethodRenderable;

import java.util.Arrays;
import java.util.List;

public interface AnnotationBodyStep
      extends AnnotationRenderable
{
   AnnotationRenderable body(String body);

   AnnotationBodyStep field(String... fields);

   default AnnotationBodyStep field(FieldRenderable... fields)
   {
      return field(Arrays.asList(fields));
   }

   AnnotationBodyStep field(List<? extends FieldRenderable> fields);

   AnnotationBodyStep method(String... methods);

   default AnnotationBodyStep method(MethodRenderable... methods)
   {
      return method(Arrays.asList(methods));
   }

   AnnotationBodyStep method(List<? extends MethodRenderable> methods);

   AnnotationBodyStep method(MethodRenderable method, AnnotationValueRenderable defaultValue);

   AnnotationBodyStep inner(String... inner);

   default AnnotationBodyStep inner(DeclaredRenderable... inner)
   {
      return inner(Arrays.asList(inner));
   }

   AnnotationBodyStep inner(List<? extends DeclaredRenderable> inner);
}
