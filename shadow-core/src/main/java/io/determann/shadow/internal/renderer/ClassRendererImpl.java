package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.modifier.Modifier;
import io.determann.shadow.api.renderer.ClassRenderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.Class;
import io.determann.shadow.api.shadow.Generic;
import io.determann.shadow.api.shadow.Interface;
import io.determann.shadow.api.shadow.Shadow;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static io.determann.shadow.meta_meta.Operations.*;
import static io.determann.shadow.meta_meta.Provider.requestOrThrow;
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
      Set<Modifier> modifiers = aClass.getModifiers();
      if (!modifiers.isEmpty())
      {
         sb.append(ModifierRendererImpl.render(modifiers));
         sb.append(' ');
      }
      sb.append("class");
      sb.append(' ');
      sb.append(requestOrThrow(aClass, NAMEABLE_NAME));

      List<Generic> generics = requestOrThrow(aClass, CLASS_GET_GENERICS);
      if (!generics.isEmpty())
      {
         sb.append('<');
         sb.append(generics.stream().map(shadow -> ShadowRendererImpl.type(context, shadow)).collect(joining(", ")));
         sb.append('>');
      }
      sb.append(' ');

      Class superClass = requestOrThrow(aClass, CLASS_GET_SUPER_CLASS);
      if (superClass != null && !requestOrThrow(superClass, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME).equals("java.lang.Object"))
      {
         sb.append("extends");
         sb.append(' ');
         sb.append(type(context, superClass));
         sb.append(' ');
      }

      List<Interface> directInterfaces = requestOrThrow(aClass, DECLARED_GET_DIRECT_INTERFACES);
      if (!directInterfaces.isEmpty())
      {
         sb.append("implements");
         sb.append(' ');
         sb.append(directInterfaces.stream()
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

      List<Shadow> genericTypes = requestOrThrow(aClass, CLASS_GET_GENERIC_TYPES);
      if (!genericTypes.isEmpty())
      {
         sb.append('<');
         sb.append(genericTypes.stream().map(shadow -> ShadowRendererImpl.type(context, shadow)).collect(joining(", ")));
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
