package com.derivandi.api.dsl.record;

import com.derivandi.api.dsl.class_.ClassRenderable;
import com.derivandi.api.dsl.constructor.ConstructorRenderable;
import com.derivandi.api.dsl.declared.DeclaredRenderable;
import com.derivandi.api.dsl.field.FieldRenderable;
import com.derivandi.api.dsl.method.MethodRenderable;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.List;

public interface RecordBodyStep
      extends ClassRenderable
{
   @Contract(value = "_ -> new", pure = true)
   ClassRenderable body(String body);

   @Contract(value = "_ -> new", pure = true)
   RecordBodyStep field(String... fields);

   @Contract(value = "_ -> new", pure = true)
   default RecordBodyStep field(FieldRenderable... fields)
   {
      return field(Arrays.asList(fields));
   }

   @Contract(value = "_ -> new", pure = true)
   RecordBodyStep field(List<? extends FieldRenderable> fields);

   @Contract(value = "_ -> new", pure = true)
   RecordBodyStep method(String... methods);

   @Contract(value = "_ -> new", pure = true)
   default RecordBodyStep method(MethodRenderable... methods)
   {
      return method(Arrays.asList(methods));
   }

   @Contract(value = "_ -> new", pure = true)
   RecordBodyStep method(List<? extends MethodRenderable> methods);

   @Contract(value = "_ -> new", pure = true)
   RecordBodyStep inner(String... inner);

   @Contract(value = "_ -> new", pure = true)
   default RecordBodyStep inner(DeclaredRenderable... inner)
   {
      return inner(Arrays.asList(inner));
   }

   @Contract(value = "_ -> new", pure = true)
   RecordBodyStep inner(List<? extends DeclaredRenderable> inner);

   @Contract(value = "_ -> new", pure = true)
   RecordBodyStep staticInitializer(String... staticInitializer);

   @Contract(value = "_ -> new", pure = true)
   RecordBodyStep constructor(String... constructors);

   @Contract(value = "_ -> new", pure = true)
   default RecordBodyStep constructor(ConstructorRenderable... constructors)
   {
      return constructor(Arrays.asList(constructors));
   }

   @Contract(value = "_ -> new", pure = true)
   RecordBodyStep constructor(List<? extends ConstructorRenderable> constructors);
}