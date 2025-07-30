package io.determann.shadow.api.dsl.result;

import io.determann.shadow.api.dsl.TypeRenderable;

public interface ResultTypeStep
{
   ResultRenderable type(String type);

   ResultRenderable type(TypeRenderable type);
}
