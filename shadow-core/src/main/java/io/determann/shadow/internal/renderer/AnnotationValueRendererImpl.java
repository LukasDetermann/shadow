package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.Operations;
import io.determann.shadow.api.Provider;
import io.determann.shadow.api.dsl.Dsl;
import io.determann.shadow.api.renderer.AnnotationValueRenderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.C_AnnotationUsage;
import io.determann.shadow.api.shadow.C_AnnotationValue;
import io.determann.shadow.api.shadow.structure.C_EnumConstant;
import io.determann.shadow.api.shadow.type.C_Type;

import java.util.List;

public class AnnotationValueRendererImpl
      implements AnnotationValueRenderer
{
   private final C_AnnotationValue annotationValue;

   public AnnotationValueRendererImpl(C_AnnotationValue annotationValue)
   {
      this.annotationValue = annotationValue;
   }

   @Override
   public String declaration(RenderingContext renderingContext)
   {
      Object value = Provider.requestOrThrow(annotationValue, Operations.ANNOTATION_VALUE_GET_VALUE);

      return (switch (value)
              {
                 case String s -> Dsl.annotationValue(s);
                 case Boolean b -> Dsl.annotationValue(b);
                 case Byte b -> Dsl.annotationValue(b);
                 case Short v -> Dsl.annotationValue(v);
                 case Integer v -> Dsl.annotationValue(v);
                 case Long v -> Dsl.annotationValue(v);
                 case Character v -> Dsl.annotationValue(v);
                 case Float v -> Dsl.annotationValue(v);
                 case Double v -> Dsl.annotationValue(v);
                 case C_EnumConstant v -> Dsl.annotationValue(v);
                 case C_Type v -> Dsl.annotationValue(v);
                 case C_AnnotationUsage v -> Dsl.annotationValue(v);
                 case List<?> v ->
                    //noinspection unchecked
                       Dsl.annotationValue(((List<C_AnnotationValue>) v));
                 default -> throw new IllegalStateException("Unexpected value: " + value);
              }).render(renderingContext);
   }
}
