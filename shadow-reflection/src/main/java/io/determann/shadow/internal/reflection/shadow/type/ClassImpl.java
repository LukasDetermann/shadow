package io.determann.shadow.internal.reflection.shadow.type;

import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.reflection.shadow.structure.PropertyReflection;
import io.determann.shadow.api.reflection.shadow.type.*;
import io.determann.shadow.api.shadow.type.Declared;
import io.determann.shadow.api.shadow.type.Shadow;
import io.determann.shadow.implementation.support.api.shadow.structure.PropertySupport;
import io.determann.shadow.implementation.support.api.shadow.type.ClassSupport;
import io.determann.shadow.internal.reflection.shadow.structure.PropertyImpl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ClassImpl extends DeclaredImpl implements ClassReflection
{
   private final List<ShadowReflection> genericShadows;

   public ClassImpl(java.lang.Class<?> aClass)
   {
      this(aClass, Collections.emptyList());
   }

   public ClassImpl(java.lang.Class<?> aClass, List<ShadowReflection> genericShadows)
   {
      super(aClass);
      this.genericShadows = genericShadows;
   }

   @Override
   public ClassReflection getSuperClass()
   {
      java.lang.Class<?> superclass = getaClass().getSuperclass();
      if (superclass == null)
      {
         return null;
      }
      return ReflectionAdapter.generalize(superclass);
   }

   @Override
   public List<ClassReflection> getPermittedSubClasses()
   {
      return Arrays.stream(getaClass().getPermittedSubclasses())
                   .map(ReflectionAdapter::generalize)
                   .map(ClassReflection.class::cast)
                   .toList();
   }

   @Override
   public List<PropertyReflection> getProperties()
   {
      return PropertySupport.propertiesOf(this)
                            .stream()
                            .map(PropertyImpl::new)
                            .map(PropertyReflection.class::cast)
                            .toList();
   }

   @Override
   public boolean isAssignableFrom(Shadow shadow)
   {
      return shadow instanceof Declared declared && getaClass().isAssignableFrom(ReflectionAdapter.particularize((DeclaredReflection) declared));
   }

   @Override
   public Optional<DeclaredReflection> getOuterType()
   {
      java.lang.Class<?> enclosingClass = getaClass().getEnclosingClass();
      if (enclosingClass == null)
      {
         return Optional.empty();
      }
      return Optional.of(ReflectionAdapter.generalize(enclosingClass));
   }

   @Override
   public List<ShadowReflection> getGenericTypes()
   {
      return genericShadows;
   }

   @Override
   public List<GenericReflection> getGenerics()
   {
      return Arrays.stream(getaClass().getTypeParameters()).map(ReflectionAdapter::generalize).map(GenericReflection.class::cast).toList();
   }

   @Override
   public PrimitiveReflection asUnboxed()
   {
      if (!getKind().isPrimitive())
      {
         throw new IllegalArgumentException();
      }
      return new PrimitiveImpl(switch (getKind())
                               {
                                  case BOOLEAN -> boolean.class;
                                  case BYTE -> byte.class;
                                  case SHORT -> short.class;
                                  case INT -> int.class;
                                  case LONG -> long.class;
                                  case CHAR -> char.class;
                                  case FLOAT -> float.class;
                                  case DOUBLE -> double.class;
                                  default -> throw new IllegalArgumentException();
                               });
   }

   @Override
   public boolean representsSameType(Shadow shadow)
   {
      return ClassSupport.representsSameType(this, shadow);
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
