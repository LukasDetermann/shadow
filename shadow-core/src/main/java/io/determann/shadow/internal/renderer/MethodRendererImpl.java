package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.renderer.MethodRenderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.Operations;
import io.determann.shadow.api.shadow.Provider;
import io.determann.shadow.api.shadow.annotationusage.AnnotationUsage;
import io.determann.shadow.api.shadow.modifier.Modifier;
import io.determann.shadow.api.shadow.structure.Method;
import io.determann.shadow.api.shadow.structure.Parameter;
import io.determann.shadow.api.shadow.type.Class;
import io.determann.shadow.api.shadow.type.Generic;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static io.determann.shadow.api.shadow.Operations.*;
import static io.determann.shadow.api.shadow.Provider.requestOrEmpty;
import static io.determann.shadow.api.shadow.Provider.requestOrThrow;

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

      //noinspection OptionalContainsCollection
      Optional<List<AnnotationUsage>> annotationUsages = requestOrEmpty(method, Operations.ANNOTATIONABLE_GET_DIRECT_ANNOTATION_USAGES);
      if (!annotationUsages.map(List::isEmpty).orElse(true))
      {
         sb.append(annotationUsages.get()
                         .stream()
                         .map(usage -> AnnotationUsageRendererImpl.usage(context, usage) + "\n")
                         .collect(Collectors.joining()));
      }
      Set<Modifier> modifiers = new HashSet<>(requestOrThrow(method, MODIFIABLE_GET_MODIFIERS));
      if (!content.isEmpty())
      {
         modifiers.remove(Modifier.ABSTRACT);
      }

      modifiers.remove(Modifier.PACKAGE_PRIVATE);
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
      sb.append(requestOrThrow(method, NAMEABLE_GET_NAME));
      sb.append('(');

      List<Parameter> parameters = requestOrThrow(method, EXECUTABLE_GET_PARAMETERS);
      Provider.requestOrEmpty(method, EXECUTABLE_GET_RECEIVER_TYPE)
              .ifPresent(declared ->
                       {
                          sb.append(ShadowRendererImpl.type(context, declared));
                          sb.append(' ');
                          sb.append(requestOrThrow(declared, NAMEABLE_GET_NAME));
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

      if (requestOrThrow(method, MODIFIABLE_HAS_MODIFIER, Modifier.ABSTRACT) && content.isBlank())
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
      return requestOrThrow(method, NAMEABLE_GET_NAME) +
             '(' +
             parameters +
             ')';
   }
}
