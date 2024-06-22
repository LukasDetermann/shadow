package io.determann.shadow.api.reflection.query;

import io.determann.shadow.api.shadow.Receiver;
import io.determann.shadow.api.shadow.Shadow;

public interface ReceiverReflection extends Receiver
{
   Shadow getType();
}
