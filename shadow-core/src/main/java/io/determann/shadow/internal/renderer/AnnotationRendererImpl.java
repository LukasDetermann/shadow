package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.Operations;
import io.determann.shadow.api.renderer.AnnotationRenderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.C_AnnotationUsage;
import io.determann.shadow.api.shadow.modifier.C_Modifier;
import io.determann.shadow.api.shadow.type.C_Annotation;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static io.determann.shadow.api.Operations.MODIFIABLE_GET_MODIFIERS;
import static io.determann.shadow.api.Operations.NAMEABLE_GET_NAME;
import static io.determann.shadow.api.Provider.requestOrEmpty;
import static io.determann.shadow.api.Provider.requestOrThrow;

public class AnnotationRendererImpl implements AnnotationRenderer
{

   private final RenderingContextWrapper context;
   private final C_Annotation annotation;

   public AnnotationRendererImpl(RenderingContext renderingContext, C_Annotation annotation)
   {
      this.context = new RenderingContextWrapper(renderingContext);
      this.annotation = annotation;
   }

   public static String declaration(RenderingContextWrapper context, C_Annotation annotation, String content)
   {
      StringBuilder sb = new StringBuilder();


      //noinspection OptionalContainsCollection
      Optional<List<? extends C_AnnotationUsage>> annotationUsages = requestOrEmpty(annotation, Operations.ANNOTATIONABLE_GET_DIRECT_ANNOTATION_USAGES);
      if (!annotationUsages.map(List::isEmpty).orElse(true))
      {
         sb.append(annotationUsages.get()
                             .stream()
                             .map(usage -> AnnotationUsageRendererImpl.usage(context, usage) + "\n")
                             .collect(Collectors.joining()));
      }

      Set<C_Modifier> modifiers = new HashSet<>(requestOrThrow(annotation, MODIFIABLE_GET_MODIFIERS));
      modifiers.remove(C_Modifier.ABSTRACT);
      modifiers.remove(C_Modifier.PACKAGE_PRIVATE);

      if (!modifiers.isEmpty())
      {
         sb.append(ModifierRendererImpl.render(modifiers));
         sb.append(' ');
      }
      sb.append("@interface");
      sb.append(' ');
      sb.append(requestOrThrow(annotation, NAMEABLE_GET_NAME));
      sb.append(" {");
      if (!content.isEmpty())
      {
         sb.append('\n');
         sb.append(content);
         if (!content.endsWith("\n"))
         {
            sb.append('\n');
         }
      }
      sb.append('}');
      sb.append('\n');

      return sb.toString();
   }

   public static String type(RenderingContextWrapper context, C_Annotation annotation)
   {
      return context.renderName(annotation);
   }

   @Override
   public String declaration()
   {
      return declaration("");
   }

   @Override
   public String declaration(String content)
   {
      return declaration(context, annotation, content);
   }

   @Override
   public String type()
   {
      return type(context, annotation);
   }
}
