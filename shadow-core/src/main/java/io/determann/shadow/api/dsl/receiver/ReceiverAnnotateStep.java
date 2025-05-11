package io.determann.shadow.api.dsl.receiver;

import io.determann.shadow.api.shadow.type.C_Annotation;

public interface ReceiverAnnotateStep
{
   ReceiverAdditionalAnnotateStep annotate(String... annotation);

   ReceiverAdditionalAnnotateStep annotate(C_Annotation... annotation);
}
