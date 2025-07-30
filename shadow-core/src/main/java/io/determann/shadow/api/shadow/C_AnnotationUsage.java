package io.determann.shadow.api.shadow;

import io.determann.shadow.api.ImplementationDefined;
import io.determann.shadow.api.dsl.Dsl;
import io.determann.shadow.api.dsl.annotation_usage.AnnotationUsageNameStep;
import io.determann.shadow.api.dsl.annotation_usage.AnnotationUsageRenderable;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.structure.C_Method;
import io.determann.shadow.api.shadow.type.C_Annotation;

import java.util.Map;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrThrow;

/**
 * {@link C_Annotation} represents the java file for the java concept of an annotation. This on the other hand represents
 * a usage of such an annotation. like <br>
 * {@code @Documented("testValue) public class Test{ }}
 */
public interface C_AnnotationUsage
      extends ImplementationDefined,
              AnnotationUsageRenderable {

   @Override
   default String renderDeclaration(RenderingContext renderingContext)
   {
      AnnotationUsageNameStep nameStep = Dsl.annotationUsage().type(requestOrThrow(this, ANNOTATION_USAGE_GET_ANNOTATION));

      for (Map.Entry<? extends C_Method, ? extends C_AnnotationValue> entry : requestOrThrow(this, ANNOTATION_USAGE_GET_VALUES).entrySet())
      {
         nameStep = nameStep.name(requestOrThrow(((C_Method) entry), NAMEABLE_GET_NAME))
                            .value(entry.getValue());
      }
      return nameStep.renderDeclaration(renderingContext);
   }
}
