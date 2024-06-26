package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.renderer.EnumRenderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.modifier.Modifier;
import io.determann.shadow.api.shadow.type.Enum;
import io.determann.shadow.api.shadow.type.Interface;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static io.determann.shadow.api.shadow.Operations.DECLARED_GET_DIRECT_INTERFACES;
import static io.determann.shadow.api.shadow.Operations.NAMEABLE_NAME;
import static io.determann.shadow.api.shadow.Provider.requestOrThrow;
import static java.util.stream.Collectors.joining;

public class EnumRendererImpl implements EnumRenderer
{
   private final RenderingContextWrapper context;
   private final Enum anEnum;

   public EnumRendererImpl(RenderingContext renderingContext, Enum anEnum)
   {
      this.context = new RenderingContextWrapper(renderingContext);
      this.anEnum = anEnum;
   }

   public static String declaration(RenderingContextWrapper context, Enum anEnum, String content)
   {
      StringBuilder sb = new StringBuilder();

      Set<Modifier> modifiers = new HashSet<>(anEnum.getModifiers());
      modifiers.remove(Modifier.FINAL);

      if (!anEnum.getDirectAnnotationUsages().isEmpty())
      {
         sb.append(anEnum.getDirectAnnotationUsages()
                         .stream()
                         .map(usage -> AnnotationUsageRendererImpl.usage(context, usage) + "\n")
                         .collect(Collectors.joining()));
      }
      if (!modifiers.isEmpty())
      {
         sb.append(ModifierRendererImpl.render(modifiers));
         sb.append(' ');
      }
      sb.append("enum");
      sb.append(' ');
      sb.append(requestOrThrow(anEnum, NAMEABLE_NAME));
      sb.append(' ');

      List<Interface> directInterfaces = requestOrThrow(anEnum, DECLARED_GET_DIRECT_INTERFACES);
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

   public static String type(RenderingContextWrapper context, Enum anEnum)
   {
      return context.renderName(anEnum);
   }

   @Override
   public String declaration()
   {
      return declaration(context, anEnum, "");
   }

   @Override
   public String declaration(String content)
   {
      return declaration(context, anEnum, content);
   }

   @Override
   public String type()
   {
      return type(context, anEnum);
   }
}
