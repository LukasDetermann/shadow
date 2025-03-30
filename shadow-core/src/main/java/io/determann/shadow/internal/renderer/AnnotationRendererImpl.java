package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.renderer.AnnotationRenderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.modifier.C_Modifier;
import io.determann.shadow.api.shadow.type.C_Annotation;

import java.util.HashSet;
import java.util.Set;

import static io.determann.shadow.api.Operations.MODIFIABLE_GET_MODIFIERS;
import static io.determann.shadow.api.Operations.NAMEABLE_GET_NAME;
import static io.determann.shadow.api.Provider.requestOrThrow;

public class AnnotationRendererImpl implements AnnotationRenderer
{
   private final C_Annotation annotation;

   public AnnotationRendererImpl(C_Annotation annotation)
   {
      this.annotation = annotation;
   }

   public static String declaration(RenderingContextWrapper context, C_Annotation annotation, String content)
   {
      StringBuilder sb = new StringBuilder();


      sb.append(RenderingSupport.annotations(context, annotation, '\n'));

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
   public String declaration(RenderingContext renderingContext)
   {
      return declaration(renderingContext, "");
   }

   @Override
   public String declaration(RenderingContext renderingContext, String content)
   {
      return declaration(new RenderingContextWrapper(renderingContext), annotation, content);
   }

   @Override
   public String type(RenderingContext renderingContext)
   {
      return type(new RenderingContextWrapper(renderingContext), annotation);
   }
}
