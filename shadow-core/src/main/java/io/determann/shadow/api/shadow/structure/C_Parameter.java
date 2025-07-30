package io.determann.shadow.api.shadow.structure;

import io.determann.shadow.api.dsl.parameter.*;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.modifier.C_FinalModifiable;
import io.determann.shadow.api.shadow.modifier.C_Modifier;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrEmpty;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.dsl.Dsl.parameter;
import static java.util.Collections.emptyList;

/// Parameter of a method or constructor
///
/// @see io.determann.shadow.api.Operations#EXECUTABLE_GET_PARAMETERS
public interface C_Parameter
      extends C_Variable,
              C_FinalModifiable,
              ParameterRenderable
{
   @Override
   default String renderDeclaration(RenderingContext renderingContext)
   {
      ParameterAnnotateStep annotateStep = parameter()
            .annotate(requestOrEmpty(this, ANNOTATIONABLE_GET_DIRECT_ANNOTATION_USAGES).orElse(emptyList()));

      ParameterTypeStep parameterTypeStep = annotateStep;
      if (requestOrEmpty(this, MODIFIABLE_HAS_MODIFIER, C_Modifier.STATIC).orElse(false))
      {
         parameterTypeStep = annotateStep.final_();
      }

      ParameterNameStep nameStep = parameterTypeStep.type(requestOrThrow(this, VARIABLE_GET_TYPE));
      ParameterVarargsStep varargsStep = nameStep.name(requestOrThrow(this, NAMEABLE_GET_NAME));

      if (!requestOrEmpty(this, PARAMETER_IS_VAR_ARGS).orElse(false))
      {
         return varargsStep.renderDeclaration(renderingContext);
      }
      return varargsStep.varArgs().renderDeclaration(renderingContext);
   }
}
