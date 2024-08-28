package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.Operations;
import io.determann.shadow.api.Provider;
import io.determann.shadow.api.renderer.ConstructorRenderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.C_AnnotationUsage;
import io.determann.shadow.api.shadow.modifier.C_Modifier;
import io.determann.shadow.api.shadow.structure.C_Constructor;
import io.determann.shadow.api.shadow.structure.C_Parameter;
import io.determann.shadow.api.shadow.type.C_Class;
import io.determann.shadow.api.shadow.type.C_Generic;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrEmpty;
import static io.determann.shadow.api.Provider.requestOrThrow;

public class ConstructorRendererImpl implements ConstructorRenderer
{
   private final RenderingContextWrapper context;
   private final C_Constructor constructor;

   public ConstructorRendererImpl(RenderingContext renderingContext, C_Constructor constructor)
   {
      this.context = new RenderingContextWrapper(renderingContext);
      this.constructor = constructor;
   }

   public static String declaration(RenderingContextWrapper context, C_Constructor constructor, String content)
   {
      StringBuilder sb = new StringBuilder();
      //noinspection OptionalContainsCollection
      Optional<List<? extends C_AnnotationUsage>> annotationUsages = requestOrEmpty(constructor, Operations.ANNOTATIONABLE_GET_DIRECT_ANNOTATION_USAGES);
      if (!annotationUsages.map(List::isEmpty).orElse(true))
      {
         sb.append(annotationUsages.get()
                              .stream()
                              .map(usage -> AnnotationUsageRendererImpl.usage(context, usage) + "\n")
                              .collect(Collectors.joining()));
      }
      Set<C_Modifier> modifiers = requestOrThrow(constructor, MODIFIABLE_GET_MODIFIERS);
      if (!modifiers.isEmpty())
      {
         sb.append(ModifierRendererImpl.render(modifiers));
         sb.append(' ');
      }

      List<? extends C_Generic> generics = requestOrThrow(constructor, EXECUTABLE_GET_GENERICS);
      if (!generics.isEmpty())
      {
         sb.append('<');
         sb.append(generics.stream()
                           .map(generic -> GenericRendererImpl.type(context, generic))
                           .collect(Collectors.joining(", ")));
         sb.append('>');
         sb.append(' ');
      }
      sb.append(TypeRendererImpl.type(new RenderingContextWrapper(RenderingContext.builder(context).withSimpleNames().build()),
                                      requestOrThrow(constructor, EXECUTABLE_GET_SURROUNDING)));
      sb.append('(');

      List<? extends C_Parameter> parameters = requestOrThrow(constructor, EXECUTABLE_GET_PARAMETERS);
      Provider.requestOrEmpty(constructor, EXECUTABLE_GET_RECEIVER_TYPE)
              .ifPresent(declared ->
                       {
                          sb.append(TypeRendererImpl.type(context, declared));
                          sb.append(' ');
                          sb.append(requestOrThrow(declared, NAMEABLE_GET_NAME));
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

      List<? extends C_Class> aThrow = requestOrThrow(constructor, EXECUTABLE_GET_THROWS);
      if (!aThrow.isEmpty())
      {
         sb.append("throws ");
         sb.append(aThrow.stream().map(aClass -> TypeRendererImpl.type(context, aClass))
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
      return TypeRendererImpl.type(new RenderingContextWrapper(RenderingContext.builder(context).withSimpleNames().build()),
                                   requestOrThrow(constructor, EXECUTABLE_GET_SURROUNDING)) +
             '(' +
             parameters +
             ')';
   }
}
