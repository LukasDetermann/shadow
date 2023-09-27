package io.determann.shadow.impl.renderer;

import io.determann.shadow.api.renderer.ConstructorRenderer;
import io.determann.shadow.api.shadow.Constructor;
import io.determann.shadow.impl.ShadowApiImpl;

import java.util.stream.Collectors;

public class ConstructorRendererImpl implements ConstructorRenderer
{
   private final Context context;
   private final Constructor constructor;

   public ConstructorRendererImpl(Constructor constructor)
   {
      this.context = ((ShadowApiImpl) constructor.getApi()).getRenderingContext();
      this.constructor = constructor;
   }

   public static String declaration(Context context, Constructor constructor, String content)
   {
      StringBuilder sb = new StringBuilder();
      if (!constructor.getDirectAnnotationUsages().isEmpty())
      {
         sb.append(constructor.getDirectAnnotationUsages()
                             .stream()
                             .map(usage -> AnnotationUsageRendererImpl.usage(context, usage) + "\n")
                             .collect(Collectors.joining()));
      }
      if (!constructor.getModifiers().isEmpty())
      {
         sb.append(ModifierRendererImpl.render(constructor.getModifiers()));
         sb.append(' ');
      }
      if (!constructor.getFormalGenerics().isEmpty())
      {
         sb.append('<');
         sb.append(constructor.getFormalGenerics()
                              .stream()
                              .map(generic -> GenericRendererImpl.type(context, generic))
                              .collect(Collectors.joining(", ")));
         sb.append('>');
         sb.append(' ');
      }
      sb.append(ShadowRendererImpl.type(Context.builder(context).withSimpleNames().build(), constructor.getSurrounding()));
      sb.append('(');

      constructor.getReceiverType().ifPresent(declared -> {

         sb.append(ShadowRendererImpl.type(context, declared));
         sb.append(' ');
         sb.append(declared.getSimpleName());
         sb.append('.');
         sb.append("this");
         if (!constructor.getParameters().isEmpty())
         {
            sb.append(' ');
         }
      });

      sb.append(constructor.getParameters()
                           .stream()
                           .map(parameter -> ParameterRendererImpl.declaration(context, parameter))
                           .collect(Collectors.joining(", ")));
      sb.append(')');
      sb.append(' ');

      if (!constructor.getThrows().isEmpty())
      {
         sb.append("throws ");
         sb.append(constructor.getThrows()
                              .stream().map(aClass -> ShadowRendererImpl.type(context, aClass))
                              .collect(Collectors.joining(", ")));
         sb.append(" ");
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

   @Override
   public String declaration()
   {
      return declaration(context, constructor, "");
   }

   @Override
   public String declaration(String content)
   {
      return declaration(context, constructor, content);
   }

   @Override
   public String invocation()
   {
      return invocation("");
   }

   @Override
   public String invocation(String parameters)
   {
      return ShadowRendererImpl.type(Context.builder(context).withSimpleNames().build(), constructor.getSurrounding()) +
             '(' +
             parameters +
             ')';
   }
}
