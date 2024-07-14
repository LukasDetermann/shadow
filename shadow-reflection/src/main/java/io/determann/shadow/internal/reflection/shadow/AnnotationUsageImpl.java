package io.determann.shadow.internal.reflection.shadow;

import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.reflection.shadow.AnnotationUsageReflection;
import io.determann.shadow.api.shadow.Provider;
import io.determann.shadow.api.shadow.annotationusage.AnnotationUsage;
import io.determann.shadow.api.shadow.annotationusage.AnnotationValue;
import io.determann.shadow.api.shadow.structure.Method;
import io.determann.shadow.api.shadow.type.Annotation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static io.determann.shadow.api.shadow.Operations.ANNOTATION_USAGE_GET_ANNOTATION;
import static io.determann.shadow.api.shadow.Operations.ANNOTATION_USAGE_GET_VALUES;
import static io.determann.shadow.internal.reflection.ReflectionProvider.IMPLEMENTATION_NAME;

public class AnnotationUsageImpl implements AnnotationUsageReflection
{
   private final java.lang.annotation.Annotation annotation;

   public AnnotationUsageImpl(java.lang.annotation.Annotation annotation)
   {
      this.annotation = annotation;
   }

   @Override
   public Map<Method, AnnotationValue> getValues()
   {
      return Arrays.stream(annotation.annotationType().getDeclaredMethods())
                   .filter(method -> method.getParameterCount() == 0)
                   .filter(method -> !Modifier.isStatic(method.getModifiers() & Modifier.methodModifiers()))
                   .collect(Collectors.toMap(ReflectionAdapter::generalize,
                                             method ->
                                             {
                                                Object defaultValue = method.getDefaultValue();
                                                if (defaultValue != null)
                                                {
                                                   return new AnnotationValueImpl(defaultValue, true);
                                                }
                                                try
                                                {
                                                   return new AnnotationValueImpl(method.invoke(annotation), false);
                                                }
                                                catch (IllegalAccessException | InvocationTargetException e)
                                                {
                                                   throw new RuntimeException(e);
                                                }
                                             }));
   }

   @Override
   public Annotation getAnnotation()
   {
      return ReflectionAdapter.generalize(annotation.annotationType());
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
      if (!(other instanceof AnnotationUsage otherAnnotationUsage))
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
   public String getImplementationName()
   {
      return IMPLEMENTATION_NAME;
   }
}
