package io.determann.shadow.internal.reflection.shadow;

import io.determann.shadow.api.Provider;
import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.reflection.shadow.AnnotationUsageReflection;
import io.determann.shadow.api.reflection.shadow.AnnotationValueReflection;
import io.determann.shadow.api.reflection.shadow.structure.MethodReflection;
import io.determann.shadow.api.reflection.shadow.type.AnnotationReflection;
import io.determann.shadow.api.shadow.AnnotationUsage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static io.determann.shadow.api.Operations.ANNOTATION_USAGE_GET_ANNOTATION;
import static io.determann.shadow.api.Operations.ANNOTATION_USAGE_GET_VALUES;
import static io.determann.shadow.internal.reflection.ReflectionProvider.IMPLEMENTATION_NAME;
import static io.determann.shadow.internal.reflection.shadow.AnnotationValueImpl.create;

public class AnnotationUsageImpl implements AnnotationUsageReflection
{
   private final java.lang.annotation.Annotation annotation;

   public AnnotationUsageImpl(java.lang.annotation.Annotation annotation)
   {
      this.annotation = annotation;
   }

   @Override
   public Map<MethodReflection, AnnotationValueReflection> getValues()
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
   public AnnotationReflection getAnnotation()
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
