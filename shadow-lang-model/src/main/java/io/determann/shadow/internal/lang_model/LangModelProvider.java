package io.determann.shadow.internal.lang_model;

import io.determann.shadow.api.lang_model.query.LangModelQueries;
import io.determann.shadow.meta_meta.AbstractProvider;
import io.determann.shadow.meta_meta.MappingBuilder;
import io.determann.shadow.meta_meta.Operations;

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
      builder.with(Operations.NAMEABLE_NAME, nameable -> LangModelQueries.query(nameable).getName())
             .withOptional(Operations.WILDCARD_EXTENDS, wildcard -> LangModelQueries.query(wildcard).getExtends())
             .withOptional(Operations.WILDCARD_SUPER, wildcard -> LangModelQueries.query(wildcard).getSuper());

   }
}
