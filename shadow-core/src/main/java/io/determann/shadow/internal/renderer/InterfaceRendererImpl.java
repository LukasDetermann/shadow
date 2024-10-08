package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.Operations;
import io.determann.shadow.api.renderer.InterfaceRenderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.C_AnnotationUsage;
import io.determann.shadow.api.shadow.modifier.C_Modifier;
import io.determann.shadow.api.shadow.type.C_Generic;
import io.determann.shadow.api.shadow.type.C_Interface;
import io.determann.shadow.api.shadow.type.C_Type;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrEmpty;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static java.util.stream.Collectors.joining;

public class InterfaceRendererImpl implements InterfaceRenderer
{
   private final RenderingContextWrapper context;
   private final C_Interface anInterface;

   public InterfaceRendererImpl(RenderingContext renderingContext, C_Interface anInterface)
   {
      this.context = new RenderingContextWrapper(renderingContext);
      this.anInterface = anInterface;
   }

   public static String declaration(RenderingContextWrapper context, C_Interface anInterface, String content)
   {
      StringBuilder sb = new StringBuilder();

      //noinspection OptionalContainsCollection
      Optional<List<? extends C_AnnotationUsage>> annotationUsages = requestOrEmpty(anInterface, Operations.ANNOTATIONABLE_GET_DIRECT_ANNOTATION_USAGES);
      if (!annotationUsages.map(List::isEmpty).orElse(true))
      {
         sb.append(annotationUsages.get()
                              .stream()
                              .map(usage -> AnnotationUsageRendererImpl.usage(context, usage) + "\n")
                              .collect(Collectors.joining()));
      }

      Set<C_Modifier> modifiers = new HashSet<>(requestOrThrow(anInterface, MODIFIABLE_GET_MODIFIERS));
      modifiers.remove(C_Modifier.ABSTRACT);
      modifiers.remove(C_Modifier.PACKAGE_PRIVATE);

      if (!modifiers.isEmpty())
      {
         sb.append(ModifierRendererImpl.render(modifiers));
         sb.append(' ');
      }
      sb.append("interface");
      sb.append(' ');
      sb.append(requestOrThrow(anInterface, NAMEABLE_GET_NAME));

      List<? extends C_Generic> generics = requestOrThrow(anInterface, INTERFACE_GET_GENERICS);
      if (!generics.isEmpty())
      {
         sb.append('<');
         sb.append(generics.stream().map(type -> TypeRendererImpl.type(context, type)).collect(joining(", ")));
         sb.append('>');
      }
      sb.append(' ');

      List<? extends C_Interface> directInterfaces = requestOrThrow(anInterface, DECLARED_GET_DIRECT_INTERFACES);

      if (!directInterfaces.isEmpty())
      {
         sb.append("extends");
         sb.append(' ');
         sb.append(directInterfaces.stream().map(anInterface1 -> type(context, anInterface1)).collect(joining(", ")));
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

   public static String type(RenderingContextWrapper context, C_Interface anInterface)
   {
      StringBuilder sb = new StringBuilder();
      sb.append(context.renderName(anInterface));

      List<? extends C_Type> genericTypes = requestOrThrow(anInterface, INTERFACE_GET_GENERIC_TYPES);
      if (!genericTypes.isEmpty())
      {
         sb.append('<');
         sb.append(genericTypes.stream().map(type -> TypeRendererImpl.type(context, type)).collect(joining(", ")));
         sb.append('>');
      }
      return sb.toString();
   }

   @Override
   public String declaration()
   {
      return declaration(context, anInterface, "");
   }

   @Override
   public String declaration(String content)
   {
      return declaration(context, anInterface, content);
   }

   @Override
   public String type()
   {
      return type(context, anInterface);
   }
}
