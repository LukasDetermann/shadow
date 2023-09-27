package io.determann.shadow.impl.renderer;

import io.determann.shadow.api.annotationvalue.AnnotationValue;
import io.determann.shadow.api.annotationvalue.AnnotationValueMapper;
import io.determann.shadow.api.renderer.AnnotationUsageRenderer;
import io.determann.shadow.api.shadow.AnnotationUsage;
import io.determann.shadow.api.shadow.EnumConstant;
import io.determann.shadow.api.shadow.Method;
import io.determann.shadow.api.shadow.Shadow;
import io.determann.shadow.impl.ShadowApiImpl;

import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static io.determann.shadow.api.ShadowApi.convert;

public class AnnotationUsageRendererImpl implements AnnotationUsageRenderer
{
   private final Context context;
   private final AnnotationUsage usage;

   public AnnotationUsageRendererImpl(AnnotationUsage usage)
   {
      this.context = ((ShadowApiImpl) usage.getApi()).getRenderingContext();
      this.usage = usage;
   }
   public static String usage(Context context, AnnotationUsage usage)
   {
      return usage(method -> Optional.empty(), context, usage);
   }

   private static String usage(Function<Method, Optional<String>> valueRenderer, Context context, AnnotationUsage usage)
   {
      StringBuilder sb = new StringBuilder();
      sb.append('@');
      sb.append(context.renderName(usage));
      if (!usage.getMethods().isEmpty())
      {
         sb.append('(');
         sb.append(usage.getMethods()
                        .stream()
                        .map(method -> method.getSimpleName() +
                                       " = " +
                                       valueRenderer.apply(method).orElseGet(() -> renderValue(context, usage.getValues().get(method))))
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

   private static String renderValue(Context context, AnnotationValue annotationValue)
   {
      return annotationValue.map(new AnnotationValueMapper<>()
      {
         @Override
         public String string(String value)
         {
            return '\"' + value + '\"';
         }

         @Override
         public String aBoolean(Boolean value)
         {
            return String.valueOf(value.booleanValue());
         }

         @Override
         public String aByte(Byte value)
         {
            return Byte.toString(value);
         }

         @Override
         public String aShort(Short value)
         {
            return Short.toString(value);
         }

         @Override
         public String integer(Integer value)
         {
            return Integer.toString(value);
         }

         @Override
         public String aLong(Long value)
         {
            return Long.toString(value) + 'L';
         }

         @Override
         public String character(Character value)
         {
            return '\'' + Character.toString(value) + '\'';
         }

         @Override
         public String aFloat(Float value)
         {
            return Float.toString(value) + 'F';
         }

         @Override
         public String aDouble(Double value)
         {
            return Double.toString(value) + 'D';
         }

         @Override
         public String type(Shadow<TypeMirror> value)
         {
            return ShadowRendererImpl.classDeclaration(context, convert(value).toDeclaredOrThrow());
         }

         @Override
         public String enumConstant(EnumConstant value)
         {
            return EnumConstantRendererImpl.type(context, value);
         }

         @Override
         public String annotationUsage(AnnotationUsage value)
         {
            return usage(context, value);
         }

         @Override
         public String values(List<AnnotationValue> values)
         {
            return '{' + values.stream().map(annotationValue -> renderValue(context, annotationValue)).collect(Collectors.joining(", ")) + '}';
         }
      });
   }
}
