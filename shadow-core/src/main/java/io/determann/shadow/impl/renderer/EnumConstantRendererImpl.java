package io.determann.shadow.impl.renderer;

import io.determann.shadow.api.renderer.EnumConstantRenderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.EnumConstant;

import java.util.stream.Collectors;

public class EnumConstantRendererImpl implements EnumConstantRenderer
{
   private final RenderingContextWrapper context;
   private final EnumConstant enumConstant;

   public EnumConstantRendererImpl(RenderingContext renderingContext, EnumConstant enumConstant)
   {
      this.context = new RenderingContextWrapper(renderingContext);
      this.enumConstant = enumConstant;
   }

   public static String declaration(RenderingContextWrapper context, EnumConstant enumConstant, String parameters, String content)
   {
      StringBuilder sb = new StringBuilder();

      if (!enumConstant.getDirectAnnotationUsages().isEmpty())
      {
         sb.append(enumConstant.getDirectAnnotationUsages()
                               .stream()
                               .map(usage -> AnnotationUsageRendererImpl.usage(context, usage) + "\n")
                               .collect(Collectors.joining()));
      }
      sb.append(enumConstant.getName());
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

   public static String type(RenderingContextWrapper context, EnumConstant enumConstant)
   {
      return context.renderName(enumConstant.getSurrounding()) + '.' + enumConstant.getName();
   }

   @Override
   public String declaration()
   {
      return declaration("");
   }

   @Override
   public String declaration(String parameters)
   {
      return declaration(parameters, "");
   }

   @Override
   public String declaration(String parameters, String content)
   {
      return declaration(context, enumConstant, parameters, content);
   }

   @Override
   public String invocation()
   {
      return type(context, enumConstant);
   }
}
