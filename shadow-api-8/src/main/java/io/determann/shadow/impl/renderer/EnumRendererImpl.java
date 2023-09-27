package io.determann.shadow.impl.renderer;

import io.determann.shadow.api.modifier.Modifier;
import io.determann.shadow.api.renderer.EnumRenderer;
import io.determann.shadow.api.shadow.Enum;
import io.determann.shadow.impl.ShadowApiImpl;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;

public class EnumRendererImpl implements EnumRenderer
{

   private final Context context;
   private final Enum anEnum;

   public EnumRendererImpl(Enum anEnum)
   {
      this.context = ((ShadowApiImpl) anEnum.getApi()).getRenderingContext();
      this.anEnum = anEnum;
   }

   public static String declaration(Context context, Enum anEnum, String content)
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
      sb.append(anEnum.getSimpleName());
      sb.append(' ');
      if (!anEnum.getDirectInterfaces().isEmpty())
      {
         sb.append("implements");
         sb.append(' ');
         sb.append(anEnum.getDirectInterfaces().stream().map(anInterface -> InterfaceRendererImpl.type(context, anInterface)).collect(joining(", ")));
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

   public static String type(Context context, Enum anEnum)
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
