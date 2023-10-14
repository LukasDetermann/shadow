package io.determann.shadow.impl.renderer;

import io.determann.shadow.api.renderer.ClassRenderer;
import io.determann.shadow.api.shadow.Class;
import io.determann.shadow.impl.annotation_processing.ShadowApiImpl;

import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;

public class ClassRendererImpl implements ClassRenderer
{
   private final Context context;
   private final Class aClass;

   public ClassRendererImpl(Class aClass)
   {
      this.context = ((ShadowApiImpl) aClass.getApi()).getRenderingContext();
      this.aClass = aClass;
   }

   public static String declaration(Context context, Class aClass, String content)
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
      sb.append(aClass.getSimpleName());
      if (!aClass.getFormalGenerics().isEmpty())
      {
         sb.append('<');
         sb.append(aClass.getFormalGenerics().stream().map(shadow -> ShadowRendererImpl.type(context, shadow)).collect(joining(", ")));
         sb.append('>');
      }
      sb.append(' ');
      if (aClass.getSuperClass() != null && !"java.lang.Object".equals(aClass.getSuperClass().getQualifiedName()))
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

   public static String type(Context context, Class aClass)
   {
      StringBuilder sb = new StringBuilder();
      sb.append(context.renderName(aClass));
      if (!aClass.getGenerics().isEmpty())
      {
         sb.append('<');
         sb.append(aClass.getGenerics().stream().map(shadow -> ShadowRendererImpl.type(context, shadow)).collect(joining(", ")));
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
