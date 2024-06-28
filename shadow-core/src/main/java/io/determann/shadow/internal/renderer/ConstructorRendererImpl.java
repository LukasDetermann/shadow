package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.renderer.ConstructorRenderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.structure.Constructor;
import io.determann.shadow.api.shadow.structure.Parameter;
import io.determann.shadow.api.shadow.type.Class;
import io.determann.shadow.api.shadow.type.Generic;

import java.util.List;
import java.util.stream.Collectors;

import static io.determann.shadow.api.shadow.Operations.*;
import static io.determann.shadow.api.shadow.Provider.request;
import static io.determann.shadow.api.shadow.Provider.requestOrThrow;

public class ConstructorRendererImpl implements ConstructorRenderer
{
   private final RenderingContextWrapper context;
   private final Constructor constructor;

   public ConstructorRendererImpl(RenderingContext renderingContext, Constructor constructor)
   {
      this.context = new RenderingContextWrapper(renderingContext);
      this.constructor = constructor;
   }

   public static String declaration(RenderingContextWrapper context, Constructor constructor, String content)
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

      List<Generic> generics = requestOrThrow(constructor, EXECUTABLE_GET_GENERICS);
      if (!generics.isEmpty())
      {
         sb.append('<');
         sb.append(generics.stream()
                           .map(generic -> GenericRendererImpl.type(context, generic))
                           .collect(Collectors.joining(", ")));
         sb.append('>');
         sb.append(' ');
      }
      sb.append(ShadowRendererImpl.type(new RenderingContextWrapper(RenderingContext.builder(context).withSimpleNames().build()),
                                        requestOrThrow(constructor, EXECUTABLE_GET_SURROUNDING)));
      sb.append('(');

      List<Parameter> parameters = requestOrThrow(constructor, EXECUTABLE_GET_PARAMETERS);
      request(constructor, EXECUTABLE_GET_RECEIVER_TYPE)
            .ifPresent(declared ->
                       {
                          sb.append(ShadowRendererImpl.type(context, declared));
                          sb.append(' ');
                          sb.append(requestOrThrow(declared, NAMEABLE_NAME));
                          sb.append('.');
                          sb.append("this");
                          if (!parameters.isEmpty())
                          {
                             sb.append(' ');
                          }
                       });

      sb.append(parameters.stream()
                          .map(parameter -> ParameterRendererImpl.declaration(context, parameter))
                          .collect(Collectors.joining(", ")));
      sb.append(')');
      sb.append(' ');

      List<Class> aThrow = requestOrThrow(constructor, EXECUTABLE_GET_THROWS);
      if (!aThrow.isEmpty())
      {
         sb.append("throws ");
         sb.append(aThrow.stream().map(aClass -> ShadowRendererImpl.type(context, aClass))
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
      return ShadowRendererImpl.type(new RenderingContextWrapper(RenderingContext.builder(context).withSimpleNames().build()),
                                     requestOrThrow(constructor, EXECUTABLE_GET_SURROUNDING)) +
             '(' +
             parameters +
             ')';
   }
}
