package io.determann.shadow.impl.renderer;

import io.determann.shadow.api.renderer.GenericRenderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.Generic;

import java.util.stream.Collectors;

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

         if (generic.getExtends().representsSameType(generic.getApi().getClassOrThrow("java.lang.Object")))
         {
            sb.append(generic.getSimpleName());
         }
         else
         {
            sb.append(generic.getSimpleName()).append(" extends ").append(ShadowRendererImpl.type(context, generic.getExtends()));
         }
         context.setRenderNestedGenerics(true);
      }
      else
      {
         sb.append(generic.getSimpleName());
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
      return generic.getSimpleName();
   }
}
