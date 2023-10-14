package io.determann.shadow.impl.renderer;

import io.determann.shadow.api.renderer.GenericRenderer;
import io.determann.shadow.api.shadow.Generic;
import io.determann.shadow.impl.annotation_processing.ShadowApiImpl;

import java.util.stream.Collectors;

public class GenericRendererImpl implements GenericRenderer
{
   private final Generic generic;
   private final Context context;

   public GenericRendererImpl(Generic generic)
   {
      this.context = ((ShadowApiImpl) generic.getApi()).getRenderingContext();
      this.generic = generic;
   }

   public static String type(Context context, Generic generic)
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
