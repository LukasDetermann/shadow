package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.renderer.AnnotationUsageRenderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.AnnotationUsage;
import io.determann.shadow.api.shadow.AnnotationValue;
import io.determann.shadow.api.shadow.structure.EnumConstant;
import io.determann.shadow.api.shadow.structure.Method;
import io.determann.shadow.api.shadow.type.Annotation;
import io.determann.shadow.api.shadow.type.Declared;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static io.determann.shadow.api.shadow.Operations.*;
import static io.determann.shadow.api.shadow.Provider.requestOrThrow;

public class AnnotationUsageRendererImpl implements AnnotationUsageRenderer
{
   private final RenderingContextWrapper context;
   private final AnnotationUsage usage;

   public AnnotationUsageRendererImpl(RenderingContext renderingContext, AnnotationUsage usage)
   {
      this.context = new RenderingContextWrapper(renderingContext);
      this.usage = usage;
   }

   public static String usage(RenderingContextWrapper context, AnnotationUsage usage)
   {
      return usage(method -> Optional.empty(), context, usage);
   }

   private static String usage(Function<Method, Optional<String>> valueRenderer, RenderingContextWrapper context, AnnotationUsage usage)
   {
      StringBuilder sb = new StringBuilder();
      sb.append('@');

      Annotation annotation = requestOrThrow(usage, ANNOTATION_USAGE_GET_ANNOTATION);
      sb.append(context.renderName(annotation));

      List<? extends Method> methods = requestOrThrow(annotation, DECLARED_GET_METHODS);

      if (!methods.isEmpty())
      {
         sb.append('(');
         sb.append(methods.stream()
                          .map(method -> requestOrThrow(method, NAMEABLE_GET_NAME) +
                                         " = " +
                                         valueRenderer.apply(method).orElseGet(() ->
                                                                               {
                                                                                  Map<? extends Method, ? extends AnnotationValue> values = requestOrThrow(usage, ANNOTATION_USAGE_GET_VALUES);
                                                                                  return renderValue(context, values.get(method));
                                                                               }))
                          .collect(Collectors.joining(", ")));
         sb.append(')');
      }
      return sb.toString();
   }

   @Override
   public String usage()
   {
      return usage(method -> Optional.empty());
   }

   @Override
   public String usage(Function<Method, Optional<String>> valueRenderer)
   {
      return usage(valueRenderer, context, usage);
   }

   private static String renderValue(RenderingContextWrapper context, AnnotationValue annotationValue)
   {
      Object value = requestOrThrow(annotationValue, ANNOTATION_VALUE_GET_VALUE);

      if (value instanceof String s)
      {
         return '\"' + s + '\"';
      }
      if (value instanceof Boolean aBoolean)
      {
         return String.valueOf(aBoolean.booleanValue());
      }
      if (value instanceof Byte aByte)
      {
         return Byte.toString(aByte);
      }
      if (value instanceof Short aShort)
      {
         return Short.toString(aShort);
      }
      if (value instanceof Integer integer)
      {
         return Integer.toString(integer);
      }
      if (value instanceof Long aLong)
      {
         return Long.toString(aLong) + 'L';
      }
      if (value instanceof Character character)
      {
         return '\'' + Character.toString(character) + '\'';
      }
      if (value instanceof Float aFloat)
      {
         return Float.toString(aFloat) + 'F';
      }
      if (value instanceof Double aDouble)
      {
         return Double.toString(aDouble) + 'D';
      }
      if (value instanceof Declared declared)
      {
         return ShadowRendererImpl.classDeclaration(context, declared);
      }
      if (value instanceof EnumConstant enumConstant)
      {
         return EnumConstantRendererImpl.type(context, enumConstant);
      }
      if (value instanceof AnnotationUsage annotationUsage)
      {
         return usage(context, annotationUsage);
      }
      if (value instanceof List<?> values)
      {
         //noinspection unchecked,OverlyStrongTypeCast
         return '{' + ((List<AnnotationValue>) values).stream().map(v -> renderValue(context, v)).collect(Collectors.joining(", ")) + '}';
      }
      throw new IllegalStateException("Unable to render " + annotationValue);
   }
}
