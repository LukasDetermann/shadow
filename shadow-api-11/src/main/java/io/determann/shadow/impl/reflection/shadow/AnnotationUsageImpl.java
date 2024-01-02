package io.determann.shadow.impl.reflection.shadow;

import io.determann.shadow.api.annotationvalue.AnnotationValue;
import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.shadow.Annotation;
import io.determann.shadow.api.shadow.AnnotationUsage;
import io.determann.shadow.api.shadow.Method;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class AnnotationUsageImpl extends DeclaredImpl implements AnnotationUsage
{
   private final java.lang.annotation.Annotation annotation;

   public AnnotationUsageImpl(java.lang.annotation.Annotation annotation)
   {
      super(annotation.annotationType());
      this.annotation = annotation;
   }

   @Override
   public Map<Method, AnnotationValue> getValues()
   {
      return Arrays.stream(annotation.annotationType().getDeclaredMethods())
                   .filter(method -> method.getParameterCount() == 0)
                   .filter(method -> !Modifier.isStatic(method.getModifiers() & Modifier.methodModifiers()))
                   .collect(Collectors.toMap(ReflectionAdapter::getShadow,
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
      return this;
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
      if (!(other instanceof AnnotationUsage))
      {
         return false;
      }
      AnnotationUsage otherAnnotationUsage = ((AnnotationUsage) other);
      return Objects.equals(getAnnotation(), otherAnnotationUsage.getAnnotation()) &&
             Objects.equals(getValues(), otherAnnotationUsage.getValues());
   }

   public java.lang.annotation.Annotation getAnnotationReflection()
   {
      return annotation;
   }
}
