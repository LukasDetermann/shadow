package io.determann.shadow.internal.reflection.shadow;

import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.modifier.Modifier;
import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.reflection.query.NameableReflection;
import io.determann.shadow.api.reflection.query.ShadowReflection;
import io.determann.shadow.api.shadow.AnnotationUsage;
import io.determann.shadow.api.shadow.Shadow;
import io.determann.shadow.api.shadow.Variable;
import io.determann.shadow.internal.reflection.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static io.determann.shadow.internal.reflection.ReflectionProvider.IMPLEMENTATION_NAME;

public abstract class ReflectionFieldImpl<SURROUNDING extends Shadow> implements Variable,
                                                                                 NameableReflection,
                                                                                 ShadowReflection
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
      return ReflectionUtil.getModifiers(modifiers, false, false);
   }

   @Override
   public Shadow getType()
   {
      return ReflectionAdapter.generalize(field.getType());
   }

   @Override
   public TypeKind getKind()
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
                   .map(ReflectionAdapter::generalize)
                   .toList();
   }

   @Override
   public List<AnnotationUsage> getDirectAnnotationUsages()
   {
      return Arrays.stream(getField().getDeclaredAnnotations())
                   .map(ReflectionAdapter::generalize)
                   .toList();
   }

   public Field getField()
   {
      return field;
   }

   public Field getReflection()
   {
      return field;
   }

   @Override
   public String getImplementationName()
   {
      return IMPLEMENTATION_NAME;
   }
}
