package io.determann.shadow.internal.reflection;

import io.determann.shadow.api.reflection.query.ReflectionQueries;
import io.determann.shadow.meta_meta.AbstractProvider;
import io.determann.shadow.meta_meta.MappingBuilder;
import io.determann.shadow.meta_meta.Operations;

public class ReflectionProvider extends AbstractProvider
{
   public static final String IMPLEMENTATION_NAME = "io.determann.shadow-reflection";

   @Override
   public String getImplementationName()
   {
      return IMPLEMENTATION_NAME;
   }

   @Override
   protected void addMappings(MappingBuilder builder)
   {
      builder.with(Operations.NAMEABLE_NAME, nameable -> ReflectionQueries.query(nameable).getName())
             .withOptional(Operations.WILDCARD_EXTENDS, wildcard -> ReflectionQueries.query(wildcard).getExtends())
             .withOptional(Operations.WILDCARD_SUPER, wildcard -> ReflectionQueries.query(wildcard).getSuper())
             .with(Operations.PRIMITIVE_IS_ASSIGNABLE_FROM, (primitive, shadow) -> ReflectionQueries.query(primitive).isAssignableFrom(shadow))
             .with(Operations.PRIMITIVE_IS_SUBTYPE_OF, (primitive, shadow) -> ReflectionQueries.query(primitive).isSubtypeOf(shadow));
   }
}
