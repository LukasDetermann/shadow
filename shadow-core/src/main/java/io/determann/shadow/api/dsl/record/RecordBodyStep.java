package io.determann.shadow.api.dsl.record;

import io.determann.shadow.api.dsl.class_.ClassRenderable;
import io.determann.shadow.api.dsl.constructor.ConstructorRenderable;
import io.determann.shadow.api.dsl.declared.DeclaredRenderable;
import io.determann.shadow.api.dsl.field.FieldRenderable;
import io.determann.shadow.api.dsl.method.MethodRenderable;

import java.util.Arrays;
import java.util.List;

public interface RecordBodyStep
      extends ClassRenderable
{
   ClassRenderable body(String body);

   RecordBodyStep field(String... fields);

   default RecordBodyStep field(FieldRenderable... fields)
   {
      return field(Arrays.asList(fields));
   }

   RecordBodyStep field(List<? extends FieldRenderable> fields);

   RecordBodyStep method(String... methods);

   default RecordBodyStep method(MethodRenderable... methods)
   {
      return method(Arrays.asList(methods));
   }

   RecordBodyStep method(List<? extends MethodRenderable> methods);

   RecordBodyStep inner(String... inner);

   default RecordBodyStep inner(DeclaredRenderable... inner)
   {
      return inner(Arrays.asList(inner));
   }

   RecordBodyStep inner(List<? extends DeclaredRenderable> inner);

   RecordBodyStep staticInitializer(String... staticInitializer);

   RecordBodyStep constructor(String... constructors);

   default RecordBodyStep constructor(ConstructorRenderable... constructors)
   {
      return constructor(Arrays.asList(constructors));
   }

   RecordBodyStep constructor(List<? extends ConstructorRenderable> constructors);
}