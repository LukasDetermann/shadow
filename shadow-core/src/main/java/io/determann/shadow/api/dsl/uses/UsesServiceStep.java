package io.determann.shadow.api.dsl.uses;

import io.determann.shadow.api.shadow.type.C_Declared;

public interface UsesServiceStep
{
   UsesRenderable service(String serviceName);

   UsesRenderable service(C_Declared service);
}
