package io.determann.shadow.api.reflection.shadow.structure;

import io.determann.shadow.api.reflection.shadow.AnnotationableReflection;
import io.determann.shadow.api.reflection.shadow.type.ShadowReflection;
import io.determann.shadow.api.shadow.structure.Receiver;

public interface ReceiverReflection extends Receiver,
                                            AnnotationableReflection
{
   ShadowReflection getType();
}
