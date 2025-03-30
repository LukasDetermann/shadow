package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.renderer.GenericRenderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.type.C_Declared;
import io.determann.shadow.api.shadow.type.C_Generic;
import io.determann.shadow.api.shadow.type.C_Type;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrThrow;

public class GenericRendererImpl implements GenericRenderer
{
   private final C_Generic generic;

   public GenericRendererImpl(C_Generic generic)
   {
      this.generic = generic;
   }

   public static String type(RenderingContextWrapper context, C_Generic generic)
   {
      StringBuilder sb = new StringBuilder();

      sb.append(RenderingSupport.annotations(context, generic, ' '));

      if (context.isRenderNestedGenerics())
      {
         context.setRenderNestedGenerics(false);

         C_Type aExtends = requestOrThrow(generic, GENERIC_GET_EXTENDS);
         if (aExtends instanceof C_Declared declared && "java.lang.Object".equals(requestOrThrow(declared, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME)))
         {
            sb.append(requestOrThrow(generic, NAMEABLE_GET_NAME));
         }
         else
         {
            sb.append(requestOrThrow(generic, NAMEABLE_GET_NAME)).append(" extends ").append(TypeRendererImpl.type(context, aExtends));
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
   public String declaration(RenderingContext renderingContext)
   {
      return type(new RenderingContextWrapper(renderingContext), generic);
   }

   @Override
   public String type(RenderingContext renderingContext)
   {
      return requestOrThrow(generic, NAMEABLE_GET_NAME);
   }
}
