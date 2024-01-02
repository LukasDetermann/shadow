package io.determann.shadow.impl.reflection.shadow;

import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.modifier.Modifier;
import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.shadow.AnnotationUsage;
import io.determann.shadow.api.shadow.Shadow;
import io.determann.shadow.api.shadow.Variable;
import io.determann.shadow.impl.reflection.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

public abstract class ReflectionFieldImpl<SURROUNDING extends Shadow> implements Variable<SURROUNDING>
{
   private final Field field;

   public ReflectionFieldImpl(Field field)
   {
      this.field = field;
   }

   @Override
   public Set<Modifier> getModifiers()
   {
      int modifiers = getField().getModifiers() & java.lang.reflect.Modifier.fieldModifiers();
      return ReflectionUtil.getModifiers(modifiers, false);
   }

   @Override
   public Shadow getType()
   {
      return ReflectionAdapter.getShadow(field.getType());
   }

   @Override
   public TypeKind getTypeKind()
   {
      if (field.isEnumConstant())
      {
         return TypeKind.ENUM_CONSTANT;
      }
      return TypeKind.FIELD;
   }

   @Override
   public String getName()
   {
      return field.getName();
   }

   @Override
   public List<AnnotationUsage> getAnnotationUsages()
   {
      return Arrays.stream(getField().getAnnotations())
                   .map(ReflectionAdapter::getAnnotationUsage)
                   .collect(collectingAndThen(toList(), Collections::unmodifiableList));
   }

   @Override
   public List<AnnotationUsage> getDirectAnnotationUsages()
   {
      return Arrays.stream(getField().getDeclaredAnnotations())
                   .map(ReflectionAdapter::getAnnotationUsage)
                   .collect(collectingAndThen(toList(), Collections::unmodifiableList));
   }

   public Field getField()
   {
      return field;
   }

   public Field getReflection()
   {
      return field;
   }
}
