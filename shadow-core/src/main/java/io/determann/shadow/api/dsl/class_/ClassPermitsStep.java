package io.determann.shadow.api.dsl.class_;

import io.determann.shadow.api.shadow.type.C_Declared;

public interface ClassPermitsStep extends ClassBodyStep
{
   ClassPermitsStep permits(String... declared);

   ClassPermitsStep permits(C_Declared... declared);
}
