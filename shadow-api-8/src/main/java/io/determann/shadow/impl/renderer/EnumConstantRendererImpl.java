package io.determann.shadow.impl.renderer;

import io.determann.shadow.api.renderer.EnumConstantRenderer;
import io.determann.shadow.api.shadow.EnumConstant;
import io.determann.shadow.impl.annotation_processing.ShadowApiImpl;

import java.util.stream.Collectors;

public class EnumConstantRendererImpl implements EnumConstantRenderer
{
   private final Context context;
   private final EnumConstant enumConstant;

   public EnumConstantRendererImpl(EnumConstant enumConstant)
   {
      this.context = ((ShadowApiImpl) enumConstant.getApi()).getRenderingContext();
      this.enumConstant = enumConstant;
   }

   public static String declaration(Context context, EnumConstant enumConstant, String parameters, String content)
   {
      StringBuilder sb = new StringBuilder();

      if (!enumConstant.getDirectAnnotationUsages().isEmpty())
      {
         sb.append(enumConstant.getDirectAnnotationUsages()
                               .stream()
                               .map(usage -> AnnotationUsageRendererImpl.usage(context, usage) + "\n")
                               .collect(Collectors.joining()));
      }
      sb.append(enumConstant.getSimpleName());
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

   public static String type(Context context, EnumConstant enumConstant)
   {
      return context.renderName(enumConstant.getSurrounding()) + '.' + enumConstant.getSimpleName();
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
