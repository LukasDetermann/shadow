package io.determann.shadow.api.shadow.module;

public interface DirectiveConsumer
{
   void exports(Exports exports);

   void opens(Opens opens);

   void provides(Provides provides);

   void requires(Requires requires);

   void uses(Uses uses);
}
