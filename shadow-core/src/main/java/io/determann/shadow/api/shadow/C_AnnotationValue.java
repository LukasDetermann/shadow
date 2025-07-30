package io.determann.shadow.api.shadow;

import io.determann.shadow.api.ImplementationDefined;
import io.determann.shadow.api.dsl.Dsl;
import io.determann.shadow.api.dsl.annotation_value.AnnotationValueRenderable;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.structure.C_EnumConstant;
import io.determann.shadow.api.shadow.type.C_Type;

import java.util.List;

import static io.determann.shadow.api.Operations.ANNOTATION_VALUE_GET_VALUE;
import static io.determann.shadow.api.Provider.requestOrThrow;

public interface C_AnnotationValue
      extends ImplementationDefined,
              AnnotationValueRenderable
{

   @Override
   default String render(RenderingContext renderingContext)
   {
      Object object = requestOrThrow(this, ANNOTATION_VALUE_GET_VALUE);

      return (switch (object)
              {
                 case Character c -> Dsl.annotationValue(c);
                 case String s -> Dsl.annotationValue(s);
                 case Boolean b -> Dsl.annotationValue(b);
                 case Byte b -> Dsl.annotationValue(b);
                 case Short s -> Dsl.annotationValue(s);
                 case Integer i -> Dsl.annotationValue(i);
                 case Long l -> Dsl.annotationValue(l);
                 case Float f -> Dsl.annotationValue(f);
                 case C_EnumConstant e -> Dsl.annotationValue(e);
                 case C_Type t -> Dsl.annotationValue(t);
                 case C_AnnotationUsage a -> Dsl.annotationValue(a);
                 case List<?> l ->
                    //noinspection unchecked
                       Dsl.annotationValue((List<AnnotationValueRenderable>) l);

                 default -> throw new IllegalStateException("Unexpected value: " + object);
              }).render(renderingContext);
   }
}
