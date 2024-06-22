package io.determann.shadow.internal.reflection.shadow;

import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.reflection.query.GenericReflection;
import io.determann.shadow.api.shadow.AnnotationUsage;
import io.determann.shadow.api.shadow.Generic;
import io.determann.shadow.api.shadow.Shadow;

import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static io.determann.shadow.api.converter.Converter.convert;
import static io.determann.shadow.api.reflection.ReflectionAdapter.generalize;
import static io.determann.shadow.internal.reflection.ReflectionProvider.IMPLEMENTATION_NAME;
import static io.determann.shadow.meta_meta.Operations.*;
import static io.determann.shadow.meta_meta.Provider.request;
import static io.determann.shadow.meta_meta.Provider.requestOrThrow;

public class GenericImpl implements GenericReflection
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
   public List<AnnotationUsage> getAnnotationUsages()
   {
      return Arrays.stream(getTypeVariable().getAnnotations()).map(ReflectionAdapter::generalize).toList();
   }

   @Override
   public List<AnnotationUsage> getDirectAnnotationUsages()
   {
      return Arrays.stream(getTypeVariable().getDeclaredAnnotations()).map(ReflectionAdapter::generalize).toList();
   }

   @Override
   public Shadow getExtends()
   {
      java.lang.reflect.Type[] bounds = getTypeVariable().getBounds();
      if (bounds.length == 1)
      {
         return generalize(bounds[0]);
      }
      return new IntersectionImpl(bounds);
   }

   @Override
   public Optional<Shadow> getSuper()
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
   public TypeKind getKind()
   {
      return TypeKind.GENERIC;
   }

   @Override
   public boolean representsSameType(Shadow shadow)
   {
      return shadow != null &&
             convert(shadow)
                   .toGeneric()
                   .map(generic ->
                        {
                           Shadow aExtends = requestOrThrow(generic, GENERIC_GET_EXTENDS);
                           Optional<Shadow> aSuper = request(generic, GENERIC_GET_SUPER);

                           return requestOrThrow(aExtends, SHADOW_REPRESENTS_SAME_TYPE, getExtends()) &&
                                  ((aSuper.isEmpty() && getSuper().isEmpty()) ||
                                   (aSuper.isPresent() && getSuper().isPresent()) &&
                                   requestOrThrow(aSuper.get(), SHADOW_REPRESENTS_SAME_TYPE, getSuper().get()));
                        })
                   .orElse(false);
   }

   public TypeVariable<?> getTypeVariable()
   {
      return typeVariable;
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(getName(),
                          getExtends(),
                          getSuper());
   }

   @Override
   public boolean equals(Object other)
   {
      if (other == this)
      {
         return true;
      }
      if (!(other instanceof Generic otherGeneric))
      {
         return false;
      }

      return request(otherGeneric, NAMEABLE_NAME).map(name -> Objects.equals(getName(), name)).orElse(false) &&
             request(otherGeneric, GENERIC_GET_EXTENDS).map(name -> Objects.equals(getExtends(), name)).orElse(false) &&
             Objects.equals(request(otherGeneric, GENERIC_GET_SUPER), getSuper());
   }

   public TypeVariable<?> getReflection()
   {
      return getTypeVariable();
   }


   @Override
   public String getImplementationName()
   {
      return IMPLEMENTATION_NAME;
   }
}