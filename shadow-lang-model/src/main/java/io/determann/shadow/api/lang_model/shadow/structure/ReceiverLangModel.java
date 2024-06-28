package io.determann.shadow.api.lang_model.shadow.structure;

import io.determann.shadow.api.shadow.structure.Receiver;
import io.determann.shadow.api.shadow.type.Shadow;

public interface ReceiverLangModel extends Receiver
{
   Shadow getType();
}
