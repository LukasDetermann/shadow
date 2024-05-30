package io.determann.shadow.internal.lang_model;

import io.determann.shadow.meta_meta.AbstractProvider;
import io.determann.shadow.meta_meta.MappingBuilder;
import io.determann.shadow.meta_meta.Operations;

import static io.determann.shadow.api.lang_model.LangModelQueries.query;

public class LangModelProvider extends AbstractProvider
{

   public static final String IMPLEMENTATION_NAME = "io.determann.shadow-lang-model";

   @Override
   public String getImplementationName()
   {
      return IMPLEMENTATION_NAME;
   }


   @Override
   protected void addMappings(MappingBuilder builder)
   {
      builder.with(Operations.NAMEABLE_NAME, nameable -> query(nameable).getName())
             .with(Operations.QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME, qualifiedNameable -> query(qualifiedNameable).getQualifiedName())
             .with(Operations.SHADOW_GET_KIND, shadow -> query(shadow).getKind())
             .with(Operations.SHADOW_REPRESENTS_SAME_TYPE, (shadow, shadow2) -> query(shadow).representsSameType(shadow2))
             .withOptional(Operations.WILDCARD_EXTENDS, wildcard -> query(wildcard).getExtends())
             .withOptional(Operations.WILDCARD_SUPER, wildcard -> query(wildcard).getSuper())
             .with(Operations.PRIMITIVE_AS_BOXED, primitive -> query(primitive).asBoxed())
             .with(Operations.PRIMITIVE_IS_ASSIGNABLE_FROM, (primitive, shadow) -> query(primitive).isAssignableFrom(shadow))
             .with(Operations.PRIMITIVE_IS_SUBTYPE_OF, (primitive, shadow) -> query(primitive).isSubtypeOf(shadow));
   }
}
