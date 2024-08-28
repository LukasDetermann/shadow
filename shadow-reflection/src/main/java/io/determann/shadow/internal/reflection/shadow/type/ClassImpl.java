package io.determann.shadow.internal.reflection.shadow.type;

import io.determann.shadow.api.reflection.R_Adapter;
import io.determann.shadow.api.reflection.shadow.structure.R_Property;
import io.determann.shadow.api.reflection.shadow.type.R_Class;
import io.determann.shadow.api.reflection.shadow.type.R_Declared;
import io.determann.shadow.api.reflection.shadow.type.R_Generic;
import io.determann.shadow.api.reflection.shadow.type.R_Type;
import io.determann.shadow.api.reflection.shadow.type.primitive.R_Primitive;
import io.determann.shadow.api.shadow.type.C_Declared;
import io.determann.shadow.api.shadow.type.C_Type;
import io.determann.shadow.api.shadow.type.primitive.C_Primitive;
import io.determann.shadow.implementation.support.api.shadow.structure.PropertySupport;
import io.determann.shadow.implementation.support.api.shadow.type.ClassSupport;
import io.determann.shadow.internal.reflection.shadow.structure.PropertyImpl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ClassImpl extends DeclaredImpl implements R_Class
{
   private final List<R_Type> genericTypes;

   public ClassImpl(java.lang.Class<?> aClass)
   {
      this(aClass, Collections.emptyList());
   }

   public ClassImpl(java.lang.Class<?> aClass, List<R_Type> genericTypes)
   {
      super(aClass);
      this.genericTypes = genericTypes;
   }

   @Override
   public R_Class getSuperClass()
   {
      java.lang.Class<?> superclass = getaClass().getSuperclass();
      if (superclass == null)
      {
         return null;
      }
      return R_Adapter.generalize(superclass);
   }

   @Override
   public List<R_Class> getPermittedSubClasses()
   {
      return Arrays.stream(getaClass().getPermittedSubclasses())
                   .map(R_Adapter::generalize)
                   .map(R_Class.class::cast)
                   .toList();
   }

   @Override
   public List<R_Property> getProperties()
   {
      return PropertySupport.propertiesOf(this)
                            .stream()
                            .map(PropertyImpl::new)
                            .map(R_Property.class::cast)
                            .toList();
   }

   @Override
   public boolean isAssignableFrom(C_Type type)
   {
      return type instanceof C_Declared declared && getaClass().isAssignableFrom(R_Adapter.particularize((R_Declared) declared));
   }

   @Override
   public Optional<R_Declared> getOuterType()
   {
      java.lang.Class<?> enclosingClass = getaClass().getEnclosingClass();
      if (enclosingClass == null)
      {
         return Optional.empty();
      }
      return Optional.of(R_Adapter.generalize(enclosingClass));
   }

   @Override
   public List<R_Type> getGenericTypes()
   {
      return genericTypes;
   }

   @Override
   public List<R_Generic> getGenerics()
   {
      return Arrays.stream(getaClass().getTypeParameters()).map(R_Adapter::generalize).map(R_Generic.class::cast).toList();
   }

   @Override
   public R_Primitive asUnboxed()
   {
      R_Type generalized = R_Adapter.generalize(getReflection());
      if (!(generalized instanceof C_Primitive))
      {
         throw new IllegalArgumentException();
      }
      return (R_Primitive) generalized;
   }

   @Override
   public boolean representsSameType(C_Type type)
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
