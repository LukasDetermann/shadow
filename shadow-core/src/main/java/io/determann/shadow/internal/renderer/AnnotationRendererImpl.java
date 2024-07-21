package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.renderer.AnnotationRenderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.modifier.Modifier;
import io.determann.shadow.api.shadow.type.Annotation;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static io.determann.shadow.api.shadow.Operations.MODIFIABLE_GET_MODIFIERS;
import static io.determann.shadow.api.shadow.Operations.NAMEABLE_GET_NAME;
import static io.determann.shadow.api.shadow.Provider.requestOrThrow;

public class AnnotationRendererImpl implements AnnotationRenderer
{

   private final RenderingContextWrapper context;
   private final Annotation annotation;

   public AnnotationRendererImpl(RenderingContext renderingContext, Annotation annotation)
   {
      this.context = new RenderingContextWrapper(renderingContext);
      this.annotation = annotation;
   }

   public static String declaration(RenderingContextWrapper context, Annotation annotation, String content)
   {
      StringBuilder sb = new StringBuilder();


      if (!annotation.getDirectAnnotationUsages().isEmpty())
      {
         sb.append(annotation.getDirectAnnotationUsages()
                             .stream()
                             .map(usage -> AnnotationUsageRendererImpl.usage(context, usage) + "\n")
                             .collect(Collectors.joining()));
      }

      Set<Modifier> modifiers = new HashSet<>(requestOrThrow(annotation, MODIFIABLE_GET_MODIFIERS));
      modifiers.remove(Modifier.ABSTRACT);
      modifiers.remove(Modifier.PACKAGE_PRIVATE);

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

   public static String type(RenderingContextWrapper context, Annotation annotation)
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
