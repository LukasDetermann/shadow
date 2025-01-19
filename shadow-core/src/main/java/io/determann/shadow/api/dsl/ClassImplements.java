package io.determann.shadow.api.dsl;

import io.determann.shadow.api.shadow.type.C_Interface;

public interface ClassImplements extends ClassPermitsStep
{
   ClassImplements implements_(String... interfaces);

   ClassImplements implements_(C_Interface... interfaces);
}
