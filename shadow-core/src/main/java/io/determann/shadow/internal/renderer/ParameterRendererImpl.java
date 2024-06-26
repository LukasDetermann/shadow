package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.renderer.ParameterRenderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.structure.Parameter;

import java.util.stream.Collectors;

import static io.determann.shadow.api.shadow.Operations.*;
import static io.determann.shadow.api.shadow.Provider.requestOrThrow;

public class ParameterRendererImpl implements ParameterRenderer
{
   private final RenderingContextWrapper context;
   private final Parameter parameter;

   public ParameterRendererImpl(RenderingContext renderingContext, Parameter parameter)
   {
      this.context = new RenderingContextWrapper(renderingContext);
      this.parameter = parameter;
   }

   public static String declaration(RenderingContextWrapper context, Parameter parameter)
   {
      StringBuilder sb = new StringBuilder();

      if (!parameter.getDirectAnnotationUsages().isEmpty())
      {
         sb.append(parameter.getDirectAnnotationUsages()
                            .stream()
                            .map(usage -> AnnotationUsageRendererImpl.usage(context, usage) + " ")
                            .collect(Collectors.joining()));
      }

      if (requestOrThrow(parameter, PARAMETER_IS_VAR_ARGS))
      {
         sb.append(ShadowRendererImpl.type(context, requestOrThrow(parameter, VARIABLE_GET_TYPE)))
           .append("... ")
           .append(requestOrThrow(parameter, NAMEABLE_NAME));
      }
      else
      {
         sb.append(ShadowRendererImpl.type(context, requestOrThrow(parameter, VARIABLE_GET_TYPE)))
           .append(' ')
           .append(requestOrThrow(parameter, NAMEABLE_NAME));
      }
      return sb.toString();
   }

   @Override
   public String declaration()
   {
      return declaration(context, parameter);
   }
}
