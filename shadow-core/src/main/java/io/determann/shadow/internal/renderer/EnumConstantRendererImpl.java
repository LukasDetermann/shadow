package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.renderer.EnumConstantRenderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.AnnotationUsage;
import io.determann.shadow.api.shadow.Operations;
import io.determann.shadow.api.shadow.structure.EnumConstant;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static io.determann.shadow.api.shadow.Operations.ENUM_CONSTANT_GET_SURROUNDING;
import static io.determann.shadow.api.shadow.Operations.NAMEABLE_GET_NAME;
import static io.determann.shadow.api.shadow.Provider.requestOrEmpty;
import static io.determann.shadow.api.shadow.Provider.requestOrThrow;

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

      //noinspection OptionalContainsCollection
      Optional<List<AnnotationUsage>> annotationUsages = requestOrEmpty(enumConstant, Operations.ANNOTATIONABLE_GET_DIRECT_ANNOTATION_USAGES);
      if (!annotationUsages.map(List::isEmpty).orElse(true))
      {
         sb.append(annotationUsages.get()
                               .stream()
                               .map(usage -> AnnotationUsageRendererImpl.usage(context, usage) + "\n")
                               .collect(Collectors.joining()));
      }
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

   public static String type(RenderingContextWrapper context, EnumConstant enumConstant)
   {
      return context.renderName(requestOrThrow(enumConstant, ENUM_CONSTANT_GET_SURROUNDING)) + '.' + requestOrThrow(enumConstant,
                                                                                                                    NAMEABLE_GET_NAME);
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
