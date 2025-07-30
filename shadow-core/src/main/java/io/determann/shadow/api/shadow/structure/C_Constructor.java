package io.determann.shadow.api.shadow.structure;

import io.determann.shadow.api.dsl.constructor.*;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.modifier.C_AccessModifiable;
import io.determann.shadow.api.shadow.type.C_Class;
import io.determann.shadow.api.shadow.type.C_Declared;
import io.determann.shadow.api.shadow.type.C_Enum;
import io.determann.shadow.api.shadow.type.C_Record;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrEmpty;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.dsl.Dsl.constructor;

public interface C_Constructor
      extends C_Executable,
              C_AccessModifiable,
              ConstructorRenderable
{

   @Override
   default String renderDeclaration(RenderingContext renderingContext)
   {
      ConstructorAnnotateStep annotateStep = constructor();

      ConstructorModifierStep modifierStep = requestOrEmpty(this, ANNOTATIONABLE_GET_DIRECT_ANNOTATION_USAGES).map(annotateStep::annotate).orElse(annotateStep);
      ConstructorGenericStep genericStep = requestOrEmpty(this, MODIFIABLE_GET_MODIFIERS).map(modifierStep::modifier).orElse(modifierStep);
      genericStep = requestOrEmpty(this, EXECUTABLE_GET_GENERICS).map(genericStep::generic).orElse(genericStep);

      C_Declared type = requestOrThrow(this, EXECUTABLE_GET_SURROUNDING);
      ConstructorReceiverStep receiverStep = switch (type)
      {
         case C_Class cClass -> genericStep.type(cClass);
         case C_Enum cEnum -> genericStep.type(cEnum);
         case C_Record cRecord -> genericStep.type(cRecord);
         default -> throw new IllegalStateException("Unexpected value: " + type);
      };

      ConstructorParameterStep parameterStep = requestOrEmpty(this, EXECUTABLE_GET_RECEIVER)
            .map(receiverStep::receiver)
            .orElse(receiverStep);

      ConstructorThrowsStep throwsStep = parameterStep.parameter(requestOrThrow(this, EXECUTABLE_GET_PARAMETERS));
      throwsStep = requestOrEmpty(this, EXECUTABLE_GET_THROWS).map(throwsStep::throws_).orElse(throwsStep);
      return throwsStep.renderDeclaration(renderingContext);
   }
}
