package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.renderer.ParameterRenderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.Parameter;

import java.util.stream.Collectors;

import static io.determann.shadow.meta_meta.Operations.NAMEABLE_NAME;
import static io.determann.shadow.meta_meta.Provider.requestOrThrow;

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

      if (parameter.isVarArgs())
      {
         sb.append(ShadowRendererImpl.type(context, parameter.getType()))
           .append("... ")
           .append(requestOrThrow(parameter, NAMEABLE_NAME));
      }
      else
      {
         sb.append(ShadowRendererImpl.type(context, parameter.getType()))
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
