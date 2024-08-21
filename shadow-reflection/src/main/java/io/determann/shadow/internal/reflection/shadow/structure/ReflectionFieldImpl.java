package io.determann.shadow.internal.reflection.shadow.structure;

import io.determann.shadow.api.reflection.R_Adapter;
import io.determann.shadow.api.reflection.shadow.R_AnnotationUsage;
import io.determann.shadow.api.reflection.shadow.structure.R_Variable;
import io.determann.shadow.api.reflection.shadow.type.R_Shadow;
import io.determann.shadow.api.shadow.C_TypeKind;
import io.determann.shadow.api.shadow.modifier.C_Modifier;
import io.determann.shadow.api.shadow.type.C_Shadow;
import io.determann.shadow.internal.reflection.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static io.determann.shadow.internal.reflection.ReflectionProvider.IMPLEMENTATION_NAME;

public abstract class ReflectionFieldImpl<SURROUNDING extends C_Shadow> implements R_Variable
{
   private final Field field;

   public ReflectionFieldImpl(Field field)
   {
      this.field = field;
   }

   @Override
   public Set<C_Modifier> getModifiers()
   {
      int modifiers = getField().getModifiers() & java.lang.reflect.Modifier.fieldModifiers();

      boolean isPackagePrivate = !java.lang.reflect.Modifier.isPublic(modifiers) &&
                                 !java.lang.reflect.Modifier.isPrivate(modifiers) &&
                                 !java.lang.reflect.Modifier.isProtected(modifiers);

      return ReflectionUtil.getModifiers(modifiers, false, false, false, isPackagePrivate);
   }

   @Override
   public R_Shadow getType()
   {
      return R_Adapter.generalize(field.getType());
   }

   @Override
   public C_TypeKind getKind()
   {
      if (field.isEnumConstant())
      {
         return C_TypeKind.ENUM_CONSTANT;
      }
      return C_TypeKind.FIELD;
   }

   @Override
   public String getName()
   {
      return field.getName();
   }

   @Override
   public List<R_AnnotationUsage> getAnnotationUsages()
   {
      return Arrays.stream(getField().getAnnotations())
                   .map(R_Adapter::generalize)
                   .toList();
   }

   @Override
   public List<R_AnnotationUsage> getDirectAnnotationUsages()
   {
      return Arrays.stream(getField().getDeclaredAnnotations())
                   .map(R_Adapter::generalize)
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
