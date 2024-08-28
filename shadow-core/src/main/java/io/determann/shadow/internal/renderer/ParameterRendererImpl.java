package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.Operations;
import io.determann.shadow.api.renderer.ParameterRenderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.C_AnnotationUsage;
import io.determann.shadow.api.shadow.structure.C_Parameter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrEmpty;
import static io.determann.shadow.api.Provider.requestOrThrow;

public class ParameterRendererImpl implements ParameterRenderer
{
   private final RenderingContextWrapper context;
   private final C_Parameter parameter;

   public ParameterRendererImpl(RenderingContext renderingContext, C_Parameter parameter)
   {
      this.context = new RenderingContextWrapper(renderingContext);
      this.parameter = parameter;
   }

   public static String declaration(RenderingContextWrapper context, C_Parameter parameter)
   {
      StringBuilder sb = new StringBuilder();

      //noinspection OptionalContainsCollection
      Optional<List<? extends C_AnnotationUsage>> annotationUsages = requestOrEmpty(parameter, Operations.ANNOTATIONABLE_GET_DIRECT_ANNOTATION_USAGES);
      if (!annotationUsages.map(List::isEmpty).orElse(true))
      {
         sb.append(annotationUsages.get()
                            .stream()
                            .map(usage -> AnnotationUsageRendererImpl.usage(context, usage) + " ")
                            .collect(Collectors.joining()));
      }

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
   public String declaration()
   {
      return declaration(context, parameter);
   }
}
