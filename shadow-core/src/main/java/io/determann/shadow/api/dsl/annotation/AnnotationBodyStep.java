package io.determann.shadow.api.dsl.annotation;

import io.determann.shadow.api.shadow.C_AnnotationValue;
import io.determann.shadow.api.shadow.structure.C_Field;
import io.determann.shadow.api.shadow.structure.C_Method;
import io.determann.shadow.api.shadow.type.C_Declared;

public interface AnnotationBodyStep
      extends AnnotationRenderable
{
   AnnotationRenderable body(String body);

   AnnotationBodyStep field(String... fields);

   AnnotationBodyStep field(C_Field... fields);

   AnnotationBodyStep method(String... methods);

   AnnotationBodyStep method(C_Method... methods);

   AnnotationBodyStep method(C_Method method, C_AnnotationValue defaultValue);

   AnnotationBodyStep inner(String... inner);

   AnnotationBodyStep inner(C_Declared... inner);
}
