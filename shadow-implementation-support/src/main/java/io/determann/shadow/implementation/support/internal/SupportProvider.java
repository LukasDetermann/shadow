package io.determann.shadow.implementation.support.internal;

import io.determann.shadow.api.query.Implementation;
import io.determann.shadow.implementation.support.api.provider.AbstractProvider;
import io.determann.shadow.implementation.support.api.provider.MappingBuilder;
import io.determann.shadow.implementation.support.internal.property.PropertyImpl;

import static io.determann.shadow.api.query.Operations.*;

public class SupportProvider extends AbstractProvider
{
   public static final Implementation IMPLEMENTATION = new Implementation("io.determann.shadow-implementation-support");
   
   @Override
   public Implementation getImplementation()
   {
      return IMPLEMENTATION;
   }

   @Override
   protected void addMappings(MappingBuilder builder)
   {
      builder.with(NAMEABLE_GET_NAME, property -> ((PropertyImpl) property).getName())
             .with(PROPERTY_GET_TYPE, property -> ((PropertyImpl) property).getType())
             .withOptional(PROPERTY_GET_FIELD, property -> ((PropertyImpl) property).getField())
             .with(PROPERTY_GET_GETTER, property -> ((PropertyImpl) property).getGetter())
             .withOptional(PROPERTY_GET_SETTER, property -> ((PropertyImpl) property).getSetter())
             .with(PROPERTY_IS_MUTABLE, property -> ((PropertyImpl) property).isMutable());
   }
}
