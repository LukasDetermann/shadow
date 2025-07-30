package io.determann.shadow.api.shadow.type;

import io.determann.shadow.api.dsl.generic.GenericExtendsStep;
import io.determann.shadow.api.dsl.generic.GenericRenderable;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.C_Annotationable;
import io.determann.shadow.api.shadow.C_Erasable;
import io.determann.shadow.api.shadow.C_Nameable;

import java.util.Optional;

import static io.determann.shadow.api.Operations.GENERIC_GET_BOUND;
import static io.determann.shadow.api.Operations.NAMEABLE_GET_NAME;
import static io.determann.shadow.api.Provider.requestOrEmpty;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.dsl.Dsl.generic;

/**
 * represents the generic parameter at a class, method, constructor etc.
 */
public interface C_Generic
      extends C_ReferenceType,
              C_Nameable,
              C_Annotationable,
              C_Erasable,
              GenericRenderable
{
   @Override
   default String renderDeclaration(RenderingContext renderingContext)
   {
      GenericExtendsStep extendsStep = generic().name(requestOrThrow(this, NAMEABLE_GET_NAME));

      Optional<C_Type> extendsType = requestOrEmpty(this, GENERIC_GET_BOUND);
      if (extendsType.isPresent())
      {
         return (switch (extendsType.get())
                 {
                    case C_Class cClass -> extendsStep.extends_(cClass);
                    case C_Interface cInterface -> extendsStep.extends_(cInterface);
                    case C_Generic generic -> extendsStep.extends_(generic);
                    default -> throw new IllegalStateException();
                 }).renderDeclaration(renderingContext);
      }
      return extendsStep.renderDeclaration(renderingContext);
   }

   @Override
   default String renderType(RenderingContext renderingContext)
   {
      return renderName(renderingContext);
   }

   @Override
   default String renderName(RenderingContext renderingContext)
   {
      return requestOrThrow(this, NAMEABLE_GET_NAME);
   }
}