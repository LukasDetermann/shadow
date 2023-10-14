package io.determann.shadow.impl.renderer;

import io.determann.shadow.api.renderer.ParameterRenderer;
import io.determann.shadow.api.shadow.Parameter;
import io.determann.shadow.impl.annotation_processing.ShadowApiImpl;

import java.util.stream.Collectors;

public class ParameterRendererImpl implements ParameterRenderer
{
   private final Context context;
   private final Parameter parameter;

   public ParameterRendererImpl(Parameter parameter)
   {
      this.context = ((ShadowApiImpl) parameter.getApi()).getRenderingContext();
      this.parameter = parameter;
   }

   public static String declaration(Context context, Parameter parameter)
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
           .append(parameter.getSimpleName());
      }
      else
      {
         sb.append(ShadowRendererImpl.type(context, parameter.getType()))
           .append(' ')
           .append(parameter.getSimpleName());
      }
      return sb.toString();
   }

   @Override
   public String declaration()
   {
      return declaration(context, parameter);
   }
}
