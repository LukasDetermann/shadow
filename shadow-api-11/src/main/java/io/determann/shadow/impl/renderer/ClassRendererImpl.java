package io.determann.shadow.impl.renderer;

import io.determann.shadow.api.renderer.ClassRenderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.Class;

import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;

public class ClassRendererImpl implements ClassRenderer
{
   private final RenderingContextWrapper context;
   private final Class aClass;

   public ClassRendererImpl(RenderingContext renderingContext, Class aClass)
   {
      this.context = new RenderingContextWrapper(renderingContext);
      this.aClass = aClass;
   }

   public static String declaration(RenderingContextWrapper context, Class aClass, String content)
   {
      StringBuilder sb = new StringBuilder();
      if (!aClass.getDirectAnnotationUsages().isEmpty())
      {
         sb.append(aClass.getDirectAnnotationUsages()
                         .stream()
                         .map(usage -> AnnotationUsageRendererImpl.usage(context, usage) + "\n")
                         .collect(Collectors.joining()));
      }
      if (!aClass.getModifiers().isEmpty())
      {
         sb.append(ModifierRendererImpl.render(aClass.getModifiers()));
         sb.append(' ');
      }
      sb.append("class");
      sb.append(' ');
      sb.append(aClass.getName());
      if (!aClass.getGenerics().isEmpty())
      {
         sb.append('<');
         sb.append(aClass.getGenerics().stream().map(shadow -> ShadowRendererImpl.type(context, shadow)).collect(joining(", ")));
         sb.append('>');
      }
      sb.append(' ');
      if (aClass.getSuperClass() != null && !aClass.getSuperClass().getQualifiedName().equals("java.lang.Object"))
      {
         sb.append("extends");
         sb.append(' ');
         sb.append(type(context, aClass.getSuperClass()));
         sb.append(' ');
      }
      if (!aClass.getDirectInterfaces().isEmpty())
      {
         sb.append("implements");
         sb.append(' ');
         sb.append(aClass.getDirectInterfaces()
                         .stream()
                         .map(anInterface -> InterfaceRendererImpl.type(context, anInterface))
                         .collect(joining(", ")));
         sb.append(' ');
      }
      sb.append('{');
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

   public static String type(RenderingContextWrapper context, Class aClass)
   {
      StringBuilder sb = new StringBuilder();
      sb.append(context.renderName(aClass));
      if (!aClass.getGenericTypes().isEmpty())
      {
         sb.append('<');
         sb.append(aClass.getGenericTypes().stream().map(shadow -> ShadowRendererImpl.type(context, shadow)).collect(joining(", ")));
         sb.append('>');
      }
      return sb.toString();
   }

   @Override
   public String declaration()
   {
      return declaration(context, aClass, "");
   }

   @Override
   public String declaration(String content)
   {
      return declaration(context, aClass, content);
   }

   @Override
   public String type()
   {
      return type(context, aClass);
   }
}
