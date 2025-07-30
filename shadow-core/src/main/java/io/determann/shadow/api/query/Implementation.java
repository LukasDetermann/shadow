package io.determann.shadow.api.query;

import java.util.Objects;

public class Implementation
{
   private final String name;

   public Implementation(String name)
   {
      this.name = name;
   }

   public String getName()
   {
      return name;
   }

   @Override
   public final boolean equals(Object o)
   {
      return o instanceof Implementation that && Objects.equals(getName(), that.getName());
   }

   @Override
   public int hashCode()
   {
      return Objects.hashCode(getName());
   }

   @Override
   public String toString()
   {
      return name;
   }
}
