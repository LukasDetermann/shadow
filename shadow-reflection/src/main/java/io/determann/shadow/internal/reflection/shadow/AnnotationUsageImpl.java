package io.determann.shadow.internal.reflection.shadow;

import io.determann.shadow.api.C;
import io.determann.shadow.api.query.Implementation;
import io.determann.shadow.api.query.Provider;
import io.determann.shadow.api.reflection.Adapter;
import io.determann.shadow.api.reflection.R;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static io.determann.shadow.api.query.Operations.ANNOTATION_USAGE_GET_ANNOTATION;
import static io.determann.shadow.api.query.Operations.ANNOTATION_USAGE_GET_VALUES;
import static io.determann.shadow.api.reflection.Adapter.IMPLEMENTATION;
import static io.determann.shadow.internal.reflection.shadow.AnnotationValueImpl.create;

public class AnnotationUsageImpl implements R.AnnotationUsage
{
   private final java.lang.annotation.Annotation annotation;

   public AnnotationUsageImpl(java.lang.annotation.Annotation annotation)
   {
      this.annotation = annotation;
   }

   @Override
   public Map<R.Method, R.AnnotationValue> getValues()
   {
      return Arrays.stream(annotation.annotationType().getDeclaredMethods())
                   .filter(method -> method.getParameterCount() == 0)
                   .filter(method -> !Modifier.isStatic(method.getModifiers() & Modifier.methodModifiers()))
                   .collect(Collectors.toMap(Adapter::generalize,
                                             method ->
                                             {
                                                Object defaultValue = method.getDefaultValue();
                                                if (defaultValue != null)
                                                {
                                                   return  create(defaultValue, true);
                                                }
                                                try
                                                {
                                                   return create(method.invoke(annotation), false);
                                                }
                                                catch (IllegalAccessException | InvocationTargetException e)
                                                {
                                                   throw new RuntimeException(e);
                                                }
                                             }));
   }

   @Override
   public R.Annotation getAnnotation()
   {
      return Adapter.generalize(annotation.annotationType());
   }


   @Override
   public int hashCode()
   {
      return Objects.hash(getAnnotation(),
                          getValues());
   }

   @Override
   public boolean equals(Object other)
   {
      if (other == this)
      {
         return true;
      }
      if (!(other instanceof C.AnnotationUsage otherAnnotationUsage))
      {
         return false;
      }
      return Provider.requestOrEmpty(otherAnnotationUsage, ANNOTATION_USAGE_GET_ANNOTATION).map(name -> Objects.equals(getAnnotation(), name)).orElse(false) &&
             Provider.requestOrEmpty(otherAnnotationUsage, ANNOTATION_USAGE_GET_VALUES).map(values -> Objects.equals(getValues(), values)).orElse(false);
   }

   public java.lang.annotation.Annotation getAnnotationReflection()
   {
      return annotation;
   }

   @Override
   public Implementation getImplementation()
   {
      return IMPLEMENTATION;
   }
}
