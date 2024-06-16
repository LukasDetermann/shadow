package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.modifier.Modifier;
import io.determann.shadow.api.renderer.MethodRenderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.Class;
import io.determann.shadow.api.shadow.Generic;
import io.determann.shadow.api.shadow.Method;
import io.determann.shadow.api.shadow.Parameter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static io.determann.shadow.meta_meta.Operations.*;
import static io.determann.shadow.meta_meta.Provider.request;
import static io.determann.shadow.meta_meta.Provider.requestOrThrow;

public class MethodRendererImpl implements MethodRenderer
{

   private final RenderingContextWrapper context;
   private final Method method;

   public MethodRendererImpl(RenderingContext renderingContext, Method method)
   {
      this.context = new RenderingContextWrapper(renderingContext);
      this.method = method;
   }

   public static String declaration(RenderingContextWrapper context, Method method, String content)
   {
      StringBuilder sb = new StringBuilder();

      if (!method.getDirectAnnotationUsages().isEmpty())
      {
         sb.append(method.getDirectAnnotationUsages()
                         .stream()
                         .map(usage -> AnnotationUsageRendererImpl.usage(context, usage) + "\n")
                         .collect(Collectors.joining()));
      }
      Set<Modifier> modifiers = new HashSet<>(method.getModifiers());
      if (!content.isEmpty())
      {
         modifiers.remove(Modifier.ABSTRACT);
      }

      if (!modifiers.isEmpty())
      {
         sb.append(ModifierRendererImpl.render(modifiers));
         sb.append(' ');
      }

      List<Generic> generics = requestOrThrow(method, EXECUTABLE_GET_GENERICS);
      if (!generics.isEmpty())
      {
         sb.append('<');
         sb.append(generics.stream()
                           .map(generic -> ShadowRendererImpl.type(context, generic))
                           .collect(Collectors.joining(", ")));
         sb.append('>');
         sb.append(' ');
      }
      sb.append(ShadowRendererImpl.type(context, requestOrThrow(method, EXECUTABLE_GET_RETURN_TYPE)));
      sb.append(' ');
      sb.append(requestOrThrow(method, NAMEABLE_NAME));
      sb.append('(');

      List<Parameter> parameters = requestOrThrow(method, EXECUTABLE_GET_PARAMETERS);
      request(method, EXECUTABLE_GET_RECEIVER_TYPE)
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

      if (!parameters.isEmpty())
      {
         sb.append(parameters.stream()
                             .map(parameter -> ParameterRendererImpl.declaration(context, parameter))
                             .collect(Collectors.joining(", ")));
      }
      sb.append(')');

      List<Class> aThrow = requestOrThrow(method, EXECUTABLE_GET_THROWS);
      if (!aThrow.isEmpty())
      {
         sb.append(' ');
         sb.append("throws ");
         sb.append(aThrow.stream().map(aClass -> ShadowRendererImpl.type(context, aClass))
                         .collect(Collectors.joining(", ")));
      }

      if (method.isAbstract() && content.isBlank())
      {
         sb.append(';');
      }
      else
      {
         sb.append(" {");
         if (!content.isEmpty())
         {
            sb.append('\n');
            sb.append(content);
            if (!content.endsWith("\n"))
            {
               sb.append('\n');
            }
         }
         sb.append("}");
      }
      sb.append('\n');
      return sb.toString();
   }

   @Override
   public String declaration()
   {
      return declaration(context, method, "");
   }

   @Override
   public String declaration(String content)
   {
      return declaration(context, method, content);
   }

   @Override
   public String invocation()
   {
      return invocation("");
   }

   @Override
   public String invocation(String parameters)
   {
      return requestOrThrow(method, NAMEABLE_NAME) +
             '(' +
             parameters +
             ')';
   }
}
