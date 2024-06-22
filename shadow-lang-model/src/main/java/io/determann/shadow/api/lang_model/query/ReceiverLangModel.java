package io.determann.shadow.api.lang_model.query;

import io.determann.shadow.api.shadow.Receiver;
import io.determann.shadow.api.shadow.Shadow;

public interface ReceiverLangModel extends Receiver
{
   Shadow getType();
}
