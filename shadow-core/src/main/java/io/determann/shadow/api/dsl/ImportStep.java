package io.determann.shadow.api.dsl;

import io.determann.shadow.api.shadow.type.C_Declared;

public interface ImportStep extends ModifierStep
{
   ImportStep import_(String... names);

   ImportStep import_(C_Declared... declared);
}
