package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.Operations;
import io.determann.shadow.api.renderer.ClassRenderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.C_AnnotationUsage;
import io.determann.shadow.api.shadow.modifier.C_Modifier;
import io.determann.shadow.api.shadow.type.C_Class;
import io.determann.shadow.api.shadow.type.C_Generic;
import io.determann.shadow.api.shadow.type.C_Interface;
import io.determann.shadow.api.shadow.type.C_Shadow;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrEmpty;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static java.util.stream.Collectors.joining;

public class ClassRendererImpl implements ClassRenderer
{
   private final RenderingContextWrapper context;
   private final C_Class aClass;

   public ClassRendererImpl(RenderingContext renderingContext, C_Class aClass)
   {
      this.context = new RenderingContextWrapper(renderingContext);
      this.aClass = aClass;
   }

   public static String declaration(RenderingContextWrapper context, C_Class aClass, String content)
   {
      StringBuilder sb = new StringBuilder();
      //noinspection OptionalContainsCollection
      Optional<List<? extends C_AnnotationUsage>> annotationUsages = requestOrEmpty(aClass, Operations.ANNOTATIONABLE_GET_DIRECT_ANNOTATION_USAGES);
      if (!annotationUsages.map(List::isEmpty).orElse(true))
      {
         sb.append(annotationUsages.get()
                         .stream()
                         .map(usage -> AnnotationUsageRendererImpl.usage(context, usage) + "\n")
                         .collect(Collectors.joining()));
      }
      Set<C_Modifier> modifiers = requestOrThrow(aClass, MODIFIABLE_GET_MODIFIERS);
      if (!modifiers.isEmpty())
      {
         sb.append(ModifierRendererImpl.render(modifiers));
         sb.append(' ');
      }
      sb.append("class");
      sb.append(' ');
      sb.append(requestOrThrow(aClass, NAMEABLE_GET_NAME));

      List<? extends C_Generic> generics = requestOrThrow(aClass, CLASS_GET_GENERICS);
      if (!generics.isEmpty())
      {
         sb.append('<');
         sb.append(generics.stream().map(shadow -> ShadowRendererImpl.type(context, shadow)).collect(joining(", ")));
         sb.append('>');
      }
      sb.append(' ');

      C_Class superClass = requestOrThrow(aClass, CLASS_GET_SUPER_CLASS);
      if (superClass != null && !requestOrThrow(superClass, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME).equals("java.lang.Object"))
      {
         sb.append("extends");
         sb.append(' ');
         sb.append(type(context, superClass));
         sb.append(' ');
      }

      List<? extends C_Interface> directInterfaces = requestOrThrow(aClass, DECLARED_GET_DIRECT_INTERFACES);
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

   public static String type(RenderingContextWrapper context, C_Class aClass)
   {
      StringBuilder sb = new StringBuilder();
      sb.append(context.renderName(aClass));

      List<? extends C_Shadow> genericTypes = requestOrThrow(aClass, CLASS_GET_GENERIC_TYPES);
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
