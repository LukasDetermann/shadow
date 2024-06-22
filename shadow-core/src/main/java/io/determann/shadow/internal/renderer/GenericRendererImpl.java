package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.renderer.GenericRenderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.Generic;
import io.determann.shadow.api.shadow.Shadow;

import java.util.stream.Collectors;

import static io.determann.shadow.api.converter.Converter.convert;
import static io.determann.shadow.meta_meta.Operations.*;
import static io.determann.shadow.meta_meta.Provider.requestOrThrow;

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

      if (!generic.getDirectAnnotationUsages().isEmpty())
      {
         sb.append(generic.getDirectAnnotationUsages()
                          .stream()
                          .map(usage -> AnnotationUsageRendererImpl.usage(context, usage) + " ")
                          .collect(Collectors.joining()));
      }

      if (context.isRenderNestedGenerics())
      {
         context.setRenderNestedGenerics(false);

         Shadow aExtends = requestOrThrow(generic, GENERIC_GET_EXTENDS);
         if (convert(aExtends)
               .toDeclared()
               .map(declared -> "java.lang.Object".equals(requestOrThrow(declared, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME)))
               .orElse(false))
         {
            sb.append(requestOrThrow(generic, NAMEABLE_NAME));
         }
         else
         {
            sb.append(requestOrThrow(generic, NAMEABLE_NAME)).append(" extends ").append(ShadowRendererImpl.type(context, aExtends));
         }
         context.setRenderNestedGenerics(true);
      }
      else
      {
         sb.append(requestOrThrow(generic, NAMEABLE_NAME));
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
      return requestOrThrow(generic, NAMEABLE_NAME);
   }
}
