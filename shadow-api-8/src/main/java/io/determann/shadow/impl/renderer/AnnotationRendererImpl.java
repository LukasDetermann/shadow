package io.determann.shadow.impl.renderer;

import io.determann.shadow.api.modifier.Modifier;
import io.determann.shadow.api.renderer.AnnotationRenderer;
import io.determann.shadow.api.shadow.Annotation;
import io.determann.shadow.impl.annotation_processing.ShadowApiImpl;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class AnnotationRendererImpl implements AnnotationRenderer
{

   private final Context context;
   private final Annotation annotation;

   public AnnotationRendererImpl(Annotation annotation)
   {
      this.context = ((ShadowApiImpl) annotation.getApi()).getRenderingContext();
      this.annotation = annotation;
   }

   public static String declaration(Context context, Annotation annotation, String content)
   {
      StringBuilder sb = new StringBuilder();
      Set<Modifier> modifiers = new HashSet<>(annotation.getModifiers());
      modifiers.remove(Modifier.ABSTRACT);

      if (!annotation.getDirectAnnotationUsages().isEmpty())
      {
         sb.append(annotation.getDirectAnnotationUsages()
                             .stream()
                             .map(usage -> AnnotationUsageRendererImpl.usage(context, usage) + "\n")
                             .collect(Collectors.joining()));
      }
      if (!modifiers.isEmpty())
      {
         sb.append(ModifierRendererImpl.render(modifiers));
         sb.append(' ');
      }
      sb.append("@interface");
      sb.append(' ');
      sb.append(annotation.getSimpleName());
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

   public static String type(Context context, Annotation annotation)
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
