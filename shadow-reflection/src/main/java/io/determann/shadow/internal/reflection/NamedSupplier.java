package io.determann.shadow.internal.reflection;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public class NamedSupplier<T>
{
   private String name;
   private T instance;
   private Supplier<T> supplier;
   private final Function<T, String> getName;

   public NamedSupplier(T instance, Function<T, String> getName)
   {
      this.instance = Objects.requireNonNull(instance);
      this.getName = Objects.requireNonNull(getName);
   }

   public NamedSupplier(String name, Supplier<T> supplier, Function<T, String> getName)
   {
      this.name = Objects.requireNonNull(name);
      this.supplier = Objects.requireNonNull(supplier);
      this.getName = Objects.requireNonNull(getName);
   }

   public String getName()
   {
      if (name == null)
      {
         name = Objects.requireNonNull(getName.apply(getInstance()));
      }
      return name;
   }

   public T getInstance()
   {
      if (instance == null)
      {
         instance = Objects.requireNonNull(supplier.get());
      }
      return instance;
   }
}
