package io.determann.shadow.internal.reflection.shadow.type;

import io.determann.shadow.api.C;
import io.determann.shadow.api.reflection.Adapter;
import io.determann.shadow.api.reflection.R;
import io.determann.shadow.implementation.support.api.shadow.structure.PropertySupport;
import io.determann.shadow.implementation.support.api.shadow.type.ClassSupport;
import io.determann.shadow.internal.reflection.shadow.structure.PropertyImpl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ClassImpl extends DeclaredImpl implements R.Class
{
   private final List<R.Type> genericTypes;

   public ClassImpl(java.lang.Class<?> aClass)
   {
      this(aClass, Collections.emptyList());
   }

   public ClassImpl(java.lang.Class<?> aClass, List<R.Type> genericTypes)
   {
      super(aClass);
      this.genericTypes = genericTypes;
   }

   @Override
   public R.Class getSuperClass()
   {
      java.lang.Class<?> superclass = getaClass().getSuperclass();
      if (superclass == null)
      {
         return null;
      }
      return Adapter.generalize(superclass);
   }

   @Override
   public List<R.Class> getPermittedSubClasses()
   {
      return Arrays.stream(getaClass().getPermittedSubclasses())
                   .map(Adapter::generalize)
                   .map(R.Class.class::cast)
                   .toList();
   }

   @Override
   public List<R.Property> getProperties()
   {
      return PropertySupport.propertiesOf(this)
                            .stream()
                            .map(PropertyImpl::new)
                            .map(R.Property.class::cast)
                            .toList();
   }

   @Override
   public boolean isAssignableFrom(C.Type type)
   {
      return type instanceof C.Declared declared && getaClass().isAssignableFrom(Adapter.particularize((R.Declared) declared));
   }

   @Override
   public List<R.Type> getGenericUsages()
   {
      return genericTypes;
   }

   @Override
   public List<R.Generic> getGenericDeclarations()
   {
      return Arrays.stream(getaClass().getTypeParameters()).map(Adapter::generalize).map(R.Generic.class::cast).toList();
   }

   @Override
   public R.Primitive asUnboxed()
   {
      R.Type generalized = Adapter.generalize(getReflection());
      if (!(generalized instanceof C.Primitive))
      {
         throw new IllegalArgumentException();
      }
      return (R.Primitive) generalized;
   }

   @Override
   public boolean representsSameType(C.Type type)
   {
      return ClassSupport.representsSameType(this, type);
   }

   @Override
   public boolean equals(Object other)
   {
      return ClassSupport.equals(this, other);
   }

   @Override
   public int hashCode()
   {
      return ClassSupport.hashCode(this);
   }

   @Override
   public String toString()
   {
      return ClassSupport.toString(this);
   }
}
