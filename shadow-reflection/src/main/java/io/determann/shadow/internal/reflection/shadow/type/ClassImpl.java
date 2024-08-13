package io.determann.shadow.internal.reflection.shadow.type;

import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.reflection.shadow.structure.PropertyReflection;
import io.determann.shadow.api.reflection.shadow.type.ClassReflection;
import io.determann.shadow.api.shadow.type.Class;
import io.determann.shadow.api.shadow.type.*;
import io.determann.shadow.implementation.support.api.shadow.structure.PropertySupport;
import io.determann.shadow.implementation.support.api.shadow.type.ClassSupport;
import io.determann.shadow.internal.reflection.shadow.structure.PropertyImpl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static io.determann.shadow.api.shadow.Operations.CLASS_GET_GENERIC_TYPES;
import static io.determann.shadow.api.shadow.Operations.SHADOW_REPRESENTS_SAME_TYPE;
import static io.determann.shadow.api.shadow.Provider.requestOrThrow;

public class ClassImpl extends DeclaredImpl implements ClassReflection
{
   private final List<Shadow> genericShadows;

   public ClassImpl(java.lang.Class<?> aClass)
   {
      this(aClass, Collections.emptyList());
   }

   public ClassImpl(java.lang.Class<?> aClass, List<Shadow> genericShadows)
   {
      super(aClass);
      this.genericShadows = genericShadows;
   }

   @Override
   public Class getSuperClass()
   {
      java.lang.Class<?> superclass = getaClass().getSuperclass();
      if (superclass == null)
      {
         return null;
      }
      return ReflectionAdapter.generalize(superclass);
   }

   @Override
   public List<Class> getPermittedSubClasses()
   {
      return Arrays.stream(getaClass().getPermittedSubclasses())
                   .map(ReflectionAdapter::generalize)
                   .map(Class.class::cast)
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
      return shadow instanceof Declared declared && getaClass().isAssignableFrom(ReflectionAdapter.particularize(declared));
   }

   @Override
   public Optional<Declared> getOuterType()
   {
      java.lang.Class<?> enclosingClass = getaClass().getEnclosingClass();
      if (enclosingClass == null)
      {
         return Optional.empty();
      }
      return Optional.of(ReflectionAdapter.generalize(enclosingClass));
   }

   @Override
   public List<Shadow> getGenericTypes()
   {
      return genericShadows;
   }

   @Override
   public List<Generic> getGenerics()
   {
      return Arrays.stream(getaClass().getTypeParameters()).map(ReflectionAdapter::generalize).map(Generic.class::cast).toList();
   }

   @Override
   public Primitive asUnboxed()
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
      return shadow instanceof Class aClass && requestOrThrow(aClass, CLASS_GET_GENERIC_TYPES)
            .stream()
            .allMatch(shadow1 -> getGenericTypes().stream().anyMatch(shadow2 -> requestOrThrow(shadow2, SHADOW_REPRESENTS_SAME_TYPE, shadow1)));
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
