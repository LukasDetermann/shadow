package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.renderer.GenericRenderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.AnnotationUsage;
import io.determann.shadow.api.shadow.Operations;
import io.determann.shadow.api.shadow.type.Declared;
import io.determann.shadow.api.shadow.type.Generic;
import io.determann.shadow.api.shadow.type.Shadow;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static io.determann.shadow.api.shadow.Operations.*;
import static io.determann.shadow.api.shadow.Provider.requestOrEmpty;
import static io.determann.shadow.api.shadow.Provider.requestOrThrow;

public class GenericRendererImpl implements GenericRenderer
{
   private final RenderingContextWrapper context;
   private final Generic generic;

   public GenericRendererImpl(RenderingContext renderingContext, Generic generic)
   {
      this.context = new RenderingContextWrapper(renderingContext);
      this.generic = generic;
   }

   public static String type(RenderingContextWrapper context, Generic generic)
   {
      StringBuilder sb = new StringBuilder();

      //noinspection OptionalContainsCollection
      Optional<List<? extends AnnotationUsage>> annotationUsages = requestOrEmpty(generic, Operations.ANNOTATIONABLE_GET_DIRECT_ANNOTATION_USAGES);
      if (!annotationUsages.map(List::isEmpty).orElse(true))
      {
         sb.append(annotationUsages.get()
                          .stream()
                          .map(usage -> AnnotationUsageRendererImpl.usage(context, usage) + " ")
                          .collect(Collectors.joining()));
      }

      if (context.isRenderNestedGenerics())
      {
         context.setRenderNestedGenerics(false);

         Shadow aExtends = requestOrThrow(generic, GENERIC_GET_EXTENDS);
         if (aExtends instanceof Declared declared && "java.lang.Object".equals(requestOrThrow(declared, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME)))
         {
            sb.append(requestOrThrow(generic, NAMEABLE_GET_NAME));
         }
         else
         {
            sb.append(requestOrThrow(generic, NAMEABLE_GET_NAME)).append(" extends ").append(ShadowRendererImpl.type(context, aExtends));
         }
         context.setRenderNestedGenerics(true);
      }
      else
      {
         sb.append(requestOrThrow(generic, NAMEABLE_GET_NAME));
      }
      return sb.toString();
   }

   @Override
   public String declaration()
   {
      return type(context, generic);
   }

   @Override
   public String type()
   {
      return requestOrThrow(generic, NAMEABLE_GET_NAME);
   }
}
