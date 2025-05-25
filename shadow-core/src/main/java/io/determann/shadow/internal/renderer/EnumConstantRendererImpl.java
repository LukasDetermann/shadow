package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.renderer.EnumConstantRenderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.structure.C_EnumConstant;

import static io.determann.shadow.api.Operations.ENUM_CONSTANT_GET_SURROUNDING;
import static io.determann.shadow.api.Operations.NAMEABLE_GET_NAME;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.internal.renderer.RenderingContextWrapper.wrap;

public record EnumConstantRendererImpl(C_EnumConstant enumConstant) implements EnumConstantRenderer
{
   public static String declaration(RenderingContextWrapper context, C_EnumConstant enumConstant, String parameters, String content)
   {
      StringBuilder sb = new StringBuilder();

      sb.append(RenderingSupport.annotations(context, enumConstant, '\n'));

      sb.append(requestOrThrow(enumConstant, NAMEABLE_GET_NAME));
      if (!parameters.isEmpty())
      {
         sb.append('(');
         sb.append(parameters);
         sb.append(')');
      }
      if (!content.isEmpty())
      {
         sb.append(" {\n");
         sb.append(content);
         if (!content.endsWith("\n"))
         {
            sb.append('\n');
         }
         sb.append('}');
      }
      sb.append('\n');
      return sb.toString();
   }

   public static String type(RenderingContextWrapper context, C_EnumConstant enumConstant)
   {
      return context.renderName(requestOrThrow(enumConstant, ENUM_CONSTANT_GET_SURROUNDING)) + '.' + requestOrThrow(enumConstant,
                                                                                                                    NAMEABLE_GET_NAME);
   }

   @Override
   public String declaration(RenderingContext renderingContext)
   {
      return declaration(wrap(renderingContext),"");
   }

   @Override
   public String declaration(RenderingContext renderingContext, String parameters)
   {
      return declaration(wrap(renderingContext), parameters, "");
   }

   @Override
   public String declaration(RenderingContext renderingContext, String parameters, String content)
   {
      return declaration(wrap(renderingContext), enumConstant, parameters, content);
   }

   @Override
   public String invocation(RenderingContext renderingContext)
   {
      return type(wrap(renderingContext), enumConstant);
   }
}
