package io.determann.shadow.internal.reflection.shadow;

import io.determann.shadow.api.property.ImmutableProperty;
import io.determann.shadow.api.property.MutableProperty;
import io.determann.shadow.api.property.Property;
import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.shadow.Class;
import io.determann.shadow.api.shadow.*;
import io.determann.shadow.internal.property.ImmutablePropertyImpl;
import io.determann.shadow.internal.property.MutablePropertyImpl;
import io.determann.shadow.internal.property.PropertyImpl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static io.determann.shadow.api.converter.Converter.convert;

public class ClassImpl extends DeclaredImpl implements Class
{
   private final List<Shadow> genericTypes;

   public ClassImpl(java.lang.Class<?> aClass)
   {
      this(aClass, Collections.emptyList());
   }

   public ClassImpl(java.lang.Class<?> aClass, List<Shadow> genericTypes)
   {
      super(aClass);
      this.genericTypes = genericTypes;
   }

   @Override
   public Class getSuperClass()
   {
      java.lang.Class<?> superclass = getaClass().getSuperclass();
      if (superclass == null)
      {
         return null;
      }
      return ReflectionAdapter.getShadow(superclass);
   }

   @Override
   public List<Class> getPermittedSubClasses()
   {
      return Arrays.stream(getaClass().getPermittedSubclasses())
                   .map(ReflectionAdapter::getShadow)
                   .map(Class.class::cast)
                   .toList();
   }

   @Override
   public List<Property> getProperties()
   {
      return PropertyImpl.of(this);
   }

   @Override
   public List<MutableProperty> getMutableProperties()
   {
      return MutablePropertyImpl.of(this);
   }

   @Override
   public List<ImmutableProperty> getImmutableProperties()
   {
      return ImmutablePropertyImpl.of(this);
   }

   @Override
   public boolean isAssignableFrom(Shadow shadow)
   {
      return convert(shadow)
            .toDeclared()
            .map(declared -> getaClass().isAssignableFrom(ReflectionAdapter.getReflection(declared)))
            .orElse(false);
   }

   @Override
   public Optional<Declared> getOuterType()
   {
      java.lang.Class<?> enclosingClass = getaClass().getEnclosingClass();
      if (enclosingClass == null)
      {
         return Optional.empty();
      }
      return Optional.of(ReflectionAdapter.getShadow(enclosingClass));
   }

   @Override
   public List<Shadow> getGenericTypes()
   {
      return genericTypes;
   }

   @Override
   public List<Generic> getGenerics()
   {
      return Arrays.stream(getaClass().getTypeParameters()).map(ReflectionAdapter::getShadow).map(Generic.class::cast).toList();
   }

   @Override
   public Primitive asUnboxed()
   {
      if (!getTypeKind().isPrimitive())
      {
         throw new IllegalArgumentException();
      }
      return new PrimitiveImpl(switch (getTypeKind())
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
      return shadow != null &&
             convert(shadow)
                      .toClass()
                      .map(aClass -> aClass.getGenericTypes()
                                           .stream()
                                           .allMatch(shadow1 -> getGenericTypes().stream()
                                                                                 .anyMatch(shadow1::representsSameType)))
                      .orElse(false);
   }
}
