package io.determann.shadow.internal.reflection.shadow.structure;

import io.determann.shadow.api.Implementation;
import io.determann.shadow.api.reflection.R_Adapter;
import io.determann.shadow.api.reflection.shadow.R_AnnotationUsage;
import io.determann.shadow.api.reflection.shadow.type.R_VariableType;
import io.determann.shadow.api.shadow.modifier.C_Modifier;
import io.determann.shadow.api.shadow.type.C_Type;
import io.determann.shadow.internal.reflection.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static io.determann.shadow.api.reflection.R_Adapter.IMPLEMENTATION;

public abstract class ReflectionFieldImpl<SURROUNDING extends C_Type>
{
   private final Field field;

   public ReflectionFieldImpl(Field field)
   {
      this.field = field;
   }

   public Set<C_Modifier> getModifiers()
   {
      int modifiers = getField().getModifiers() & java.lang.reflect.Modifier.fieldModifiers();

      boolean isPackagePrivate = !java.lang.reflect.Modifier.isPublic(modifiers) &&
                                 !java.lang.reflect.Modifier.isPrivate(modifiers) &&
                                 !java.lang.reflect.Modifier.isProtected(modifiers);

      return ReflectionUtil.getModifiers(modifiers, false, false, false, isPackagePrivate);
   }

   public R_VariableType getType()
   {
      return R_Adapter.generalize(field.getType());
   }

   public String getName()
   {
      return field.getName();
   }

   public List<R_AnnotationUsage> getAnnotationUsages()
   {
      return Arrays.stream(getField().getAnnotations())
                   .map(R_Adapter::generalize)
                   .toList();
   }

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

   public Implementation getImplementation()
   {
      return IMPLEMENTATION;
   }
}
