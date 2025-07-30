package io.determann.shadow.internal.reflection.shadow.type;

import io.determann.shadow.api.C;
import io.determann.shadow.api.query.Implementation;
import io.determann.shadow.api.reflection.Adapter;
import io.determann.shadow.api.reflection.R;
import io.determann.shadow.implementation.support.api.shadow.type.ArraySupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static io.determann.shadow.api.query.Operations.*;
import static io.determann.shadow.api.query.Provider.requestOrThrow;
import static io.determann.shadow.api.reflection.Adapter.IMPLEMENTATION;

public class ArrayImpl implements R.Array
{
   private static final List<R.Type> PRIMITIVE_SUPERTYPES = List.of(new ClassImpl(Object.class),
                                                                    new InterfaceImpl(Cloneable.class),
                                                                    new InterfaceImpl(Serializable.class));
   private final Class<?> array;

   public ArrayImpl(Class<?> array)
   {
      this.array = array;
   }

   @Override
   public boolean isSubtypeOf(C.Type type)
   {
      C.Type componentType = getComponentType();
      if (isPrimitiveOrObject(componentType))
      {
         return PRIMITIVE_SUPERTYPES.stream().anyMatch(type::equals);
      }
      if (!(type instanceof C.Array otherArray))
      {
         return false;
      }
      C.Type otherComponentType = requestOrThrow(otherArray, ARRAY_GET_COMPONENT_TYPE);

      if (componentType instanceof C.Array nestedArray && otherComponentType instanceof C.Array)
      {
         return requestOrThrow(nestedArray, ARRAY_IS_SUBTYPE_OF, otherComponentType);
      }
      if (componentType instanceof C.Declared declared)
      {
         return requestOrThrow(declared, DECLARED_IS_SUBTYPE_OF, otherComponentType);
      }
      if (componentType instanceof C.RecordComponent recordComponent)
      {
         return requestOrThrow(recordComponent, RECORD_COMPONENT_IS_SUBTYPE_OF, otherComponentType);
      }
      if (componentType instanceof C.Variable variable)
      {
         return requestOrThrow(variable, VARIABLE_IS_SUBTYPE_OF, otherComponentType);
      }
      return false;
   }

   @Override
   public R.Type getComponentType()
   {
      return Adapter.generalize(getArray().getComponentType());
   }

   @Override
   public R.Array asArray()
   {
      return Adapter.generalize(array.arrayType());
   }

   @Override
   public List<R.Type> getDirectSuperTypes()
   {
      C.Type componentType = getComponentType();
      if (isPrimitiveOrObject(componentType))
      {
         return PRIMITIVE_SUPERTYPES;
      }
      if (componentType instanceof C.Array componentArray)
      {
         return requestOrThrow(componentArray, ARRAY_GET_DIRECT_SUPER_TYPES)
               .stream()
               .map(type ->
                    {
                       if (type instanceof R.Array array)
                       {
                          return Adapter.particularize(array);
                       }
                       if (type instanceof R.Declared declared)
                       {
                          return Adapter.particularize(declared);
                       }
                       if (type instanceof R.Primitive primitive)
                       {
                          return Adapter.particularize(primitive);
                       }
                       throw new IllegalStateException();
                    })
               .map(aClass -> java.lang.reflect.Array.newInstance(aClass, 0).getClass())
               .map(Adapter::generalize)
               .map(R.Type.class::cast)
               .toList();
      }

      List<Class<?>> directSuperTypes = new ArrayList<>();

      if (componentType instanceof C.Declared declared)
      {
         Class<?> reflection = Adapter.particularize((R.Declared) declared);
         directSuperTypes.add(reflection.getSuperclass());
         Collections.addAll(directSuperTypes, reflection.getInterfaces());
      }

      return directSuperTypes.stream()
                             .map(aClass -> java.lang.reflect.Array.newInstance(aClass, 0).getClass())
                             .map(Adapter::generalize)
                             .map(R.Type.class::cast)
                             .toList();
   }

   private static boolean isPrimitiveOrObject(C.Type componentType)
   {
      return componentType instanceof C.Primitive ||
             componentType instanceof C.Declared declared &&
             requestOrThrow(declared, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME).equals("java.lang.Object");
   }

   @Override
   public boolean representsSameType(C.Type type)
   {
      return ArraySupport.representsSameType(this, type);
   }

   public Class<?> getArray()
   {
      return array;
   }

   @Override
   public int hashCode()
   {
      return ArraySupport.hashCode(this);
   }

   @Override
   public boolean equals(Object other)
   {
      return ArraySupport.equals(this, other);
   }

   @Override
   public String toString()
   {
      return ArraySupport.toString(this);
   }

   public Class<?> getReflection()
   {
      return array;
   }

   @Override
   public Implementation getImplementation()
   {
      return IMPLEMENTATION;
   }
}
