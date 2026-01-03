package io.determann.shadow.internal.annotation_processing.shadow.structure;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.query.Implementation;

import java.util.Objects;
import java.util.Optional;

import static java.util.Optional.ofNullable;

public class PropertyImpl
      implements Ap.Property
{
   private final Ap.Context context;

   private final String name;
   private final Ap.VariableType type;
   private final Ap.Field field;
   private final Ap.Method getter;
   private final Ap.Method setter;

   PropertyImpl(Ap.Context api,
                String name,
                Ap.VariableType type,
                Ap.Field field,
                Ap.Method getter,
                Ap.Method setter)
   {
      this.name = name;
      this.type = type;
      this.field = field;
      this.getter = getter;
      this.setter = setter;
      this.context = api;
   }

   @Override
   public String getName()
   {
      return name;
   }

   @Override
   public Ap.VariableType getType()
   {
      return type;
   }

   @Override
   public Optional<Ap.Field> getField()
   {
      return ofNullable(field);
   }

   @Override
   public Ap.Field getFieldOrThrow()
   {
      return getField().orElseThrow();
   }

   @Override
   public Ap.Method getGetter()
   {
      return getter;
   }

   @Override
   public Optional<Ap.Method> getSetter()
   {
      return ofNullable(setter);
   }

   @Override
   public Ap.Method getSetterOrThrow()
   {
      return getSetter().orElseThrow();
   }

   @Override
   public boolean isMutable()
   {
      return getSetter().isPresent();
   }

   @Override
   public Implementation getImplementation()
   {
      return context.getImplementation();
   }

   @Override
   public boolean equals(Object other)
   {
      return other instanceof Ap.Property property &&
             Objects.equals(getName(), property.getName()) &&
             Objects.equals(getType(), property.getType());
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(getName(), getType());
   }

   @Override
   public String toString()
   {
      return "Property{" +
             "name='" + getName() + '\'' +
             ", type=" + getType() +
             '}';
   }
}
