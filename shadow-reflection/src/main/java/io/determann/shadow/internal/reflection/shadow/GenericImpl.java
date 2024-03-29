package io.determann.shadow.internal.reflection.shadow;

import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.shadow.AnnotationUsage;
import io.determann.shadow.api.shadow.Generic;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.Shadow;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static io.determann.shadow.api.converter.Converter.convert;

public class GenericImpl implements Generic
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
      return Arrays.stream(getTypeVariable().getAnnotations()).map(ReflectionAdapter::getAnnotationUsage).toList();
   }

   @Override
   public List<AnnotationUsage> getDirectAnnotationUsages()
   {
      return Arrays.stream(getTypeVariable().getDeclaredAnnotations()).map(ReflectionAdapter::getAnnotationUsage).toList();
   }

   @Override
   public Shadow getExtends()
   {
      Type[] bounds = getTypeVariable().getBounds();
      if (bounds.length == 1)
      {
         return ReflectionAdapter.getShadow(bounds[0]);
      }
      return new IntersectionImpl(bounds);
   }

   @Override
   public Optional<Shadow> getSuper()
   {
      return Optional.empty();
   }

   @Override
   public Shadow getEnclosing()
   {
      return ReflectionAdapter.getShadow(getTypeVariable().getGenericDeclaration());
   }

   @Override
   public Package getPackage()
   {
      return switch (getEnclosing().getTypeKind())
      {
         case CLASS, INTERFACE, ENUM, ANNOTATION, RECORD -> convert(getEnclosing()).toDeclaredOrThrow().getPackage();
         case METHOD, CONSTRUCTOR -> convert(getEnclosing()).toExecutableOrThrow().getPackage();
         case ENUM_CONSTANT, FIELD, PARAMETER -> convert(getEnclosing()).toVariableOrThrow().getPackage();
         case GENERIC -> convert(getEnclosing()).toGenericOrThrow().getPackage();
         default -> throw new IllegalStateException();
      };
   }

   @Override
   public TypeKind getTypeKind()
   {
      return TypeKind.GENERIC;
   }

   @Override
   public boolean representsSameType(Shadow shadow)
   {
      return shadow != null &&
             convert(shadow)
                   .toGeneric()
                   .map(generic -> generic.getExtends().representsSameType(getExtends()) &&
                                   ((generic.getSuper().isEmpty() && getSuper().isEmpty()) ||
                                    (generic.getSuper().isPresent() && getSuper().isPresent()) &&
                                    generic.getSuper().get().representsSameType(getSuper().get())))
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
      return Objects.equals(getName(), otherGeneric.getName()) &&
             Objects.equals(getExtends(), otherGeneric.getExtends()) &&
             Objects.equals(getSuper(), otherGeneric.getSuper());
   }

   public TypeVariable<?> getReflection()
   {
      return getTypeVariable();
   }
}