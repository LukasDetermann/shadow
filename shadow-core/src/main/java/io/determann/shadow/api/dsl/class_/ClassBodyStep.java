package io.determann.shadow.api.dsl.class_;

import io.determann.shadow.api.dsl.constructor.ConstructorRenderable;
import io.determann.shadow.api.dsl.declared.DeclaredRenderable;
import io.determann.shadow.api.dsl.field.FieldRenderable;
import io.determann.shadow.api.dsl.method.MethodRenderable;
import io.determann.shadow.api.shadow.structure.C_Constructor;
import io.determann.shadow.api.shadow.structure.C_Field;
import io.determann.shadow.api.shadow.structure.C_Method;
import io.determann.shadow.api.shadow.type.C_Declared;

public interface ClassBodyStep extends ClassRenderable
{
   ClassRenderable body(String body);

   ClassBodyStep field(String... fields);

   ClassBodyStep field(C_Field... fields);
   
   ClassBodyStep field(FieldRenderable... fields);

   ClassBodyStep method(String... methods);

   ClassBodyStep method(C_Method... methods);

   ClassBodyStep method(MethodRenderable... methods);

   ClassBodyStep inner(String... inner);

   ClassBodyStep inner(C_Declared... inner);

   ClassBodyStep inner(DeclaredRenderable... inner);

   ClassBodyStep instanceInitializer(String... instanceInitializers);

   ClassBodyStep staticInitializer(String... staticInitializer);

   ClassBodyStep constructor(String... constructors);

   ClassBodyStep constructor(C_Constructor... constructors);

   ClassBodyStep constructor(ConstructorRenderable... constructors);
}
