package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.renderer.ParameterRenderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.structure.C_Parameter;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.internal.renderer.RenderingContextWrapper.wrap;

public record ParameterRendererImpl(C_Parameter parameter) implements ParameterRenderer
{
   public static String declaration(RenderingContextWrapper context, C_Parameter parameter)
   {
      StringBuilder sb = new StringBuilder();

      sb.append(RenderingSupport.annotations(context, parameter, ' '));

      if (requestOrThrow(parameter, PARAMETER_IS_VAR_ARGS))
      {
         sb.append(TypeRendererImpl.type(context, requestOrThrow(parameter, VARIABLE_GET_TYPE)))
           .append("... ")
           .append(requestOrThrow(parameter, NAMEABLE_GET_NAME));
      }
      else
      {
         sb.append(TypeRendererImpl.type(context, requestOrThrow(parameter, VARIABLE_GET_TYPE)))
           .append(' ')
           .append(requestOrThrow(parameter, NAMEABLE_GET_NAME));
      }
      return sb.toString();
   }

   @Override
   public String declaration(RenderingContext renderingContext)
   {
      return declaration(wrap(renderingContext), parameter);
   }
}
