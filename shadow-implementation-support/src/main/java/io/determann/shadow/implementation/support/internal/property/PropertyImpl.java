package io.determann.shadow.implementation.support.internal.property;

import io.determann.shadow.api.C;
import io.determann.shadow.api.query.Implementation;
import io.determann.shadow.implementation.support.api.shadow.structure.PropertySupport;

import java.util.Optional;

import static io.determann.shadow.implementation.support.internal.SupportProvider.IMPLEMENTATION;

public class PropertyImpl implements C.Property
{
   private final String name;
   private final C.VariableType type;
   private final C.Field field;
   private final C.Method getter;
   private final C.Method setter;

   PropertyImpl(String name,
                C.VariableType type,
                C.Field field,
                C.Method getter,
                C.Method setter)
   {
      this.name = name;
      this.type = type;
      this.field = field;
      this.getter = getter;
      this.setter = setter;
   }

   public String getName()
   {
      return name;
   }

   public C.VariableType getType()
   {
      return type;
   }

   public Optional<C.Field> getField()
   {
      return Optional.ofNullable(field);
   }

   public C.Method getGetter()
   {
      return getter;
   }

   public Optional<C.Method> getSetter()
   {
      return Optional.ofNullable(setter);
   }

   public boolean isMutable()
   {
      return getSetter().isPresent();
   }

   @Override
   public int hashCode()
   {
      return PropertySupport.hashCode(this);
   }

   @Override
   public boolean equals(Object other)
   {
      return PropertySupport.equals(this, other);
   }

   @Override
   public String toString()
   {
      return PropertySupport.toString(this);
   }

   @Override
   public Implementation getImplementation()
   {
      return IMPLEMENTATION;
   }
}
