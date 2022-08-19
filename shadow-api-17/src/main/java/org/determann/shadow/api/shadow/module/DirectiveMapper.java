package org.determann.shadow.api.shadow.module;

public interface DirectiveMapper<T>
{
   T exports(Exports exports);

   T opens(Opens opens);

   T provides(Provides provides);

   T requires(Requires requires);

   T uses(Uses uses);
}
