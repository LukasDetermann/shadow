package io.determann.shadow.impl.renderer;

import io.determann.shadow.api.modifier.Modifier;
import io.determann.shadow.api.renderer.InterfaceRenderer;
import io.determann.shadow.api.shadow.Interface;
import io.determann.shadow.impl.annotation_processing.ShadowApiImpl;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;

public class InterfaceRendererImpl implements InterfaceRenderer
{
   private final Context context;
   private final Interface anInterface;

   public InterfaceRendererImpl(Interface anInterface)
   {
      this.context = ((ShadowApiImpl) anInterface.getApi()).getRenderingContext();
      this.anInterface = anInterface;
   }

   public static String declaration(Context context, Interface anInterface, String content)
   {
      StringBuilder sb = new StringBuilder();
      Set<Modifier> modifiers = new HashSet<>(anInterface.getModifiers());
      modifiers.remove(Modifier.ABSTRACT);

      if (!anInterface.getDirectAnnotationUsages().isEmpty())
      {
         sb.append(anInterface.getDirectAnnotationUsages()
                              .stream()
                              .map(usage -> AnnotationUsageRendererImpl.usage(context, usage) + "\n")
                              .collect(Collectors.joining()));
      }
      if (!modifiers.isEmpty())
      {
         sb.append(ModifierRendererImpl.render(modifiers));
         sb.append(' ');
      }
      sb.append("interface");
      sb.append(' ');
      sb.append(anInterface.getSimpleName());
      if (!anInterface.getFormalGenerics().isEmpty())
      {
         sb.append('<');
         sb.append(anInterface.getFormalGenerics().stream().map(shadow -> ShadowRendererImpl.type(context, shadow)).collect(joining(", ")));
         sb.append('>');
      }
      sb.append(' ');
      if (!anInterface.getDirectInterfaces().isEmpty())
      {
         sb.append("extends");
         sb.append(' ');
         sb.append(anInterface.getDirectInterfaces().stream().map(anInterface1 -> type(context, anInterface1)).collect(joining(", ")));
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

   public static String type(Context context, Interface anInterface)
   {
      StringBuilder sb = new StringBuilder();
      sb.append(context.renderName(anInterface));
      if (!anInterface.getGenerics().isEmpty())
      {
         sb.append('<');
         sb.append(anInterface.getGenerics().stream().map(shadow -> ShadowRendererImpl.type(context, shadow)).collect(joining(", ")));
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
