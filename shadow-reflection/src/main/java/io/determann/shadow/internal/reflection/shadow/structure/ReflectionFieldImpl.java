package io.determann.shadow.internal.reflection.shadow.structure;

import io.determann.shadow.api.C;
import io.determann.shadow.api.Modifier;
import io.determann.shadow.api.query.Implementation;
import io.determann.shadow.api.reflection.Adapter;
import io.determann.shadow.api.reflection.R;
import io.determann.shadow.internal.reflection.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static io.determann.shadow.api.reflection.Adapter.IMPLEMENTATION;

public abstract class ReflectionFieldImpl<SURROUNDING extends C.Type>
{
   private final Field field;

   public ReflectionFieldImpl(Field field)
   {
      this.field = field;
   }

   public Set<Modifier> getModifiers()
   {
      int modifiers = getField().getModifiers() & java.lang.reflect.Modifier.fieldModifiers();

      boolean isPackagePrivate = !java.lang.reflect.Modifier.isPublic(modifiers) &&
                                 !java.lang.reflect.Modifier.isPrivate(modifiers) &&
                                 !java.lang.reflect.Modifier.isProtected(modifiers);

      return ReflectionUtil.getModifiers(modifiers, false, false, false, isPackagePrivate);
   }

   public R.VariableType getType()
   {
      return Adapter.generalize(field.getType());
   }

   public String getName()
   {
      return field.getName();
   }

   public List<R.AnnotationUsage> getAnnotationUsages()
   {
      return Arrays.stream(getField().getAnnotations())
                   .map(Adapter::generalize)
                   .toList();
   }

   public List<R.AnnotationUsage> getDirectAnnotationUsages()
   {
      return Arrays.stream(getField().getDeclaredAnnotations())
                   .map(Adapter::generalize)
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
