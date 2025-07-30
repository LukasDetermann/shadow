package io.determann.shadow.api.shadow.structure;

import io.determann.shadow.api.dsl.field.FieldInitializationStep;
import io.determann.shadow.api.dsl.field.FieldRenderable;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.C_Documented;
import io.determann.shadow.api.shadow.modifier.C_AccessModifiable;
import io.determann.shadow.api.shadow.modifier.C_FinalModifiable;
import io.determann.shadow.api.shadow.modifier.C_StaticModifiable;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrEmpty;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.dsl.Dsl.field;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;

public interface C_Field
      extends C_Variable,
              C_AccessModifiable,
              C_FinalModifiable,
              C_StaticModifiable,
              C_Documented,
              FieldRenderable
{
   @Override
   default String renderDeclaration(RenderingContext renderingContext)
   {
      FieldInitializationStep initializationStep = field()
            .annotate(requestOrEmpty(this, ANNOTATIONABLE_GET_DIRECT_ANNOTATION_USAGES).orElse(emptyList()))
            .modifier(requestOrEmpty(this, MODIFIABLE_GET_MODIFIERS).orElse(emptySet()))
            .type(requestOrThrow(this, VARIABLE_GET_TYPE))
            .name(requestOrThrow(this, NAMEABLE_GET_NAME));

      if (!requestOrEmpty(this, FIELD_IS_CONSTANT).orElse(false))
      {
         return initializationStep.renderDeclaration(renderingContext);
      }
      return initializationStep.initializer(requestOrThrow(this, FIELD_GET_CONSTANT_VALUE).toString()).renderDeclaration(renderingContext);
   }
}