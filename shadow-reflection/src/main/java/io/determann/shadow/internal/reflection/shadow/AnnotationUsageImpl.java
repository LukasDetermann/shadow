package io.determann.shadow.internal.reflection.shadow;

import io.determann.shadow.api.Provider;
import io.determann.shadow.api.reflection.R_Adapter;
import io.determann.shadow.api.reflection.shadow.R_AnnotationUsage;
import io.determann.shadow.api.reflection.shadow.R_AnnotationValue;
import io.determann.shadow.api.reflection.shadow.structure.R_Method;
import io.determann.shadow.api.reflection.shadow.type.R_Annotation;
import io.determann.shadow.api.shadow.C_AnnotationUsage;

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

public class AnnotationUsageImpl implements R_AnnotationUsage
{
   private final java.lang.annotation.Annotation annotation;

   public AnnotationUsageImpl(java.lang.annotation.Annotation annotation)
   {
      this.annotation = annotation;
   }

   @Override
   public Map<R_Method, R_AnnotationValue> getValues()
   {
      return Arrays.stream(annotation.annotationType().getDeclaredMethods())
                   .filter(method -> method.getParameterCount() == 0)
                   .filter(method -> !Modifier.isStatic(method.getModifiers() & Modifier.methodModifiers()))
                   .collect(Collectors.toMap(R_Adapter::generalize,
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
   public R_Annotation getAnnotation()
   {
      return R_Adapter.generalize(annotation.annotationType());
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
      if (!(other instanceof C_AnnotationUsage otherAnnotationUsage))
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
