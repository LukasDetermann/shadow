package io.determann.shadow.internal.reflection.shadow.type;

import io.determann.shadow.api.Implementation;
import io.determann.shadow.api.Provider;
import io.determann.shadow.api.reflection.R_Adapter;
import io.determann.shadow.api.reflection.shadow.R_AnnotationUsage;
import io.determann.shadow.api.reflection.shadow.type.R_Generic;
import io.determann.shadow.api.reflection.shadow.type.R_Type;
import io.determann.shadow.api.shadow.type.C_Generic;
import io.determann.shadow.api.shadow.type.C_Type;
import io.determann.shadow.implementation.support.api.shadow.type.GenericSupport;

import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.reflection.R_Adapter.IMPLEMENTATION;
import static io.determann.shadow.api.reflection.R_Adapter.generalize;

public class GenericImpl implements R_Generic
{
   private final TypeVariable<?> typeVariable;

   public GenericImpl(TypeVariable<?> typeVariable)
   {
      this.typeVariable = typeVariable;
   }

   @Override
   public String getName()
   {
      return getTypeVariable().getName();
   }

   @Override
   public List<R_AnnotationUsage> getAnnotationUsages()
   {
      return Arrays.stream(getTypeVariable().getAnnotations()).map(R_Adapter::generalize).toList();
   }

   @Override
   public List<R_AnnotationUsage> getDirectAnnotationUsages()
   {
      return Arrays.stream(getTypeVariable().getDeclaredAnnotations()).map(R_Adapter::generalize).toList();
   }

   @Override
   public R_Type getExtends()
   {
      java.lang.reflect.Type[] bounds = getTypeVariable().getBounds();
      if (bounds.length == 1)
      {
         return generalize(bounds[0]);
      }
      return new IntersectionImpl(bounds);
   }

   @Override
   public Optional<R_Type> getSuper()
   {
      return Optional.empty();
   }

   @Override
   public Object getEnclosing()
   {
      if (getTypeVariable().getGenericDeclaration() instanceof Class<?> aClass)
      {
         return generalize(aClass);
      }
      if (getTypeVariable().getGenericDeclaration() instanceof java.lang.reflect.Executable executable)
      {
         return generalize(executable);
      }
      throw new IllegalStateException();
   }

   @Override
   public boolean representsSameType(C_Type type)
   {
      if (!(type instanceof C_Generic generic))
      {
         return false;
      }

      C_Type aExtends = requestOrThrow(generic, GENERIC_GET_EXTENDS);
      Optional<C_Type> aSuper = Provider.requestOrEmpty(generic, GENERIC_GET_SUPER);

      return requestOrThrow(aExtends, TYPE_REPRESENTS_SAME_TYPE, getExtends()) &&
             ((aSuper.isEmpty() && getSuper().isEmpty()) ||
              (aSuper.isPresent() && getSuper().isPresent()) &&
              requestOrThrow(aSuper.get(), TYPE_REPRESENTS_SAME_TYPE, getSuper().get()));
   }

   public TypeVariable<?> getTypeVariable()
   {
      return typeVariable;
   }

   @Override
   public String toString()
   {
      return GenericSupport.toString(this);
   }

   @Override
   public int hashCode()
   {
      return GenericSupport.hashCode(this);
   }

   @Override
   public boolean equals(Object other)
   {
      return GenericSupport.equals(this, other);
   }

   public TypeVariable<?> getReflection()
   {
      return getTypeVariable();
   }


   @Override
   public Implementation getImplementation()
   {
      return IMPLEMENTATION;
   }
}