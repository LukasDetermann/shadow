package io.determann.shadow.api.lang_model.shadow.structure;

import io.determann.shadow.api.shadow.structure.Return;
import io.determann.shadow.api.shadow.type.Shadow;

public interface ReturnLangModel extends Return
{
   Shadow getType();
}
