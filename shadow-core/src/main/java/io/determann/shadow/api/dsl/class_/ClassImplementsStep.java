package io.determann.shadow.api.dsl.class_;

import io.determann.shadow.api.shadow.type.C_Interface;

public interface ClassImplementsStep
      extends ClassPermitsStep
{
   ClassImplementsStep implements_(String... interfaces);

   ClassImplementsStep implements_(C_Interface... interfaces);
}
