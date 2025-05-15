package io.determann.shadow.api.dsl.result;

import io.determann.shadow.api.shadow.type.C_Type;

public interface ResultTypeStep
{
   ResultRenderable type(String type);

   ResultRenderable type(C_Type type);
}
