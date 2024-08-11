package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.renderer.InterfaceRenderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.AnnotationUsage;
import io.determann.shadow.api.shadow.Operations;
import io.determann.shadow.api.shadow.modifier.Modifier;
import io.determann.shadow.api.shadow.type.Generic;
import io.determann.shadow.api.shadow.type.Interface;
import io.determann.shadow.api.shadow.type.Shadow;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static io.determann.shadow.api.shadow.Operations.*;
import static io.determann.shadow.api.shadow.Provider.requestOrEmpty;
import static io.determann.shadow.api.shadow.Provider.requestOrThrow;
import static java.util.stream.Collectors.joining;

public class InterfaceRendererImpl implements InterfaceRenderer
{
   private final RenderingContextWrapper context;
   private final Interface anInterface;

   public InterfaceRendererImpl(RenderingContext renderingContext, Interface anInterface)
   {
      this.context = new RenderingContextWrapper(renderingContext);
      this.anInterface = anInterface;
   }

   public static String declaration(RenderingContextWrapper context, Interface anInterface, String content)
   {
      StringBuilder sb = new StringBuilder();

      //noinspection OptionalContainsCollection
      Optional<List<AnnotationUsage>> annotationUsages = requestOrEmpty(anInterface, Operations.ANNOTATIONABLE_GET_DIRECT_ANNOTATION_USAGES);
      if (!annotationUsages.map(List::isEmpty).orElse(true))
      {
         sb.append(annotationUsages.get()
                              .stream()
                              .map(usage -> AnnotationUsageRendererImpl.usage(context, usage) + "\n")
                              .collect(Collectors.joining()));
      }

      Set<Modifier> modifiers = new HashSet<>(requestOrThrow(anInterface, MODIFIABLE_GET_MODIFIERS));
      modifiers.remove(Modifier.ABSTRACT);
      modifiers.remove(Modifier.PACKAGE_PRIVATE);

      if (!modifiers.isEmpty())
      {
         sb.append(ModifierRendererImpl.render(modifiers));
         sb.append(' ');
      }
      sb.append("interface");
      sb.append(' ');
      sb.append(requestOrThrow(anInterface, NAMEABLE_GET_NAME));

      List<Generic> generics = requestOrThrow(anInterface, INTERFACE_GET_GENERICS);
      if (!generics.isEmpty())
      {
         sb.append('<');
         sb.append(generics.stream().map(shadow -> ShadowRendererImpl.type(context, shadow)).collect(joining(", ")));
         sb.append('>');
      }
      sb.append(' ');

      List<Interface> directInterfaces = requestOrThrow(anInterface, DECLARED_GET_DIRECT_INTERFACES);

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

   public static String type(RenderingContextWrapper context, Interface anInterface)
   {
      StringBuilder sb = new StringBuilder();
      sb.append(context.renderName(anInterface));

      List<Shadow> genericTypes = requestOrThrow(anInterface, INTERFACE_GET_GENERIC_TYPES);
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
