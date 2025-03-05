package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.renderer.AnnotationUsageRenderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.C_AnnotationUsage;
import io.determann.shadow.api.shadow.C_AnnotationValue;
import io.determann.shadow.api.shadow.structure.C_EnumConstant;
import io.determann.shadow.api.shadow.structure.C_Method;
import io.determann.shadow.api.shadow.type.C_Annotation;
import io.determann.shadow.api.shadow.type.C_Declared;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrThrow;

public class AnnotationUsageRendererImpl implements AnnotationUsageRenderer
{
   private final C_AnnotationUsage usage;

   public AnnotationUsageRendererImpl(C_AnnotationUsage usage)
   {
      this.usage = usage;
   }

   public static String usage(RenderingContextWrapper context, C_AnnotationUsage usage)
   {
      return usage(method -> Optional.empty(), context, usage);
   }

   private static String usage(Function<C_Method, Optional<String>> valueRenderer, RenderingContextWrapper context, C_AnnotationUsage usage)
   {
      StringBuilder sb = new StringBuilder();
      sb.append('@');

      C_Annotation annotation = requestOrThrow(usage, ANNOTATION_USAGE_GET_ANNOTATION);
      sb.append(context.renderName(annotation));

      List<? extends C_Method> methods = requestOrThrow(annotation, DECLARED_GET_METHODS);

      if (!methods.isEmpty())
      {
         sb.append('(');
         sb.append(methods.stream()
                          .map(method -> requestOrThrow(method, NAMEABLE_GET_NAME) +
                                         " = " +
                                         valueRenderer.apply(method).orElseGet(() ->
                                                                               {
                                                                                  Map<? extends C_Method, ? extends C_AnnotationValue> values = requestOrThrow(usage, ANNOTATION_USAGE_GET_VALUES);
                                                                                  return renderValue(context, values.get(method));
                                                                               }))
                          .collect(Collectors.joining(", ")));
         sb.append(')');
      }
      return sb.toString();
   }

   @Override
   public String usage(RenderingContext renderingContext)
   {
      return usage(new RenderingContextWrapper(renderingContext),method -> Optional.empty());
   }

   @Override
   public String usage(RenderingContext renderingContext, Function<C_Method, Optional<String>> valueRenderer)
   {
      return usage(valueRenderer, new RenderingContextWrapper(renderingContext), usage);
   }

   private static String renderValue(RenderingContextWrapper context, C_AnnotationValue annotationValue)
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
      if (value instanceof C_Declared declared)
      {
         return TypeRendererImpl.classDeclaration(context, declared);
      }
      if (value instanceof C_EnumConstant enumConstant)
      {
         return EnumConstantRendererImpl.type(context, enumConstant);
      }
      if (value instanceof C_AnnotationUsage annotationUsage)
      {
         return usage(context, annotationUsage);
      }
      if (value instanceof List<?> values)
      {
         //noinspection unchecked,OverlyStrongTypeCast
         return '{' + ((List<C_AnnotationValue>) values).stream().map(v -> renderValue(context, v)).collect(Collectors.joining(", ")) + '}';
      }
      throw new IllegalStateException("Unable to render " + annotationValue);
   }
}
